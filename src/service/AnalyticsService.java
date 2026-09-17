package service;

import model.Marks;
import model.Student;
import repository.MarksRepository;
import repository.StudentRepository;
import util.Logger;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service class providing analytics and statistical operations
 * on student performance data.
 *
 * @author Smart Student Management System
 * @version 1.0
 */
public class AnalyticsService {

    private final StudentRepository studentRepository;
    private final MarksRepository marksRepository;
    private final Logger logger = Logger.getInstance();

    /**
     * Constructs the analytics service with required repositories.
     *
     * @param studentRepository The student data repository
     * @param marksRepository   The marks data repository
     */
    public AnalyticsService(StudentRepository studentRepository,
                            MarksRepository marksRepository) {
        this.studentRepository = studentRepository;
        this.marksRepository = marksRepository;
    }

    // ─── Top Performer ─────────────────────────────────────────

    /**
     * Displays the student with the highest average marks.
     */
    public void displayTopPerformer() {
        List<Marks> allMarks = marksRepository.getAll();
        if (allMarks.isEmpty()) {
            System.out.println("\n  No marks data available.\n");
            return;
        }

        Marks topMarks = allMarks.stream()
                .max(Comparator.comparingDouble(Marks::getAverageMarks))
                .orElse(null);

        if (topMarks != null) {
            Student student = studentRepository.findById(topMarks.getStudentId());

            System.out.println("\n  +========================================+");
            System.out.println("  |        ** TOP PERFORMER **             |");
            System.out.println("  +========================================+");
            if (student != null) {
                System.out.printf("  |  Name    : %-27s |\n", student.getName());
            }
            System.out.printf("  |  ID      : %-27s |\n", topMarks.getStudentId());
            System.out.printf("  |  Average : %-27.2f |\n", topMarks.getAverageMarks());
            System.out.printf("  |  Grade   : %-27s |\n", topMarks.getGrade().getLabel());
            System.out.printf("  |  Total   : %-27.1f |\n", topMarks.getTotalMarks());
            System.out.println("  +========================================+\n");

            logger.info("Top performer displayed: " + topMarks.getStudentId());
        }
    }

    // ─── Class Average ─────────────────────────────────────────

    /**
     * Displays the overall class average and subject-wise averages.
     */
    public void displayClassAverage() {
        List<Marks> allMarks = marksRepository.getAll();
        if (allMarks.isEmpty()) {
            System.out.println("\n  No marks data available.\n");
            return;
        }

        double classAvg = allMarks.stream()
                .mapToDouble(Marks::getAverageMarks)
                .average()
                .orElse(0.0);

        // Build subject-wise averages using HashMap
        HashMap<String, List<Double>> subjectWise = new HashMap<>();
        for (Marks m : allMarks) {
            for (Map.Entry<String, Double> entry : m.getSubjectMarks().entrySet()) {
                subjectWise.computeIfAbsent(entry.getKey(), k -> new ArrayList<>())
                        .add(entry.getValue());
            }
        }

        System.out.println("\n  +--------------------------------------------+");
        System.out.println("  |          CLASS AVERAGE REPORT              |");
        System.out.println("  +--------------------------------------------+");
        System.out.printf("  |  Overall Class Average : %-17.2f |\n", classAvg);
        System.out.println("  +--------------------------------------------+");
        System.out.println("  |  Subject-wise Averages:                    |");
        System.out.println("  |  ----------------------------------------  |");

        for (Map.Entry<String, List<Double>> entry : subjectWise.entrySet()) {
            double avg = entry.getValue().stream()
                    .mapToDouble(Double::doubleValue)
                    .average()
                    .orElse(0.0);
            System.out.printf("  |  %-20s : %6.2f              |\n", entry.getKey(), avg);
        }
        System.out.println("  +--------------------------------------------+\n");

        logger.info("Class average report displayed.");
    }

    // ─── Pass / Fail Statistics ────────────────────────────────

    /**
     * Displays pass/fail counts, percentages, and grade distribution.
     */
    public void displayPassFailStatistics() {
        List<Marks> allMarks = marksRepository.getAll();
        if (allMarks.isEmpty()) {
            System.out.println("\n  No marks data available.\n");
            return;
        }

        long passed = allMarks.stream().filter(Marks::isPassed).count();
        long failed = allMarks.size() - passed;
        double passPercentage = (double) passed / allMarks.size() * 100;
        double failPercentage = (double) failed / allMarks.size() * 100;

        System.out.println("\n  +--------------------------------------------+");
        System.out.println("  |        PASS / FAIL STATISTICS              |");
        System.out.println("  +--------------------------------------------+");
        System.out.printf("  |  Total Students      : %-19d |\n", allMarks.size());
        System.out.printf("  |  Passed              : %-3d (%5.1f%%)         |\n",
                passed, passPercentage);
        System.out.printf("  |  Failed              : %-3d (%5.1f%%)         |\n",
                failed, failPercentage);
        System.out.println("  +--------------------------------------------+");

        // Grade distribution using HashMap
        HashMap<String, Integer> gradeDist = new HashMap<>();
        for (Marks m : allMarks) {
            String grade = m.getGrade().getLabel();
            gradeDist.merge(grade, 1, Integer::sum);
        }

        System.out.println("  |  Grade Distribution:                       |");
        System.out.println("  |  ----------------------------------------  |");
        for (Map.Entry<String, Integer> entry : gradeDist.entrySet()) {
            int barLen = (int) ((double) entry.getValue() / allMarks.size() * 20);
            if (barLen == 0 && entry.getValue() > 0) barLen = 1;
            String bar = "#".repeat(barLen) + ".".repeat(20 - barLen);
            System.out.printf("  |  Grade %-3s: %s  %d    |\n",
                    entry.getKey(), bar, entry.getValue());
        }
        System.out.println("  +--------------------------------------------+\n");

        logger.info("Pass/Fail statistics displayed.");
    }

    // ─── Ranking ───────────────────────────────────────────────

    /**
     * Returns a list of student IDs sorted by average marks (descending).
     *
     * @return Ordered list of student IDs
     */
    public List<String> getRankings() {
        return marksRepository.getAll().stream()
                .sorted(Comparator.comparingDouble(Marks::getAverageMarks).reversed())
                .map(Marks::getStudentId)
                .collect(Collectors.toList());
    }

    /**
     * Returns the class rank (1-based) for a given student.
     *
     * @param studentId The student's ID
     * @return The rank, or 0 if the student has no marks
     */
    public int getRank(String studentId) {
        List<String> rankings = getRankings();
        int index = rankings.indexOf(studentId);
        return index >= 0 ? index + 1 : 0;
    }
}
