package model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Represents the academic marks for a single student across multiple subjects.
 * Uses HashMap to store subject-wise marks – demonstrates the use of
 * collections and stream operations.
 *
 * @author Smart Student Management System
 * @version 1.0
 */
public class Marks implements Serializable {

    private static final long serialVersionUID = 1L;

    /** The default subjects used throughout the system. */
    public static final String[] DEFAULT_SUBJECTS = {
            "Mathematics", "Physics", "Chemistry", "English", "Computer Science"
    };

    // ─── Private Fields ────────────────────────────────────────

    private String studentId;
    private HashMap<String, Double> subjectMarks;

    // ─── Constructors ──────────────────────────────────────────

    /** Default constructor – initialises an empty marks map. */
    public Marks() {
        this.subjectMarks = new HashMap<>();
    }

    /**
     * Creates a Marks object for a given student with an empty marks map.
     *
     * @param studentId The ID of the student
     */
    public Marks(String studentId) {
        this.studentId = studentId;
        this.subjectMarks = new HashMap<>();
    }

    /**
     * Creates a Marks object with a pre-populated marks map.
     *
     * @param studentId    The ID of the student
     * @param subjectMarks A HashMap of subject names to marks
     */
    public Marks(String studentId, HashMap<String, Double> subjectMarks) {
        this.studentId = studentId;
        this.subjectMarks = new HashMap<>(subjectMarks);
    }

    // ─── Getters and Setters ───────────────────────────────────

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public HashMap<String, Double> getSubjectMarks() {
        return subjectMarks;
    }

    public void setSubjectMarks(HashMap<String, Double> subjectMarks) {
        this.subjectMarks = new HashMap<>(subjectMarks);
    }

    /**
     * Adds or updates marks for a single subject.
     *
     * @param subject The subject name
     * @param marks   The marks obtained (0-100)
     */
    public void addSubjectMark(String subject, double marks) {
        subjectMarks.put(subject, marks);
    }

    /**
     * Retrieves marks for a specific subject.
     *
     * @param subject The subject name
     * @return The marks, or null if not found
     */
    public Double getSubjectMark(String subject) {
        return subjectMarks.get(subject);
    }

    // ─── Calculation Methods ───────────────────────────────────

    /**
     * Calculates total marks across all subjects.
     *
     * @return Sum of all subject marks
     */
    public double getTotalMarks() {
        return subjectMarks.values().stream()
                .mapToDouble(Double::doubleValue)
                .sum();
    }

    /**
     * Calculates the average marks.
     *
     * @return Average of all subject marks, or 0.0 if no marks exist
     */
    public double getAverageMarks() {
        if (subjectMarks.isEmpty()) return 0.0;
        return getTotalMarks() / subjectMarks.size();
    }

    /**
     * Determines the grade based on the average marks.
     *
     * @return The corresponding Grade enum value
     */
    public Grade getGrade() {
        return Grade.fromAverage(getAverageMarks());
    }

    /**
     * Checks whether the student has passed all subjects (>= 40 in each).
     *
     * @return true if all subject marks are >= 40
     */
    public boolean isPassed() {
        return subjectMarks.values().stream()
                .allMatch(marks -> marks >= 40);
    }

    // ─── Serialization Helpers ─────────────────────────────────

    /**
     * Converts marks data to CSV format.
     * Format: studentId,subject1:marks1|subject2:marks2|...
     *
     * @return CSV representation of marks
     */
    public String toCSV() {
        String marksStr = subjectMarks.entrySet().stream()
                .map(e -> e.getKey() + ":" + e.getValue())
                .collect(Collectors.joining("|"));
        return studentId + "," + marksStr;
    }

    /**
     * Factory method – creates a Marks object from a CSV-formatted line.
     *
     * @param csvLine A string in the format: id,sub1:m1|sub2:m2|...
     * @return A new Marks instance
     * @throws IllegalArgumentException if the format is invalid
     */
    public static Marks fromCSV(String csvLine) {
        String[] parts = csvLine.split(",", 2);
        if (parts.length != 2) {
            throw new IllegalArgumentException("Invalid CSV format for Marks: " + csvLine);
        }

        Marks marks = new Marks(parts[0].trim());
        if (!parts[1].trim().isEmpty()) {
            String[] subjects = parts[1].trim().split("\\|");
            for (String subject : subjects) {
                String[] subParts = subject.split(":");
                if (subParts.length == 2) {
                    marks.addSubjectMark(subParts[0].trim(),
                            Double.parseDouble(subParts[1].trim()));
                }
            }
        }
        return marks;
    }

    // ─── Display ───────────────────────────────────────────────

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("  Student ID: %s\n", studentId));
        sb.append(String.format("  %-20s | %-10s\n", "Subject", "Marks"));
        sb.append("  ").append("-".repeat(33)).append("\n");
        for (Map.Entry<String, Double> entry : subjectMarks.entrySet()) {
            sb.append(String.format("  %-20s | %-10.1f\n", entry.getKey(), entry.getValue()));
        }
        sb.append("  ").append("-".repeat(33)).append("\n");
        sb.append(String.format("  Total: %.1f | Average: %.1f | Grade: %s\n",
                getTotalMarks(), getAverageMarks(), getGrade().getLabel()));
        return sb.toString();
    }
}
