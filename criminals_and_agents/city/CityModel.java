package city;

import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;
import jason.environment.*;

import java.util.HashSet;
import java.util.logging.*;

public class CityModel extends GridWorldModel {

    private Logger logger = Logger.getLogger("criminals_and_agents.mas2j." + CityModel.class.getName());

    public static final int JAIL = 128;
    public static final int CLUE_AGENT = 256;
    public static final int POLICE_AGENT = 512;
    public static final int CIVILIAN_AGENT = 1024;
    public static final int CRIMINAL_AGENT = 2048;

    protected static CityModel city_model = null;
    Location jail;

    // Create the city model
    synchronized public static CityModel create(int width, int height, int agents_number) {
        if (city_model == null) {
            city_model = new CityModel(width, height, agents_number);
        }
        return city_model;
    }

    // Set the jail location
    private void setJail(int i, int j) {
        jail = new Location(i, j);
        data[i][j] = JAIL;
        // Print location
        logger.info("Jail location: " + jail);

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

    private boolean isInGrid(int x, int y) {
        return x >= 0 && x < 40 && y >= 0 && y < 40;
    }

    // Set position of the clue agent
    public boolean setClueAgentPos(int agId, int x, int y) {
        if (isInGrid(x, y) && !hasObject(OBSTACLE, x, y) && !hasObject(JAIL, x, y) && !hasObject(AGENT, x, y)) {
            setAgPos(agId, x, y);
            add(CLUE_AGENT, x, y); // Mark the cell with the CLUE_AGENT identifier
            return true;
        }
        return false;
    }

    // Set position of the police agent
    public boolean setPoliceAgentPos(int agId, int x, int y) {
        if (isInGrid(x, y) && !hasObject(OBSTACLE, x, y) && !hasObject(JAIL, x, y) && !hasObject(AGENT, x, y)) {
            setAgPos(agId, x, y);
            add(POLICE_AGENT, x, y);  // Mark the cell with the POLICE_AGENT identifier
            return true;
        }
        return false;
    }

    // Set position of the civilian agent
    public boolean setCivilianAgentPos(int agId, int x, int y) {
        if (isInGrid(x, y) && !hasObject(OBSTACLE, x, y) && !hasObject(JAIL, x, y) && !hasObject(AGENT, x, y)) {
            setAgPos(agId, x, y);
            add(CIVILIAN_AGENT, x, y);  // Mark the cell with the POLICE_AGENT identifier
            return true;
        }
        return false;
    }

    // Set position of the criminal agent
    public boolean setCriminalAgentPos(int agId, int x, int y) {
        if (isInGrid(x, y) && !hasObject(OBSTACLE, x, y) && !hasObject(JAIL, x, y) && !hasObject(AGENT, x, y)) {
            setAgPos(agId, x, y);
            add(CRIMINAL_AGENT, x, y);  // Mark the cell with the POLICE_AGENT identifier
            return true;
        }
        return false;
    }


    //  --------------- City 1 ---------------
    static CityModel city1() throws Exception {
        CityModel city_model = CityModel.create(40, 40, 13);
        // ----- Set jail location -----
        city_model.setJail(35, 35);

        // ----- Set police location -----
        // set three agents near the jail position
        city_model.setPoliceAgentPos(0, 35, 34);
        city_model.setPoliceAgentPos(1, 34, 35);
        city_model.setPoliceAgentPos(2, 34, 34);

        // ----- Set civilians location -----
        city_model.setCivilianAgentPos(3, 1, 1);
        city_model.setCivilianAgentPos(4, 1, 29);
        city_model.setCivilianAgentPos(5, 29, 1);
        city_model.setCivilianAgentPos(6, 29, 29);

        // ----- Set criminals location -----
        city_model.setCriminalAgentPos(7, 10, 10);
        city_model.setCriminalAgentPos(8, 10, 20);

        // ----- Set clues location -----
        // four clues outside walls
        city_model.setClueAgentPos(9, 5, 5);
        city_model.setClueAgentPos(10, 5, 35);
        city_model.setClueAgentPos(11, 35, 5);
        city_model.setClueAgentPos(12, 35, 35);

//        city_model.setAgPos(0, 1, 1);
//        city_model.setAgPos(1, 1, 29);
//        city_model.setAgPos(2, 29, 1);
//        city_model.setAgPos(3, 29, 29);


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




    private CityModel(int width, int height, int agents_number) {
        super(width, height, agents_number);
    }
    
}
