# AI-Powered Industrial Visual Inspection and Quality Management System

[![Java](https://img.shields.io/badge/Java-SE%208%2B-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Swing/AWT](https://img.shields.io/badge/GUI-Swing%2FAWT-blue?style=for-the-badge)](https://docs.oracle.com/javase/8/docs/technotes/guides/swing/)
[![ISO-9001](https://img.shields.io/badge/Compliance-ISO--9001-green?style=for-the-badge)](https://www.iso.org/iso-9001-quality-management.html)
[![JDBC](https://img.shields.io/badge/Persistence-JDBC%20Transactional-orange?style=for-the-badge)](https://docs.oracle.com/javase/8/docs/technotes/guides/jdbc/)

An end-to-end, single-file Java SE industrial quality-control application modeling an automated optical inspection (AOI) station on a smart manufacturing assembly line. The system demonstrates **all 15 core Object-Oriented Programming (OOP) and Java SE syllabus topics** in a single consolidated architecture.

---

## Key Features

- **Consolidated Architecture:** Complete system encapsulated within a single standalone file: [`IndustrialInspectionSystem.java`](IndustrialInspectionSystem.java).
- **Two Runtime Modes:**
  - **Desktop GUI Mode:** Full Swing/AWT operator dashboard featuring real-time optical camera simulation, glowing defect bounding boxes, live KPI cards (FPY, Defect Count, Average Score), dynamic catalog filtering, and JDBC CRUD controls.
  - **Automated CLI Verification Mode (`--cli`):** Headless test runner executing 6 test suites verifying all 15 Java topics with 100% automated pass confirmation.
- **Zero External Dependencies:** Built with pure Java SE standard libraries (no Maven, Gradle, or external JAR requirements).
- **Dual Source Distribution:**
  - `IndustrialInspectionSystem.java`: Full source code with detailed architectural engineering comments explaining the reason for implementing each of the 15 Java topics.
  - `IndustrialInspectionSystem_NoComments.java`: Clean, comment-free production code ideal for printing, report documentation, and academic appendix submission.

---

## 15 Core Java Syllabus Topics & Architectural Rationale

| # | Core Java Topic | Implementation Point | Architectural Reason for Implementation |
|:---:|---|---|---|
| **01** | **Encapsulation** | `DefectRecord`, `InspectionItem` | Private fields with public getters/setters protect sensitive quality metrics (confidence, severity, quality score) from unauthorized external mutation. |
| **02** | **Abstract Classes** | `InspectionItem` | All manufactured workpieces share common metadata (ID, batch, line), but the mathematical formula for computing quality scores differs between electronic PCBs and CNC parts. |
| **03** | **Inheritance** | `PCBComponent`, `MechanicalPart` | Eliminates duplicated boilerplate and establishes an extensible manufacturing domain hierarchy. |
| **04** | **Polymorphism & `@Override`** | `calculateFinalQualityScore()` | Enables the visual inspection engine to evaluate any manufactured part uniformly via a base `InspectionItem` reference using dynamic method dispatch. |
| **05** | **Method Overloading** | `addDefect(...)`, `updateRecord(...)` | Supports multiple data ingestion formats: pre-assembled `DefectRecord` objects vs. raw optical vision coordinates `(x, y, w, h, severity)`. |
| **06** | **Interfaces** | `Inspectable`, `QualityAuditor` | Decouples optical inspection algorithms and ISO-9001 reporting from the UI and persistence layers, allowing new sensor engines to plug in seamlessly. |
| **07** | **Static Keyword** | `FACILITY_NAME`, `globalInspectionCounter` | Universally shares facility metadata without heap duplication and maintains an atomic plant-wide counter across all threads. |
| **08** | **Final Keyword** | `PASS_THRESHOLD_SCORE`, `CRITICAL_SEVERITY_LIMIT` | Safety-critical quality thresholds and line-halt limits must remain immutable and protected against runtime corruption. |
| **09** | **`this` & `super` Keywords** | Constructors | Enforces proper constructor chaining so base properties are validated before subclass attributes initialize. |
| **10** | **Exception Handling** | `CriticalDefectException`, `InspectionException` | Simple return codes can be ignored by callers. Throwing checked exceptions forces an immediate emergency line-halt when severe defects occur. |
| **11** | **Multithreading** | `Thread`, `Runnable`, `synchronized` | Handles continuous camera conveyor arrivals concurrently without freezing or dropping frames on the operator's Swing UI. |
| **12** | **Collections Framework** | `ArrayList`, `Queue`, `Stack`, `Hashtable`, `Vector` | Uses specialized data structures matching factory workflows: FIFO `Queue` for conveyor lines, LIFO `Stack` for quarantine rework, `Hashtable` for station registries. |
| **13** | **Lambdas & Streams** | `Comparator`, `.stream().filter().collect()` | Enables declarative, high-speed sorting by quality scores and instant First-Pass Yield (FPY) calculations without error-prone nested loops. |
| **14** | **File I/O Streams** | `PrintWriter`, `BufferedReader`, `FileInputStream` | Character streams generate human-readable ISO-9001 CSV audit logs for Excel; Byte streams enable bit-exact binary snapshot backups. |
| **15** | **JDBC & Swing GUI** | `PreparedStatement`, `JFrame`, `JPanel` | Provides SQL-injection-proof transactional persistence alongside an operator dashboard with live defect bounding boxes and instant batch editing. |

---

## Quick Start & Execution

### Prerequisites
- Java Development Kit (JDK 8 or higher)
- Windows / macOS / Linux

### 1. Compile
```bash
javac IndustrialInspectionSystem.java
```

### 2. Run Desktop GUI Dashboard
```bash
java IndustrialInspectionSystem
```
*Or simply double-click `run.bat` on Windows.*

### 3. Run Automated CLI Test Suite (Headless / Terminal Mode)
```bash
java IndustrialInspectionSystem --cli
```

---

## Project Structure

```text
├── .gitignore                                 # Git ignore file for compiled classes & IDE files
├── IndustrialInspectionSystem.java            # Consolidated single-file system (with detailed topic reason comments)
├── IndustrialInspectionSystem_NoComments.java # Clean, non-commented source code for academic reports
├── README.md                                  # Complete project documentation and topic reference
├── run.bat                                    # One-click Windows compilation & launch script
└── data/
    └── inspection_audit.csv                   # ISO-9001 audit report exported via Character Streams
```

---

## Quality Metrics & Standards

- **First-Pass Yield (FPY):**
  $$\text{FPY} = \left(\frac{\text{Units Passed Without Rework}}{\text{Total Units Inspected}}\right) \times 100$$
- **Quality Score Deduction Formula:**
  - **PCB Component:** $\text{Score} = \text{Base} - \sum (\text{Defect Severity} \times 1.75) - (\text{Solder Risk} \times 2.5)$
  - **Mechanical Part:** $\text{Score} = \text{Base} - \sum (\text{Defect Severity} \times 2.2) - \text{Tolerance Deductions}$
- **Emergency Halt Limit:** Severity $\ge 8.0/10.0$ triggers immediate `CriticalDefectException` and pushes part to LIFO Quarantine Stack.

---

## License
Educational Microproject - Open Source for Academic & Demonstration Purposes.
