package exception;

/**
 * Custom exception thrown when attempting to add a student
 * whose ID already exists in the system.
 *
 * @author Smart Student Management System
 * @version 1.0
 */
public class DuplicateStudentException extends Exception {

    /**
     * Constructs the exception with a custom message.
     *
     * @param message Descriptive error message
     */
    public DuplicateStudentException(String message) {
        super(message);
    }

    /**
     * Convenience constructor that formats a message using the duplicate ID.
     *
     * @param studentId The ID that already exists
     * @param ignored   Disambiguation parameter (not used)
     */
    public DuplicateStudentException(String studentId, boolean ignored) {
        super("Student with ID '" + studentId + "' already exists in the system.");
    }
}
