package city;

import jason.asSyntax.*;
import java.util.logging.*;
import jason.environment.grid.Location;


public class CityEnvironment extends jason.environment.Environment {
    private static CityEnvironment instance;
    private Logger logger = Logger.getLogger("criminals_and_agents.mas2j." + CityEnvironment.class.getName());

    CityModel city_model;
    CityView city_view;

    // Type of city 
    int cityType;
    boolean gui = true;

    public CityEnvironment() {
        instance = this;
    }

    public static CityEnvironment getInstance() {
        return instance;
    }

    @Override
    public void init(String[] args) {
        initCity(Integer.parseInt(args[0]));
    }

    public int getCityType() {
        return cityType;
    }

    public CityModel getCityModel() {
        return this.city_model;
    }


    public void initCity(int x) {
        cityType = x;
        logger.info("Initializing city type  INITCITY" + x);
        try {
            // Clear obstacles if city_model is already initialized
            if (city_model != null) {
                city_model.clearObstacles();
                city_model.clearAgents();
                city_model.clearJail();
            }

            switch (x) {
            case 1:
                city_model = CityModel.city1();
                //System.out.println("City 1");
                break;
            case 2:
                city_model = CityModel.city2();
                //System.out.println("City 2");
                break;
            case 3:
                city_model = CityModel.city3();
                //System.out.println("City 3");
                break;
            case 4:
                city_model = CityModel.city4();
                //System.out.println("City 4");
                break;
            default:
                logger.info("Invalid city type");
                return;
            }

            if(city_view == null) {
                city_view = new CityView(city_model);
                city_view.setEnv(this);
            } else {
                // Before to update the view clear the obstacles
                // city_view.clearObstacles();
                city_view.updateView(city_model);
                city_view.repaint();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error initializing city: " + e.getMessage(), e);
        }
    }


    public void updateAgentPercepts(String agentType, int agId, int x, int y) {
        Literal positionPercept = ASSyntax.createLiteral("at",
                ASSyntax.createNumber(x),
                ASSyntax.createNumber(y));

        //System.out.println("agent type: " + agentType + String.valueOf(agId + 1));
        //addPercept("police" + String.valueOf(agId + 1), positionPercept);
        addPercept(agentType + String.valueOf(agId + 1), positionPercept);
    }
}