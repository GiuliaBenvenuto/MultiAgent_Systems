package city;

import jason.asSyntax.*;
import jason.environment.*;
import java.util.logging.*;

public class CityEnvironment extends jason.environment.Environment {
    private Logger logger = Logger.getLogger("criminals_and_agents.mas2j." + CityEnvironment.class.getName());

    CityModel city_model;
    CityView city_view;
}