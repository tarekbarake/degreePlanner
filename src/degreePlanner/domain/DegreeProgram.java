package degreePlanner.domain;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class DegreeProgram {
    private String name;
    private Set<String> requiredCourseCodes;
    private int totalCreditsRequired;

    public DegreeProgram() {
        this.requiredCourseCodes = new HashSet<>();
    }

    public DegreeProgram(String name, Set<String> requiredCourseCodes, int totalCreditsRequired) {
        this.name = name;
        this.requiredCourseCodes = requiredCourseCodes;
        this.totalCreditsRequired = totalCreditsRequired;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<String> getRequiredCourseCodes() {
        return Collections.unmodifiableSet(requiredCourseCodes);
    }

    public void setRequiredCourseCodes(Set<String> requiredCourseCodes) {
        this.requiredCourseCodes = requiredCourseCodes;
    }

    public int getTotalCreditsRequired() {
        return totalCreditsRequired;
    }

    public void setTotalCreditsRequired(int totalCreditsRequired) {
        this.totalCreditsRequired = totalCreditsRequired;
    }
}