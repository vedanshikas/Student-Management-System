package service;

import model.Marks;
import model.Report;
import model.Student;
import repository.MarksRepository;
import repository.StudentRepository;
import util.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Service class for generating individual and class-wide reports.
 * Aggregates data from student and marks repositories to produce
 * formatted performance reports.
 *
 * @author Smart Student Management System
 * @version 1.0
 */
public class ReportService {

    private final StudentRepository studentRepository;
    private final MarksRepository marksRepository;
    private final AnalyticsService analyticsService;
    private final Logger logger = Logger.getInstance();

    /**
     * Constructs the report service with required dependencies.
     *
     * @param studentRepository The student data repository
     * @param marksRepository   The marks data repository
     * @param analyticsService  The analytics service (for rankings)
     */
    public ReportService(StudentRepository studentRepository,
                         MarksRepository marksRepository,
                         AnalyticsService analyticsService) {
        this.studentRepository = studentRepository;
        this.marksRepository = marksRepository;
        this.analyticsService = analyticsService;
    }

    // ─── Individual Student Report ─────────────────────────────

    /**
     * Generates and displays a detailed report for a single student,
     * including personal info, subject marks, grade, and rank.
     *
     * @param studentId The student's ID
     */
    public void generateStudentReport(String studentId) {
        Student student = studentRepository.findById(studentId);
        if (student == null) {
            System.out.println("\n  Student not found with ID: " + studentId);
            return;
        }

        Marks marks = marksRepository.findByStudentId(studentId);
        int rank = marks != null ? analyticsService.getRank(studentId) : 0;
        int totalWithMarks = marksRepository.getAll().size();

        Report report = new Report(student, marks, rank, totalWithMarks);
        System.out.println(report.generateReport());

        logger.info("Report generated for student: " + studentId);
    }

    // ─── Class Report ──────────────────────────────────────────

    /**
     * Generates and displays a class-wide performance report,
     * listing all students sorted by average marks (descending).
     */
    public void generateClassReport() {
        List<Student> students = studentRepository.getAll();
        List<Marks> allMarks = marksRepository.getAll();

        if (students.isEmpty()) {
            System.out.println("\n  No students in the system.\n");
            return;
        }

        String border = "=".repeat(74);

        System.out.println("\n" + border);
        System.out.println("                        CLASS PERFORMANCE REPORT");
        System.out.println(border);
        System.out.printf("  Total Students Enrolled : %d\n", students.size());
        System.out.printf("  Students with Marks     : %d\n", allMarks.size());
        System.out.println(border);

        if (allMarks.isEmpty()) {
            System.out.println("  No marks data available to generate report.");
            System.out.println(border + "\n");
            return;
        }

        // Sort by average (descending)
        List<Marks> sorted = new ArrayList<>(allMarks);
        sorted.sort(Comparator.comparingDouble(Marks::getAverageMarks).reversed());

        System.out.printf("  %-6s %-10s %-20s %-10s %-8s %-6s %-6s\n",
                "Rank", "ID", "Name", "Total", "Avg", "Grade", "Status");
        System.out.println("  " + "-".repeat(68));

        int rank = 1;
        for (Marks m : sorted) {
            Student s = studentRepository.findById(m.getStudentId());
            String name = s != null ? s.getName() : "Unknown";
            String status = m.isPassed() ? "PASS" : "FAIL";
            System.out.printf("  %-6d %-10s %-20s %-10.1f %-8.1f %-6s %-6s\n",
                    rank++, m.getStudentId(), name, m.getTotalMarks(),
                    m.getAverageMarks(), m.getGrade().getLabel(), status);
        }

        System.out.println(border + "\n");
        logger.info("Class report generated.");
    }
}
