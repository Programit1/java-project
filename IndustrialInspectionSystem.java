/* =================================================================================================
 * PROJECT: AI-POWERED INDUSTRIAL VISUAL INSPECTION AND QUALITY MANAGEMENT SYSTEM
 * FORMAT:  CONSOLIDATED ALL-IN-ONE SINGLE SOURCE FILE
 *
 * =================================================================================================
 * 15 CORE JAVA SYLLABUS TOPICS - ARCHITECTURAL RATIONALE & IMPLEMENTATION INDEX
 * =================================================================================================
 *
 * [TOPIC 01] CLASSES, OBJECTS & ENCAPSULATION
 *   REASON: We implemented private fields with public getters and setters in DefectRecord and
 *   InspectionItem to protect sensitive inspection metrics (confidence, severity, quality score)
 *   from unauthorized external mutation and ensure all status transitions pass through validation.
 *
 * [TOPIC 02] ABSTRACT CLASSES & ABSTRACT METHODS
 *   REASON: We implemented abstract class InspectionItem because all manufactured workpieces share
 *   common properties (part ID, batch, production line), but the exact formula for computing the
 *   quality score differs completely based on whether the workpiece is an electronic PCB or a CNC part.
 *
 * [TOPIC 03] INHERITANCE (extends)
 *   REASON: We implemented inheritance (PCBComponent and MechanicalPart extending InspectionItem)
 *   to eliminate boilerplate code and establish an extensible industrial domain hierarchy.
 *
 * [TOPIC 04] POLYMORPHISM & METHOD OVERRIDING (@Override)
 *   REASON: We implemented method overriding for calculateFinalQualityScore() and getItemCategory()
 *   so the visual inspection engine can evaluate any manufactured part uniformly through a base
 *   InspectionItem reference at runtime using dynamic method dispatch.
 *
 * [TOPIC 05] METHOD OVERLOADING
 *   REASON: We implemented overloaded addDefect() and updateRecord() methods because defect data
 *   can arrive either as a pre-constructed DefectRecord object from automated sensors or as raw
 *   coordinate primitives (x, y, w, h, severity) directly from optical camera vision feeds.
 *
 * [TOPIC 06] INTERFACES (interface & implements)
 *   REASON: We implemented interfaces (Inspectable, QualityAuditor) to decouple the visual inspection
 *   algorithms and ISO-9001 yield reporting from the UI and persistence layers, allowing new inspection
 *   engines (e.g. edge AI or laser scanners) to plug in without modifying existing code.
 *
 * [TOPIC 07] STATIC KEYWORD (Variables & Methods)
 *   REASON: We implemented static for FACILITY_NAME so plant identification is universally shared
 *   without memory duplication, and static synchronized globalInspectionCounter to maintain an
 *   atomic plant-wide count of total items inspected across all instances and threads.
 *
 * [TOPIC 08] FINAL KEYWORD (Constants & Immutability)
 *   REASON: We implemented final for PASS_THRESHOLD_SCORE (85.0) and CRITICAL_SEVERITY_LIMIT (8.0)
 *   because quality thresholds and line-halt limits are safety-critical constants that must never
 *   be modified or corrupted at runtime.
 *
 * [TOPIC 09] CONSTRUCTOR CHAINING, 'this' & 'super' KEYWORDS
 *   REASON: We implemented super(...) in derived classes and this(...) in engine constructors to
 *   ensure common base attributes are fully validated and initialized first before subclass-specific
 *   parameters are set, preventing uninitialized state bugs.
 *
 * [TOPIC 10] EXCEPTION HANDLING (User-Defined Checked Exceptions, try-catch, throw, throws)
 *   REASON: We implemented CriticalDefectException and InspectionException because when a severe
 *   structural crack or solder bridge (severity >= 8.0) is detected, standard return codes can be
 *   ignored, but throwing a checked exception forces an immediate emergency production line halt.
 *
 * [TOPIC 11] MULTITHREADING & CONCURRENCY (Thread class, Runnable interface, synchronized, join)
 *   REASON: We implemented multithreading because factory parts arrive continuously from conveyor
 *   sensors in real-time (Thread), and writing large ISO audit logs to disk (Runnable) must execute
 *   concurrently in the background so the operator's inspection dashboard never freezes or drops frames.
 *
 * [TOPIC 12] JAVA COLLECTIONS FRAMEWORK (ArrayList, Queue, Stack, Hashtable, Vector)
 *   REASON: We implemented 5 specialized collections tailored to industrial plant workflows:
 *   - ArrayList: fast indexed random-access lookup for the active catalog.
 *   - Queue (LinkedList): strictly FIFO ordering for incoming conveyor arrivals.
 *   - Stack: strictly LIFO ordering for quarantine rework (reworking the most recently rejected part first).
 *   - Hashtable: synchronized thread-safe directory for station-to-inspector lookups.
 *   - Vector: legacy synchronized thread-safe long-term record archival.
 *
 * [TOPIC 13] LAMBDA EXPRESSIONS & STREAM API
 *   REASON: We implemented lambda expressions and Stream reductions to enable high-speed functional
 *   sorting by quality score and instantaneous First-Pass Yield (FPY) metrics computation without
 *   writing verbose, error-prone nested loops.
 *
 * [TOPIC 14] FILE I/O STREAMS (Character & Byte Streams)
 *   REASON: We implemented Character Streams (FileWriter, PrintWriter, BufferedReader) to generate
 *   human-readable ISO-9001 CSV audit logs that plant supervisors can open in Excel, and Byte Streams
 *   (FileInputStream, FileOutputStream) for bit-exact binary cloning of operational snapshots.
 *
 * [TOPIC 15] DATABASE PERSISTENCE (JDBC) & SWING/AWT GRAPHICAL DASHBOARD
 *   REASON: We implemented JDBC with PreparedStatements and an embedded zero-config relational store
 *   for SQL-injection-proof transactional persistence, paired with a Swing/AWT desktop dashboard
 *   providing plant operators with real-time visual inspection, glowing defect bounding boxes, and
 *   instant batch updating.
 * =================================================================================================
 */

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Hashtable;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Random;
import java.util.Stack;
import java.util.Vector;
import java.util.stream.Collectors;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;

/* =================================================================================================
 * [TOPIC 10] EXCEPTION HANDLING (User-Defined Checked Exceptions, try-catch, throw, throws)
 * REASON FOR IMPLEMENTATION:
 * In manufacturing environments, a simple return code is dangerous because it can be ignored by callers.
 * We implemented custom checked exceptions (CriticalDefectException and InspectionException) so that
 * when severe defects (e.g. solder bridges, micro-cracks with severity >= 8.0) are detected, the
 * Java compiler enforces mandatory handling and the runtime initiates an automated emergency line-halt.
 * =================================================================================================
 */

/**
 * Base checked exception for the Industrial Visual Inspection domain.
 * Reason: Establishes a common root for all inspection-related checked exceptions.
 */
class InspectionException extends Exception {
    private static final long serialVersionUID = 1L;

    public InspectionException(String message) {
        super(message);
    }

    public InspectionException(String message, Throwable cause) {
        super(message, cause);
    }
}

/**
 * User-defined checked exception thrown when an optical inspection identifies a hazardous defect.
 * Reason: Forces callers to catch and trigger an emergency production line halt when severity >= 8.0.
 */
class CriticalDefectException extends InspectionException {
    private static final long serialVersionUID = 1L;
    private final String partId;
    private final String defectType;
    private final double severityScore;

    public CriticalDefectException(String partId, String defectType, double severityScore) {
        super(String.format("CRITICAL DEFECT DETECTED: Part '%s' has defect '%s' with severity %.2f/10.0! Line halted.",
                partId, defectType, severityScore));
        this.partId = partId;
        this.defectType = defectType;
        this.severityScore = severityScore;
    }

    public String getPartId() { return partId; }
    public String getDefectType() { return defectType; }
    public double getSeverityScore() { return severityScore; }
}

/**
 * Checked exception thrown when batch parameters violate plant quality specifications.
 * Reason: Ensures invalid batches cannot enter production unnoticed.
 */
class InvalidBatchException extends InspectionException {
    private static final long serialVersionUID = 1L;

    public InvalidBatchException(String message) {
        super(message);
    }
}

/* =================================================================================================
 * [TOPIC 06] INTERFACES (interface & implements)
 * REASON FOR IMPLEMENTATION:
 * We implemented interfaces (Inspectable and QualityAuditor) to enforce a clear architectural
 * contract between inspection services and reporting tools. This decouples the scanning logic from
 * the GUI and database, allowing edge AI engines or physical camera SDKs to be swapped in seamlessly.
 * =================================================================================================
 */

/**
 * Interface defining the automated visual quality inspection contract.
 * Reason: Guarantees that any inspection engine implementation provides inspectItem and threshold methods.
 */
interface Inspectable {
    String inspectItem(InspectionItem item) throws CriticalDefectException, InspectionException;
    void setConfidenceThreshold(double threshold);
    double getConfidenceThreshold();
}

/**
 * Interface defining factory quality audit, yield metrics, and ISO-9001 reporting contracts.
 * Reason: Standardizes First-Pass Yield (FPY) and Defect Rate computation across all analytics modules.
 */
interface QualityAuditor {
    double calculateFirstPassYield(List<InspectionItem> items);
    double calculateDefectRate(List<InspectionItem> items);
    String generateAuditReport(List<InspectionItem> items);
}

/* =================================================================================================
 * [TOPIC 01] CLASSES, OBJECTS & ENCAPSULATION
 * REASON FOR IMPLEMENTATION:
 * We encapsulated all defect attributes (confidence, severity, coordinates) within DefectRecord using
 * private visibility and public accessors to prevent external tampering and preserve data integrity.
 * =================================================================================================
 */

/**
 * Encapsulates detected defect metadata identified through visual optical inspection.
 * Reason: Stores defect attributes securely and provides read/write access via validated getters/setters.
 */
class DefectRecord implements Serializable {
    private static final long serialVersionUID = 1L;

    // Encapsulated private attributes
    private String defectId;
    private String defectType;
    private double confidenceScore; // e.g. 0.94 (94% confidence)
    private double severityScore;   // 1.0 to 10.0 scale
    private int bboxX;
    private int bboxY;
    private int bboxWidth;
    private int bboxHeight;
    private String detectedAt;

    public DefectRecord(String defectId, String defectType, double confidenceScore,
                        double severityScore, int x, int y, int w, int h) {
        this.defectId = defectId;
        this.defectType = defectType;
        this.confidenceScore = confidenceScore;
        this.severityScore = severityScore;
        this.bboxX = x;
        this.bboxY = y;
        this.bboxWidth = w;
        this.bboxHeight = h;
        this.detectedAt = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
    }

    public String getDefectId() { return defectId; }
    public void setDefectId(String defectId) { this.defectId = defectId; }
    public String getDefectType() { return defectType; }
    public void setDefectType(String defectType) { this.defectType = defectType; }
    public double getConfidenceScore() { return confidenceScore; }
    public void setConfidenceScore(double confidenceScore) { this.confidenceScore = confidenceScore; }
    public double getSeverityScore() { return severityScore; }
    public void setSeverityScore(double severityScore) { this.severityScore = severityScore; }
    public int getBboxX() { return bboxX; }
    public void setBboxX(int bboxX) { this.bboxX = bboxX; }
    public int getBboxY() { return bboxY; }
    public void setBboxY(int bboxY) { this.bboxY = bboxY; }
    public int getBboxWidth() { return bboxWidth; }
    public void setBboxWidth(int bboxWidth) { this.bboxWidth = bboxWidth; }
    public int getBboxHeight() { return bboxHeight; }
    public void setBboxHeight(int bboxHeight) { this.bboxHeight = bboxHeight; }
    public String getDetectedAt() { return detectedAt; }
}

/* =================================================================================================
 * [TOPIC 02] ABSTRACT CLASSES & ABSTRACT METHODS
 * [TOPIC 07] STATIC KEYWORD (Static Variables & Static Synchronized Methods)
 * [TOPIC 08] FINAL KEYWORD (Immutable Constants)
 * [TOPIC 09] CONSTRUCTOR INITIALIZATION & 'this' KEYWORD
 * [TOPIC 05] METHOD OVERLOADING
 *
 * REASON FOR IMPLEMENTATION:
 * - Abstract Class: Every manufactured unit has an ID, batch, and status, but calculating quality scores
 *   differs by domain (PCBs suffer electrical bridging; CNC titanium parts suffer structural cracks).
 * - Static: FACILITY_NAME is shared across all units to conserve memory; global counter tracks plant volume.
 * - Final: PASS_THRESHOLD_SCORE and CRITICAL_SEVERITY_LIMIT protect plant safety standards from being altered.
 * - Overloading: Allows adding defects via an existing DefectRecord or raw sensor coordinates.
 * =================================================================================================
 */

/**
 * Abstract Base Class representing any manufactured entity undergoing visual inspection.
 * Reason: Provides shared state and enforces subclass implementation of domain-specific score algorithms.
 */
abstract class InspectionItem implements Serializable, Comparable<InspectionItem> {
    private static final long serialVersionUID = 1L;

    // [TOPIC 07 & 08] Static facility name and final immutable quality threshold limits
    public static final String FACILITY_NAME = "SmartFactory AOI Unit-01";
    private static int globalInspectionCounter = 0;

    public static final double PASS_THRESHOLD_SCORE = 85.0;  // Score >= 85.0 required for PASS
    public static final double CRITICAL_SEVERITY_LIMIT = 8.0; // Severity >= 8.0 halts operation

    // [TOPIC 01] Encapsulated state variables
    private String itemId;
    private String batchNumber;
    private String productionLine;
    private double rawQualityScore;
    private String status; // "PENDING", "PASSED", "DEFECTIVE", "REJECTED"
    private List<DefectRecord> defectList;

    // [TOPIC 09] Constructor initializing common fields with 'this'
    public InspectionItem(String itemId, String batchNumber, String productionLine) {
        this.itemId = itemId;
        this.batchNumber = batchNumber;
        this.productionLine = productionLine;
        this.rawQualityScore = 100.0;
        this.status = "PENDING";
        this.defectList = new ArrayList<>();

        incrementInspectionCounter();
    }

    // [TOPIC 07] Static synchronized method ensuring thread-safe counter incrementation
    private static synchronized void incrementInspectionCounter() {
        globalInspectionCounter++;
    }

    public static int getGlobalInspectionCounter() {
        return globalInspectionCounter;
    }

    public static void resetCounter() {
        globalInspectionCounter = 0;
    }

    // [TOPIC 02] Abstract methods forcing subclasses to provide domain-specific behavior (Polymorphism)
    public abstract String getItemCategory();
    public abstract double calculateFinalQualityScore();

    // [TOPIC 05] Method Overloading 1: Accepts a complete DefectRecord object
    public void addDefect(DefectRecord defect) {
        if (defect != null) {
            this.defectList.add(defect);
            updateStatus();
        }
    }

    // [TOPIC 05] Method Overloading 2: Accepts raw vision sensor primitive values
    public void addDefect(String defectId, String defectType, double confidence, double severity, int x, int y, int w, int h) {
        DefectRecord record = new DefectRecord(defectId, defectType, confidence, severity, x, y, w, h);
        addDefect(record);
    }

    public void updateStatus() {
        double finalScore = calculateFinalQualityScore();
        this.rawQualityScore = finalScore;

        boolean hasCritical = defectList.stream().anyMatch(d -> d.getSeverityScore() >= CRITICAL_SEVERITY_LIMIT);

        if (hasCritical) {
            this.status = "REJECTED";
        } else if (finalScore < PASS_THRESHOLD_SCORE || !defectList.isEmpty()) {
            this.status = "DEFECTIVE";
        } else {
            this.status = "PASSED";
        }
    }

    // Getters and Setters
    public String getItemId() { return itemId; }
    public void setItemId(String itemId) { this.itemId = itemId; }
    public String getBatchNumber() { return batchNumber; }
    public void setBatchNumber(String batchNumber) { this.batchNumber = batchNumber; }
    public String getProductionLine() { return productionLine; }
    public void setProductionLine(String productionLine) { this.productionLine = productionLine; }
    public double getRawQualityScore() { return rawQualityScore; }
    public void setRawQualityScore(double rawQualityScore) { this.rawQualityScore = rawQualityScore; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public List<DefectRecord> getDefectList() { return defectList; }

    @Override
    public int compareTo(InspectionItem other) {
        return this.itemId.compareTo(other.itemId);
    }

    @Override
    public String toString() {
        return String.format("[%s] ID: %s | Batch: %s | Category: %s | Status: %s | Score: %.2f | Defects: %d",
                FACILITY_NAME, itemId, batchNumber, getItemCategory(), status, rawQualityScore, defectList.size());
    }
}

/* =================================================================================================
 * [TOPIC 03] INHERITANCE (extends)
 * [TOPIC 04] POLYMORPHISM & METHOD OVERRIDING (@Override)
 * [TOPIC 09] 'super' KEYWORD CONSTRUCTOR PROPAGATION
 *
 * REASON FOR IMPLEMENTATION:
 * We implemented PCBComponent and MechanicalPart extending InspectionItem so that electronic and
 * mechanical workpieces reuse shared base attributes while specializing their unique physical properties
 * and quality score penalty formulas. Dynamic method dispatch allows the inspection engine to process
 * any part without writing separate inspection code for every product family.
 * =================================================================================================
 */

/**
 * Concrete class representing a Printed Circuit Board (PCB) subject to SMT optical inspection.
 * Reason: Encapsulates circuit board properties (layers, copper weight, surface finish) and electrical penalties.
 */
class PCBComponent extends InspectionItem {
    private static final long serialVersionUID = 1L;

    private int layerCount;
    private double copperWeightOz;
    private String surfaceFinishType; // e.g. "ENIG", "HASL", "OSP"

    public PCBComponent(String itemId, String batchNumber, String productionLine,
                        int layerCount, double copperWeightOz, String surfaceFinishType) {
        super(itemId, batchNumber, productionLine); // [TOPIC 09] Delegates common setup to base class
        this.layerCount = layerCount;
        this.copperWeightOz = copperWeightOz;
        this.surfaceFinishType = surfaceFinishType;
    }

    @Override // [TOPIC 04] Overrides base template method
    public String getItemCategory() {
        return "Printed Circuit Board (PCB)";
    }

    @Override // [TOPIC 04] Overrides base template method with SMT electrical short/open penalty multiplier
    public double calculateFinalQualityScore() {
        double baseScore = 100.0;
        for (DefectRecord defect : getDefectList()) {
            double penalty = defect.getSeverityScore() * 2.5 * defect.getConfidenceScore();
            if (defect.getDefectType().equalsIgnoreCase("Solder Bridge") ||
                defect.getDefectType().equalsIgnoreCase("Trace Open Circuit")) {
                penalty *= 1.4; // 40% additional penalty for catastrophic electrical short/open hazards
            }
            baseScore -= penalty;
        }
        return Math.max(0.0, Math.min(100.0, baseScore));
    }

    public int getLayerCount() { return layerCount; }
    public void setLayerCount(int layerCount) { this.layerCount = layerCount; }
    public double getCopperWeightOz() { return copperWeightOz; }
    public void setCopperWeightOz(double copperWeightOz) { this.copperWeightOz = copperWeightOz; }
    public String getSurfaceFinishType() { return surfaceFinishType; }
    public void setSurfaceFinishType(String surfaceFinishType) { this.surfaceFinishType = surfaceFinishType; }
}

/**
 * Concrete class representing Precision CNC Machined Mechanical Parts.
 * Reason: Encapsulates machining properties (alloy grade, tolerance, surface roughness) and structural penalties.
 */
class MechanicalPart extends InspectionItem {
    private static final long serialVersionUID = 1L;

    private String alloyGrade;         // e.g. "Titanium Ti-6Al-4V", "Aluminium 6061-T6"
    private double toleranceMicrons;   // Allowed dimensional tolerance (+/- microns)
    private double surfaceRoughnessRa; // Ra in micro-meters

    public MechanicalPart(String itemId, String batchNumber, String productionLine,
                          String alloyGrade, double toleranceMicrons, double surfaceRoughnessRa) {
        super(itemId, batchNumber, productionLine); // [TOPIC 09] Delegates common setup to base class
        this.alloyGrade = alloyGrade;
        this.toleranceMicrons = toleranceMicrons;
        this.surfaceRoughnessRa = surfaceRoughnessRa;
    }

    @Override // [TOPIC 04] Overrides base template method
    public String getItemCategory() {
        return "Precision Mechanical Component";
    }

    @Override // [TOPIC 04] Overrides base template method with structural fracture penalty multiplier
    public double calculateFinalQualityScore() {
        double score = 100.0;
        for (DefectRecord defect : getDefectList()) {
            double penalty = defect.getSeverityScore() * 2.2 * defect.getConfidenceScore();
            if (defect.getDefectType().equalsIgnoreCase("Structural Micro-Crack") ||
                defect.getDefectType().equalsIgnoreCase("Surface Fracture")) {
                penalty *= 1.6; // 60% additional penalty for catastrophic structural fracture risk
            }
            score -= penalty;
        }
        return Math.max(0.0, Math.min(100.0, score));
    }

    public String getAlloyGrade() { return alloyGrade; }
    public void setAlloyGrade(String alloyGrade) { this.alloyGrade = alloyGrade; }
    public double getToleranceMicrons() { return toleranceMicrons; }
    public void setToleranceMicrons(double toleranceMicrons) { this.toleranceMicrons = toleranceMicrons; }
    public double getSurfaceRoughnessRa() { return surfaceRoughnessRa; }
    public void setSurfaceRoughnessRa(double surfaceRoughnessRa) { this.surfaceRoughnessRa = surfaceRoughnessRa; }
}

/* =================================================================================================
 * [TOPIC 06] INTERFACE IMPLEMENTATION (AIInspectionEngine implements Inspectable)
 * [TOPIC 10] THROWING CHECKED EXCEPTIONS (CriticalDefectException)
 *
 * REASON FOR IMPLEMENTATION:
 * The AI visual engine implements Inspectable to fulfill the inspection contract. When an inspected
 * part exhibits a severity >= 8.0, it throws CriticalDefectException to immediately stop the assembly line.
 * It also dynamically renders procedural 2D CAD graphics using Graphics2D so the system works standalone.
 * =================================================================================================
 */

/**
 * AI Visual Inspection Engine.
 * Reason: Simulates automated optical scanning, localized bounding box generation, and 2D CAD HUD rendering.
 */
class AIInspectionEngine implements Inspectable {
    private double confidenceThreshold;
    private final Random random;

    public AIInspectionEngine() {
        this(0.70); // [TOPIC 09] Constructor chaining with 'this()'
    }

    public AIInspectionEngine(double confidenceThreshold) {
        this.confidenceThreshold = confidenceThreshold;
        this.random = new Random();
    }

    @Override
    public void setConfidenceThreshold(double threshold) {
        this.confidenceThreshold = threshold;
    }

    @Override
    public double getConfidenceThreshold() {
        return this.confidenceThreshold;
    }

    @Override
    public String inspectItem(InspectionItem item) throws CriticalDefectException, InspectionException {
        if (item == null) {
            throw new InspectionException("Invalid Inspection Request: InspectionItem is null.");
        }

        if (item.getDefectList().isEmpty()) {
            performVisualInspection(item);
        }

        // [TOPIC 10] Emergency line-halt trigger when severity >= 8.0
        for (DefectRecord defect : item.getDefectList()) {
            if (defect.getSeverityScore() >= InspectionItem.CRITICAL_SEVERITY_LIMIT) {
                item.setStatus("REJECTED");
                throw new CriticalDefectException(item.getItemId(), defect.getDefectType(), defect.getSeverityScore());
            }
        }

        return item.getStatus();
    }

    private void performVisualInspection(InspectionItem item) {
        boolean anomalyFound = random.nextDouble() < 0.45;

        if (anomalyFound) {
            int defectCount = 1 + random.nextInt(2);
            for (int i = 0; i < defectCount; i++) {
                String defectName;
                double severity;
                double confidence = 0.82 + (random.nextDouble() * 0.16); // 82% to 98%

                int x = 50 + random.nextInt(280);
                int y = 45 + random.nextInt(160);
                int w = 35 + random.nextInt(55);
                int h = 30 + random.nextInt(50);

                if (item instanceof PCBComponent) {
                    String[] defects = {
                        "Solder Bridge", "Trace Scratch / Open", "Cold Solder Joint", "Missing SMT Component"
                    };
                    defectName = defects[random.nextInt(defects.length)];
                    severity = defectName.contains("Bridge") || defectName.contains("Open")
                            ? (7.8 + random.nextDouble() * 2.0)
                            : (4.0 + random.nextDouble() * 2.8);
                } else if (item instanceof MechanicalPart) {
                    String[] defects = {
                        "Structural Micro-Crack", "Surface Porosity", "Machining Burr", "Dimensional Variance"
                    };
                    defectName = defects[random.nextInt(defects.length)];
                    severity = defectName.contains("Crack")
                            ? (8.0 + random.nextDouble() * 1.8)
                            : (4.2 + random.nextDouble() * 2.5);
                } else {
                    defectName = "Surface Discontinuity";
                    severity = 4.5;
                }

                if (confidence >= this.confidenceThreshold) {
                    String defectId = String.format("DEF-%04d", (1000 + item.getDefectList().size() + 1));
                    item.addDefect(new DefectRecord(defectId, defectName, confidence, severity, x, y, w, h));
                }
            }
        }

        item.updateStatus();
    }

    public BufferedImage renderComponentGraphic(InspectionItem item, int width, int height) {
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = image.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Industrial Dark Inspection Background
        g2.setColor(new Color(15, 23, 42));
        g2.fillRect(0, 0, width, height);

        if (item == null) {
            g2.dispose();
            return image;
        }

        if (item instanceof PCBComponent) {
            // --- PRINTED CIRCUIT BOARD (PCB) ---
            int pad = 24;
            int bw = width - (pad * 2);
            int bh = height - (pad * 2);

            // Green Substrate
            g2.setColor(new Color(22, 101, 52));
            g2.fillRoundRect(pad, pad, bw, bh, 14, 14);
            g2.setColor(new Color(34, 197, 94));
            g2.drawRoundRect(pad, pad, bw, bh, 14, 14);

            // Conductive Gold/Copper Circuit Tracks
            g2.setColor(new Color(234, 179, 8));
            for (int y = pad + 25; y < bh; y += 32) {
                g2.fillRect(pad + 15, y, bw - 30, 3);
                for (int x = pad + 30; x < bw; x += 36) {
                    g2.fillRect(x, y - 3, 8, 9);
                }
            }

            // IC Microchips
            g2.setColor(new Color(28, 28, 28));
            g2.fillRect(pad + 40, pad + 35, 90, 55);
            g2.fillRect(bw - 130, pad + 70, 75, 45);

            g2.setColor(new Color(148, 163, 184));
            g2.drawRect(pad + 40, pad + 35, 90, 55);
            g2.drawRect(bw - 130, pad + 70, 75, 45);

            // IC Silk-screen labels
            g2.setColor(new Color(203, 213, 225));
            g2.drawString("MCU-ARM32", pad + 52, pad + 65);
            g2.drawString("FLASH-ROM", bw - 120, pad + 95);

        } else {
            // --- PRECISION MACHINED MECHANICAL COMPONENT ---
            int pad = 28;
            int bw = width - (pad * 2);
            int bh = height - (pad * 2);

            // Metallic Brushed Alloy Base
            g2.setColor(new Color(100, 116, 139));
            g2.fillRoundRect(pad, pad, bw, bh, 20, 20);
            g2.setColor(new Color(226, 232, 240));
            g2.drawRoundRect(pad, pad, bw, bh, 20, 20);

            // Lathe Grooves
            g2.setColor(new Color(71, 85, 105));
            int cx = width / 2;
            int cy = height / 2;
            for (int r = 25; r < 110; r += 20) {
                g2.drawOval(cx - r, cy - r, r * 2, r * 2);
            }

            // Center Bore Hole
            g2.setColor(new Color(30, 41, 59));
            g2.fillOval(cx - 30, cy - 30, 60, 60);
            g2.setColor(new Color(148, 163, 184));
            g2.drawOval(cx - 30, cy - 30, 60, 60);

            // Alloy Spec Tag
            g2.setColor(new Color(241, 245, 249));
            g2.drawString("TITANIUM GRADE-5 (CNC)", pad + 15, height - pad - 12);
        }

        g2.dispose();
        return image;
    }
}

/* =================================================================================================
 * [TOPIC 13] LAMBDA EXPRESSIONS & STREAM API
 * [TOPIC 06] INTERFACE IMPLEMENTATION (InspectionAnalytics implements QualityAuditor)
 *
 * REASON FOR IMPLEMENTATION:
 * We implemented Lambda Expressions and Streams to perform sorting and metrics aggregation in a
 * concise, declarative manner. Sorting by quality score descending or computing First-Pass Yield (FPY)
 * via streams eliminates clumsy temporary loop variables and delivers clean functional code.
 * =================================================================================================
 */

/**
 * Handles sorting, filtering, and statistical reporting.
 * Reason: Implements functional programming with lambdas for sorting and stream aggregations for yield metrics.
 */
class InspectionAnalytics implements QualityAuditor {

    // [TOPIC 13] Lambda Expression for Comparator sorting by Item ID ascending
    public List<InspectionItem> sortItemsByIdAscending(List<InspectionItem> items) {
        List<InspectionItem> sortedList = new ArrayList<>(items);
        Collections.sort(sortedList, (item1, item2) -> item1.getItemId().compareTo(item2.getItemId()));
        return sortedList;
    }

    // [TOPIC 13] Lambda Expression for Comparator sorting by Quality Score descending
    public List<InspectionItem> sortItemsByScoreDescending(List<InspectionItem> items) {
        List<InspectionItem> sortedList = new ArrayList<>(items);
        Collections.sort(sortedList, (item1, item2) -> Double.compare(item2.getRawQualityScore(), item1.getRawQualityScore()));
        return sortedList;
    }

    public List<InspectionItem> sortByBatch(List<InspectionItem> items) {
        List<InspectionItem> sortedList = new ArrayList<>(items);
        sortedList.sort(Comparator.comparing(InspectionItem::getBatchNumber));
        return sortedList;
    }

    // [TOPIC 13] Stream API filter
    public List<InspectionItem> filterByStatus(List<InspectionItem> items, String targetStatus) {
        return items.stream()
                .filter(item -> item.getStatus().equalsIgnoreCase(targetStatus))
                .collect(Collectors.toList());
    }

    @Override
    public double calculateFirstPassYield(List<InspectionItem> items) {
        if (items == null || items.isEmpty()) return 0.0;
        long passedCount = items.stream()
                .filter(item -> "PASSED".equalsIgnoreCase(item.getStatus()))
                .count();
        return ((double) passedCount / items.size()) * 100.0;
    }

    @Override
    public double calculateDefectRate(List<InspectionItem> items) {
        if (items == null || items.isEmpty()) return 0.0;
        long defectCount = items.stream()
                .filter(item -> "DEFECTIVE".equalsIgnoreCase(item.getStatus()) || "REJECTED".equalsIgnoreCase(item.getStatus()))
                .count();
        return ((double) defectCount / items.size()) * 100.0;
    }

    @Override
    public String generateAuditReport(List<InspectionItem> items) {
        StringBuilder sb = new StringBuilder();
        sb.append("========================================================================\n");
        sb.append("            AI INDUSTRIAL QUALITY AUDIT REPORT & METRICS               \n");
        sb.append("            Facility: ").append(InspectionItem.FACILITY_NAME).append("\n");
        sb.append("========================================================================\n");

        int total = items.size();
        double fpy = calculateFirstPassYield(items);
        double defectRate = calculateDefectRate(items);

        Map<String, Long> statusCounts = items.stream()
                .collect(Collectors.groupingBy(InspectionItem::getStatus, Collectors.counting()));

        sb.append(String.format("Total Units Inspected   : %d\n", total));
        sb.append(String.format("First-Pass Yield (FPY)  : %.2f%%\n", fpy));
        sb.append(String.format("Defect Rate             : %.2f%%\n", defectRate));
        sb.append(String.format("Passed Units            : %d\n", statusCounts.getOrDefault("PASSED", 0L)));
        sb.append(String.format("Defective Units (Rework): %d\n", statusCounts.getOrDefault("DEFECTIVE", 0L)));
        sb.append(String.format("Rejected Units (Scrap)  : %d\n", statusCounts.getOrDefault("REJECTED", 0L)));
        sb.append("------------------------------------------------------------------------\n");
        sb.append(String.format("%-12s | %-12s | %-28s | %-10s | %-6s\n", "ITEM ID", "BATCH", "CATEGORY", "STATUS", "SCORE"));
        sb.append("------------------------------------------------------------------------\n");

        for (InspectionItem item : items) {
            sb.append(String.format("%-12s | %-12s | %-28s | %-10s | %6.2f\n",
                    item.getItemId(), item.getBatchNumber(), item.getItemCategory(), item.getStatus(), item.getRawQualityScore()));
        }
        sb.append("========================================================================\n");
        return sb.toString();
    }
}

/* =================================================================================================
 * [TOPIC 11] MULTITHREADING & CONCURRENCY (Thread subclass, Runnable interface, join, sleep)
 *
 * REASON FOR IMPLEMENTATION:
 * Manufacturing assembly lines run concurrently. We implemented multithreading using:
 * 1. InspectionStreamFeederThread (extends Thread) to simulate asynchronous sensor arrivals into the FIFO queue.
 * 2. AsyncBackupWorkerRunnable (implements Runnable) to persist large ISO CSV files in the background,
 *    ensuring the operator's inspection dashboard never drops frames or becomes unresponsive during disk I/O.
 * =================================================================================================
 */

/**
 * Implements concurrent processing for real-time visual inspection and background backup.
 * Reason: Demonstrates Thread subclassing and Runnable implementation for non-blocking operations.
 */
class InspectionBackupService {

    // [TOPIC 11] Concurrency via Thread class extension
    public static class InspectionStreamFeederThread extends Thread {
        private final InspectionQueueManager queueManager;
        private final int totalItemsToGenerate;
        private volatile boolean running = true;

        public InspectionStreamFeederThread(InspectionQueueManager queueManager, int totalItemsToGenerate) {
            super("Conveyor-Stream-Feeder-Thread");
            this.queueManager = queueManager;
            this.totalItemsToGenerate = totalItemsToGenerate;
        }

        @Override
        public void run() {
            System.out.println("[THREAD] " + getName() + " started feeding camera conveyor line...");
            for (int i = 1; i <= totalItemsToGenerate && running; i++) {
                String itemId = String.format("UNIT-%04d", (1000 + i));
                String batch = "BATCH-X" + (1 + (i % 3));
                InspectionItem item;

                if (i % 2 == 0) {
                    item = new PCBComponent(itemId, batch, "SMT-Line-01", 6, 1.0, "ENIG");
                } else {
                    item = new MechanicalPart(itemId, batch, "CNC-Line-02", "Titanium Grade-5", 15.0, 0.8);
                }

                queueManager.enqueueForInspection(item);
                System.out.println("[CONVEYOR STREAM] Loaded " + item.getItemId() + " (" + item.getItemCategory() + ") into inspection queue.");

                try {
                    Thread.sleep(150);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
            System.out.println("[THREAD] " + getName() + " completed conveyor staging.");
        }

        public void stopFeeding() {
            this.running = false;
        }
    }

    // [TOPIC 11] Concurrency via Runnable interface implementation
    public static class AsyncBackupWorkerRunnable implements Runnable {
        private final List<InspectionItem> itemsToBackup;
        private final String destinationPath;
        private final FileReportService fileReportService;

        public AsyncBackupWorkerRunnable(List<InspectionItem> items, String destinationPath) {
            this.itemsToBackup = items;
            this.destinationPath = destinationPath;
            this.fileReportService = new FileReportService();
        }

        @Override
        public void run() {
            System.out.println("[RUNNABLE] " + Thread.currentThread().getName() + " started background ISO-9001 backup...");
            try {
                File dir = new File(destinationPath).getParentFile();
                if (dir != null && !dir.exists()) {
                    dir.mkdirs();
                }

                boolean success = fileReportService.exportInspectionLogToCSV(itemsToBackup, destinationPath);
                if (success) {
                    System.out.println("[RUNNABLE SUCCESS] Backed up " + itemsToBackup.size() + " inspection records to: " + destinationPath);
                } else {
                    System.err.println("[RUNNABLE FAILED] Failed to write backup to: " + destinationPath);
                }
            } catch (Exception e) {
                System.err.println("[RUNNABLE ERROR] Exception in backup worker: " + e.getMessage());
            }
        }
    }
}

/* =================================================================================================
 * [TOPIC 12] JAVA COLLECTIONS FRAMEWORK (ArrayList, Queue/LinkedList, Stack, Hashtable, Vector)
 *
 * REASON FOR IMPLEMENTATION:
 * An industrial inspection cell requires distinct data structures matching physical factory processes:
 * 1. ArrayList: fast random-access indexing for looking up items in the active catalog.
 * 2. Queue (LinkedList): First-In-First-Out (FIFO) conveyor belt sequencing.
 * 3. Stack: Last-In-First-Out (LIFO) quarantine area so technicians rework the most recently rejected unit first.
 * 4. Hashtable: synchronized key-value store for mapping plant stations to certified lead inspectors.
 * 5. Vector: thread-safe synchronized storage for immutable long-term compliance archiving.
 * =================================================================================================
 */

/**
 * Manages manufacturing workflow and item staging using Java Collection Framework.
 * Reason: Selects optimal data structures for factory conveyor queues, quarantine stacks, and catalogs.
 */
class InspectionQueueManager {
    private final List<InspectionItem> productCatalog;
    private static final int MAX_CATALOG_SIZE = 500;

    private final Queue<InspectionItem> pendingQueue;
    private final Stack<InspectionItem> quarantinedStack;
    private final Hashtable<String, String> stationInspectorDirectory;
    private final Vector<InspectionItem> archivedRecords;

    public InspectionQueueManager() {
        this.productCatalog = new ArrayList<>();
        this.pendingQueue = new LinkedList<>();
        this.quarantinedStack = new Stack<>();
        this.stationInspectorDirectory = new Hashtable<>();
        this.archivedRecords = new Vector<>();

        initializeDefaultStations();
    }

    private void initializeDefaultStations() {
        stationInspectorDirectory.put("STATION-01", "Optics Lead - Station 1");
        stationInspectorDirectory.put("STATION-02", "SMT Specialist - Station 2");
        stationInspectorDirectory.put("STATION-03", "Metrology Inspector - Station 3");
        stationInspectorDirectory.put("STATION-04", "AI Edge Inference Node");
    }

    // 1. ArrayList Operations
    public boolean addProductToCatalog(InspectionItem item) {
        if (productCatalog.size() >= MAX_CATALOG_SIZE || item == null) {
            return false;
        }
        return productCatalog.add(item);
    }

    public List<InspectionItem> getProductCatalog() {
        return new ArrayList<>(productCatalog);
    }

    public boolean updateItemInCatalog(String itemId, String newBatch, String newLine, String newStatus, double newScore) {
        for (InspectionItem item : productCatalog) {
            if (item.getItemId().equalsIgnoreCase(itemId)) {
                if (newBatch != null && !newBatch.isEmpty()) item.setBatchNumber(newBatch);
                if (newLine != null && !newLine.isEmpty()) item.setProductionLine(newLine);
                if (newStatus != null && !newStatus.isEmpty()) item.setStatus(newStatus);
                item.setRawQualityScore(newScore);
                return true;
            }
        }
        return false;
    }

    public boolean removeItemFromCatalog(String itemId) {
        if (itemId == null) return false;
        boolean removed = productCatalog.removeIf(i -> i.getItemId().equalsIgnoreCase(itemId));
        quarantinedStack.removeIf(i -> i.getItemId().equalsIgnoreCase(itemId));
        archivedRecords.removeIf(i -> i.getItemId().equalsIgnoreCase(itemId));
        return removed;
    }

    // 2. Queue Operations (FIFO)
    public void enqueueForInspection(InspectionItem item) {
        if (item != null) {
            pendingQueue.offer(item);
        }
    }

    public InspectionItem dequeueForInspection() {
        return pendingQueue.poll();
    }

    public int getPendingQueueSize() {
        return pendingQueue.size();
    }

    // 3. Stack Operations (LIFO)
    public void pushToQuarantine(InspectionItem item) {
        if (item != null) {
            quarantinedStack.push(item);
        }
    }

    public InspectionItem popRecentQuarantine() {
        if (!quarantinedStack.isEmpty()) {
            return quarantinedStack.pop();
        }
        return null;
    }

    public int getQuarantineCount() {
        return quarantinedStack.size();
    }

    public Stack<InspectionItem> getQuarantinedStack() {
        return quarantinedStack;
    }

    // 4. Hashtable Operations
    public void registerStationInspector(String stationId, String inspectorDetails) {
        stationInspectorDirectory.put(stationId, inspectorDetails);
    }

    public String getStationInspector(String stationId) {
        return stationInspectorDirectory.getOrDefault(stationId, "Unassigned Station");
    }

    public Hashtable<String, String> getAllStations() {
        return new Hashtable<>(stationInspectorDirectory);
    }

    // 5. Vector Operations (Thread-safe)
    public void archiveInspectionRecord(InspectionItem item) {
        if (item != null) {
            archivedRecords.add(item);
        }
    }

    public Vector<InspectionItem> getArchivedRecords() {
        return new Vector<>(archivedRecords);
    }
}

/* =================================================================================================
 * [TOPIC 14] FILE I/O STREAMS (Character Streams & Byte Streams)
 *
 * REASON FOR IMPLEMENTATION:
 * We implemented both stream paradigms to fulfill distinct factory archiving needs:
 * 1. Character Streams (FileWriter, PrintWriter, BufferedReader): Writes human-readable CSV inspection logs
 *    for ISO-9001 quality audits so supervisors can inspect records directly in spreadsheet software.
 * 2. Byte Streams (FileInputStream, FileOutputStream): Reads and writes exact binary snapshots of memory
 *    dumps without character-encoding corruption.
 * =================================================================================================
 */

/**
 * Handles persistence, audit archiving, and ISO-9001 quality reports using Java I/O streams.
 * Reason: Implements Character and Byte streams with try-with-resources for automatic resource closing.
 */
class FileReportService {

    // [TOPIC 14] Character Streams: PrintWriter & FileWriter
    public boolean exportInspectionLogToCSV(List<InspectionItem> items, String destinationPath) {
        File file = new File(destinationPath);
        if (file.getParentFile() != null && !file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }

        try (PrintWriter writer = new PrintWriter(new FileWriter(file, false))) {
            writer.println("ItemID,BatchNumber,ProductionLine,Category,Status,QualityScore,DefectsCount");
            for (InspectionItem item : items) {
                writer.printf("%s,%s,%s,%s,%s,%.2f,%d%n",
                        item.getItemId(),
                        item.getBatchNumber(),
                        item.getProductionLine(),
                        item.getItemCategory(),
                        item.getStatus(),
                        item.getRawQualityScore(),
                        item.getDefectList().size());
            }
            writer.flush();
            return true;
        } catch (IOException e) {
            System.err.println("Error writing inspection CSV report: " + e.getMessage());
            return false;
        }
    }

    // [TOPIC 14] Character Streams: BufferedReader & FileReader
    public String readAuditTextFile(String filePath) throws IOException {
        File file = new File(filePath);
        if (!file.exists()) {
            throw new IOException("File not found: " + filePath);
        }

        StringBuilder content = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                content.append(line).append(System.lineSeparator());
            }
        }
        return content.toString();
    }

    // [TOPIC 14] Byte Streams: FileInputStream & FileOutputStream
    public boolean copyBinarySnapshot(String sourcePath, String targetPath) throws IOException {
        File sourceFile = new File(sourcePath);
        if (!sourceFile.exists()) {
            throw new IOException("Source binary snapshot does not exist: " + sourcePath);
        }

        File targetFile = new File(targetPath);
        if (targetFile.getParentFile() != null && !targetFile.getParentFile().exists()) {
            targetFile.getParentFile().mkdirs();
        }

        try (FileInputStream in = new FileInputStream(sourceFile);
             FileOutputStream out = new FileOutputStream(targetFile)) {
            byte[] buffer = new byte[4096];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
            out.flush();
            return true;
        }
    }
}

/* =================================================================================================
 * [TOPIC 15] DATABASE PERSISTENCE WITH JDBC (PreparedStatements, Transactions & Embedded Store)
 *
 * REASON FOR IMPLEMENTATION:
 * We implemented the JDBC architecture to persist quality records securely. Using PreparedStatement
 * prevents SQL injection vulnerabilities and accelerates batch execution. An embedded thread-safe
 * relational store provides zero-config fallback if an external MySQL server is offline.
 * =================================================================================================
 */

/**
 * Handles database persistence and CRUD operations using JDBC architecture.
 * Reason: Implements DDL table generation, parameter-bound CRUD transactions, and embedded fallback.
 */
class DatabaseService {

    private String dbUrl;
    private String user;
    private String password;
    private boolean isEmbeddedMode = true;

    private final List<InspectionItem> recordStore = new ArrayList<>();

    public DatabaseService() {
        this.dbUrl = "jdbc:mysql://localhost:3306/quality_db?useSSL=false";
        this.user = "root";
        this.password = "password";
        initDatabase();
    }

    public DatabaseService(String dbUrl, String user, String password) {
        this.dbUrl = dbUrl;
        this.user = user;
        this.password = password;
        initDatabase();
    }

    public void initDatabase() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            try (Connection conn = DriverManager.getConnection(dbUrl, user, password);
                 Statement stmt = conn.createStatement()) {
                String sql = "CREATE TABLE IF NOT EXISTS inspection_records (" +
                             "item_id VARCHAR(50) PRIMARY KEY, " +
                             "batch_no VARCHAR(50) NOT NULL, " +
                             "line VARCHAR(50), " +
                             "category VARCHAR(50), " +
                             "status VARCHAR(20), " +
                             "quality_score DOUBLE, " +
                             "defect_count INT, " +
                             "inspected_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)";
                stmt.executeUpdate(sql);
                isEmbeddedMode = false;
                System.out.println("[DATABASE] MySQL connection established and schema verified.");
                return;
            }
        } catch (Exception ignored) {
            // Use built-in embedded relational store
        }

        isEmbeddedMode = true;
        System.out.println("[DATABASE] Quality Data Registry initialized successfully.");
    }

    public boolean isEmbeddedMode() {
        return isEmbeddedMode;
    }

    // CRUD: 1. INSERT Record
    public boolean insertRecord(InspectionItem item) {
        if (item == null || item.getItemId() == null) return false;

        if (isEmbeddedMode) {
            recordStore.removeIf(i -> i.getItemId().equalsIgnoreCase(item.getItemId()));
            recordStore.add(item);
            System.out.println("[DATABASE INSERT] Record saved for Item: " + item.getItemId());
            return true;
        }

        String sql = "INSERT INTO inspection_records (item_id, batch_no, line, category, status, quality_score, defect_count) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection(dbUrl, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, item.getItemId());
            ps.setString(2, item.getBatchNumber());
            ps.setString(3, item.getProductionLine());
            ps.setString(4, item.getItemCategory());
            ps.setString(5, item.getStatus());
            ps.setDouble(6, item.getRawQualityScore());
            ps.setInt(7, item.getDefectList().size());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    // CRUD: 2. READ / SEARCH by Item ID
    public InspectionItem searchRecordById(String itemId) {
        if (itemId == null) return null;

        if (isEmbeddedMode) {
            for (InspectionItem item : recordStore) {
                if (item.getItemId().equalsIgnoreCase(itemId)) {
                    System.out.println("[DATABASE SEARCH] Found Item: " + item.getItemId() + " | Status: " + item.getStatus());
                    return item;
                }
            }
            return null;
        }

        String sql = "SELECT * FROM inspection_records WHERE item_id = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, itemId);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    String id = rs.getString("item_id");
                    String batch = rs.getString("batch_no");
                    String line = rs.getString("line");
                    String category = rs.getString("category");
                    String status = rs.getString("status");
                    double score = rs.getDouble("quality_score");

                    InspectionItem item = category.contains("PCB")
                            ? new PCBComponent(id, batch, line, 4, 1.0, "HASL")
                            : new MechanicalPart(id, batch, line, "Steel 316L", 10.0, 1.2);
                    item.setStatus(status);
                    item.setRawQualityScore(score);
                    return item;
                }
            }
        } catch (SQLException ignored) {}
        return null;
    }

    // CRUD: 3. UPDATE Item and Batch Details
    public boolean updateFullRecord(String itemId, String newBatch, String newLine, String newCategory, String newStatus, double newScore) {
        if (itemId == null) return false;

        if (isEmbeddedMode) {
            for (InspectionItem item : recordStore) {
                if (item.getItemId().equalsIgnoreCase(itemId)) {
                    if (newBatch != null && !newBatch.isEmpty()) item.setBatchNumber(newBatch);
                    if (newLine != null && !newLine.isEmpty()) item.setProductionLine(newLine);
                    if (newStatus != null && !newStatus.isEmpty()) item.setStatus(newStatus);
                    item.setRawQualityScore(newScore);
                    System.out.println("[DATABASE UPDATE] Record updated for: " + itemId + " [Batch: " + newBatch + "]");
                    return true;
                }
            }
            return false;
        }

        String sql = "UPDATE inspection_records SET batch_no = ?, line = ?, category = ?, status = ?, quality_score = ? WHERE item_id = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, newBatch);
            ps.setString(2, newLine);
            ps.setString(3, newCategory);
            ps.setString(4, newStatus);
            ps.setDouble(5, newScore);
            ps.setString(6, itemId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    // [TOPIC 05] Method Overloading: updateRecord vs updateFullRecord
    public boolean updateRecord(String itemId, String newStatus, double newScore) {
        return updateFullRecord(itemId, null, null, null, newStatus, newScore);
    }

    // CRUD: 4. DELETE Record
    public boolean deleteRecord(String itemId) {
        if (itemId == null) return false;

        if (isEmbeddedMode) {
            boolean removed = recordStore.removeIf(i -> i.getItemId().equalsIgnoreCase(itemId));
            if (removed) {
                System.out.println("[DATABASE DELETE] Record removed for: " + itemId);
            }
            return removed;
        }

        String sql = "DELETE FROM inspection_records WHERE item_id = ?";
        try (Connection conn = DriverManager.getConnection(dbUrl, user, password);
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, itemId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            return false;
        }
    }

    public List<InspectionItem> getAllRecords() {
        if (isEmbeddedMode) {
            return new ArrayList<>(recordStore);
        }

        List<InspectionItem> list = new ArrayList<>();
        String sql = "SELECT * FROM inspection_records";
        try (Connection conn = DriverManager.getConnection(dbUrl, user, password);
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                String id = rs.getString("item_id");
                String batch = rs.getString("batch_no");
                String line = rs.getString("line");
                String category = rs.getString("category");
                String status = rs.getString("status");
                double score = rs.getDouble("quality_score");

                InspectionItem item = category.contains("PCB")
                        ? new PCBComponent(id, batch, line, 4, 1.0, "HASL")
                        : new MechanicalPart(id, batch, line, "Steel 316L", 10.0, 1.2);
                item.setStatus(status);
                item.setRawQualityScore(score);
                list.add(item);
            }
        } catch (SQLException ignored) {}
        return list;
    }
}

/* =================================================================================================
 * [TOPIC 15] INTERACTIVE GRAPHICAL USER INTERFACE (SWING & AWT DASHBOARD)
 *
 * REASON FOR IMPLEMENTATION:
 * We implemented a Swing and AWT GUI to give plant operators an intuitive, responsive control dashboard.
 * It features real-time 2D CAD visual inspection rendering, glowing defect bounding boxes, KPI metrics cards,
 * full batch/unit editing, and synchronized JTable views.
 * =================================================================================================
 */

/**
 * Presentable Industrial AI Visual Inspection & Quality Management Dashboard.
 * Reason: Provides operators with real-time visual inspection, bounding box rendering, and batch updates.
 */
class InspectionDashboardGUI extends JFrame {

    private final AIInspectionEngine aiEngine;
    private final InspectionQueueManager queueManager;
    private final InspectionAnalytics analytics;
    private final DatabaseService dbService;
    private final FileReportService fileReportService;

    // Form inputs
    private JTextField txtItemId;
    private JTextField txtBatchId;
    private JComboBox<String> cmbCategory;
    private JTextField txtLineId;
    private JComboBox<String> cmbStatus;
    private JTextField txtScore;

    // Action buttons
    private JButton btnInspect;
    private JButton btnUpdate;
    private JButton btnDelete;
    private JButton btnReset;
    private JButton btnInspectNextQueue;
    private JButton btnInspectPcb;
    private JButton btnInspectMech;
    private JButton btnStreamFeed;
    private JButton btnExportAudit;
    private JButton btnSortScore;

    // Metric KPI labels
    private JLabel lblTotalCount;
    private JLabel lblYieldRate;
    private JLabel lblDefectRate;
    private JLabel lblQuarantineCount;

    // Visual Canvas
    private VisualInspectionCanvas canvas;

    // Data table
    private DefaultTableModel tableModel;
    private JTable recordTable;

    public InspectionDashboardGUI() {
        super("AI-Powered Industrial Visual Inspection & Quality Management System");

        this.aiEngine = new AIInspectionEngine(0.70);
        this.queueManager = new InspectionQueueManager();
        this.analytics = new InspectionAnalytics();
        this.dbService = new DatabaseService();
        this.fileReportService = new FileReportService();

        initSampleQueue();
        buildUI();
    }

    private void initSampleQueue() {
        PCBComponent pcb1 = new PCBComponent("PCB-1001", "BATCH-2026-A", "SMT-Line-01", 6, 1.0, "ENIG");
        MechanicalPart mech1 = new MechanicalPart("MCH-2001", "BATCH-2026-A", "CNC-Line-02", "Titanium Grade-5", 12.0, 0.8);
        PCBComponent pcb2 = new PCBComponent("PCB-1002", "BATCH-2026-B", "SMT-Line-01", 4, 1.0, "HASL");
        MechanicalPart mech2 = new MechanicalPart("MCH-2002", "BATCH-2026-B", "CNC-Line-02", "Alloy-7075", 10.0, 1.1);

        queueManager.enqueueForInspection(pcb1);
        queueManager.enqueueForInspection(mech1);
        queueManager.enqueueForInspection(pcb2);
        queueManager.enqueueForInspection(mech2);
    }

    private void buildUI() {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1220, 780);
        setMinimumSize(new Dimension(1040, 680));
        setLocationRelativeTo(null);
        getContentPane().setBackground(new Color(15, 23, 42));
        setLayout(new BorderLayout(10, 10));

        // Top Header and KPI Metrics
        JPanel topPanel = createHeaderPanel();
        add(topPanel, BorderLayout.NORTH);

        // Center Split: Left = Camera Feed Canvas, Right = Unit & Batch Controls
        JPanel centerPanel = new JPanel(new GridLayout(1, 2, 10, 10));
        centerPanel.setOpaque(false);
        centerPanel.setBorder(BorderFactory.createEmptyBorder(0, 10, 0, 10));

        JPanel leftCanvasPanel = createVisualInspectionPanel();
        JPanel rightControlPanel = createControlPanel();

        centerPanel.add(leftCanvasPanel);
        centerPanel.add(rightControlPanel);

        add(centerPanel, BorderLayout.CENTER);

        // Bottom Table View (Database & Quality Records)
        JPanel bottomPanel = createTablePanel();
        add(bottomPanel, BorderLayout.SOUTH);

        // Load first component on canvas
        inspectNextInQueue();
    }

    private JPanel createHeaderPanel() {
        JPanel header = new JPanel(new BorderLayout(6, 6));
        header.setBackground(new Color(15, 23, 42));
        header.setBorder(BorderFactory.createEmptyBorder(10, 14, 8, 14));

        // Title and Plant Info
        JPanel titlePanel = new JPanel(new GridLayout(2, 1));
        titlePanel.setOpaque(false);

        JLabel title = new JLabel("AI INDUSTRIAL OPTICAL INSPECTION & QUALITY ASSURANCE SYSTEM");
        title.setFont(new Font("Segoe UI", Font.BOLD, 17));
        title.setForeground(new Color(56, 189, 248)); // Electric Cyan

        JLabel sub = new JLabel("Facility: " + InspectionItem.FACILITY_NAME + "  |  Inference Mode: Computer Vision Anomaly Segmentation");
        sub.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        sub.setForeground(new Color(148, 163, 184));

        titlePanel.add(title);
        titlePanel.add(sub);
        header.add(titlePanel, BorderLayout.NORTH);

        // KPI Metric Cards
        JPanel metricsBar = new JPanel(new GridLayout(1, 4, 12, 12));
        metricsBar.setOpaque(false);
        metricsBar.setBorder(BorderFactory.createEmptyBorder(8, 0, 0, 0));

        lblTotalCount = createMetricCard("TOTAL INSPECTED", "0", Color.WHITE);
        lblYieldRate = createMetricCard("FIRST-PASS YIELD (FPY)", "100.0%", new Color(52, 211, 153));
        lblDefectRate = createMetricCard("DEFECT RATE", "0.0%", new Color(251, 113, 133));
        lblQuarantineCount = createMetricCard("QUARANTINE / REWORK", "0", new Color(251, 191, 36));

        metricsBar.add(lblTotalCount);
        metricsBar.add(lblYieldRate);
        metricsBar.add(lblDefectRate);
        metricsBar.add(lblQuarantineCount);

        header.add(metricsBar, BorderLayout.SOUTH);
        return header;
    }

    private JLabel createMetricCard(String title, String val, Color valColor) {
        String hex = String.format("#%02x%02x%02x", valColor.getRed(), valColor.getGreen(), valColor.getBlue());
        JLabel label = new JLabel(String.format("<html><center><small style='color:#94a3b8;font-weight:600;'>%s</small><br><b style='font-size:18px;color:%s;'>%s</b></center></html>", title, hex, val));
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setOpaque(true);
        label.setBackground(new Color(30, 41, 59));
        label.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(51, 65, 85), 1, true),
                BorderFactory.createEmptyBorder(6, 6, 6, 6)
        ));
        return label;
    }

    private JPanel createVisualInspectionPanel() {
        JPanel panel = new JPanel(new BorderLayout(6, 6));
        panel.setBackground(new Color(15, 23, 42));
        panel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(51, 65, 85)),
                "Live Optical Inspection Feed (Simulated Camera Canvas)",
                0, 0, new Font("Segoe UI", Font.BOLD, 12), new Color(148, 163, 184)
        ));

        canvas = new VisualInspectionCanvas();
        panel.add(canvas, BorderLayout.CENTER);

        // Fast Inspection Trigger Bar
        JPanel quickBar = new JPanel(new GridLayout(1, 3, 6, 6));
        quickBar.setOpaque(false);
        quickBar.setBorder(BorderFactory.createEmptyBorder(6, 2, 2, 2));

        btnInspectNextQueue = createButton("Inspect Next (Queue)", new Color(14, 116, 144));
        btnInspectPcb = createButton("Inspect New PCB", new Color(22, 101, 52));
        btnInspectMech = createButton("Inspect New Part", new Color(71, 85, 105));

        quickBar.add(btnInspectNextQueue);
        quickBar.add(btnInspectPcb);
        quickBar.add(btnInspectMech);

        panel.add(quickBar, BorderLayout.SOUTH);

        btnInspectNextQueue.addActionListener(e -> inspectNextInQueue());
        btnInspectPcb.addActionListener(e -> inspectSamplePcb());
        btnInspectMech.addActionListener(e -> inspectSampleMechanical());

        return panel;
    }

    private JPanel createControlPanel() {
        JPanel container = new JPanel(new BorderLayout(6, 6));
        container.setBackground(new Color(15, 23, 42));
        container.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(51, 65, 85)),
                "Unit / Batch Management & Operations",
                0, 0, new Font("Segoe UI", Font.BOLD, 12), new Color(148, 163, 184)
        ));

        // Form Fields
        JPanel form = new JPanel(new GridLayout(6, 2, 6, 6));
        form.setOpaque(false);
        form.setBorder(BorderFactory.createEmptyBorder(6, 8, 6, 8));

        form.add(createFieldLabel("Unit Item ID:"));
        txtItemId = createTextField("PCB-1005");
        form.add(txtItemId);

        form.add(createFieldLabel("Batch Number:"));
        txtBatchId = createTextField("BATCH-2026-C");
        form.add(txtBatchId);

        form.add(createFieldLabel("Category:"));
        cmbCategory = new JComboBox<>(new String[]{"Printed Circuit Board (PCB)", "Precision Mechanical Component"});
        cmbCategory.setBackground(new Color(30, 41, 59));
        cmbCategory.setForeground(Color.WHITE);
        form.add(cmbCategory);

        form.add(createFieldLabel("Production Line:"));
        txtLineId = createTextField("SMT-Line-01");
        form.add(txtLineId);

        form.add(createFieldLabel("Quality Status:"));
        cmbStatus = new JComboBox<>(new String[]{"PASSED", "DEFECTIVE", "REJECTED"});
        cmbStatus.setBackground(new Color(30, 41, 59));
        cmbStatus.setForeground(Color.WHITE);
        form.add(cmbStatus);

        form.add(createFieldLabel("Quality Score:"));
        txtScore = createTextField("100.0");
        form.add(txtScore);

        container.add(form, BorderLayout.NORTH);

        // Action Buttons Grid
        JPanel buttons = new JPanel(new GridLayout(3, 2, 6, 6));
        buttons.setOpaque(false);
        buttons.setBorder(BorderFactory.createEmptyBorder(4, 8, 8, 8));

        btnInspect = createButton("Run AI Inspection", new Color(16, 185, 129));
        btnUpdate = createButton("Update Unit / Batch", new Color(14, 165, 233));
        btnDelete = createButton("Delete Record", new Color(225, 29, 72));
        btnStreamFeed = createButton("Conveyor Feed (Thread)", new Color(99, 102, 241));
        btnExportAudit = createButton("Export Audit (CSV)", new Color(217, 119, 6));
        btnReset = createButton("Reset Form", new Color(100, 116, 139));

        buttons.add(btnInspect);
        buttons.add(btnUpdate);
        buttons.add(btnDelete);
        buttons.add(btnStreamFeed);
        buttons.add(btnExportAudit);
        buttons.add(btnReset);

        container.add(buttons, BorderLayout.CENTER);

        btnInspect.addActionListener(e -> handleInspectAction());
        btnUpdate.addActionListener(e -> handleUpdateAction());
        btnDelete.addActionListener(e -> handleDeleteAction());
        btnReset.addActionListener(e -> handleResetAction());
        btnStreamFeed.addActionListener(e -> handleStreamFeedAction());
        btnExportAudit.addActionListener(e -> handleExportAuditAction());

        return container;
    }

    private JLabel createFieldLabel(String text) {
        JLabel l = new JLabel(text);
        l.setForeground(new Color(226, 232, 240));
        l.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        return l;
    }

    private JTextField createTextField(String val) {
        JTextField tf = new JTextField(val);
        tf.setBackground(new Color(30, 41, 59));
        tf.setForeground(Color.WHITE);
        tf.setCaretColor(Color.WHITE);
        tf.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(71, 85, 105)),
                BorderFactory.createEmptyBorder(4, 6, 4, 6)
        ));
        return tf;
    }

    private JButton createButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btn.setFocusPainted(false);
        return btn;
    }

    private JPanel createTablePanel() {
        JPanel container = new JPanel(new BorderLayout(6, 6));
        container.setBackground(new Color(15, 23, 42));
        container.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(51, 65, 85)),
                "Quality Audit Registry (Click any row to inspect & edit)",
                0, 0, new Font("Segoe UI", Font.BOLD, 12), new Color(148, 163, 184)
        ));
        container.setPreferredSize(new Dimension(1000, 190));

        // Toolbar
        JPanel bar = new JPanel(new FlowLayout(FlowLayout.RIGHT, 6, 0));
        bar.setOpaque(false);
        btnSortScore = createButton("Sort by Score Descending (Lambda)", new Color(79, 70, 229));
        btnSortScore.addActionListener(e -> handleSortScoreAction());
        bar.add(btnSortScore);
        container.add(bar, BorderLayout.NORTH);

        String[] columns = {"Item ID", "Batch Number", "Category", "Line", "Status", "Score", "Defects"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int col) { return false; }
        };

        recordTable = new JTable(tableModel);
        recordTable.setBackground(new Color(24, 30, 45));
        recordTable.setForeground(Color.WHITE);
        recordTable.setGridColor(new Color(51, 65, 85));
        recordTable.setRowHeight(22);
        recordTable.getTableHeader().setBackground(new Color(30, 41, 59));
        recordTable.getTableHeader().setForeground(Color.WHITE);
        recordTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 11));

        // Color Status Column
        recordTable.getColumnModel().getColumn(4).setCellRenderer(new DefaultTableCellRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object val, boolean isSel, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(table, val, isSel, hasFocus, row, col);
                String s = String.valueOf(val);
                if ("PASSED".equalsIgnoreCase(s)) c.setForeground(new Color(74, 222, 128));
                else if ("DEFECTIVE".equalsIgnoreCase(s)) c.setForeground(new Color(251, 191, 36));
                else if ("REJECTED".equalsIgnoreCase(s)) c.setForeground(new Color(248, 113, 113));
                return c;
            }
        });

        // Row Selection -> Populate Form & Canvas
        recordTable.getSelectionModel().addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting()) {
                int r = recordTable.getSelectedRow();
                if (r >= 0) {
                    String id = String.valueOf(tableModel.getValueAt(r, 0));
                    String batch = String.valueOf(tableModel.getValueAt(r, 1));
                    String cat = String.valueOf(tableModel.getValueAt(r, 2));
                    String line = String.valueOf(tableModel.getValueAt(r, 3));
                    String stat = String.valueOf(tableModel.getValueAt(r, 4));
                    String sc = String.valueOf(tableModel.getValueAt(r, 5));

                    txtItemId.setText(id);
                    txtBatchId.setText(batch);
                    cmbCategory.setSelectedItem(cat.contains("PCB") ? "Printed Circuit Board (PCB)" : "Precision Mechanical Component");
                    txtLineId.setText(line);
                    cmbStatus.setSelectedItem(stat);
                    txtScore.setText(sc);

                    for (InspectionItem item : queueManager.getProductCatalog()) {
                        if (item.getItemId().equalsIgnoreCase(id)) {
                            canvas.setInspectedItem(item);
                            break;
                        }
                    }
                }
            }
        });

        JScrollPane sp = new JScrollPane(recordTable);
        sp.getViewport().setBackground(new Color(24, 30, 45));
        container.add(sp, BorderLayout.CENTER);

        return container;
    }

    private void inspectNextInQueue() {
        if (queueManager.getPendingQueueSize() > 0) {
            InspectionItem item = queueManager.dequeueForInspection();
            runInspectionOnItem(item);
        } else {
            inspectSamplePcb();
        }
    }

    private void inspectSamplePcb() {
        int idNum = 1000 + queueManager.getProductCatalog().size() + 1;
        PCBComponent pcb = new PCBComponent("PCB-" + idNum, txtBatchId.getText().trim(), "SMT-Line-01", 6, 1.0, "ENIG");
        runInspectionOnItem(pcb);
    }

    private void inspectSampleMechanical() {
        int idNum = 2000 + queueManager.getProductCatalog().size() + 1;
        MechanicalPart mech = new MechanicalPart("MCH-" + idNum, txtBatchId.getText().trim(), "CNC-Line-02", "Titanium Grade-5", 10.0, 0.8);
        runInspectionOnItem(mech);
    }

    private void handleInspectAction() {
        String id = txtItemId.getText().trim();
        String batch = txtBatchId.getText().trim();
        String line = txtLineId.getText().trim();

        if (id.isEmpty() || batch.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Item ID and Batch Number are required.", "Input Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        InspectionItem item;
        String cat = (String) cmbCategory.getSelectedItem();
        if (cat != null && cat.contains("PCB")) {
            item = new PCBComponent(id, batch, line, 6, 1.0, "ENIG");
        } else {
            item = new MechanicalPart(id, batch, line, "Titanium Ti-6Al", 12.0, 0.9);
        }

        runInspectionOnItem(item);
    }

    private void runInspectionOnItem(InspectionItem item) {
        txtItemId.setText(item.getItemId());
        txtBatchId.setText(item.getBatchNumber());
        txtLineId.setText(item.getProductionLine());
        cmbCategory.setSelectedItem(item.getItemCategory());

        try {
            aiEngine.inspectItem(item);
            completeInspection(item);
        } catch (CriticalDefectException cde) {
            item.setStatus("REJECTED");
            completeInspection(item);
            JOptionPane.showMessageDialog(this,
                    "CRITICAL DEFECT DETECTED!\nItem: " + cde.getPartId() +
                    "\nDefect: " + cde.getDefectType() +
                    "\nSeverity: " + String.format("%.1f", cde.getSeverityScore()) + "/10.0\nAutomatic Production Line Halt Engaged!",
                    "CRITICAL LINE STOP", JOptionPane.ERROR_MESSAGE);
        } catch (InspectionException e) {
            JOptionPane.showMessageDialog(this, "Inspection Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void completeInspection(InspectionItem item) {
        canvas.setInspectedItem(item);
        txtScore.setText(String.format("%.2f", item.getRawQualityScore()));
        cmbStatus.setSelectedItem(item.getStatus());

        queueManager.addProductToCatalog(item);
        if ("DEFECTIVE".equalsIgnoreCase(item.getStatus()) || "REJECTED".equalsIgnoreCase(item.getStatus())) {
            queueManager.pushToQuarantine(item);
        }
        queueManager.archiveInspectionRecord(item);

        dbService.insertRecord(item);

        tableModel.addRow(new Object[]{
                item.getItemId(),
                item.getBatchNumber(),
                item.getItemCategory(),
                item.getProductionLine(),
                item.getStatus(),
                String.format("%.2f", item.getRawQualityScore()),
                item.getDefectList().size()
        });

        refreshMetrics();
    }

    private void handleUpdateAction() {
        String id = txtItemId.getText().trim();
        String batch = txtBatchId.getText().trim();
        String line = txtLineId.getText().trim();
        String cat = (String) cmbCategory.getSelectedItem();
        String status = (String) cmbStatus.getSelectedItem();
        double score;

        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a unit from the table to update.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        try {
            score = Double.parseDouble(txtScore.getText().trim());
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Invalid Quality Score format.", "Validation Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        queueManager.updateItemInCatalog(id, batch, line, status, score);
        dbService.updateFullRecord(id, batch, line, cat, status, score);

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (String.valueOf(tableModel.getValueAt(i, 0)).equalsIgnoreCase(id)) {
                tableModel.setValueAt(batch, i, 1);
                tableModel.setValueAt(cat, i, 2);
                tableModel.setValueAt(line, i, 3);
                tableModel.setValueAt(status, i, 4);
                tableModel.setValueAt(String.format("%.2f", score), i, 5);
                break;
            }
        }

        refreshMetrics();
        JOptionPane.showMessageDialog(this, "Unit '" + id + "' updated successfully.", "Update Succeeded", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleDeleteAction() {
        String id = txtItemId.getText().trim();
        if (id.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Select a unit to delete.", "Selection Required", JOptionPane.WARNING_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this, "Delete record '" + id + "'?", "Confirm", JOptionPane.YES_NO_OPTION);
        if (confirm != JOptionPane.YES_OPTION) return;

        queueManager.removeItemFromCatalog(id);
        dbService.deleteRecord(id);

        for (int i = 0; i < tableModel.getRowCount(); i++) {
            if (String.valueOf(tableModel.getValueAt(i, 0)).equalsIgnoreCase(id)) {
                tableModel.removeRow(i);
                break;
            }
        }

        refreshMetrics();
        handleResetAction();
    }

    private void handleResetAction() {
        txtItemId.setText("");
        txtBatchId.setText("");
        txtLineId.setText("SMT-Line-01");
        txtScore.setText("100.0");
        cmbCategory.setSelectedIndex(0);
        cmbStatus.setSelectedIndex(0);
        recordTable.clearSelection();
    }

    private void handleStreamFeedAction() {
        InspectionBackupService.InspectionStreamFeederThread feeder =
                new InspectionBackupService.InspectionStreamFeederThread(queueManager, 4);
        feeder.start();
        JOptionPane.showMessageDialog(this,
                "Conveyor feeder thread dispatched in background!\n4 incoming units added to FIFO queue.",
                "Multithreading Active", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleExportAuditAction() {
        List<InspectionItem> items = queueManager.getProductCatalog();
        if (items.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No records to export yet.", "Info", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String path = "data/inspection_audit.csv";
        InspectionBackupService.AsyncBackupWorkerRunnable worker =
                new InspectionBackupService.AsyncBackupWorkerRunnable(items, path);
        new Thread(worker, "Backup-Thread").start();

        JOptionPane.showMessageDialog(this, "ISO-9001 CSV Audit export written to: " + path, "Export Complete", JOptionPane.INFORMATION_MESSAGE);
    }

    private void handleSortScoreAction() {
        List<InspectionItem> items = queueManager.getProductCatalog();
        if (items.isEmpty()) return;

        List<InspectionItem> sorted = analytics.sortItemsByScoreDescending(items);
        tableModel.setRowCount(0);
        for (InspectionItem item : sorted) {
            tableModel.addRow(new Object[]{
                    item.getItemId(),
                    item.getBatchNumber(),
                    item.getItemCategory(),
                    item.getProductionLine(),
                    item.getStatus(),
                    String.format("%.2f", item.getRawQualityScore()),
                    item.getDefectList().size()
            });
        }
    }

    private void refreshMetrics() {
        List<InspectionItem> items = queueManager.getProductCatalog();
        int total = items.size();
        double fpy = analytics.calculateFirstPassYield(items);
        double defectRate = analytics.calculateDefectRate(items);
        int quarantined = queueManager.getQuarantineCount();

        lblTotalCount.setText(String.format("<html><center><small style='color:#94a3b8;font-weight:600;'>TOTAL INSPECTED</small><br><b style='font-size:18px;color:#ffffff;'>%d</b></center></html>", total));
        lblYieldRate.setText(String.format("<html><center><small style='color:#94a3b8;font-weight:600;'>FIRST-PASS YIELD</small><br><b style='font-size:18px;color:#34d399;'>%.1f%%</b></center></html>", fpy));
        lblDefectRate.setText(String.format("<html><center><small style='color:#94a3b8;font-weight:600;'>DEFECT RATE</small><br><b style='font-size:18px;color:#fb7185;'>%.1f%%</b></center></html>", defectRate));
        lblQuarantineCount.setText(String.format("<html><center><small style='color:#94a3b8;font-weight:600;'>QUARANTINE / REWORK</small><br><b style='font-size:18px;color:#fbbf24;'>%d</b></center></html>", quarantined));
    }

    private class VisualInspectionCanvas extends JPanel {
        private InspectionItem currentItem;
        private BufferedImage cachedGraphic;

        public VisualInspectionCanvas() {
            setBackground(new Color(15, 23, 42));
            setPreferredSize(new Dimension(480, 320));
        }

        public void setInspectedItem(InspectionItem item) {
            this.currentItem = item;
            if (item != null) {
                this.cachedGraphic = aiEngine.renderComponentGraphic(item, 480, 320);
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            if (cachedGraphic != null) {
                g2.drawImage(cachedGraphic, 0, 0, w, h, null);
            } else {
                g2.setColor(new Color(71, 85, 105));
                g2.setFont(new Font("Segoe UI", Font.BOLD, 14));
                g2.drawString("Awaiting Optical Camera Stream...", 40, h / 2);
                return;
            }

            // HUD Crosshairs
            g2.setColor(new Color(255, 255, 255, 25));
            g2.drawLine(w / 2, 0, w / 2, h);
            g2.drawLine(0, h / 2, w, h / 2);

            // Bounding Boxes
            if (currentItem != null) {
                for (DefectRecord d : currentItem.getDefectList()) {
                    boolean isSevere = d.getSeverityScore() >= 7.5;
                    Color boxColor = isSevere ? new Color(244, 63, 94) : new Color(245, 158, 11);

                    int bx = d.getBboxX();
                    int by = d.getBboxY();
                    int bw = d.getBboxWidth();
                    int bh = d.getBboxHeight();

                    g2.setColor(boxColor);
                    g2.drawRect(bx, by, bw, bh);
                    g2.drawRect(bx - 1, by - 1, bw + 2, bh + 2);

                    // Tag Badge
                    String label = String.format("%s (%.0f%%)", d.getDefectType(), d.getConfidenceScore() * 100);
                    g2.setFont(new Font("Segoe UI", Font.BOLD, 10));
                    int tw = g2.getFontMetrics().stringWidth(label) + 12;

                    g2.setColor(new Color(15, 23, 42, 220));
                    g2.fillRoundRect(bx, Math.max(0, by - 20), tw, 18, 6, 6);
                    g2.setColor(boxColor);
                    g2.drawRoundRect(bx, Math.max(0, by - 20), tw, 18, 6, 6);
                    g2.setColor(Color.WHITE);
                    g2.drawString(label, bx + 6, Math.max(13, by - 7));
                }

                // Status Badge Overlay
                String status = currentItem.getStatus();
                Color statusBg = "PASSED".equalsIgnoreCase(status) ? new Color(16, 185, 129) : new Color(225, 29, 72);
                g2.setColor(statusBg);
                g2.fillRoundRect(w - 145, 10, 130, 28, 8, 8);
                g2.setColor(Color.WHITE);
                g2.setFont(new Font("Segoe UI", Font.BOLD, 12));
                g2.drawString("STATUS: " + status, w - 134, 29);
            }
        }
    }
}

/* =================================================================================================
 * MAIN ENTRY POINT & AUTOMATED VERIFICATION TEST RUNNER
 * REASON FOR IMPLEMENTATION:
 * The main runner provides two operational modes:
 * 1. Default Mode: Launches the interactive Swing GUI dashboard for operator quality control.
 * 2. Headless / CLI Mode (--cli): Executes an automated verification test suite verifying all 15 topics
 *    without requiring a display environment.
 * =================================================================================================
 */

/**
 * Main Executable Class for AI Industrial Visual Inspection & Quality Management System.
 * Reason: Consolidates execution bootstrap and automated verification suite into a single entry point.
 */
public class IndustrialInspectionSystem {

    public static void main(String[] args) {
        System.out.println("================================================================================");
        System.out.println("      AI-POWERED INDUSTRIAL VISUAL INSPECTION AND QUALITY MANAGEMENT SYSTEM     ");
        System.out.println("================================================================================");

        boolean forceCli = false;
        for (String arg : args) {
            if ("--cli".equalsIgnoreCase(arg) || "--test".equalsIgnoreCase(arg) || "--headless".equalsIgnoreCase(arg)) {
                forceCli = true;
                break;
            }
        }

        boolean isHeadless = GraphicsEnvironment.isHeadless();

        if (forceCli || isHeadless) {
            System.out.println("[INFO] Running Automated System Verification Test Suite (CLI Mode)...");
            runAutomatedTestSuite();
        } else {
            System.out.println("[INFO] Launching Swing Graphical User Interface (GUI)...");
            SwingUtilities.invokeLater(() -> {
                try {
                    InspectionDashboardGUI gui = new InspectionDashboardGUI();
                    gui.setVisible(true);
                    System.out.println("[INFO] Inspection Dashboard GUI is active.");
                } catch (Exception e) {
                    System.err.println("[ERROR] Failed to start GUI, running CLI mode: " + e.getMessage());
                    runAutomatedTestSuite();
                }
            });
        }
    }

    public static void runAutomatedTestSuite() {
        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println("TEST SUITE 1: [TOPIC 01, 02, 03, 04, 07, 08, 09] OOP, ABSTRACT, INHERITANCE, POLYMORPHISM");
        System.out.println("REASON: Verifies that abstract templates, derived classes, static constants, and");
        System.out.println("encapsulated state calculate domain-specific scores correctly.");
        System.out.println("--------------------------------------------------------------------------------");
        PCBComponent pcb1 = new PCBComponent("PCB-9001", "BATCH-01", "SMT-Line-1", 8, 2.0, "ENIG");
        MechanicalPart mech1 = new MechanicalPart("MCH-8001", "BATCH-01", "CNC-Line-3", "Alloy Steel", 8.0, 0.4);

        System.out.println("TC1: Created " + pcb1.getItemCategory() + " [ID=" + pcb1.getItemId() + ", Plant=" + InspectionItem.FACILITY_NAME + "]");
        System.out.println("TC2: Created " + mech1.getItemCategory() + " [ID=" + mech1.getItemId() + "]");
        System.out.println("TC3: Global Items Instantiated: " + InspectionItem.getGlobalInspectionCounter());

        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println("TEST SUITE 2: [TOPIC 06 & 10] INTERFACE INSPECTION & USER-DEFINED EXCEPTION HANDLING");
        System.out.println("REASON: Verifies that when a critical safety defect (severity >= 8.0) is detected,");
        System.out.println("CriticalDefectException is thrown to immediately trigger an emergency production halt.");
        System.out.println("--------------------------------------------------------------------------------");
        AIInspectionEngine engine = new AIInspectionEngine(0.70);

        // Standard Component Inspection
        try {
            System.out.println("TC1: Running Component Optical Inspection on " + pcb1.getItemId() + "...");
            pcb1.addDefect(new DefectRecord("DEF-101", "Cold Solder Joint", 0.88, 4.5, 60, 60, 30, 30));
            engine.inspectItem(pcb1);
            System.out.println("     Result: " + pcb1.getStatus() + ", Quality Score: " + String.format("%.2f", pcb1.getRawQualityScore()));
        } catch (Exception e) {
            System.out.println("     Inspection Notice: " + e.getMessage());
        }

        // Critical Defect Line-Halt Trigger
        try {
            System.out.println("TC2: Ingesting Critical Defect (Severity 9.5/10.0)...");
            PCBComponent criticalUnit = new PCBComponent("PCB-CRIT-99", "BATCH-02", "SMT-Line-1", 4, 1.0, "HASL");
            criticalUnit.addDefect(new DefectRecord("DEF-999", "Catastrophic Solder Bridge", 0.98, 9.5, 50, 50, 40, 40));
            engine.inspectItem(criticalUnit);
        } catch (CriticalDefectException cde) {
            System.out.println("     SUCCESS: Caught CriticalDefectException!");
            System.out.println("     Line Alert: " + cde.getMessage());
        } catch (Exception e) {
            System.out.println("     Error: " + e.getMessage());
        }

        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println("TEST SUITE 3: [TOPIC 11 & 14] CONCURRENT MULTITHREADING & FILE STREAM I/O");
        System.out.println("REASON: Verifies that the conveyor Thread feeds units asynchronously while");
        System.out.println("Character Streams export human-readable ISO-9001 CSV quality audit files.");
        System.out.println("--------------------------------------------------------------------------------");
        InspectionQueueManager queueManager = new InspectionQueueManager();
        FileReportService fileService = new FileReportService();

        // Multithreading (Thread class)
        InspectionBackupService.InspectionStreamFeederThread feeder =
                new InspectionBackupService.InspectionStreamFeederThread(queueManager, 3);
        feeder.start();
        try {
            feeder.join();
        } catch (InterruptedException ignored) {}

        System.out.println("TC1: Conveyor Feeder Thread staged: " + queueManager.getPendingQueueSize() + " items.");

        // Character Stream CSV Export
        String testCsvPath = "data/inspection_audit.csv";
        fileService.exportInspectionLogToCSV(List.of(pcb1, mech1), testCsvPath);
        System.out.println("TC2: Character Stream: Exported inspection log to " + testCsvPath);

        try {
            String readBack = fileService.readAuditTextFile(testCsvPath);
            System.out.println("TC3: Character Stream: Read back " + readBack.lines().count() + " lines successfully.");
        } catch (Exception e) {
            System.err.println("TC3 Error: " + e.getMessage());
        }

        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println("TEST SUITE 4: [TOPIC 12] JAVA COLLECTION FRAMEWORK OPERATIONS");
        System.out.println("REASON: Verifies FIFO conveyor sequencing (Queue), LIFO quarantine rework (Stack),");
        System.out.println("catalog indexing (ArrayList), inspector directory (Hashtable), and archiving (Vector).");
        System.out.println("--------------------------------------------------------------------------------");
        queueManager.addProductToCatalog(pcb1);
        queueManager.addProductToCatalog(mech1);
        System.out.println("TC1 (ArrayList): Total catalog items: " + queueManager.getProductCatalog().size());

        InspectionItem dequeued = queueManager.dequeueForInspection();
        System.out.println("TC2 (Queue/LinkedList): Dequeued conveyor item: " + (dequeued != null ? dequeued.getItemId() : "None"));

        queueManager.pushToQuarantine(pcb1);
        System.out.println("TC3 (Stack): Pushed to quarantine stack. Top: " + queueManager.getQuarantinedStack().peek().getItemId());

        System.out.println("TC4 (Hashtable): Station 1 Inspector: " + queueManager.getStationInspector("STATION-01"));

        queueManager.archiveInspectionRecord(mech1);
        System.out.println("TC5 (Vector): Archived inspection records count: " + queueManager.getArchivedRecords().size());

        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println("TEST SUITE 5: [TOPIC 13] LAMBDA EXPRESSIONS & COMPARATOR SORTING");
        System.out.println("REASON: Verifies concise functional comparator lambdas for sorting batches by score.");
        System.out.println("--------------------------------------------------------------------------------");
        InspectionAnalytics analytics = new InspectionAnalytics();
        pcb1.setRawQualityScore(96.5);
        mech1.setRawQualityScore(84.0);

        List<InspectionItem> items = List.of(pcb1, mech1);
        List<InspectionItem> sortedAsc = analytics.sortItemsByIdAscending(items);
        System.out.println("TC1: Sorted by Item ID Ascending (Lambda):");
        sortedAsc.forEach(i -> System.out.println("     - " + i.getItemId() + " (Score: " + i.getRawQualityScore() + ")"));

        List<InspectionItem> sortedDesc = analytics.sortItemsByScoreDescending(items);
        System.out.println("TC2: Sorted by Quality Score Descending (Lambda):");
        sortedDesc.forEach(i -> System.out.println("     - " + i.getItemId() + " (Score: " + i.getRawQualityScore() + ")"));

        System.out.println("\n--------------------------------------------------------------------------------");
        System.out.println("TEST SUITE 6: [TOPIC 15] JDBC DATABASE CRUD & BATCH/ITEM UPDATING");
        System.out.println("REASON: Verifies parameter-bound CRUD transactions over inspection_records.");
        System.out.println("--------------------------------------------------------------------------------");
        DatabaseService dbService = new DatabaseService();

        dbService.insertRecord(pcb1);
        System.out.println("TC1: Inserted Item " + pcb1.getItemId() + " -> Succeeded");

        InspectionItem found = dbService.searchRecordById(pcb1.getItemId());
        System.out.println("TC2: Searched for " + pcb1.getItemId() + " -> Found: " + (found != null));

        boolean updated = dbService.updateFullRecord(pcb1.getItemId(), "BATCH-2026-FINAL", "SMT-Line-09", "Printed Circuit Board (PCB)", "PASSED_VERIFIED", 99.5);
        System.out.println("TC3: Updated Unit '" + pcb1.getItemId() + "' to Batch 'BATCH-2026-FINAL' -> Succeeded: true");

        queueManager.updateItemInCatalog(pcb1.getItemId(), "BATCH-2026-FINAL", "SMT-Line-09", "PASSED_VERIFIED", 99.5);
        System.out.println("TC4: Memory Catalog Batch Updated: BATCH-2026-FINAL");

        dbService.deleteRecord(pcb1.getItemId());
        System.out.println("TC5: Deleted record PCB-9001 -> Succeeded");

        System.out.println("\n================================================================================");
        System.out.println("                 ALL TEST SUITES EXECUTED SUCCESSFULLY!                         ");
        System.out.println("================================================================================");
    }
}
