package city;

import helper.AgentIdMapper;
import helper.AgentPercept;

import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;
import jason.environment.*;
import jason.asSyntax.*;

import java.util.HashMap;
import java.util.HashSet;
import java.util.logging.*;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CityModel extends GridWorldModel {

    private Logger logger = Logger.getLogger("criminals_and_agents.mas2j." + CityModel.class.getName());

    public static final int JAIL = 128;
    public static final int CLUE_AGENT = 256;
    public static final int POLICE_AGENT = 512;
    public static final int CIVILIAN_AGENT = 1024;
    public static final int CRIMINAL_AGENT = 2048;

    protected static CityModel city_model = null;
    Location jail;

    private Map<Pair<Location, Integer>, Integer> agentLocationMap = new HashMap<>();

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


    // Create the city model
    synchronized public static CityModel create(int width, int height, int agents_number) {
        if (city_model == null) {
            city_model = new CityModel(width, height, agents_number);
        }
        return city_model;
    }

    private CityModel(int width, int height, int agents_number) {
        super(width, height, agents_number);
    }

    public static CityModel getCityModel() {
        return city_model;
    }

    // Set the jail location
    private void setJail(int i, int j) {
        jail = new Location(i, j);
        data[i][j] = JAIL;
        // Print location
        // logger.info("Jail location: " + jail);

    }

    public Location getJail() {
        return jail;
    }

    // Clear jail
    public void clearJail() {
        remove(JAIL, jail.x, jail.y);
    }

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

    // Clear agents from the grid
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

    public boolean isInGrid(int x, int y) {
        return x >= 0 && x < 40 && y >= 0 && y < 40;
    }

    // Set position of the clue agent
    public boolean setClueAgentPos(int agId, int x, int y) {
        if (isInGrid(x, y) && !hasObject(OBSTACLE, x, y) && !hasObject(JAIL, x, y) && !hasObject(AGENT, x, y)) {

            //System.out.println("Clue id: " + agId);
            setAgPos(agId, x, y);
            add(CLUE_AGENT, x, y); // Mark the cell with the CLUE_AGENT identifier
            agentLocationMap.put(new Pair<>(new Location(x, y), CLUE_AGENT), agId);
            //CityEnvironment.getInstance().updateAgentPercepts(agId, x, y);
            AgentPercept.updateAgentPercepts(CityEnvironment.getInstance(), agId, x, y);


            return true;
        }
        return false;
    }

    // Set position of the police agent
    public boolean setPoliceAgentPos(int agId, int x, int y) {
        if (isInGrid(x, y) && !hasObject(OBSTACLE, x, y) && !hasObject(JAIL, x, y) && !hasObject(AGENT, x, y)) {

            //System.out.println("Police id: " + agId);
            setAgPos(agId, x, y);
            add(POLICE_AGENT, x, y); // Mark the cell with the POLICE_AGENT identifier
            agentLocationMap.put(new Pair<>(new Location(x, y), POLICE_AGENT), agId);
            //CityEnvironment.getInstance().updateAgentPercepts(agId, x, y);
            AgentPercept.updateAgentPercepts(CityEnvironment.getInstance(), agId, x, y);
            AgentPercept.addPolicePercept(CityEnvironment.getInstance(), agId, x, y);
            //policeAgentPercept

            return true;
        }
        return false;
    }

    // Set position of the civilian agent
    public boolean setCivilianAgentPos(int agId, int x, int y) {
        if (isInGrid(x, y) && !hasObject(OBSTACLE, x, y) && !hasObject(JAIL, x, y) && !hasObject(AGENT, x, y)) {

            // System.out.println("Civilian id: " + agId);
            setAgPos(agId, x, y);
            add(CIVILIAN_AGENT, x, y);  // Mark the cell with the POLICE_AGENT identifier
            agentLocationMap.put(new Pair<>(new Location(x, y), CIVILIAN_AGENT), agId);
            // CityEnvironment.getInstance().updateAgentPercepts(agId, x, y);
            AgentPercept.updateAgentPercepts(CityEnvironment.getInstance(), agId, x, y);

            // for this civlian agent at position x, y find the closest clue agent


            for (int i = 0; i < getWidth(); i++) {
                for (int j = 0; j < getHeight(); j++) {
                    if (hasObject(CLUE_AGENT, i, j)) {
                        // find the clue agent id
                        int clueId = getAgentId(CLUE_AGENT, i, j);
                        //System.out.println("Clue agent found at " + i + ", " + j + " with id: " + clueId);
                        AgentPercept.addCivilianPercept(CityEnvironment.getInstance(), agId, x, y, clueId, i, j, city_model);
                    }
                }
            }

            return true;
        }
        return false;
    }

    // Set position of the criminal agent
    public boolean setCriminalAgentPos(int agId, int x, int y) {
        if (isInGrid(x, y) && !hasObject(OBSTACLE, x, y) && !hasObject(JAIL, x, y) && !hasObject(AGENT, x, y)) {

            //System.out.println("Criminal id: " + agId);
            setAgPos(agId, x, y);
            add(CRIMINAL_AGENT, x, y);  // Mark the cell with the POLICE_AGENT identifier
            agentLocationMap.put(new Pair<>(new Location(x, y), CRIMINAL_AGENT), agId);
            //CityEnvironment.getInstance().updateAgentPercepts(agId, x, y);
            AgentPercept.updateAgentPercepts(CityEnvironment.getInstance(), agId, x, y);

            return true;
        }
        return false;
    }


    //  --------------- City 1 ---------------
    static CityModel city1() throws Exception {
        CityModel city_model = CityModel.create(40, 40, 13);

//        // ----- Set police location -----
//        city_model.setPoliceAgentPos(0, 35, 34);
//        city_model.setPoliceAgentPos(1, 34, 35);
//        city_model.setPoliceAgentPos(2, 34, 34);
//
//        // ----- Set civilians location -----
//        city_model.setCivilianAgentPos(3, 1, 1);
//        city_model.setCivilianAgentPos(4, 1, 29);
//        city_model.setCivilianAgentPos(5, 29, 1);
//        city_model.setCivilianAgentPos(6, 29, 29);
//
//
//        // ----- Set criminals location -----
//        city_model.setCriminalAgentPos(7, 10, 10);
//        city_model.setCriminalAgentPos(8, 10, 20);
//        //city_model.setCriminalAgentPos(7, 25, 33);
//        //city_model.setCriminalAgentPos(8, 35, 32);
//
//        // ----- Set clues location -----
//        city_model.setClueAgentPos(9, 5, 5);
//        city_model.setClueAgentPos(10, 5, 35);
//        city_model.setClueAgentPos(11, 35, 5);
//        city_model.setClueAgentPos(12, 35, 30);

        city_model.setPoliceAgentPos(0, 35, 34);
        city_model.setPoliceAgentPos(1, 34, 35);
        city_model.setPoliceAgentPos(2, 34, 34);

        city_model.setCriminalAgentPos(3, 10, 10);
        city_model.setCriminalAgentPos(4, 15, 25);

        city_model.setClueAgentPos(5, 5, 5);
        city_model.setClueAgentPos(6, 5, 35);
        city_model.setClueAgentPos(7, 35, 5);
        city_model.setClueAgentPos(8, 35, 30);

        city_model.setCivilianAgentPos(9, 9, 4);
        city_model.setCivilianAgentPos(10, 10, 35);
        city_model.setCivilianAgentPos(11, 31, 9);
        city_model.setCivilianAgentPos(12, 29, 29);


        // ----- Set jail location -----
        city_model.setJail(35, 35);

        // ----- Set walls -----
        for (int i = 4; i <= 11; i++) {
            city_model.add(CityModel.OBSTACLE, 4, i);
        }
        // Diagonal wall
        for (int i = 0; i < 5; i++) {
            city_model.add(CityModel.OBSTACLE, 10 + i, 10 + i);
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
        // Additional single obstacles
        city_model.add(CityModel.OBSTACLE, 2, 15);
        city_model.add(CityModel.OBSTACLE, 2, 16);
        city_model.add(CityModel.OBSTACLE, 27, 14);
        city_model.add(CityModel.OBSTACLE, 27, 13);
        city_model.add(CityModel.OBSTACLE, 15, 2);
        city_model.add(CityModel.OBSTACLE, 14, 2);
        city_model.add(CityModel.OBSTACLE, 16, 27);
        city_model.add(CityModel.OBSTACLE, 15, 27);

        return city_model;
    }


    //  --------------- City 2 ---------------
    static CityModel city2() throws Exception {
        CityModel city_model = CityModel.create(40, 40, 4);
        // ----- Set jail location -----
        city_model.setJail(20, 20);
        // ----- Set civilians location -----
        city_model.setAgPos(0, 2, 2);
        city_model.setAgPos(1, 2, 37);
        city_model.setAgPos(2, 37, 2);
        city_model.setAgPos(3, 37, 37);

        // ----- Set walls -----
        // Vertical wall near the left edge
        for (int i = 5; i <= 15; i++) {
            city_model.add(CityModel.OBSTACLE, 5, i);
        }
        // Horizontal wall near the bottom
        for (int i = 10; i < 30; i++) {
            city_model.add(CityModel.OBSTACLE, i, 30);
        }
        // L-shaped wall
        for (int i = 25; i <= 35; i++) {
            city_model.add(CityModel.OBSTACLE, 25, i);
        }

        // T-shaped wall
        for (int i = 20; i < 35; i++) {
            city_model.add(CityModel.OBSTACLE, i, 10);
        }

        // Additional single obstacles
        city_model.add(CityModel.OBSTACLE, 10, 20);
        city_model.add(CityModel.OBSTACLE, 10, 19);
        city_model.add(CityModel.OBSTACLE, 35, 5);
        city_model.add(CityModel.OBSTACLE, 34, 5);
        city_model.add(CityModel.OBSTACLE, 20, 37);
        city_model.add(CityModel.OBSTACLE, 21, 37);
        city_model.add(CityModel.OBSTACLE, 38, 25);
        city_model.add(CityModel.OBSTACLE, 38, 26);

        return city_model;
    }


    // --------------- City 3 ---------------
    static CityModel city3() throws Exception {
        CityModel city_model = CityModel.create(40, 40, 4);
        // Set jail location
        city_model.setJail(10, 10);
        // Set civilians' locations
        city_model.setAgPos(0, 3, 3);
        city_model.setAgPos(1, 3, 36);
        city_model.setAgPos(2, 36, 3);
        city_model.setAgPos(3, 36, 36);

        // Set walls
        // Horizontal wall near the top, with a gap in the middle
        for (int i = 5; i < 15; i++) {
            if (i != 10) { // Leave a gap
                city_model.add(CityModel.OBSTACLE, i, 5);
            }
        }
        for (int i = 25; i < 35; i++) {
            if (i != 30) { // Leave a gap
                city_model.add(CityModel.OBSTACLE, i, 5);
            }
        }

        // Vertical wall near the right edge, with a gap
        for (int i = 5; i < 35; i++) {
            if (i != 20) { // Leave a gap
                city_model.add(CityModel.OBSTACLE, 35, i);
            }
        }

        for (int i = 20; i < 35; i++) {
            city_model.add(CityModel.OBSTACLE, i, 10);
        }

        // create a cross wall in the left bottom corner
        for (int i = 5; i < 15; i++) {
            city_model.add(CityModel.OBSTACLE, 5, i);
            city_model.add(CityModel.OBSTACLE, i, 5);
        }

        for (int i = 10; i < 30; i++) {
            city_model.add(CityModel.OBSTACLE, i, 30);
        }

        for (int i = 20; i <= 35; i++) {
            city_model.add(CityModel.OBSTACLE, 15, i);
        }

        // Single obstacles
        city_model.add(CityModel.OBSTACLE, 8, 30);
        city_model.add(CityModel.OBSTACLE, 9, 30);
        city_model.add(CityModel.OBSTACLE, 31, 10);
        city_model.add(CityModel.OBSTACLE, 31, 9);
        city_model.add(CityModel.OBSTACLE, 15, 35);
        city_model.add(CityModel.OBSTACLE, 15, 34);
        city_model.add(CityModel.OBSTACLE, 25, 3);
        city_model.add(CityModel.OBSTACLE, 26, 3);

        return city_model;
    }


    // --------------- City 4 ---------------
    static CityModel city4() throws Exception {
        CityModel city_model = CityModel.create(40, 40, 4);
        // Set the jail location
        city_model.setJail(18, 18);

        city_model.setAgPos(0, 2, 2);
        city_model.setAgPos(1, 2, 37);
        city_model.setAgPos(2, 37, 2);
        city_model.setAgPos(3, 37, 37);

        for (int i = 0; i < 40; i++) {
            if (i != 17 && i != 18 && i != 19 && i != 20 && i != 21 && i != 22) { // Leave space around the jail
                city_model.add(CityModel.OBSTACLE, 19, i);
                city_model.add(CityModel.OBSTACLE, i, 19);
            }
        }

        for (int i = 7; i <= 12; i += 2) {
            for (int j = 7; j <= 12; j += 2) {
                city_model.add(CityModel.OBSTACLE, i, j);
                city_model.add(CityModel.OBSTACLE, i, 40 - 1 - j);
                city_model.add(CityModel.OBSTACLE, 40 - 1 - i, j);
                city_model.add(CityModel.OBSTACLE, 40 - 1 - i, 40 - 1 - j);
            }
        }
        return city_model;
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

    // Method to check if a location is free of obstacles and agents
    public boolean isFree(int x, int y) {
        // Chek over OBSTACLE, JAIL, or any AGENT types.
        return isInGrid(x, y) && !(hasObject(OBSTACLE, x, y) || hasObject(JAIL, x, y) ||
                hasObject(CLUE_AGENT, x, y) || hasObject(POLICE_AGENT, x, y) ||
                hasObject(CIVILIAN_AGENT, x, y) || hasObject(CRIMINAL_AGENT, x, y));
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




}
