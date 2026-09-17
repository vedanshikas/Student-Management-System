package exception;

/**
 * Custom exception thrown when user input fails validation.
 * Carries the name of the offending field for better error messages.
 *
 * @author Smart Student Management System
 * @version 1.0
 */
public class InvalidInputException extends Exception {

    private final String fieldName;

    /**
     * Constructs the exception with a generic message.
     *
     * @param message Descriptive error message
     */
    public InvalidInputException(String message) {
        super(message);
        this.fieldName = "Unknown";
    }

    /**
     * Constructs the exception with a field name and a specific message.
     *
     * @param fieldName The name of the field that failed validation
     * @param message   A human-readable description of the problem
     */
    public InvalidInputException(String fieldName, String message) {
        super(String.format("Invalid input for '%s': %s", fieldName, message));
        this.fieldName = fieldName;
    }

    /**
     * Returns the name of the field that caused the validation failure.
     *
     * @return Field name string
     */
    public String getFieldName() {
        return fieldName;
    }
}
