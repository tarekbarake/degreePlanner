package degreePlanner.domain;

import java.util.ArrayList;
import java.util.List;
import degreePlanner.service.CourseCatalogService;

public class SemesterPlan {
    private List<String> plannedCourseCodes=new ArrayList<String>();
    private int maxCredits = 18;


    public boolean addCourse(String code, CourseCatalogService catalogService) {
        Course course = catalogService.getCourseByCode(code); // catalogService hides the Map
        if (course == null) {
            System.out.println("Course not found in catalog");
            return false;
        }

        int currentCredits = plannedCourseCodes.stream()
                .mapToInt(c -> catalogService.getCourseByCode(c).getCourseCredits())
                .sum();

        if (currentCredits + course.getCourseCredits() > maxCredits) return false;

        plannedCourseCodes.add(code);
        return true;
    }


    public List<String> getPlannedCourseCodes() {
        return plannedCourseCodes;
    }


    public int getMaxCredits() {
        return maxCredits;
    }


}