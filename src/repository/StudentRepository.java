package repository;

import model.Student;
import util.FileHandler;
import util.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Repository implementation for {@link Student} entities.
 * Implements {@link DataRepository} and uses {@link FileHandler}
 * for CSV-based persistence. Maintains an in-memory {@link ArrayList}
 * synchronised with the data file.
 *
 * @author Smart Student Management System
 * @version 1.0
 */
public class StudentRepository implements DataRepository<Student> {

    private static final String FILENAME = "students.csv";
    private final Logger logger = Logger.getInstance();

    /** In-memory cache of all students. */
    private ArrayList<Student> students;

    /**
     * Constructs the repository and loads existing data from file.
     */
    public StudentRepository() {
        this.students = new ArrayList<>(loadAll());
        logger.info("StudentRepository initialised – loaded " + students.size() + " students.");
    }

    // ─── DataRepository Contract ───────────────────────────────

    @Override
    public void saveAll(List<Student> items) {
        try {
            List<String> lines = items.stream()
                    .map(Student::toCSV)
                    .collect(Collectors.toList());
            FileHandler.writeLines(FILENAME, lines);
            logger.info("Saved " + items.size() + " students to file.");
        } catch (IOException e) {
            logger.error("Failed to save students: " + e.getMessage());
            System.err.println("Error saving student data: " + e.getMessage());
        }
    }

    @Override
    public List<Student> loadAll() {
        try {
            List<String> lines = FileHandler.readLines(FILENAME);
            return lines.stream()
                    .map(Student::fromCSV)
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            logger.error("Failed to load students: " + e.getMessage());
            return new ArrayList<>();
        } catch (IllegalArgumentException e) {
            logger.error("Corrupt student data: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public boolean exists(String id) {
        return students.stream().anyMatch(s -> s.getStudentId().equals(id));
    }

    // ─── CRUD Operations ───────────────────────────────────────

    /**
     * Adds a student to the in-memory list and persists the change.
     *
     * @param student The student to add
     */
    public void add(Student student) {
        students.add(student);
        saveAll(students);
    }

    /**
     * Updates an existing student (matched by student ID) and persists.
     *
     * @param student The updated student object
     */
    public void update(Student student) {
        for (int i = 0; i < students.size(); i++) {
            if (students.get(i).getStudentId().equals(student.getStudentId())) {
                students.set(i, student);
                break;
            }
        }
        saveAll(students);
    }

    /**
     * Deletes a student by ID and persists the change.
     *
     * @param studentId The ID of the student to remove
     */
    public void delete(String studentId) {
        students.removeIf(s -> s.getStudentId().equals(studentId));
        saveAll(students);
    }

    /**
     * Finds a student by their unique ID.
     *
     * @param studentId The student ID to look up
     * @return The matching Student, or null if not found
     */
    public Student findById(String studentId) {
        return students.stream()
                .filter(s -> s.getStudentId().equals(studentId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns a defensive copy of all students.
     *
     * @return A new ArrayList containing all students
     */
    public ArrayList<Student> getAll() {
        return new ArrayList<>(students);
    }

    /**
     * Generates the next sequential student ID (e.g., STU006).
     *
     * @return A new, unique student ID string
     */
    public String generateNextId() {
        int maxId = students.stream()
                .map(s -> {
                    try {
                        String id = s.getStudentId();
                        if (id != null && id.startsWith("STU") && id.length() > 3) {
                            return Integer.parseInt(id.substring(3).trim());
                        }
                        return 0;
                    } catch (Exception e) {
                        return 0;
                    }
                })
                .max(Integer::compareTo)
                .orElse(0);
        return String.format("STU%03d", maxId + 1);
    }
}
