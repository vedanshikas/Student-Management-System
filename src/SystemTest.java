import exception.DuplicateStudentException;
import exception.InvalidInputException;
import exception.StudentNotFoundException;
import model.Grade;
import model.Marks;
import model.Report;
import model.Student;
import repository.MarksRepository;
import repository.StudentRepository;
import service.AnalyticsService;
import service.MarksService;
import service.ReportService;
import service.StudentService;
import util.FileHandler;
import util.InputValidator;

import java.io.File;
import java.util.List;

/**
 * Automated test suite for validating all layers of the
 * Smart Student Management System.
 */
public class SystemTest {

    private static int testsPassed = 0;
    private static int testsFailed = 0;

    public static void main(String[] args) {
        System.out.println("==================================================");
        System.out.println("  RUNNING SMART STUDENT MANAGEMENT SYSTEM TESTS   ");
        System.out.println("==================================================");

        testStudentModel();
        testMarksAndGradeModel();
        testInputValidator();
        testRepositoriesAndServices();
        testAnalyticsAndRankings();
        testReportGeneration();

        System.out.println("\n==================================================");
        System.out.printf("  TEST RESULTS: %d PASSED, %d FAILED\n", testsPassed, testsFailed);
        System.out.println("==================================================");

        if (testsFailed > 0) {
            System.exit(1);
        }
    }

    private static void assertTrue(String testName, boolean condition) {
        if (condition) {
            System.out.printf("  [PASS] %s\n", testName);
            testsPassed++;
        } else {
            System.err.printf("  [FAIL] %s\n", testName);
            testsFailed++;
        }
    }

    private static void testStudentModel() {
        System.out.println("\n--- Testing Student Model ---");
        Student s = new Student("STU991", "Test User", 20, "test@example.com", "CS", "2024-01-01");
        assertTrue("Student ID getter", "STU991".equals(s.getStudentId()));
        assertTrue("Student Name getter", "Test User".equals(s.getName()));
        assertTrue("Student CSV serialization", s.toCSV().equals("STU991,Test User,20,test@example.com,CS,2024-01-01"));

        Student parsed = Student.fromCSV(s.toCSV());
        assertTrue("Student CSV deserialization equality", s.equals(parsed));
    }

    private static void testMarksAndGradeModel() {
        System.out.println("\n--- Testing Marks & Grade Calculation ---");
        Marks m = new Marks("STU991");
        m.addSubjectMark("Mathematics", 90.0);
        m.addSubjectMark("Physics", 80.0);
        m.addSubjectMark("Chemistry", 70.0);
        m.addSubjectMark("English", 80.0);
        m.addSubjectMark("Computer Science", 100.0);

        assertTrue("Total marks calculation", Math.abs(m.getTotalMarks() - 420.0) < 0.001);
        assertTrue("Average marks calculation", Math.abs(m.getAverageMarks() - 84.0) < 0.001);
        assertTrue("Grade calculation", m.getGrade() == Grade.A);
        assertTrue("Passed check", m.isPassed());

        Grade aPlus = Grade.fromAverage(95);
        Grade bPlusDecimal = Grade.fromAverage(79.8);
        Grade fail = Grade.fromAverage(35);
        assertTrue("Grade A+ determination", aPlus == Grade.A_PLUS);
        assertTrue("Grade B+ decimal (79.8) determination", bPlusDecimal == Grade.B_PLUS);
        assertTrue("Grade F determination", fail == Grade.F);
        assertTrue("Grade isPassing", aPlus.isPassing() && !fail.isPassing());

        Marks deserialized = Marks.fromCSV(m.toCSV());
        assertTrue("Marks deserialization total match", Math.abs(deserialized.getTotalMarks() - 420.0) < 0.001);
    }

    private static void testInputValidator() {
        System.out.println("\n--- Testing InputValidator ---");
        try {
            String name = InputValidator.validateName("John Doe");
            assertTrue("Valid name acceptance", "John Doe".equals(name));
        } catch (Exception e) {
            assertTrue("Valid name acceptance", false);
        }

        try {
            InputValidator.validateName("J123");
            assertTrue("Invalid name rejection", false);
        } catch (InvalidInputException e) {
            assertTrue("Invalid name rejection", true);
        }

        try {
            int age = InputValidator.validateAge("22");
            assertTrue("Valid age acceptance", age == 22);
        } catch (Exception e) {
            assertTrue("Valid age acceptance", false);
        }

        try {
            InputValidator.validateAge("15");
            assertTrue("Underage rejection", false);
        } catch (InvalidInputException e) {
            assertTrue("Underage rejection", true);
        }

        try {
            String email = InputValidator.validateEmail("valid.email@domain.com");
            assertTrue("Valid email acceptance", email.equals("valid.email@domain.com"));
        } catch (Exception e) {
            assertTrue("Valid email acceptance", false);
        }

        try {
            InputValidator.validateEmail("invalid-email");
            assertTrue("Invalid email rejection", false);
        } catch (InvalidInputException e) {
            assertTrue("Invalid email rejection", true);
        }

        try {
            String id = InputValidator.validateStudentId("stu001");
            assertTrue("Student ID format normalization", "STU001".equals(id));
        } catch (Exception e) {
            assertTrue("Student ID format normalization", false);
        }

        try {
            double mark = InputValidator.validateMarks("87.5");
            assertTrue("Valid marks acceptance", Math.abs(mark - 87.5) < 0.001);
        } catch (Exception e) {
            assertTrue("Valid marks acceptance", false);
        }
    }

    private static void testRepositoriesAndServices() {
        System.out.println("\n--- Testing Repositories & Services ---");
        StudentRepository studentRepo = new StudentRepository();
        MarksRepository marksRepo = new MarksRepository();
        StudentService studentService = new StudentService(studentRepo);
        MarksService marksService = new MarksService(marksRepo);

        String testId = "STU999";
        try {
            // Clean up testId if exists
            if (studentRepo.exists(testId)) {
                studentService.delete(testId);
            }
            if (marksRepo.exists(testId)) {
                marksService.deleteMarks(testId);
            }

            Student testStudent = new Student(testId, "Unit Test Student", 21, "unittest@test.com", "Testing", "2024-01-01");
            studentService.add(testStudent);
            assertTrue("Student added successfully", studentRepo.exists(testId));

            Student retrieved = studentService.search(testId);
            assertTrue("Student retrieved correctly", retrieved != null && retrieved.getName().equals("Unit Test Student"));

            testStudent.setName("Updated Test Student");
            studentService.update(testId, testStudent);
            assertTrue("Student updated correctly", studentService.search(testId).getName().equals("Updated Test Student"));

            Marks testMarks = new Marks(testId);
            for (String sub : Marks.DEFAULT_SUBJECTS) {
                testMarks.addSubjectMark(sub, 75.0);
            }
            marksService.addMarks(testMarks);
            assertTrue("Marks added successfully", marksRepo.exists(testId));
            assertTrue("Marks average match", Math.abs(marksService.calculateAverage(testId) - 75.0) < 0.001);

            // Clean up
            studentService.delete(testId);
            marksService.deleteMarks(testId);
            assertTrue("Student cleaned up", !studentRepo.exists(testId));
            assertTrue("Marks cleaned up", !marksRepo.exists(testId));
        } catch (Exception e) {
            assertTrue("Repository/Service operations error: " + e.getMessage(), false);
        }
    }

    private static void testAnalyticsAndRankings() {
        System.out.println("\n--- Testing Analytics & Ranking Calculation ---");
        StudentRepository studentRepo = new StudentRepository();
        MarksRepository marksRepo = new MarksRepository();
        AnalyticsService analytics = new AnalyticsService(studentRepo, marksRepo);

        List<String> rankings = analytics.getRankings();
        assertTrue("Ranking list generated", rankings != null);
    }

    private static void testReportGeneration() {
        System.out.println("\n--- Testing Report Generation ---");
        Student s = new Student("STU001", "Alice Johnson", 20, "alice@test.com", "CS", "2024-01-01");
        Marks m = new Marks("STU001");
        m.addSubjectMark("Mathematics", 95.0);
        Report report = new Report(s, m, 1, 10);
        String output = report.generateReport();
        assertTrue("Report contains student name", output.contains("Alice Johnson"));
        assertTrue("Report contains rank info", output.contains("Class Rank"));
    }
}
