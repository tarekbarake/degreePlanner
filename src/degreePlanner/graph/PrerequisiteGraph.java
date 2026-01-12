package degreePlanner.graph;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import degreePlanner.domain.Course;

public class PrerequisiteGraph {
    private  Map<String, List<String> > adjacencyList= new HashMap<>();

    public void buildFromCatalog(Collection<Course> courses){
        for (Course course : courses) {
            adjacencyList.put(course.getCourseNameAndNumber(), course.getCoursePrerequisites());
        }
    }

    public Map<String, List<String>> getAdjacencyList() {
        return adjacencyList;
    }
}