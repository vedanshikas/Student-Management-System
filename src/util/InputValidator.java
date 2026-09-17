package util;

import exception.InvalidInputException;

/**
 * Utility class providing static methods for validating user input.
 * Each method either returns the validated value or throws
 * {@link InvalidInputException} with a descriptive message.
 *
 * @author Smart Student Management System
 * @version 1.0
 */
public class InputValidator {

    // ─── Name Validation ───────────────────────────────────────

    /**
     * Validates a student name.
     * Rules: non-empty, 2–50 characters, letters and spaces only.
     *
     * @param name The raw name input
     * @return The trimmed, validated name
     * @throws InvalidInputException if the name is invalid
     */
    public static String validateName(String name) throws InvalidInputException {
        if (name == null || name.trim().isEmpty()) {
            throw new InvalidInputException("Name", "Name cannot be empty.");
        }
        String trimmed = name.trim();
        if (trimmed.length() < 2 || trimmed.length() > 50) {
            throw new InvalidInputException("Name",
                    "Name must be between 2 and 50 characters.");
        }
        if (!trimmed.matches("[a-zA-Z\\s]+")) {
            throw new InvalidInputException("Name",
                    "Name can only contain letters and spaces.");
        }
        return trimmed;
    }

    // ─── Age Validation ────────────────────────────────────────

    /**
     * Validates a student age.
     * Rules: must be a number between 16 and 60 (inclusive).
     *
     * @param ageStr The raw age input as a string
     * @return The validated age as an int
     * @throws InvalidInputException if the age is invalid
     */
    public static int validateAge(String ageStr) throws InvalidInputException {
        try {
            int age = Integer.parseInt(ageStr.trim());
            if (age < 16 || age > 60) {
                throw new InvalidInputException("Age",
                        "Age must be between 16 and 60.");
            }
            return age;
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Age",
                    "Age must be a valid number.");
        }
    }

    // ─── Email Validation ──────────────────────────────────────

    /**
     * Validates an email address using a simple regex pattern.
     *
     * @param email The raw email input
     * @return The trimmed, validated email
     * @throws InvalidInputException if the email is invalid
     */
    public static String validateEmail(String email) throws InvalidInputException {
        if (email == null || email.trim().isEmpty()) {
            throw new InvalidInputException("Email", "Email cannot be empty.");
        }
        String trimmed = email.trim();
        if (!trimmed.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) {
            throw new InvalidInputException("Email",
                    "Please enter a valid email address (e.g., user@example.com).");
        }
        return trimmed;
    }

    // ─── Student ID Validation ─────────────────────────────────

    /**
     * Validates a student ID.
     * Expected format: STU followed by exactly 3 digits (e.g., STU001).
     *
     * @param id The raw ID input
     * @return The trimmed, validated student ID
     * @throws InvalidInputException if the ID format is invalid
     */
    public static String validateStudentId(String id) throws InvalidInputException {
        if (id == null || id.trim().isEmpty()) {
            throw new InvalidInputException("Student ID",
                    "Student ID cannot be empty.");
        }
        String trimmed = id.trim().toUpperCase();
        if (!trimmed.matches("STU\\d{3}")) {
            throw new InvalidInputException("Student ID",
                    "Student ID must be in the format STU001.");
        }
        return trimmed;
    }

    // ─── Marks Validation ──────────────────────────────────────

    /**
     * Validates subject marks.
     * Rules: must be a number between 0 and 100 (inclusive).
     *
     * @param marksStr The raw marks input as a string
     * @return The validated marks as a double
     * @throws InvalidInputException if the marks are invalid
     */
    public static double validateMarks(String marksStr) throws InvalidInputException {
        try {
            double marks = Double.parseDouble(marksStr.trim());
            if (marks < 0 || marks > 100) {
                throw new InvalidInputException("Marks",
                        "Marks must be between 0 and 100.");
            }
            return marks;
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Marks",
                    "Marks must be a valid number.");
        }
    }

    // ─── Department Validation ─────────────────────────────────

    /**
     * Validates a department name (non-empty check).
     *
     * @param dept The raw department input
     * @return The trimmed department name
     * @throws InvalidInputException if the department is empty
     */
    public static String validateDepartment(String dept) throws InvalidInputException {
        if (dept == null || dept.trim().isEmpty()) {
            throw new InvalidInputException("Department",
                    "Department cannot be empty.");
        }
        return dept.trim();
    }

    // ─── Menu Choice Validation ────────────────────────────────

    /**
     * Validates a numeric menu choice within a given range.
     *
     * @param input The raw input string
     * @param min   Minimum valid value (inclusive)
     * @param max   Maximum valid value (inclusive)
     * @return The validated choice as an int
     * @throws InvalidInputException if the choice is out of range or non-numeric
     */
    public static int validateMenuChoice(String input, int min, int max)
            throws InvalidInputException {
        try {
            int choice = Integer.parseInt(input.trim());
            if (choice < min || choice > max) {
                throw new InvalidInputException("Menu Choice",
                        String.format("Please enter a number between %d and %d.", min, max));
            }
            return choice;
        } catch (NumberFormatException e) {
            throw new InvalidInputException("Menu Choice",
                    "Please enter a valid number.");
        }
    }
}
