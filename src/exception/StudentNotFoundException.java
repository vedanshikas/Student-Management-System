package exception;

/**
 * Custom exception thrown when a student is not found in the system.
 * Demonstrates inheritance by extending the Exception class.
 *
 * @author Smart Student Management System
 * @version 1.0
 */
public class StudentNotFoundException extends Exception {

    /**
     * Constructs the exception with a custom message.
     *
     * @param message Descriptive error message
     */
    public StudentNotFoundException(String message) {
        super(message);
    }

    /**
     * Constructs the exception using a student ID and a root cause.
     *
     * @param studentId The ID that was not found
     * @param cause     The underlying exception
     */
    public StudentNotFoundException(String studentId, Throwable cause) {
        super("Student not found with ID: " + studentId, cause);
    }
}
