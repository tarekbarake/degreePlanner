package degreePlanner.domain;

import java.util.Collections;
import java.util.List;
enum enCourseStatus{NOT_REGISTERED, PASSED, REGISTERED, FAILED};
public class Course {
    private String courseName;
    private String courseNumber;
    private String courseDescription;
    private int courseCredits;
    private List<String> coursePrerequisites; // List of prerequisite course codes
    private enCourseStatus courseStatus;

    // Full Constructor
    public Course(String courseName, String courseNumber, String courseDescription, int courseCredits,
                  List<String> coursePrerequisites, List<String> courseCorequisites) {
        this.courseName = courseName;
        this.courseNumber = courseNumber;
        this.courseDescription = courseDescription;
        this.courseCredits = courseCredits;
        this.courseStatus = enCourseStatus.NOT_REGISTERED;
        this.coursePrerequisites = coursePrerequisites != null ? coursePrerequisites : List.of();
    }

    // Simplified Constructor (Matches Main.java usage)
    public Course(String courseNumber, String courseName, int courseCredits, List<String> coursePrerequisites) {
        this(courseName, courseNumber, "", courseCredits, coursePrerequisites, null);
    }

    // Getters and Setters
    public String getCourseName() {
        return courseName;
    }
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getCourseNumber() {
        return courseNumber;
    }
    public void setCourseNumber(String courseNumber) {
        this.courseNumber = courseNumber;
    }

    // Helper for main.java usage "getCode()"
    public String getCode() {
        return courseNumber;
    }

    public String getCourseDescription() {
        return courseDescription;
    }
    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public int getCourseCredits() {
        return courseCredits;
    }
    public void setCourseCredits(int courseCredits) {
        this.courseCredits = courseCredits;
    }

    // Return unmodifiable lists to protect internal data
    public List<String> getCoursePrerequisites() {
        return Collections.unmodifiableList(coursePrerequisites);
    }
    public void setCoursePrerequisites(List<String> coursePrerequisites) {
        this.coursePrerequisites = coursePrerequisites != null ? coursePrerequisites : List.of();
    }

    // Convenience method: full course identifier (Name + Number)
    public String getCourseNameAndNumber() {
        // Warning: This is used as the key in CatalogService. If Main uses just "CS101" as key but this returns "Intro CS101", lookup fails.
        // For consistency with Main.java, we should ensure keys match.
        // Main uses "CS101" for lookups.
        return courseNumber;
    }

    public enCourseStatus getCourseStatus() {
        return courseStatus;
    }

    public void setCourseStatus(enCourseStatus courseStatus) {
        this.courseStatus = courseStatus;
    }
    @Override
    public String toString(){
        return this.getCourseNameAndNumber();
    }
}