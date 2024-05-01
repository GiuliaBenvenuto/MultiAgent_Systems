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
        PriorityQueue<Node> openSet = new PriorityQueue<>(Comparator.comparingInt(Node::getF));
        Map<Location, Node> allNodes = new HashMap<>();

        Node startNode = new Node(start, null, 0, estimateHeuristic(start, end));
        allNodes.put(start, startNode);
        openSet.add(startNode);

        while (!openSet.isEmpty()) {
            Node current = openSet.poll();
            if (current.location.equals(end)) {
                return buildPath(current);
            }

            for (Location neighbor : cityModel.getNeighbors(current.location)) {
                if (!cityModel.isFree(neighbor.x, neighbor.y)) {
                    // Skip non-walkable nodes or nodes occupied by other agents
                    continue;
                }

                Node neighborNode = allNodes.getOrDefault(neighbor, new Node(neighbor));
                allNodes.put(neighbor, neighborNode);

                // Assuming cost between neighbors is always 1
                int tentativeG = current.g + 1;
                if (tentativeG < neighborNode.g) {
                    neighborNode.parent = current;
                    neighborNode.g = tentativeG;
                    neighborNode.f = tentativeG + estimateHeuristic(neighbor, end);

                    openSet.add(neighborNode);
                }
            }
        }
        // No path found
        return Collections.emptyList();
    }


    // Method to build the path from the target node to the start node
    private List<Location> buildPath(Node target) {
        List<Location> path = new LinkedList<>();
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
        Location location;
        Node parent;
        int g; // Cost from start
        int f; // Total cost

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
