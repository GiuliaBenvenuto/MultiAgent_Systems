package path;

import city.CityModel;
import jason.environment.grid.Location;
import java.util.*;


/** ---------- AStar ----------
 * This class represents the A* algorithm to find the shortest path between two locations in the city.
 */

public class AStar {

    private CityModel cityModel;

    // Constructor
    public AStar(CityModel cityModel) {
        this.cityModel = cityModel;
    }


    // Find the shortest path between two locations (start - end) in the city
    public List<Location> findPath(int agentId, Location start, Location end) {
        // Priority queue to hold nodes to be evaluated, sorted by "f" total cost (total estimated cost)
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(Node::getF));
        // Create a map to keep track of all nodes
        Map<Location, Node> allNodes = new HashMap<>();

        // Init the startNode with the start location and add it to the openSet, g cost = 0 and h cost = heuristic
        Node startNode = new Node(start, null, 0, estimateHeuristic(start, end));
        allNodes.put(start, startNode);
        openSet.add(startNode);

        // Loop until the openSet is empty
        while (!openSet.isEmpty()) {
            // Poll a node from the openSet and set it as the current node
            Node current = openSet.poll();
            // Check if the current node is the target node
            if (current.location.equals(end)) {
                // If it is, build the path and return it
                return buildPath(current);
            }

            // For each neighbor of the current node
            for (Location neighbor : cityModel.getNeighbors(current.location)) {
                // Skip non-walkable nodes or nodes occupied by other agents
                if (!cityModel.isFree(neighbor.x, neighbor.y)) {
                    // Skip non-walkable nodes or nodes occupied by other agents
                    continue;
                }
                // Retreive the neighbor node from the map or create a new one
                Node neighborNode = allNodes.getOrDefault(neighbor, new Node(neighbor));
                allNodes.put(neighbor, neighborNode);

                // Assuming cost between neighbors is always 1
                // Calculate the tentative g cost for the neighbor node, assuming cost between neighbors is always 1
                int tentativeG = current.g + 1;
                // if the tentative g cost is less than the neighbor's g cost
                if (tentativeG < neighborNode.g) {
                    // Set the neighbor node's parent to the current node
                    neighborNode.parent = current;
                    // Update the neighbor node's g and f costs
                    neighborNode.g = tentativeG;
                    neighborNode.f = tentativeG + estimateHeuristic(neighbor, end);

                    // Add the neighbor node to the openSet
                    openSet.add(neighborNode);
                }
            }
        }
        // No path found return an empty list
        return Collections.emptyList();
    }


    // Method to build the path from the target node to the start node
    private List<Location> buildPath(Node target) {
        // Create the path as a list of locations (linked list)
        List<Location> path = new LinkedList<>();
        // Add each location to the front of the linked list
        while (target != null) {
            ((LinkedList<Location>) path).addFirst(target.location);
            target = target.parent;
        }
        return path;
    }


    // Method to estimate the heuristic cost between two locations
    private int estimateHeuristic(Location a, Location b) {
        // Manhattan distance
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }


    // Inner class to represent a node in the A* algorithm
    private static class Node {
        Location location; // Location of the node
        Node parent; // Parent node
        int g; // Cost from start
        int f; // Total cost

        // Constructor
        private Node(Location loc, Node parent, int g, int h) {
            this.location = loc;
            this.parent = parent;
            this.g = g;
            this.f = g + h;
        }

        // Method to create a new node
        private Node(Location loc) {
            this(loc, null, Integer.MAX_VALUE, 0);
        }

        // Method to get the total cost
        public int getF() {
            return f;
        }
    }
}
