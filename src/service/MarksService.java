package service;

import model.Grade;
import model.Marks;
import repository.MarksRepository;
import exception.StudentNotFoundException;
import util.Logger;

import java.util.List;

/**
 * Service class for managing student marks, including CRUD operations,
 * calculations, and grade determination.
 *
 * @author Smart Student Management System
 * @version 1.0
 */
public class MarksService {

    private final MarksRepository repository;
    private final Logger logger = Logger.getInstance();

    /**
     * Constructs the service with its required repository.
     *
     * @param repository The marks data repository
     */
    public MarksService(MarksRepository repository) {
        this.repository = repository;
    }

    // ─── CRUD Operations ───────────────────────────────────────

    /**
     * Adds marks for a student. If marks already exist, they are updated.
     *
     * @param marks The marks record to add or update
     */
    public void addMarks(Marks marks) {
        if (repository.exists(marks.getStudentId())) {
            repository.update(marks);
            logger.info("Marks updated for student: " + marks.getStudentId());
        } else {
            repository.add(marks);
            logger.info("Marks added for student: " + marks.getStudentId());
        }
    }

    /**
     * Updates marks for a specific subject.
     *
     * @param studentId The student's ID
     * @param subject   The subject name
     * @param newMarks  The new marks value
     * @throws StudentNotFoundException if no marks exist for the student
     */
    public void updateMarks(String studentId, String subject, double newMarks)
            throws StudentNotFoundException {
        Marks marks = repository.findByStudentId(studentId);
        if (marks == null) {
            throw new StudentNotFoundException("No marks found for student: " + studentId);
        }
        marks.addSubjectMark(subject, newMarks);
        repository.update(marks);
        logger.info("Marks updated for " + studentId + " in " + subject + ": " + newMarks);
    }

    /**
     * Retrieves the marks record for a student.
     *
     * @param studentId The student's ID
     * @return The marks record
     * @throws StudentNotFoundException if no marks exist for the student
     */
    public Marks getMarks(String studentId) throws StudentNotFoundException {
        Marks marks = repository.findByStudentId(studentId);
        if (marks == null) {
            throw new StudentNotFoundException("No marks found for student: " + studentId);
        }
        return marks;
    }

    /**
     * Deletes all marks for a student.
     *
     * @param studentId The student's ID
     */
    public void deleteMarks(String studentId) {
        repository.delete(studentId);
        logger.info("Marks deleted for student: " + studentId);
    }

    // ─── Calculation Methods ───────────────────────────────────

    /**
     * Calculates the total marks for a student.
     *
     * @param studentId The student's ID
     * @return Total marks
     * @throws StudentNotFoundException if no marks exist
     */
    public double calculateTotal(String studentId) throws StudentNotFoundException {
        return getMarks(studentId).getTotalMarks();
    }

    /**
     * Calculates the average marks for a student.
     *
     * @param studentId The student's ID
     * @return Average marks
     * @throws StudentNotFoundException if no marks exist
     */
    public double calculateAverage(String studentId) throws StudentNotFoundException {
        return getMarks(studentId).getAverageMarks();
    }

    /**
     * Determines the grade for a student based on average marks.
     *
     * @param studentId The student's ID
     * @return The Grade enum value
     * @throws StudentNotFoundException if no marks exist
     */
    public Grade getGrade(String studentId) throws StudentNotFoundException {
        return getMarks(studentId).getGrade();
    }

    // ─── Display ───────────────────────────────────────────────

    /**
     * Displays marks for a specific student in formatted output.
     *
     * @param studentId The student's ID
     * @throws StudentNotFoundException if no marks exist
     */
    public void displayMarks(String studentId) throws StudentNotFoundException {
        Marks marks = getMarks(studentId);
        System.out.println("\n" + marks.toString());
    }

    // ─── Query Methods ─────────────────────────────────────────

    /**
     * Returns all marks records.
     *
     * @return List of all Marks
     */
    public List<Marks> getAllMarks() {
        return repository.getAll();
    }

    /**
     * Checks whether marks exist for a given student.
     *
     * @param studentId The student's ID
     * @return true if marks are present
     */
    public boolean hasMarks(String studentId) {
        return repository.exists(studentId);
    }
}
