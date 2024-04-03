package city;

import jason.environment.grid.Location;

import java.util.*;

public class AStar {

    private CityModel cityModel;

    public AStar(CityModel cityModel) {
        this.cityModel = cityModel;
    }

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
                    continue; // Skip non-walkable nodes or nodes occupied by other agents.
                }

                Node neighborNode = allNodes.getOrDefault(neighbor, new Node(neighbor));
                allNodes.put(neighbor, neighborNode);

                int tentativeG = current.g + 1; // Assuming cost between neighbors is always 1.
                if (tentativeG < neighborNode.g) {
                    neighborNode.parent = current;
                    neighborNode.g = tentativeG;
                    neighborNode.f = tentativeG + estimateHeuristic(neighbor, end);

                    openSet.add(neighborNode);
                }
            }
        }
        return Collections.emptyList(); // No path found
    }

    private List<Location> buildPath(Node target) {
        List<Location> path = new LinkedList<>();
        while (target != null) {
            ((LinkedList<Location>) path).addFirst(target.location);
            target = target.parent;
        }
        return path;
    }

    private int estimateHeuristic(Location a, Location b) {
        // Manhattan distance
        return Math.abs(a.x - b.x) + Math.abs(a.y - b.y);
    }

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

        private Node(Location loc) {
            this(loc, null, Integer.MAX_VALUE, 0);
        }

        public int getF() {
            return f;
        }
    }
}
