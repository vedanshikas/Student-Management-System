package service;

import model.Student;
import repository.StudentRepository;
import exception.DuplicateStudentException;
import exception.StudentNotFoundException;
import util.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * Service class for student CRUD operations.
 * Implements the {@link Manageable} interface – demonstrates
 * polymorphism through interface implementation.
 *
 * @author Smart Student Management System
 * @version 1.0
 */
public class StudentService implements Manageable<Student> {

    private final StudentRepository repository;
    private final Logger logger = Logger.getInstance();

    /**
     * Constructs the service with its required repository.
     *
     * @param repository The student data repository
     */
    public StudentService(StudentRepository repository) {
        this.repository = repository;
    }

    // ─── Manageable Contract ───────────────────────────────────

    @Override
    public void add(Student student) throws DuplicateStudentException {
        if (repository.exists(student.getStudentId())) {
            throw new DuplicateStudentException(
                    "Student with ID '" + student.getStudentId() + "' already exists.");
        }
        repository.add(student);
        logger.info("Student added: " + student.getStudentId() + " - " + student.getName());
    }

    @Override
    public void update(String id, Student student) throws StudentNotFoundException {
        if (!repository.exists(id)) {
            throw new StudentNotFoundException("Student not found with ID: " + id);
        }
        student.setStudentId(id);
        repository.update(student);
        logger.info("Student updated: " + id);
    }

    @Override
    public void delete(String id) throws StudentNotFoundException {
        if (!repository.exists(id)) {
            throw new StudentNotFoundException("Student not found with ID: " + id);
        }
        repository.delete(id);
        logger.info("Student deleted: " + id);
    }

    @Override
    public Student search(String id) throws StudentNotFoundException {
        Student student = repository.findById(id);
        if (student == null) {
            throw new StudentNotFoundException("Student not found with ID: " + id);
        }
        logger.info("Student searched: " + id);
        return student;
    }

    @Override
    public List<Student> getAll() {
        return repository.getAll();
    }

    @Override
    public void displayAll() {
        ArrayList<Student> students = repository.getAll();
        if (students.isEmpty()) {
            System.out.println("\n  No students found in the system.\n");
            return;
        }

        String header = String.format("| %-10s | %-20s | %-4s | %-25s | %-15s | %-12s |",
                "ID", "Name", "Age", "Email", "Department", "Enrolled");
        String separator = "-".repeat(header.length());

        System.out.println("\n" + separator);
        System.out.println(header);
        System.out.println(separator);
        for (Student s : students) {
            System.out.println(s.toString());
        }
        System.out.println(separator);
        System.out.printf("  Total Students: %d\n\n", students.size());
    }

    // ─── Additional Methods ────────────────────────────────────

    /**
     * Adds a student with an auto-generated ID and returns the new ID.
     *
     * @param student The student to add (ID will be overwritten)
     * @return The generated student ID
     * @throws DuplicateStudentException if a conflict occurs
     */
    public String addAndGenerateId(Student student) throws DuplicateStudentException {
        String newId = repository.generateNextId();
        student.setStudentId(newId);
        repository.add(student);
        logger.info("Student added with auto-ID: " + newId + " - " + student.getName());
        return newId;
    }

    /**
     * Returns the total number of students in the system.
     *
     * @return Student count
     */
    public int getStudentCount() {
        return repository.getAll().size();
    }
}
