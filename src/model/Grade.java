package model;

/**
 * Enum representing academic grades with corresponding mark ranges.
 * Demonstrates the use of enums with fields, constructors, and methods.
 *
 * @author Smart Student Management System
 * @version 1.0
 */
public enum Grade {
    A_PLUS("A+", 90, 100, "Outstanding"),
    A("A", 80, 89, "Excellent"),
    B_PLUS("B+", 70, 79, "Very Good"),
    B("B", 60, 69, "Good"),
    C("C", 50, 59, "Average"),
    D("D", 40, 49, "Below Average"),
    F("F", 0, 39, "Fail");

    private final String label;
    private final int minMarks;
    private final int maxMarks;
    private final String description;

    /**
     * Constructor for Grade enum.
     *
     * @param label       Short label for the grade
     * @param minMarks    Minimum marks for this grade
     * @param maxMarks    Maximum marks for this grade
     * @param description Description of the grade level
     */
    Grade(String label, int minMarks, int maxMarks, String description) {
        this.label = label;
        this.minMarks = minMarks;
        this.maxMarks = maxMarks;
        this.description = description;
    }

    // ─── Getters ───────────────────────────────────────────────

    public String getLabel() {
        return label;
    }

    public int getMinMarks() {
        return minMarks;
    }

    public int getMaxMarks() {
        return maxMarks;
    }

    public String getDescription() {
        return description;
    }

    /**
     * Checks whether this grade is a passing grade.
     *
     * @return true if the grade is not F
     */
    public boolean isPassing() {
        return this != F;
    }

    /**
     * Determines the grade based on an average marks value.
     * Demonstrates static factory method pattern.
     *
     * @param average The average marks to evaluate
     * @return The corresponding Grade enum value
     */
    public static Grade fromAverage(double average) {
        if (average >= 90.0) return A_PLUS;
        if (average >= 80.0) return A;
        if (average >= 70.0) return B_PLUS;
        if (average >= 60.0) return B;
        if (average >= 50.0) return C;
        if (average >= 40.0) return D;
        return F;
    }

    @Override
    public String toString() {
        return label + " (" + description + ")";
    }
}
