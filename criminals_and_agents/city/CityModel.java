package city;

import jason.environment.grid.GridWorldModel;
import jason.environment.*;

import java.util.HashSet;
import java.util.logging.*;

public class CityModel extends GridWorldModel {
    private Logger logger = Logger.getLogger("criminals_and_agents.mas2j." + CityModel.class.getName());

    protected static CityModel city_model = null;

    synchronized public static CityModel create(int width, int height, int agents_number) {
        if (city_model == null) {
            city_model = new CityModel(width, height, agents_number);
        }
        return city_model;
    }

    static CityModel city1() throws Exception {
        CityModel city_model = CityModel.create(20, 20, 4);
        return city_model;
    }

    private CityModel(int width, int height, int agents_number) {
        super(width, height, agents_number);
    }

/*     public static CityModel city1() {
        return new CityModel(10, 10, 4);
    }

    public static CityModel city2() {
        return new CityModel(10, 10, 4);
    }

    public static CityModel city3() {
        return new CityModel(10, 10, 4);
    }

    public static CityModel city4() {
        return new CityModel(10, 10, 4);
    }

    public CityModel(int w, int h, int nbAgs) {
        super(w, h, nbAgs);
    } */
}
