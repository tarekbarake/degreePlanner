package degreePlanner.graph;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.stream.Collectors;

import degreePlanner.domain.Course;
import degreePlanner.service.CourseCatalogService;

public class TopologicalPlanner {
    private int maxCreditPerSemester;
    private Map<String,Course> catalog;

    public List<String> computeOrder(PrerequisiteGraph graph) {
        Map<String, List<String>> adjList = graph.getAdjacencyList();

        // 1. Calculate In-Degree (Number of Unmet Prerequisites)
        // In a dependency graph (Course -> Prereqs), in-degree is just the number of prereqs.0
        Map<String, Integer> inDegree = new HashMap<>();
        for (String course : adjList.keySet()) {
            inDegree.put(course, adjList.get(course).size());
        }

        // 2. Queue for courses with ZERO prerequisites (Ready to take)
        Queue<String> queue = new LinkedList<>();
        for (String course : inDegree.keySet()) {
            if (inDegree.get(course) == 0) {
                queue.add(course);
            }
        }

        List<String> order = new ArrayList<>();
        while (!queue.isEmpty()) {
            String current = queue.poll();
            order.add(current);

            // 3. Update courses that depend on 'current'
            // Since our graph is Course -> Prereqs, we must scan to find who lists 'current' as a prereq.
            for (String course : adjList.keySet()) {
                List<String> prereqs = adjList.get(course);
                if (prereqs.contains(current)) {
                    // 'course' depends on 'current'. One prereq satisfied.
                    inDegree.put(course, inDegree.get(course) - 1);
                    if (inDegree.get(course) == 0) {
                        queue.add(course);
                    }
                }
            }
        }

        return order;
    }
    public List<String> computeSemesterPlan(PrerequisiteGraph graph, int maxCredits){
        // plan 3 courses per semester starting from the pre-req
        // or from the courses that their pre-req have been met
        int currentCredits=0;
        List<String> topologicallyOrderedCourses = computeOrder(graph);
        List<String>semsterList=new ArrayList<>();
        for (String course : topologicallyOrderedCourses){
            int new_credit=CourseCatalogService.getInstance().getCourseByCode(course).getCourseCredits();
            if (currentCredits+new_credit<=maxCredits) {
                semsterList.add(course);
                currentCredits += new_credit;
            }
            else{
                System.out.println("Registeration Failed . Reached max credit");
                break;
            }
        }
        return semsterList;
    }
}