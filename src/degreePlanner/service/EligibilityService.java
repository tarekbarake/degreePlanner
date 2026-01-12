package degreePlanner.service;
import degreePlanner.domain.StudentRecord;
import degreePlanner.domain.DegreeProgram;
import degreePlanner.domain.Course;
/*
Eligibility conditions :
1- I have not completed this course before
2- It is required by the degree program
3- I have completed all prerequisites.
 */
/*Example use case

 */
public class EligibilityService {

    public static boolean isCourseEligible(String courseCode, StudentRecord studentRecord, DegreeProgram  degreeProgram){
        if (studentRecord.getPassedCourses().contains(courseCode)){
            System.out.println("  [DEBUG] Course " + courseCode + " already passed.");
            return false;
        }

        if (!degreeProgram.getRequiredCourseCodes().contains(courseCode)){
            System.out.println("  [DEBUG] Course " + courseCode + " not in degree requirements.");
            return false;
        }

        // Check 3: Have I completed all prerequisites?
        Course course = CourseCatalogService.getInstance().getCourseByCode(courseCode);
        if (course != null) {
            for (String prereq : course.getCoursePrerequisites()) {
                if (!studentRecord.getPassedCourses().contains(prereq)) {
                    System.out.println("  [DEBUG] Missing prereq " + prereq + " for " + courseCode);
                    return false;
                }
            }
        } else {
            System.out.println("  [DEBUG] Course " + courseCode + " not found in catalog.");
        }

        return true;
    }


}