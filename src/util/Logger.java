package util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Singleton logger that writes timestamped log entries to a file.
 * Demonstrates the Singleton design pattern and file I/O.
 *
 * @author Smart Student Management System
 * @version 1.0
 */
public class Logger {

    private static final String LOG_DIR = "logs";
    private static final String LOG_FILE = LOG_DIR + File.separator + "application.log";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    /** The single instance (eager-style would also work). */
    private static Logger instance;

    /**
     * Private constructor – creates the log directory if it does not exist.
     */
    private Logger() {
        File dir = new File(LOG_DIR);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    /**
     * Returns the singleton Logger instance (lazy initialisation).
     *
     * @return The Logger singleton
     */
    public static synchronized Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    // ─── Core Logging ──────────────────────────────────────────

    /**
     * Writes a log entry with a given severity level.
     *
     * @param level   The severity level (INFO, WARNING, ERROR, DEBUG)
     * @param message The message to log
     */
    private void writeLog(String level, String message) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String logEntry = String.format("[%s] [%-7s] %s", timestamp, level, message);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(LOG_FILE, true))) {
            writer.write(logEntry);
            writer.newLine();
        } catch (IOException e) {
            System.err.println("Error writing to log file: " + e.getMessage());
        }
    }

    // ─── Convenience Methods ───────────────────────────────────

    /**
     * Logs an informational message.
     *
     * @param message The message
     */
    public void info(String message) {
        writeLog("INFO", message);
    }

    /**
     * Logs a warning message.
     *
     * @param message The message
     */
    public void warning(String message) {
        writeLog("WARNING", message);
    }

    /**
     * Logs an error message.
     *
     * @param message The message
     */
    public void error(String message) {
        writeLog("ERROR", message);
    }

    /**
     * Logs a debug message.
     *
     * @param message The message
     */
    public void debug(String message) {
        writeLog("DEBUG", message);
    }
}
