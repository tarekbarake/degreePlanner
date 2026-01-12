package degreePlanner.service;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import degreePlanner.domain.Course;

public class CourseCatalogService {
    private static Map<String, Course> catalog = new HashMap<>();
    private static CourseCatalogService instance;

    public static CourseCatalogService getInstance() {
        if (instance == null) {
            instance = new CourseCatalogService();
        }
        return instance;
    }
    public void addCourse(Course course) {
        catalog.put(course.getCourseNameAndNumber(), course);
    }
    public Course getCourseByCode(String code) {
        return catalog.get(code);
    }

    public Collection<Course> getAllCourses() {
        return catalog.values(); // for iteration
    }
}