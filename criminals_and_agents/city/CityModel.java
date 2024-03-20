package city;

import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;
import jason.environment.*;

import java.util.HashSet;
import java.util.logging.*;

public class CityModel extends GridWorldModel {

    private Logger logger = Logger.getLogger("criminals_and_agents.mas2j." + CityModel.class.getName());

    public static final int JAIL = 0;
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
        this.jail = new Location(i, j);
        // Print location
        logger.info("Jail location: " + jail);

    }

    public Location getJail() {
        return jail;
    }


    // City 1
    static CityModel city1() throws Exception {
        CityModel city_model = CityModel.create(30, 30, 4);
        // ----- Set jail location -----
        city_model.setJail(15, 15);
        // ----- Set civilians location -----
        city_model.setAgPos(0, 1, 1);
        city_model.setAgPos(1, 1, 29);
        city_model.setAgPos(2, 29, 1);
        city_model.setAgPos(3, 29, 29);

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

    // City 2
    static CityModel city2() throws Exception {
        CityModel city_model = CityModel.create(40, 40, 4);
        // Set the jail location
        city_model.setJail(18, 18);
        // Set the civilians' locations at different points, ensuring no conflict with obstacles
        city_model.setAgPos(0, 2, 2);
        city_model.setAgPos(1, 2, 37);
        city_model.setAgPos(2, 37, 2);
        city_model.setAgPos(3, 37, 37);

        // Create two main avenues without blocking the whole row and column
        for (int i = 0; i < 40; i++) {
            if (i != 17 && i != 18 && i != 19 && i != 20 && i != 21 && i != 22) { // Leave space around the jail
                city_model.add(CityModel.OBSTACLE, 19, i);
                city_model.add(CityModel.OBSTACLE, i, 19);
            }
        }

        // Add intersections in the avenues
        for (int i = 5; i < 40; i += 5) {
            for (int j = 5; j < 40; j += 5) {
                city_model.remove(CityModel.OBSTACLE, 19, j); // Horizontal street openings
                city_model.remove(CityModel.OBSTACLE, i, 19); // Vertical street openings
            }
        }

        // Create "parks" as open spaces in each quadrant
        for (int i = 5; i <= 14; i++) {
            for (int j = 5; j <= 14; j++) {
                city_model.remove(CityModel.OBSTACLE, i, j);
                city_model.remove(CityModel.OBSTACLE, i, 40 - 1 - j);
                city_model.remove(CityModel.OBSTACLE, 40 - 1 - i, j);
                city_model.remove(CityModel.OBSTACLE, 40 - 1 - i, 40 - 1 - j);
            }
        }

        // Create some decorative obstacles inside the parks
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
