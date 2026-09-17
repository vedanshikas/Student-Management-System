package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Utility class for reading and writing text-based data files.
 * Provides static helper methods for CSV file I/O operations.
 * Demonstrates file handling with BufferedReader / BufferedWriter.
 *
 * @author Smart Student Management System
 * @version 1.0
 */
public class FileHandler {

    /** Directory where all data files are stored. */
    private static final String DATA_DIR = "data";

    /* Ensure the data directory exists at class-load time. */
    static {
        File dir = new File(DATA_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    // ─── Write Operations ──────────────────────────────────────

    /**
     * Writes a list of strings as lines to the specified file,
     * replacing any existing content.
     *
     * @param filename The file name (relative to the data directory)
     * @param lines    The lines to write
     * @throws IOException if an I/O error occurs
     */
    public static void writeLines(String filename, List<String> lines) throws IOException {
        String filepath = DATA_DIR + File.separator + filename;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        }
    }

    /**
     * Appends a single line to the specified file.
     *
     * @param filename The file name (relative to the data directory)
     * @param line     The line to append
     * @throws IOException if an I/O error occurs
     */
    public static void appendLine(String filename, String line) throws IOException {
        String filepath = DATA_DIR + File.separator + filename;
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filepath, true))) {
            writer.write(line);
            writer.newLine();
        }
    }

    // ─── Read Operations ───────────────────────────────────────

    /**
     * Reads all non-empty lines from the specified file.
     * Returns an empty list if the file does not exist.
     *
     * @param filename The file name (relative to the data directory)
     * @return A list of trimmed, non-empty lines
     * @throws IOException if an I/O error occurs (other than file-not-found)
     */
    public static List<String> readLines(String filename) throws IOException {
        String filepath = DATA_DIR + File.separator + filename;
        List<String> lines = new ArrayList<>();
        File file = new File(filepath);

        if (!file.exists()) {
            return lines;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(filepath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    lines.add(line.trim());
                }
            }
        }
        return lines;
    }

    // ─── Utility ───────────────────────────────────────────────

    /**
     * Checks whether a data file exists.
     *
     * @param filename The file name (relative to the data directory)
     * @return true if the file exists
     */
    public static boolean fileExists(String filename) {
        return new File(DATA_DIR + File.separator + filename).exists();
    }

    /**
     * Deletes a data file.
     *
     * @param filename The file name (relative to the data directory)
     * @return true if the file was successfully deleted
     */
    public static boolean deleteFile(String filename) {
        File file = new File(DATA_DIR + File.separator + filename);
        return file.exists() && file.delete();
    }
}
