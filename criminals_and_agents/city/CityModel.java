package city;

import jason.environment.grid.GridWorldModel;
import jason.environment.grid.Location;
import jason.environment.*;

import java.util.HashSet;
import java.util.logging.*;

public class CityModel extends GridWorldModel {

    private Logger logger = Logger.getLogger("criminals_and_agents.mas2j." + CityModel.class.getName());

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


    static CityModel city1() throws Exception {
        CityModel city_model = CityModel.create(30, 30, 4);
        city_model.setJail(15, 15);
        //city_model.setCivilianPosition(0, 1, 1);

        return city_model;
    }


    


    private CityModel(int width, int height, int agents_number) {
        super(width, height, agents_number);
    }
    
}
