import exception.DuplicateStudentException;
import exception.InvalidInputException;
import exception.StudentNotFoundException;
import model.Marks;
import model.Student;
import repository.MarksRepository;
import repository.StudentRepository;
import service.AnalyticsService;
import service.MarksService;
import service.ReportService;
import service.StudentService;
import util.FileHandler;
import util.InputValidator;
import util.Logger;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

/**
 * Main application class providing the interactive Command Line Interface (CLI)
 * for the Smart Student Management System.
 *
 * @author Smart Student Management System
 * @version 1.0
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final Logger logger = Logger.getInstance();

    // ─── Repositories & Services ───────────────────────────────
    private static StudentRepository studentRepo;
    private static MarksRepository marksRepo;
    private static StudentService studentService;
    private static MarksService marksService;
    private static AnalyticsService analyticsService;
    private static ReportService reportService;

    public static void main(String[] args) {
        initializeDependencies();
        logger.info("Application started successfully.");

        boolean running = true;
        while (running) {
            displayMainMenu();
            try {
                System.out.print("  Enter choice (0-6): ");
                String input = scanner.nextLine();
                int choice = InputValidator.validateMenuChoice(input, 0, 6);

                switch (choice) {
                    case 1:
                        handleStudentMenu();
                        break;
                    case 2:
                        handleMarksMenu();
                        break;
                    case 3:
                        handleReportsMenu();
                        break;
                    case 4:
                        handleAnalyticsMenu();
                        break;
                    case 5:
                        handleSeedSampleData();
                        break;
                    case 6:
                        handleViewLogs();
                        break;
                    case 0:
                        running = false;
                        displayExitMessage();
                        break;
                }
            } catch (InvalidInputException e) {
                System.out.println("\n  [ERROR] " + e.getMessage());
                pause();
            } catch (Exception e) {
                System.out.println("\n  [UNEXPECTED ERROR] " + e.getMessage());
                logger.error("Unexpected error in main loop: " + e.getMessage());
                pause();
            }
        }

        scanner.close();
        logger.info("Application closed cleanly.");
    }

    private static void initializeDependencies() {
        studentRepo = new StudentRepository();
        marksRepo = new MarksRepository();
        studentService = new StudentService(studentRepo);
        marksService = new MarksService(marksRepo);
        analyticsService = new AnalyticsService(studentRepo, marksRepo);
        reportService = new ReportService(studentRepo, marksRepo, analyticsService);
    }

    // ─── Menus ─────────────────────────────────────────────────

    private static void displayMainMenu() {
        System.out.println("\n============================================================");
        System.out.println("             SMART STUDENT MANAGEMENT SYSTEM                ");
        System.out.println("============================================================");
        System.out.printf("  Active Students: %-5d | Records with Marks: %-5d\n",
                studentService.getStudentCount(), marksService.getAllMarks().size());
        System.out.println("------------------------------------------------------------");
        System.out.println("  1. Student Management       (Add, View, Search, Edit, Delete)");
        System.out.println("  2. Marks & Grades           (Add, Update, View Marks)");
        System.out.println("  3. Reports & Cards          (Student Report, Class Table)");
        System.out.println("  4. Analytics & Insights     (Toppers, Averages, Pass Rate)");
        System.out.println("  5. Seed Demo Sample Data    (Quick test data setup)");
        System.out.println("  6. System Logs Viewer       (View application logs)");
        System.out.println("  0. Exit");
        System.out.println("============================================================");
    }

    // ─── 1. Student Management ─────────────────────────────────

    private static void handleStudentMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- [ Student Management ] ---------------------------------");
            System.out.println("  1. Add New Student (Manual ID)");
            System.out.println("  2. Add New Student (Auto-generated ID)");
            System.out.println("  3. View All Students");
            System.out.println("  4. Search Student by ID");
            System.out.println("  5. Update Student Details");
            System.out.println("  6. Delete Student");
            System.out.println("  0. Back to Main Menu");
            System.out.println("------------------------------------------------------------");

            try {
                System.out.print("  Enter choice (0-6): ");
                int choice = InputValidator.validateMenuChoice(scanner.nextLine(), 0, 6);

                switch (choice) {
                    case 1:
                        addNewStudent(false);
                        break;
                    case 2:
                        addNewStudent(true);
                        break;
                    case 3:
                        studentService.displayAll();
                        pause();
                        break;
                    case 4:
                        searchStudent();
                        break;
                    case 5:
                        updateStudent();
                        break;
                    case 6:
                        deleteStudent();
                        break;
                    case 0:
                        back = true;
                        break;
                }
            } catch (InvalidInputException e) {
                System.out.println("\n  [ERROR] " + e.getMessage());
                pause();
            }
        }
    }

    private static void addNewStudent(boolean autoId) {
        System.out.println("\n--- Add New Student ---");
        try {
            String studentId = "";
            if (!autoId) {
                System.out.print("  Enter Student ID (e.g. STU001): ");
                studentId = InputValidator.validateStudentId(scanner.nextLine());
            }

            System.out.print("  Enter Full Name: ");
            String name = InputValidator.validateName(scanner.nextLine());

            System.out.print("  Enter Age (16-60): ");
            int age = InputValidator.validateAge(scanner.nextLine());

            System.out.print("  Enter Email Address: ");
            String email = InputValidator.validateEmail(scanner.nextLine());

            System.out.print("  Enter Department (e.g. Computer Science): ");
            String dept = InputValidator.validateDepartment(scanner.nextLine());

            System.out.print("  Enter Enrollment Date [YYYY-MM-DD] (Press Enter for today): ");
            String dateInput = scanner.nextLine().trim();
            String date = dateInput.isEmpty() ? LocalDate.now().toString() : dateInput;

            Student student = new Student(studentId, name, age, email, dept, date);

            if (autoId) {
                String assignedId = studentService.addAndGenerateId(student);
                System.out.println("\n  [SUCCESS] Student created successfully with ID: " + assignedId);
            } else {
                studentService.add(student);
                System.out.println("\n  [SUCCESS] Student created successfully with ID: " + studentId);
            }
        } catch (DuplicateStudentException | InvalidInputException e) {
            System.out.println("\n  [ERROR] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n  [ERROR] Failed to add student: " + e.getMessage());
        }
        pause();
    }

    private static void searchStudent() {
        System.out.print("\n  Enter Student ID to search: ");
        try {
            String id = InputValidator.validateStudentId(scanner.nextLine());
            Student s = studentService.search(id);
            System.out.println("\n  Student Details Found:");
            System.out.println("  --------------------------------------------------");
            System.out.println("  ID             : " + s.getStudentId());
            System.out.println("  Name           : " + s.getName());
            System.out.println("  Age            : " + s.getAge());
            System.out.println("  Email          : " + s.getEmail());
            System.out.println("  Department     : " + s.getDepartment());
            System.out.println("  Enrollment Date: " + s.getEnrollmentDate());
            System.out.println("  --------------------------------------------------");
        } catch (StudentNotFoundException | InvalidInputException e) {
            System.out.println("\n  [ERROR] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n  [ERROR] " + e.getMessage());
        }
        pause();
    }

    private static void updateStudent() {
        System.out.print("\n  Enter Student ID to update: ");
        try {
            String id = InputValidator.validateStudentId(scanner.nextLine());
            Student current = studentService.search(id);

            System.out.println("  Updating student: " + current.getName() + " (" + id + ")");
            System.out.println("  (Press Enter without typing to keep existing value)");

            System.out.print("  Name [" + current.getName() + "]: ");
            String nameInput = scanner.nextLine().trim();
            String name = nameInput.isEmpty() ? current.getName() : InputValidator.validateName(nameInput);

            System.out.print("  Age [" + current.getAge() + "]: ");
            String ageInput = scanner.nextLine().trim();
            int age = ageInput.isEmpty() ? current.getAge() : InputValidator.validateAge(ageInput);

            System.out.print("  Email [" + current.getEmail() + "]: ");
            String emailInput = scanner.nextLine().trim();
            String email = emailInput.isEmpty() ? current.getEmail() : InputValidator.validateEmail(emailInput);

            System.out.print("  Department [" + current.getDepartment() + "]: ");
            String deptInput = scanner.nextLine().trim();
            String dept = deptInput.isEmpty() ? current.getDepartment() : InputValidator.validateDepartment(deptInput);

            System.out.print("  Enrollment Date [" + current.getEnrollmentDate() + "]: ");
            String dateInput = scanner.nextLine().trim();
            String date = dateInput.isEmpty() ? current.getEnrollmentDate() : dateInput;

            Student updated = new Student(id, name, age, email, dept, date);
            studentService.update(id, updated);
            System.out.println("\n  [SUCCESS] Student details updated successfully.");
        } catch (StudentNotFoundException | InvalidInputException e) {
            System.out.println("\n  [ERROR] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n  [ERROR] Update failed: " + e.getMessage());
        }
        pause();
    }

    private static void deleteStudent() {
        System.out.print("\n  Enter Student ID to delete: ");
        try {
            String id = InputValidator.validateStudentId(scanner.nextLine());
            Student s = studentService.search(id);

            System.out.printf("  Are you sure you want to delete '%s' (%s)? (y/N): ", s.getName(), id);
            String confirm = scanner.nextLine().trim();
            if (confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")) {
                studentService.delete(id);
                // Also remove any marks linked to this student
                if (marksService.hasMarks(id)) {
                    marksService.deleteMarks(id);
                }
                System.out.println("\n  [SUCCESS] Student and related marks deleted successfully.");
            } else {
                System.out.println("\n  [CANCELLED] Deletion cancelled.");
            }
        } catch (StudentNotFoundException | InvalidInputException e) {
            System.out.println("\n  [ERROR] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n  [ERROR] " + e.getMessage());
        }
        pause();
    }

    // ─── 2. Marks Management ───────────────────────────────────

    private static void handleMarksMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- [ Marks Management ] -----------------------------------");
            System.out.println("  1. Add / Update All Marks for a Student");
            System.out.println("  2. View Marks for a Student");
            System.out.println("  3. Update Single Subject Mark");
            System.out.println("  4. Delete Marks for a Student");
            System.out.println("  0. Back to Main Menu");
            System.out.println("------------------------------------------------------------");

            try {
                System.out.print("  Enter choice (0-4): ");
                int choice = InputValidator.validateMenuChoice(scanner.nextLine(), 0, 4);

                switch (choice) {
                    case 1:
                        addOrUpdateStudentMarks();
                        break;
                    case 2:
                        viewStudentMarks();
                        break;
                    case 3:
                        updateSingleSubject();
                        break;
                    case 4:
                        deleteStudentMarks();
                        break;
                    case 0:
                        back = true;
                        break;
                }
            } catch (InvalidInputException e) {
                System.out.println("\n  [ERROR] " + e.getMessage());
                pause();
            }
        }
    }

    private static void addOrUpdateStudentMarks() {
        System.out.print("\n  Enter Student ID: ");
        try {
            String id = InputValidator.validateStudentId(scanner.nextLine());
            Student student = studentService.search(id);
            System.out.println("  Entering marks for: " + student.getName() + " (" + id + ")");

            Marks marks = new Marks(id);
            for (String subject : Marks.DEFAULT_SUBJECTS) {
                System.out.printf("  Marks for %-18s (0-100): ", subject);
                double val = InputValidator.validateMarks(scanner.nextLine());
                marks.addSubjectMark(subject, val);
            }

            marksService.addMarks(marks);
            System.out.println("\n  [SUCCESS] Marks recorded successfully.");
            marksService.displayMarks(id);
        } catch (StudentNotFoundException | InvalidInputException e) {
            System.out.println("\n  [ERROR] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n  [ERROR] " + e.getMessage());
        }
        pause();
    }

    private static void viewStudentMarks() {
        System.out.print("\n  Enter Student ID: ");
        try {
            String id = InputValidator.validateStudentId(scanner.nextLine());
            marksService.displayMarks(id);
        } catch (StudentNotFoundException | InvalidInputException e) {
            System.out.println("\n  [ERROR] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n  [ERROR] " + e.getMessage());
        }
        pause();
    }

    private static void updateSingleSubject() {
        System.out.print("\n  Enter Student ID: ");
        try {
            String id = InputValidator.validateStudentId(scanner.nextLine());
            Marks marks = marksService.getMarks(id);

            System.out.println("\n  Current Subjects & Marks:");
            int idx = 1;
            String[] subjects = marks.getSubjectMarks().keySet().toArray(new String[0]);
            for (String sub : subjects) {
                System.out.printf("    %d. %-20s : %.1f\n", idx++, sub, marks.getSubjectMark(sub));
            }

            System.out.print("  Select Subject Number or type subject name: ");
            String subInput = scanner.nextLine().trim();
            String targetSubject;

            if (subInput.matches("\\d+")) {
                int selectedIdx = Integer.parseInt(subInput);
                if (selectedIdx >= 1 && selectedIdx <= subjects.length) {
                    targetSubject = subjects[selectedIdx - 1];
                } else {
                    throw new InvalidInputException("Subject Selection", "Invalid subject number.");
                }
            } else {
                targetSubject = subInput;
            }

            System.out.printf("  Enter new marks for %s (0-100): ", targetSubject);
            double newMark = InputValidator.validateMarks(scanner.nextLine());

            marksService.updateMarks(id, targetSubject, newMark);
            System.out.println("\n  [SUCCESS] Subject mark updated successfully.");
            marksService.displayMarks(id);
        } catch (StudentNotFoundException | InvalidInputException e) {
            System.out.println("\n  [ERROR] " + e.getMessage());
        } catch (Exception e) {
            System.out.println("\n  [ERROR] " + e.getMessage());
        }
        pause();
    }

    private static void deleteStudentMarks() {
        System.out.print("\n  Enter Student ID: ");
        try {
            String id = InputValidator.validateStudentId(scanner.nextLine());
            if (!marksService.hasMarks(id)) {
                System.out.println("\n  [INFO] No marks recorded for student ID: " + id);
            } else {
                System.out.printf("  Are you sure you want to delete all marks for %s? (y/N): ", id);
                String confirm = scanner.nextLine().trim();
                if (confirm.equalsIgnoreCase("y") || confirm.equalsIgnoreCase("yes")) {
                    marksService.deleteMarks(id);
                    System.out.println("\n  [SUCCESS] Marks deleted successfully.");
                } else {
                    System.out.println("\n  [CANCELLED] Operation cancelled.");
                }
            }
        } catch (InvalidInputException e) {
            System.out.println("\n  [ERROR] " + e.getMessage());
        }
        pause();
    }

    // ─── 3. Reports & Cards ────────────────────────────────────

    private static void handleReportsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- [ Reports & Performance Cards ] ------------------------");
            System.out.println("  1. Generate Individual Student Report Card");
            System.out.println("  2. Generate Full Class Leaderboard Report");
            System.out.println("  0. Back to Main Menu");
            System.out.println("------------------------------------------------------------");

            try {
                System.out.print("  Enter choice (0-2): ");
                int choice = InputValidator.validateMenuChoice(scanner.nextLine(), 0, 2);

                switch (choice) {
                    case 1:
                        System.out.print("\n  Enter Student ID: ");
                        String id = InputValidator.validateStudentId(scanner.nextLine());
                        reportService.generateStudentReport(id);
                        pause();
                        break;
                    case 2:
                        reportService.generateClassReport();
                        pause();
                        break;
                    case 0:
                        back = true;
                        break;
                }
            } catch (InvalidInputException e) {
                System.out.println("\n  [ERROR] " + e.getMessage());
                pause();
            }
        }
    }

    // ─── 4. Analytics & Insights ───────────────────────────────

    private static void handleAnalyticsMenu() {
        boolean back = false;
        while (!back) {
            System.out.println("\n--- [ Academic Analytics & Insights ] ----------------------");
            System.out.println("  1. Top Performer Analysis");
            System.out.println("  2. Class & Subject Average Report");
            System.out.println("  3. Pass/Fail Statistics & Grade Distribution");
            System.out.println("  4. Complete Class Rankings");
            System.out.println("  0. Back to Main Menu");
            System.out.println("------------------------------------------------------------");

            try {
                System.out.print("  Enter choice (0-4): ");
                int choice = InputValidator.validateMenuChoice(scanner.nextLine(), 0, 4);

                switch (choice) {
                    case 1:
                        analyticsService.displayTopPerformer();
                        pause();
                        break;
                    case 2:
                        analyticsService.displayClassAverage();
                        pause();
                        break;
                    case 3:
                        analyticsService.displayPassFailStatistics();
                        pause();
                        break;
                    case 4:
                        displayRankings();
                        pause();
                        break;
                    case 0:
                        back = true;
                        break;
                }
            } catch (InvalidInputException e) {
                System.out.println("\n  [ERROR] " + e.getMessage());
                pause();
            }
        }
    }

    private static void displayRankings() {
        List<String> rankingIds = analyticsService.getRankings();
        if (rankingIds.isEmpty()) {
            System.out.println("\n  No marks available to calculate rankings.\n");
            return;
        }

        System.out.println("\n  +======================================================+");
        System.out.println("  |                 ACADEMIC RANKINGS                    |");
        System.out.println("  +======================================================+");
        System.out.printf("  | %-6s | %-10s | %-20s | %-8s |\n", "Rank", "ID", "Name", "Average");
        System.out.println("  +--------+------------+----------------------+----------+");

        int rank = 1;
        for (String id : rankingIds) {
            Student s = studentRepo.findById(id);
            String name = s != null ? s.getName() : "Unknown";
            Marks m = marksRepo.findByStudentId(id);
            double avg = m != null ? m.getAverageMarks() : 0.0;

            System.out.printf("  | %-6d | %-10s | %-20s | %-8.2f |\n", rank++, id, name, avg);
        }
        System.out.println("  +======================================================+\n");
    }

    // ─── 5. Seed Demo Data ─────────────────────────────────────

    private static void handleSeedSampleData() {
        System.out.print("\n  This will populate standard demo students and marks. Proceed? (y/N): ");
        String confirm = scanner.nextLine().trim();
        if (!confirm.equalsIgnoreCase("y") && !confirm.equalsIgnoreCase("yes")) {
            System.out.println("  [CANCELLED] Seeding cancelled.");
            pause();
            return;
        }

        Student[] demoStudents = {
                new Student("STU001", "Alice Johnson", 20, "alice.j@example.com", "Computer Science", "2024-08-15"),
                new Student("STU002", "Bob Smith", 21, "bob.smith@example.com", "Information Tech", "2024-08-15"),
                new Student("STU003", "Charlie Brown", 19, "charlie.b@example.com", "Computer Science", "2024-08-16"),
                new Student("STU004", "Diana Prince", 22, "diana.p@example.com", "Electrical Eng", "2024-08-18"),
                new Student("STU005", "Ethan Hunt", 20, "ethan.h@example.com", "Mechanical Eng", "2024-08-20")
        };

        double[][] demoMarks = {
                {95.0, 88.0, 92.0, 85.0, 98.0}, // Alice - High distinction
                {78.0, 82.0, 74.0, 80.0, 85.0}, // Bob - Good
                {62.0, 58.0, 65.0, 70.0, 60.0}, // Charlie - Average
                {91.0, 94.0, 89.0, 93.0, 96.0}, // Diana - Top Performer
                {35.0, 42.0, 38.0, 50.0, 45.0}  // Ethan - Failed subjects
        };

        int added = 0;
        for (int i = 0; i < demoStudents.length; i++) {
            Student s = demoStudents[i];
            if (!studentRepo.exists(s.getStudentId())) {
                try {
                    studentService.add(s);
                    added++;
                } catch (Exception ignored) {}
            }

            Marks m = new Marks(s.getStudentId());
            for (int j = 0; j < Marks.DEFAULT_SUBJECTS.length; j++) {
                m.addSubjectMark(Marks.DEFAULT_SUBJECTS[j], demoMarks[i][j]);
            }
            marksService.addMarks(m);
        }

        System.out.printf("\n  [SUCCESS] Seeded %d demo students with academic records.\n", added);
        logger.info("Demo sample data seeded into the system.");
        pause();
    }

    // ─── 6. System Logs Viewer ─────────────────────────────────

    private static void handleViewLogs() {
        System.out.println("\n--- [ Recent Application Logs ] ----------------------------");
        try {
            List<String> logLines = FileHandler.readLines("logs/application.log");
            if (logLines.isEmpty()) {
                // Also check if logs directory is used
                java.io.File logFile = new java.io.File("logs/application.log");
                if (logFile.exists()) {
                    List<String> lines = java.nio.file.Files.readAllLines(logFile.toPath());
                    int start = Math.max(0, lines.size() - 25);
                    for (int i = start; i < lines.size(); i++) {
                        System.out.println("  " + lines.get(i));
                    }
                } else {
                    System.out.println("  No log entries found yet.");
                }
            } else {
                int start = Math.max(0, logLines.size() - 25);
                for (int i = start; i < logLines.size(); i++) {
                    System.out.println("  " + logLines.get(i));
                }
            }
        } catch (Exception e) {
            System.out.println("  Unable to load logs: " + e.getMessage());
        }
        System.out.println("------------------------------------------------------------");
        pause();
    }

    private static void displayExitMessage() {
        System.out.println("\n============================================================");
        System.out.println("   Thank you for using Smart Student Management System!     ");
        System.out.println("============================================================\n");
    }

    private static void pause() {
        System.out.print("\n  Press [Enter] to continue...");
        scanner.nextLine();
    }
}
