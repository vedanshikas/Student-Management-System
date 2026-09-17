package model;

import java.util.Map;

/**
 * Represents a comprehensive academic report for a student, combining
 * personal details with performance metrics. Used by ReportService
 * to generate formatted output.
 *
 * @author Smart Student Management System
 * @version 1.0
 */
public class Report {

    // ─── Private Fields ────────────────────────────────────────

    private Student student;
    private Marks marks;
    private int rank;
    private int totalStudents;

    // ─── Constructors ──────────────────────────────────────────

    /** Default constructor. */
    public Report() {
    }

    /**
     * Parameterized constructor.
     *
     * @param student       The student this report is for
     * @param marks         The student's marks (may be null)
     * @param rank          Class rank of the student
     * @param totalStudents Total number of students with marks
     */
    public Report(Student student, Marks marks, int rank, int totalStudents) {
        this.student = student;
        this.marks = marks;
        this.rank = rank;
        this.totalStudents = totalStudents;
    }

    // ─── Getters and Setters ───────────────────────────────────

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    public Marks getMarks() {
        return marks;
    }

    public void setMarks(Marks marks) {
        this.marks = marks;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public int getTotalStudents() {
        return totalStudents;
    }

    public void setTotalStudents(int totalStudents) {
        this.totalStudents = totalStudents;
    }

    // ─── Report Generation ─────────────────────────────────────

    /**
     * Generates a beautifully formatted report string containing
     * student details and academic performance.
     *
     * @return A multi-line formatted report string
     */
    public String generateReport() {
        StringBuilder sb = new StringBuilder();
        String border = "=".repeat(60);
        String line = "-".repeat(40);

        sb.append("\n").append(border).append("\n");
        sb.append("              STUDENT PERFORMANCE REPORT\n");
        sb.append(border).append("\n\n");

        // ── Student Details ──
        sb.append("  STUDENT DETAILS\n");
        sb.append("  ").append(line).append("\n");
        sb.append(String.format("  Student ID     : %s\n", student.getStudentId()));
        sb.append(String.format("  Name           : %s\n", student.getName()));
        sb.append(String.format("  Age            : %d\n", student.getAge()));
        sb.append(String.format("  Email          : %s\n", student.getEmail()));
        sb.append(String.format("  Department     : %s\n", student.getDepartment()));
        sb.append(String.format("  Enrollment Date: %s\n", student.getEnrollmentDate()));

        // ── Academic Performance ──
        if (marks != null && !marks.getSubjectMarks().isEmpty()) {
            sb.append("\n  ACADEMIC PERFORMANCE\n");
            sb.append("  ").append(line).append("\n");

            for (Map.Entry<String, Double> entry : marks.getSubjectMarks().entrySet()) {
                String status = entry.getValue() >= 40 ? "PASS" : "FAIL";
                sb.append(String.format("  %-20s: %6.1f  [%s]\n",
                        entry.getKey(), entry.getValue(), status));
            }

            sb.append("\n  ").append(line).append("\n");
            sb.append(String.format("  Total Marks    : %.1f / %.1f\n",
                    marks.getTotalMarks(), marks.getSubjectMarks().size() * 100.0));
            sb.append(String.format("  Average        : %.2f%%\n", marks.getAverageMarks()));
            sb.append(String.format("  Grade          : %s\n", marks.getGrade()));
            sb.append(String.format("  Status         : %s\n",
                    marks.isPassed() ? "PASSED" : "FAILED"));
            sb.append(String.format("  Class Rank     : %d / %d\n", rank, totalStudents));
        } else {
            sb.append("\n  [No marks recorded for this student]\n");
        }

        sb.append("\n").append(border).append("\n");
        return sb.toString();
    }

    @Override
    public String toString() {
        return generateReport();
    }
}
