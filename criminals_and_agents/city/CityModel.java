package city;

import helper.AgentIdMapper;
import helper.AgentPercept;

import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;
import jason.environment.*;
import jason.asSyntax.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.*;
import java.util.List;
import java.util.Map;


/** ---------- CITY MODEL CLASS ----------
 * This class manages the creation, the state and the evolution of the city.
 *
 * In this class there are all the data structures that represent the city and the agents in it, such as:
 * - The grid of the city (40 x 40 cells)
 * - The obstacles in the city (walls, houses)
 * - The agents in the city (police, civilians, criminals, clue agents)
 * - The jail in the city
 *
 * Then in the class there are all the methods to place the agents and add the percpets to them.
 * (setPoliceAgentPos, setCivilianAgentPos, setCriminalAgentPos, setClueAgentPos)
 *
 * Furthermore in this class there are the methods to:
 * - Move agents without going through obstacles (isFree)
 * - Updating agent percepts based on their current positions and interactions
 * - Arresting criminals, discovering clues, and escorting criminals to jail
 */

public class CityModel extends GridWorldModel {

    private Logger logger = Logger.getLogger("criminals_and_agents.mas2j." + CityModel.class.getName());

    public static final int JAIL = 128;
    public static final int CLUE_AGENT = 256;
    public static final int POLICE_AGENT = 512;
    public static final int CIVILIAN_AGENT = 1024;
    public static final int CRIMINAL_AGENT = 2048;
    protected static CityModel city_model = null;
    public int n_arrested_criminals = 0;
    private int clueAgentPosCallCount = 0;

    Location jail;
    // Map to store the agent location
    private Map<Pair<Location, Integer>, Integer> agentLocationMap = new HashMap<>();
    // Map to store the police escorting state depending on the police agent id
    private Map<Integer, Boolean> policeEscortingState = new HashMap<>();
    // Map to store the police at jail state depeding on the police agent id
    private Map<Integer, Boolean> policeAtJailMap = new HashMap<>();


    // Pair class to store the agent location and type
    private static class Pair<L, R> {
        private final L left;
        private final R right;

        public Pair(L left, R right) {
            this.left = left;
            this.right = right;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Pair<?, ?> pair = (Pair<?, ?>) o;
            return Objects.equals(left, pair.left) &&
                    Objects.equals(right, pair.right);
        }

        @Override
        public int hashCode() {
            return Objects.hash(left, right);
        }
    }

    // Methods to create the city model
    synchronized public static CityModel create(int width, int height, int agents_number) {
        if (city_model == null) {
            city_model = new CityModel(width, height, agents_number);
        }
        return city_model;
    }

    private CityModel(int width, int height, int agents_number) {
        super(width, height, agents_number);
    }

    // Method to get the city model
    public static CityModel getCityModel() {
        return city_model;
    }

    // Method to set the jail location
    private void setJail(int i, int j) {
        jail = new Location(i, j);
        data[i][j] = JAIL;
    }

    // Method to get the jail location
    public Location getJail() {
        return jail;
    }

    // Method to clear the jail
    public void clearJail() {
        remove(JAIL, jail.x, jail.y);
    }

    // Method to clear the obstacles
    public void clearObstacles() {
        // Iterate over the entire grid and remove OBSTACLE at each position
        for (int i = 0; i < 40; i++) {
            for (int j = 0; j < 40; j++) {
                if (hasObject(OBSTACLE, i, j)) {
                    remove(OBSTACLE, i, j);
                }
            }
        }
    }

    // Method to clear the agents
    public void clearAgents() {
        for (int i = 0; i < 40; i++) {
            for (int j = 0; j < 40; j++) {
                if (hasObject(AGENT, i, j)) {
                    System.out.println("Removing agent at " + i + ", " + j);
                    remove(AGENT, i, j);
                }
            }
        }
    }

    // Method to get the number of arrested criminals
    public int getArrestedCriminals() {
        return n_arrested_criminals;
    }

    // Method to verify if a position is within the grid
    public boolean isInGrid(int x, int y) {
        return x >= 0 && x < 40 && y >= 0 && y < 40;
    }

    // Method to set the position of the clue agents and to add them the percepts about
    // their own position and id and about criminal position X or Y
    public boolean setClueAgentPos(int agId, int x, int y) {
        if (isInGrid(x, y) && !hasObject(OBSTACLE, x, y) && !hasObject(JAIL, x, y) && !hasObject(AGENT, x, y)) {
            // Set the position of the clue agent
            setAgPos(agId, x, y);
            add(CLUE_AGENT, x, y);
            agentLocationMap.put(new Pair<>(new Location(x, y), CLUE_AGENT), agId);

            // Add percepts about the clue agent position and id
            AgentPercept.updateAgentPercepts(CityEnvironment.getInstance(), agId, x, y);

            // Add percepts about criminal position X or Y
            clueAgentPosCallCount++;
            System.out.println("Clue agent position call count: " + clueAgentPosCallCount);
            // Determine which criminal info to use based on the counter
            if (clueAgentPosCallCount % 4 == 1) {
                AgentPercept.addCluePerceptY(CityEnvironment.getInstance(), agId, x, y, 3, 12);
            } else if (clueAgentPosCallCount % 4 == 2) {
                AgentPercept.addCluePerceptX(CityEnvironment.getInstance(), agId, x, y, 3, 10);
            } else if (clueAgentPosCallCount % 4 == 3) {
                AgentPercept.addCluePerceptY(CityEnvironment.getInstance(), agId, x, y, 4, 21);
            } else if (clueAgentPosCallCount % 4 == 0) {
                AgentPercept.addCluePerceptX(CityEnvironment.getInstance(), agId, x, y, 4, 26);
            }
            return true;
        }
        return false;
    }

    // Method to set the position of the police agents and to add them the percepts about
    // their own position and id and about start - end positions and jail position
    public boolean setPoliceAgentPos(int agId, int x, int y) {
        if (isInGrid(x, y) && !hasObject(OBSTACLE, x, y) && !hasObject(JAIL, x, y) && !hasObject(AGENT, x, y)) {
            // Set the position of the police agent
            setAgPos(agId, x, y);
            add(POLICE_AGENT, x, y);
            agentLocationMap.put(new Pair<>(new Location(x, y), POLICE_AGENT), agId);

            // Add percepts about the police agent position and id
            AgentPercept.updateAgentPercepts(CityEnvironment.getInstance(), agId, x, y);

            // Add start - end positions and jail position percept
            AgentPercept.addPolicePercept(CityEnvironment.getInstance(), agId, x, y);

            return true;
        }
        return false;
    }

    // Method to set the position of the civilian agents and to add them the percepts about
    // their own position and id and about the closest clue agent
    public boolean setCivilianAgentPos(int agId, int x, int y) {
        if (isInGrid(x, y) && !hasObject(OBSTACLE, x, y) && !hasObject(JAIL, x, y) && !hasObject(AGENT, x, y)) {
            // Set the position of the civilian agent
            setAgPos(agId, x, y);
            add(CIVILIAN_AGENT, x, y);
            agentLocationMap.put(new Pair<>(new Location(x, y), CIVILIAN_AGENT), agId);

            // Add percepts about the civilian agent position and id
            AgentPercept.updateAgentPercepts(CityEnvironment.getInstance(), agId, x, y);

            // Find the closest clue agent and add it as a percept to the civilian agent
            for (int i = 0; i < getWidth(); i++) {
                for (int j = 0; j < getHeight(); j++) {
                    if (hasObject(CLUE_AGENT, i, j)) {
                        // Find the clue agent id
                        int clueId = getAgentId(CLUE_AGENT, i, j);
                        // Add the closest clue agent as a percept
                        AgentPercept.addCivilianPercept(CityEnvironment.getInstance(), agId, x, y, clueId, i, j, city_model);
                    }
                }
            }
            return true;
        }
        return false;
    }

    // Method to set the position of the criminal agents and to add them the percepts about
    // their own position and id
    public boolean setCriminalAgentPos(int agId, int x, int y) {
        if (isInGrid(x, y) && !hasObject(OBSTACLE, x, y) && !hasObject(JAIL, x, y) && !hasObject(AGENT, x, y)) {
            // Set the position of the criminal agent
            setAgPos(agId, x, y);
            add(CRIMINAL_AGENT, x, y);
            agentLocationMap.put(new Pair<>(new Location(x, y), CRIMINAL_AGENT), agId);

            // Add percepts about the criminal agent position and id
            AgentPercept.updateAgentPercepts(CityEnvironment.getInstance(), agId, x, y);

            return true;
        }
        return false;
    }


    // Method to create the city model with the agents and the obstacles positioned in it
    static CityModel city1() throws Exception {
        CityModel city_model = CityModel.create(40, 40, 13);
        // Police agents
        city_model.setPoliceAgentPos(0, 35, 34);
        city_model.setPoliceAgentPos(1, 34, 35);
        city_model.setPoliceAgentPos(2, 34, 34);
        // Criminal agents
        city_model.setCriminalAgentPos(3, 10, 12);
        city_model.setCriminalAgentPos(4, 26, 21);
        // Clue agents
        city_model.setClueAgentPos(5, 5, 5);
        city_model.setClueAgentPos(6, 5, 35);
        city_model.setClueAgentPos(7, 35, 5);
        city_model.setClueAgentPos(8, 35, 30);
        // Civilian agents
        city_model.setCivilianAgentPos(9, 9, 4);
        city_model.setCivilianAgentPos(10, 10, 35);
        city_model.setCivilianAgentPos(11, 31, 9);
        city_model.setCivilianAgentPos(12, 29, 29);
        // Jail
        city_model.setJail(35, 35);

        // Obstacles
        // Top left corner
        for (int i = 3; i <= 9; i++) {
            city_model.add(CityModel.OBSTACLE, 4, i);
        }
        // Single obstacles
        city_model.add(CityModel.OBSTACLE, 4, 13);
        city_model.add(CityModel.OBSTACLE, 4, 15);
        city_model.add(CityModel.OBSTACLE, 15, 2);
        city_model.add(CityModel.OBSTACLE, 14, 2);

        // Vertical wall left bottom corener
        for (int i = 4; i < 9; i++) {
            city_model.add(CityModel.OBSTACLE, i, 14);
        }

        for (int i = 19; i < 22; i++) {
            city_model.add(CityModel.OBSTACLE, 4, i);
        }

        for (int i = 5; i < 9; i++) {
            city_model.add(CityModel.OBSTACLE, i, 20);
        }

        for (int i = 25; i <= 35; i++) {
            city_model.add(CityModel.OBSTACLE, 4, i);
        }

        // Diagonal wall top left corner
        for (int i = 0; i < 5; i++) {
            city_model.add(CityModel.OBSTACLE, 11 + i, 10 + i);
        }

        // Diagonal wall bottom left corner
        for (int i = 0; i < 5; i++) {
            city_model.add(CityModel.OBSTACLE, 14 + i, 35 - i);
        }

        // Horizontal wall
        for (int i = 5; i < 25; i++) {
            city_model.add(CityModel.OBSTACLE, 20, i);
        }

        // Vertical wall
        for (int i = 15; i < 25; i++) {
            city_model.add(CityModel.OBSTACLE, i, 20);
        }

        // Cross wall
        for (int i = 22; i < 29; i++) {
            city_model.add(CityModel.OBSTACLE, i, 25);
            city_model.add(CityModel.OBSTACLE, 25, i);
        }

        // Right up corner walls
        for (int i  = 26; i < 31; i++) {
            city_model.add(CityModel.OBSTACLE, i, 2);
        }
        for (int i  = 34; i < 38; i++) {
            city_model.add(CityModel.OBSTACLE, i, 2);
        }
        for (int i = 3; i < 6; i++) {
             city_model.add(CityModel.OBSTACLE, 37, i);
        }
        for (int i = 9; i < 14; i++) {
            city_model.add(CityModel.OBSTACLE, 37, i);
        }

        // Right middle walls
        city_model.add(CityModel.OBSTACLE, 32, 25);
        city_model.add(CityModel.OBSTACLE, 33, 25);
        city_model.add(CityModel.OBSTACLE, 37, 25);
        city_model.add(CityModel.OBSTACLE, 38, 25);

        // Houses
        int[] xCoords = {25, 27, 29};
        int[] yCoords = {14, 12, 10};
        for (int x : xCoords) {
            for (int y : yCoords) {
                city_model.add(CityModel.OBSTACLE, x, y);
            }
        }

        return city_model;
    }


    // Method to check if a location is free of obstacles and agents
    public boolean isFree(int x, int y) {
        // Chek over obstacle, jail, or any agent type
        return isInGrid(x, y) && !(hasObject(OBSTACLE, x, y) || hasObject(JAIL, x, y) ||
                hasObject(CLUE_AGENT, x, y) || hasObject(POLICE_AGENT, x, y) ||
                hasObject(CIVILIAN_AGENT, x, y) || hasObject(CRIMINAL_AGENT, x, y));
    }


    public int getAgentId(int agentType, int x, int y) {
        // using agentLocationMap
        return agentLocationMap.getOrDefault(new Pair<>(new Location(x, y), agentType), -1);
    }


    // Find closest clue agent
    public Location findClosestClueAgent(int x, int y) {
        Location closestClue = null;
        double minDistance = Double.MAX_VALUE;

        for (Map.Entry<Pair<Location, Integer>, Integer> entry : agentLocationMap.entrySet()) {
            if (entry.getKey().right.equals(CLUE_AGENT)) {
                Location clueLocation = entry.getKey().left;
                double distance = Math.sqrt(Math.pow(clueLocation.x - x, 2) + Math.pow(clueLocation.y - y, 2));
                if (distance < minDistance) {
                    minDistance = distance;
                    closestClue = clueLocation;
                }
            }
        }
        return closestClue;
    }


    // Update the position of the police agent following the path
    public void updatePoliceAgentPosition(int agId, int x, int y) {
        if (isFree(x, y)) {
            Location currentLoc = getAgPos(agId);
            remove(POLICE_AGENT, currentLoc.x, currentLoc.y);
            setAgPos(agId, x, y);
            add(POLICE_AGENT, x, y);
        }
    }

    // Police agent escorting a criminal to jail
    public void startEscorting(int policeId) {
        // Add logic to mark the police agent as escorting
        // This could be a simple boolean flag or a more complex state management
        // For example, you could add a Map<Integer, Boolean> to track the escorting state of each police agent
        policeEscortingState.put(policeId, true);
    }

    public void stopEscorting(int policeId) {
        policeEscortingState.put(policeId, false);
        n_arrested_criminals++;
        System.out.println("POLICE ID:" + policeId);
        // get x and y from policeId
        // Retrieve the current location of the police agent
        Location policeLocation = getAgPos(policeId);

        if (policeLocation != null) {
            CityEnvironment.getInstance().setContinueExploration(false);
        }
    }

    public boolean isEscorting(int policeId) {
        return policeEscortingState.getOrDefault(policeId, false);
    }


    public void setPoliceAtJail(int agId, boolean atJail) {
        this.policeAtJailMap.put(agId, atJail);
        // If the agent is no longer at jail, it should be visible again.
        if (!atJail) {
            // Code to make the agent visible again.
            // This could involve adding the POLICE_AGENT object back to the location it is supposed to be.
        }
    }

    public boolean isPoliceAtJail(int agId) {
        return policeAtJailMap.getOrDefault(agId, false);
    }

    // Police agent reached the jail so remove it from the grid and from the agentLocationMap
    public void removePoliceAgent(int agId, int x, int y) {
        // x --> currentPoliceLoc.x
        // y --> currentPoliceLoc.y

        if (hasObject(POLICE_AGENT, x, y)) {
            city_model.setPoliceAtJail(agId, true);

            remove(POLICE_AGENT, x, y); // Remove from the grid
            //remove(POLICE_AGENT, 34, 35);
            agentLocationMap.remove(new Pair<>(new Location(x, y), POLICE_AGENT)); // Remove from the map
            //agentLocationMap.remove(new Pair<>(new Location(34, 35), POLICE_AGENT));
            System.out.println("Police agent removed from location: " + x + ", " + y);

            String agentName = "police" + (agId+1);
            System.out.println("REMOVE POLICE AGENT WITH --> Agent name: " + agentName);

            AgentPercept.destroyAgent(CityEnvironment.getInstance(), agentName);
        }
    }


    // Criminal arrested so remove it from the grid and from the agentLocationMap
    public void arrestCriminal(int criminalId, int x, int y) {
        remove(AGENT, x, y);
        remove(CRIMINAL_AGENT, x, y);
        Location loc = new Location(x, y);
        agentLocationMap.remove(new Pair<>(loc, CRIMINAL_AGENT));
    }



    // ---------- Methods for A* algorithm ----------
    public List<Location> getNeighbors(Location loc) {
        List<Location> neighbors = new ArrayList<>();

        int[] dx = {-1, 1, 0, 0}; // Change in x (left, right)
        int[] dy = {0, 0, -1, 1}; // Change in y (up, down)

        for (int i = 0; i < dx.length; i++) {
            int newX = loc.x + dx[i];
            int newY = loc.y + dy[i];

            // Check if new location is within grid bounds and not an obstacle
            if (isInGrid(newX, newY) && isFree(newX, newY)) {
                neighbors.add(new Location(newX, newY));
            }
        }
        return neighbors;
    }


}
