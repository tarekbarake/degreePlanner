package degreePlanner.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;
import degreePlanner.service.CourseCatalogService;


public class StudentRecord {
    private Set<String> completedCourses = new HashSet<>();
    public void addCompletedCourse(String code){
        completedCourses.add(code);
        Course c = CourseCatalogService.getInstance().getCourseByCode(code);
        if (c != null) {
            c.setCourseStatus(enCourseStatus.PASSED);
        }
    }
    public boolean hasCompleted(String code){
        return completedCourses.contains(code);
    }
    public Set<String> getPassedCourses() {
        return completedCourses.stream()
                .filter(c -> CourseCatalogService.getInstance().getCourseByCode(c).getCourseStatus() == enCourseStatus.PASSED)
                .collect(Collectors.toSet());
    }
    public Set<String> getCompletedCourses(){
        return Collections.unmodifiableSet(completedCourses);
    }

}