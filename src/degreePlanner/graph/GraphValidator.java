package degreePlanner.graph;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GraphValidator {
    public boolean hasCycle(PrerequisiteGraph graph) {
        Map<String, List<String>> adjList = graph.getAdjacencyList();
        Set<String> visited = new HashSet<>();
        Set<String> recStack = new HashSet<>();

        // We loop through all nodes
        for (String course : adjList.keySet()) {
            if (dfs(course, adjList, visited, recStack)) {
                return true;
            }
        }
        return false;
    }

    // Returns true if a cycle is found
    private boolean dfs(String current, Map<String, List<String>> adjList,
                        Set<String> visited, Set<String> recStack) {

        // If it's in the current recursion stack, we found a cycle
        if (recStack.contains(current)) return true;

        // If we've already fully processed this node before, no need to check again
        if (visited.contains(current)) return false;

        // Add to both sets
        visited.add(current);
        recStack.add(current);

        // Visit neighbors (prerequisites)
        List<String> neighbors = adjList.get(current);
        if (neighbors != null) {
            for (String neighbor : neighbors) {
                if (dfs(neighbor, adjList, visited, recStack)) {
                    return true;
                }
            }
        }

        // Remove from recursion stack as we backtrack
        recStack.remove(current);
        return false;
    }

}