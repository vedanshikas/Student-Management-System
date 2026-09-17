# Smart Student Management System

A production-ready, modular Java console application for managing academic student profiles, marks evaluation, report generation, and real-time performance analytics. Built following strict Object-Oriented Programming (OOP) principles and enterprise design patterns.

---

## Key Features

- **Student Lifecycle Management**: Full CRUD operations for student records with manual or automated ID generation (`STUxxx`).
- **Academic Marks & Assessment**: Multi-subject evaluation with validation, cumulative totals, average percentages, and grade mapping.
- **Performance Reports**: Detailed individual student report cards and ranked class leaderboard matrices.
- **Statistical Analytics Engine**: Automatic topper detection, subject-wise and class averages, pass/fail metrics, and ASCII grade distribution charts.
- **Data Persistence & Audit Logging**: CSV file storage with automated synchronization and thread-safe singleton logging (`application.log`).
- **Interactive CLI & Demo Seeder**: Menu-driven console interface with robust input sanitization and one-step sample dataset seeding.

---

## Architecture & Design Patterns

The system follows a layered architecture enforcing separation of concerns:

1. **Model Layer (`model`)**: Encapsulated domain entities (`Student`, `Marks`, `Report`, `Grade`) with CSV serialization and standard object overrides (`equals`, `hashCode`, `toString`).
2. **Repository Layer (`repository`)**: Generic interface abstraction (`DataRepository<T>`) backed by CSV persistence and in-memory caching.
3. **Service Layer (`service`)**: Business logic implementations (`StudentService`, `MarksService`, `ReportService`, `AnalyticsService`) adhering to the `Manageable<T>` contract.
4. **Utility Layer (`util`)**: Reusable tools including `InputValidator` regex engine, `FileHandler` buffered I/O, and `Logger` singleton.
5. **Exception Handling (`exception`)**: Domain-specific checked exceptions (`DuplicateStudentException`, `StudentNotFoundException`, `InvalidInputException`).

---

## Project Structure

```text
Smart-Student-Management-System/
├── data/                          # CSV storage directory
│   ├── marks.csv                  # Persisted marks records
│   └── students.csv               # Persisted student profiles
├── logs/                          # Application execution logs
│   └── application.log            # Timestamped diagnostic logs
├── src/                           # Java source code
│   ├── exception/                 # Custom domain exceptions
│   │   ├── DuplicateStudentException.java
│   │   ├── InvalidInputException.java
│   │   └── StudentNotFoundException.java
│   ├── model/                     # Domain entity classes
│   │   ├── Grade.java             # Grade enum & threshold rules
│   │   ├── Marks.java             # Subject marks & calculations
│   │   ├── Report.java            # Formatted report model
│   │   └── Student.java           # Student entity
│   ├── repository/                # Persistence & Data Access Object layer
│   │   ├── DataRepository.java    # Generic repository interface
│   │   ├── MarksRepository.java   # Marks DAO implementation
│   │   └── StudentRepository.java # Student DAO implementation
│   ├── service/                   # Business logic services
│   │   ├── AnalyticsService.java  # Ranking & stats engine
│   │   ├── Manageable.java        # Generic CRUD service interface
│   │   ├── MarksService.java      # Marks processing service
│   │   ├── ReportService.java     # Formatted report generator
│   │   └── StudentService.java    # Student management service
│   ├── util/                      # Utilities & helpers
│   │   ├── FileHandler.java       # Buffered file I/O operations
│   │   ├── InputValidator.java    # Input validation & sanitization
│   │   └── Logger.java            # Singleton file logger
│   ├── Main.java                  # Interactive CLI entry point
│   └── SystemTest.java            # Automated verification test suite
├── run.bat                        # Windows one-click compile & run script
├── run.sh                         # Unix/macOS compile & run script
└── README.md                      # Comprehensive project documentation
```

---

## Grading Scale

| Grade | Range (%) | Description | Passing Status |
|:-----:|:---------:|:-----------:|:--------------:|
| **A+** | 90 - 100 | Outstanding | Pass |
| **A** | 80 - 89.99 | Excellent | Pass |
| **B+** | 70 - 79.99 | Very Good | Pass |
| **B** | 60 - 69.99 | Good | Pass |
| **C** | 50 - 59.99 | Average | Pass |
| **D** | 40 - 49.99 | Below Average | Pass |
| **F** | 0 - 39.99 | Fail | Fail |

---

## Prerequisites

- **Java Development Kit (JDK)**: Version 11 or higher (Tested on OpenJDK / Oracle JDK 17, 21, 24).
- Operating System: Windows, macOS, or Linux.

---

## Quick Start & Execution

### 1. Run via Automated Scripts
- **Windows**: Double-click `run.bat` or execute in Command Prompt:
  ```cmd
  .\run.bat
  ```
- **Linux / macOS**: Grant permissions and execute:
  ```bash
  chmod +x run.sh && ./run.sh
  ```

### 2. Manual Compilation and Execution
```bash
javac -d bin src/exception/*.java src/model/*.java src/repository/*.java src/service/*.java src/util/*.java src/Main.java src/SystemTest.java
java -cp bin Main
```

### 3. Running Automated Test Suite
```bash
java -cp bin SystemTest
```

---

## License

Released under the MIT License. Developed for educational and institutional management demonstration.