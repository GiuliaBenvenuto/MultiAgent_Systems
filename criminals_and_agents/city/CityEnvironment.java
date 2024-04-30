package city;

import helper.LookAround;
import helper.AgentIdMapper;

import java.util.logging.*;
import java.util.List;
import java.util.Random;

import javax.swing.SwingUtilities;

import jason.asSyntax.*;
import jason.environment.grid.Location;



public class CityEnvironment extends jason.environment.Environment {
    private static CityEnvironment instance;
    private Logger logger = Logger.getLogger("criminals_and_agents.mas2j." + CityEnvironment.class.getName());

    CityModel city_model;
    CityView city_view;

    boolean continueExploration = true;

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

    public void setContinueExploration(boolean continueEpxloration) {
        this.continueExploration = continueExploration;
    }

    // get view
    public CityView getView() {
        return city_view;
    }


    /*
    public void initCity(int x) {
        cityType = x;
        //logger.info("Initializing city type  INITCITY" + x);
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
                break;
            case 2:
                city_model = CityModel.city2();
                break;
            case 3:
                city_model = CityModel.city3();
                break;
            case 4:
                city_model = CityModel.city4();
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
    }*/
    public void initCity(int x) {
        cityType = x;

        try {
            // Clear obstacles if city_model is already initialized
            if (city_model != null) {
                city_model.clearObstacles();
                city_model.clearAgents();
                city_model.clearJail();
            }

            // Always initialize CityModel as city1
            city_model = CityModel.city1();

            // Initialize or update the city view
            if (city_view == null) {
                city_view = new CityView(city_model);
                city_view.setEnv(this);
            } else {
                city_view.updateView(city_model);
                city_view.repaint();
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Error initializing city: " + e.getMessage(), e);
        }
    }



    public void processPath(int agId, List<Location> path) {
        new Thread(() -> {
            for (Location step : path) {
                try {
                    Thread.sleep(400); // Delay for visualization
                    SwingUtilities.invokeLater(() -> {
                        city_model.updatePoliceAgentPosition(agId, step.x, step.y);
                        city_view.updateView(city_model);

                        instance.removePercept("police" + (agId + 1), Literal.parseLiteral("currentPos(_,_)"));
                        // Add the new currentPos belief with the updated location
                        instance.addPercept("police" + (agId + 1), Literal.parseLiteral("currentPos(" + step.x + "," + step.y + ")"));

                        // Look around at the new position
                        List<String> agentsAround = LookAround.checkSurroundings(city_model, step.x, step.y, agId);
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }


            if (continueExploration) {
                // ------- RECURSION OF EXPLORATION -------
                SwingUtilities.invokeLater(() -> {
                    // add percept to inform that the police agent has arrived at the destination
                    instance.addPercept("police" + (agId + 1), ASSyntax.createLiteral("arrivedAtDestination"));
                    // take last location in path as the current location
                    Location currentLocation = path.get(path.size() - 1);
                    // add percept to inform the current location in the new startPos removing the one already present in the agent
                    Literal newStartPos = ASSyntax.createLiteral("startPos",
                            ASSyntax.createNumber(currentLocation.x),
                            ASSyntax.createNumber(currentLocation.y));

                    // new end position for police agents
                    Random random = new Random();
                    int endX, endY;
                    do {
                        endX = random.nextInt(39) + 1; // Generates a number between 1 and 39
                        endY = random.nextInt(39) + 1;
                    } while (!instance.getCityModel().isFree(endX, endY));

                    Literal newEndPos = ASSyntax.createLiteral("endPos",
                            ASSyntax.createNumber(endX),
                            ASSyntax.createNumber(endY));

                    instance.addPercept("police" + (agId + 1), newStartPos);
                    instance.addPercept("police" + (agId + 1), newEndPos);

                });
            }

        }).start();
    }



} //CityEnvironment
