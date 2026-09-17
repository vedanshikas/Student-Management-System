package repository;

import model.Marks;
import util.FileHandler;
import util.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Repository implementation for {@link Marks} entities.
 * Implements {@link DataRepository} and provides CSV-based persistence.
 *
 * @author Smart Student Management System
 * @version 1.0
 */
public class MarksRepository implements DataRepository<Marks> {

    private static final String FILENAME = "marks.csv";
    private final Logger logger = Logger.getInstance();

    /** In-memory cache of all marks records. */
    private ArrayList<Marks> marksList;

    /**
     * Constructs the repository and loads existing data from file.
     */
    public MarksRepository() {
        this.marksList = new ArrayList<>(loadAll());
        logger.info("MarksRepository initialised – loaded " + marksList.size() + " marks records.");
    }

    // ─── DataRepository Contract ───────────────────────────────

    @Override
    public void saveAll(List<Marks> items) {
        try {
            List<String> lines = items.stream()
                    .map(Marks::toCSV)
                    .collect(Collectors.toList());
            FileHandler.writeLines(FILENAME, lines);
            logger.info("Saved marks for " + items.size() + " students to file.");
        } catch (IOException e) {
            logger.error("Failed to save marks: " + e.getMessage());
            System.err.println("Error saving marks data: " + e.getMessage());
        }
    }

    @Override
    public List<Marks> loadAll() {
        try {
            List<String> lines = FileHandler.readLines(FILENAME);
            return lines.stream()
                    .map(Marks::fromCSV)
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            logger.error("Failed to load marks: " + e.getMessage());
            return new ArrayList<>();
        } catch (IllegalArgumentException e) {
            logger.error("Corrupt marks data: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    @Override
    public boolean exists(String studentId) {
        return marksList.stream().anyMatch(m -> m.getStudentId().equals(studentId));
    }

    // ─── CRUD Operations ───────────────────────────────────────

    /**
     * Adds a marks record and persists the change.
     *
     * @param marks The marks record to add
     */
    public void add(Marks marks) {
        marksList.add(marks);
        saveAll(marksList);
    }

    /**
     * Updates an existing marks record (matched by student ID) and persists.
     *
     * @param marks The updated marks object
     */
    public void update(Marks marks) {
        for (int i = 0; i < marksList.size(); i++) {
            if (marksList.get(i).getStudentId().equals(marks.getStudentId())) {
                marksList.set(i, marks);
                break;
            }
        }
        saveAll(marksList);
    }

    /**
     * Deletes a marks record by student ID and persists the change.
     *
     * @param studentId The student ID whose marks should be removed
     */
    public void delete(String studentId) {
        marksList.removeIf(m -> m.getStudentId().equals(studentId));
        saveAll(marksList);
    }

    /**
     * Finds marks by student ID.
     *
     * @param studentId The student ID to look up
     * @return The matching Marks record, or null if not found
     */
    public Marks findByStudentId(String studentId) {
        return marksList.stream()
                .filter(m -> m.getStudentId().equals(studentId))
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns a defensive copy of all marks records.
     *
     * @return A new ArrayList containing all marks
     */
    public ArrayList<Marks> getAll() {
        return new ArrayList<>(marksList);
    }
}
