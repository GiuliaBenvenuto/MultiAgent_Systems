package city;

import jason.asSyntax.*;
import jason.environment.grid.Location;
import java.util.logging.*;

public class CityEnvironment extends jason.environment.Environment {
    private Logger logger = Logger.getLogger("criminals_and_agents.mas2j." + CityEnvironment.class.getName());

    CityModel city_model;
    CityView city_view;

    // Type of city 
    int city_type = 1;

    @Override
    public void init(String[] args) {   
        initCity(Integer.parseInt(args[0]));
    }

    private void initCity(int x) {
        city_type = x;
        try {
            switch (x) {
            case 1:
                city_model = CityModel.city1();
                break;
            case 2:
                //city_model = CityModel.city2();
                break;
            case 3:
                //city_model = CityModel.city3();
                break;
            case 4:
                //city_model = CityModel.city4();
                break;
            default:
                logger.info("Invalid city type");
                return;
            }
            city_view = new CityView(city_model);
            city_view.setEnv(this);
        } catch (Exception e) {
            logger.info("Error initializing city");
        }
    }
}