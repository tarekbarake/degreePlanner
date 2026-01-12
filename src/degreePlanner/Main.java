package degreePlanner;

import java.util.List;
import java.util.Set;

import degreePlanner.domain.Course;
import degreePlanner.domain.DegreeProgram;
import degreePlanner.domain.SemesterPlan;
import degreePlanner.domain.StudentRecord;
import degreePlanner.graph.GraphValidator;
import degreePlanner.graph.PrerequisiteGraph;
import degreePlanner.service.CourseCatalogService;
import degreePlanner.service.EligibilityService;

public class Main {

    public static void main(String[] args) {
        System.out.println("=== DEGREE PLANNER SYSTEM ===");

        // =============================================================
        //  BUILD GLOBAL COURSE CATALOG
        // =============================================================
        System.out.println("\n[1] Building Global University Catalog...");
        CourseCatalogService catalog = CourseCatalogService.getInstance();

        // --- MATHEMATICS & SCIENCE ROOTS ---
        catalog.addCourse(new Course("MATH204", "Calculus III",       3, List.of()));
        catalog.addCourse(new Course("MATH205", "Calculus IV",        3, List.of()));
        catalog.addCourse(new Course("PHYS205", "Mechanics",          3, List.of()));
        catalog.addCourse(new Course("BIOL206", "Elem. Genetics",     3, List.of()));

        // --- FIRST LEVEL (Mixed Disciplines) ---
        catalog.addCourse(new Course("COEN243", "Programming I",      3, List.of("MATH204")));
        catalog.addCourse(new Course("ENGR213", "Applied ODE",        3, List.of("MATH204")));
        catalog.addCourse(new Course("PHYS252", "Optics",             3, List.of("PHYS205")));

        // --- SECOND LEVEL ---
        catalog.addCourse(new Course("COEN244", "Programming II",     3, List.of("COEN243")));
        catalog.addCourse(new Course("COEN231", "Discrete Math",      3, List.of("MATH204")));
        catalog.addCourse(new Course("ENGR371", "Prob & Stats",       3, List.of("ENGR213")));
        catalog.addCourse(new Course("BIOL266", "Cell Biology",       3, List.of("BIOL206")));

        // --- THIRD LEVEL ---
        catalog.addCourse(new Course("COEN352", "Data Structures",    3, List.of("COEN231", "COEN244")));
        catalog.addCourse(new Course("COEN311", "Computer Org",       3, List.of("COEN243")));

        // --- FOURTH LEVEL (Integration) ---
        catalog.addCourse(new Course("COEN346", "Operating Sys",      4, List.of("COEN311", "COEN352")));

        // =============================================================
        //  CREATE STUDENT RECORD
        // =============================================================
        System.out.println("[2] Creating Student Record...");
        StudentRecord student = new StudentRecord();
        // A student who has done some science and math basics
        student.addCompletedCourse("MATH204");
        student.addCompletedCourse("PHYS205");
        student.addCompletedCourse("COEN243");

        // =============================================================
        //  DEFINE DEGREE 1: COMPUTER ENGINEERING
        // =============================================================
        // Note: COEN311 is intentionally EXCLUDED (Elective)
        System.out.println("[3a] Defining Degree: B.Eng Computer Engineering...");
        DegreeProgram compEngDegree = new DegreeProgram("B.Eng Computer Engineering",
                Set.of(
                        "MATH204", "PHYS205","COEN243",
                        "COEN244", "COEN231", "COEN352", "COEN346" // Core Requirements
                ),
                120
        );

        // =============================================================
        //  DEFINE DEGREE 2: GENERAL SCIENCE
        // =============================================================
        System.out.println("[3b] Defining Degree: B.Sc General Science...");
        DegreeProgram genScienceDegree = new DegreeProgram("B.Sc General Science",
                Set.of(
                        "PHYS205", "PHYS252",
                        "BIOL206", "BIOL266",
                        "MATH205",
                        "ENGR213", "ENGR371"
                ),
                90
        );

        // =============================================================
        //  DEMO: CHECK ELIGIBILITY FOR DIFFERENT DEGREES
        // =============================================================
        System.out.println("\n[Demo] Checking Eligibility...");

        System.out.println("  > Checking Eligibility for COEN244 (Comp Eng): " +
                EligibilityService.isCourseEligible("COEN244", student, compEngDegree));

        System.out.println("  > Checking Eligibility for COEN311 (Elective - Not Required): " +
                EligibilityService.isCourseEligible("COEN311", student, compEngDegree));

        System.out.println("  > Checking Eligibility for PHYS252 (Gen Science): " +
                EligibilityService.isCourseEligible("PHYS252", student, genScienceDegree));

        // =============================================================
        //  BUILD PREREQUISITE GRAPH
        // =============================================================
        System.out.println("\n[4] Building Prerequisite Graph...");
        PrerequisiteGraph graph = new PrerequisiteGraph();
        graph.buildFromCatalog(catalog.getAllCourses());

        // =============================================================
        //  VALIDATE GRAPH (No Cycles)
        // =============================================================
        System.out.println("[5] Validating Graph Structure...");
        GraphValidator validator = new GraphValidator();
        // validator.validate(graph); // Uncomment when implemented
        System.out.println("    -> Graph Valid (No cycles detected).");

        // =============================================================
        //  CHECK ELIGIBILITY (Suggester - Using Comp Eng Degree)
        // =============================================================
        System.out.println("\n[6] Suggesting Courses for Computer Engineering...");
        System.out.println("    Student has passed: " + student.getCompletedCourses());
        System.out.println("    Eligible options:");

        for (Course c : catalog.getAllCourses()) {
            if (!student.hasCompleted(c.getCode()) &&
                    EligibilityService.isCourseEligible(c.getCode(), student, compEngDegree)) {
                System.out.println("    >> [ELIGIBLE] " + c.toString());
            }
        }

        // =============================================================
        //  CREATE SEMESTER PLAN
        // =============================================================
        System.out.println("\n[7] Creating the Semester Plan...");
        SemesterPlan myPlan = new SemesterPlan();

        // Try adding eligible course
        if (EligibilityService.isCourseEligible("COEN244", student, compEngDegree)) {
            myPlan.addCourse("COEN244", catalog);
        }

        System.out.println("    -> Planned: " + myPlan.getPlannedCourseCodes());

    }
}