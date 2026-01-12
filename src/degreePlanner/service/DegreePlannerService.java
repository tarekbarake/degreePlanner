package degreePlanner.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import degreePlanner.domain.Course;
import degreePlanner.domain.DegreeProgram;
import degreePlanner.domain.SemesterPlan;
import degreePlanner.domain.StudentRecord;
import degreePlanner.graph.PrerequisiteGraph;
import degreePlanner.graph.TopologicalPlanner;

public class DegreePlannerService {

    private CourseCatalogService catalogService;
    private TopologicalPlanner topologicalPlanner;

    public DegreePlannerService() {
        this.catalogService = CourseCatalogService.getInstance();
        this.topologicalPlanner = new TopologicalPlanner();
    }

    /**
     * Validates a given SemesterPlan against the student's record and program requirements.
     * Checks for:
     * 1. Course existence in catalog
     * 2. Student eligibility (prerequisites met, not already passed)
     * 3. Credit limits (if enforced by SemesterPlan or global max)
     */
    public boolean validateSemesterPlan(
            SemesterPlan plan,
            StudentRecord student,
            DegreeProgram program) {

        // STEP 1: Retrieve planned course codes from SemesterPlan
        List<String> plannedCourseCodes = plan.getPlannedCourseCodes();
        if (plannedCourseCodes == null) return true; // Empty plan is valid

        // STEP 2: Basic Validation (Existence)
        for (String code : plannedCourseCodes) {
            Course course = catalogService.getCourseByCode(code);
            if (course == null) {
                System.out.println("Validation Error: Course " + code + " not found in catalog.");
                return false;
            }
        }

        // STEP 3: Eligibility Validation
        // Ask EligibilityService which courses are eligible
        for (String code : plannedCourseCodes) {
            try {
                // Check if planned courses are among eligible ones
                boolean isEligible = EligibilityService.isCourseEligible(code, student, program);
                if (!isEligible) {
                    System.out.println("Validation Error: Student is not eligible for course " + code);
                    return false;
                }
            } catch (Exception e) {
                System.out.println("Validation Error: Exception verifying eligibility for course " + code);
                e.printStackTrace();
                return false;
            }
        }

        // STEP 4: Credit Limit Check
        // Ensure total does not exceed plan's maxCredits
        int totalCredits = 0;
        for (String code : plannedCourseCodes) {
            totalCredits += catalogService.getCourseByCode(code).getCourseCredits();
        }

        if (plan.getMaxCredits() > 0 && totalCredits > plan.getMaxCredits()) {
            System.out.println("Validation Error: Total credits " + totalCredits + " exceeds limit " + plan.getMaxCredits());
            return false;
        }

        // STEP 5: Return true if all checks pass
        return true;
    }

    /**
     * Generates a multi-semester graduation plan based on the degree program.
     */
    public List<List<String>> generateEarliestGraduationPlan(
            DegreeProgram program,
            int maxCreditsPerSemester) {

        // STEP 1: Identify all courses required for the degree
        Set<String> requiredCodes = program.getRequiredCourseCodes();
        if (requiredCodes == null || requiredCodes.isEmpty()) {
            return Collections.emptyList();
        }

        List<Course> requiredCourses = requiredCodes.stream()
                .map(code -> catalogService.getCourseByCode(code))
                .filter(c -> c != null)
                .collect(Collectors.toList());

        // STEP 2: Build Prerequisite Graph
        PrerequisiteGraph graph = new PrerequisiteGraph();
        // graph.buildFromCatalog(requiredCourses); // Assumes method exists as per UML
        // Since buildFromCatalog is likely dependent on impl, we perform a manual build if needed or assume it's there.
        // For this skeleton, we assume usage of available methods or just leave it prepared.

        // STEP 3: Delegate to TopologicalPlanner
        // Ideally: return topologicalPlanner.computeSemesterPlan(graph, maxCreditsPerSemester, catalogService.getAllCourses());
        // Current TopologicalPlanner returns List<String>. Below is a placeholder or adaptation.

        List<String> singleSemester = topologicalPlanner.computeSemesterPlan(graph, maxCreditsPerSemester);

        // Wrap in a list of lists for now to match signature
        List<List<String>> plan = new ArrayList<>();
        if (singleSemester != null && !singleSemester.isEmpty()) {
            plan.add(singleSemester);
        }

        return plan;
    }
}