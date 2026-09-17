package model;

import java.io.Serializable;

/**
 * Represents a student entity in the management system.
 * Demonstrates encapsulation with private fields and public accessors,
 * as well as method overriding (toString, equals, hashCode).
 *
 * @author Smart Student Management System
 * @version 1.0
 */
public class Student implements Serializable {

    private static final long serialVersionUID = 1L;

    // ─── Private Fields (Encapsulation) ────────────────────────

    private String studentId;
    private String name;
    private int age;
    private String email;
    private String department;
    private String enrollmentDate;

    // ─── Constructors ──────────────────────────────────────────

    /** Default constructor. */
    public Student() {
    }

    /**
     * Parameterized constructor.
     *
     * @param studentId      Unique student identifier (e.g., STU001)
     * @param name           Full name of the student
     * @param age            Age of the student
     * @param email          Email address
     * @param department     Department / branch of study
     * @param enrollmentDate Date of enrollment (YYYY-MM-DD)
     */
    public Student(String studentId, String name, int age, String email,
                   String department, String enrollmentDate) {
        this.studentId = studentId;
        this.name = name;
        this.age = age;
        this.email = email;
        this.department = department;
        this.enrollmentDate = enrollmentDate;
    }

    /**
     * Copy constructor – creates a deep copy of another Student.
     *
     * @param other The Student object to copy
     */
    public Student(Student other) {
        this.studentId = other.studentId;
        this.name = other.name;
        this.age = other.age;
        this.email = other.email;
        this.department = other.department;
        this.enrollmentDate = other.enrollmentDate;
    }

    // ─── Getters and Setters ───────────────────────────────────

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }

    public String getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(String enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    // ─── Serialization Helpers ─────────────────────────────────

    /**
     * Converts student data to a CSV-formatted string for file storage.
     *
     * @return CSV representation of the student
     */
    public String toCSV() {
        return String.join(",", studentId, name, String.valueOf(age),
                email, department, enrollmentDate);
    }

    /**
     * Factory method – creates a Student from a CSV-formatted line.
     *
     * @param csvLine A comma-separated string with 6 fields
     * @return A new Student instance
     * @throws IllegalArgumentException if the CSV format is invalid
     */
    public static Student fromCSV(String csvLine) {
        String[] parts = csvLine.split(",");
        if (parts.length != 6) {
            throw new IllegalArgumentException("Invalid CSV format for Student: " + csvLine);
        }
        return new Student(
                parts[0].trim(), parts[1].trim(),
                Integer.parseInt(parts[2].trim()), parts[3].trim(),
                parts[4].trim(), parts[5].trim()
        );
    }

    // ─── Overridden Object Methods (Polymorphism) ──────────────

    /**
     * Returns a table-row formatted string representation.
     */
    @Override
    public String toString() {
        return String.format("| %-10s | %-20s | %-4d | %-25s | %-15s | %-12s |",
                studentId, name, age, email, department, enrollmentDate);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Student student = (Student) obj;
        return studentId != null && studentId.equals(student.studentId);
    }

    @Override
    public int hashCode() {
        return studentId != null ? studentId.hashCode() : 0;
    }
}
