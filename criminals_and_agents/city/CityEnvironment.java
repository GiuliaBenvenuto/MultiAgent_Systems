package city;

import helper.LookAround;
import helper.AgentIdMapper;

import java.util.logging.*;
import java.util.List;
import java.util.Random;
import javax.swing.SwingUtilities;

import jason.asSyntax.*;
import jason.environment.grid.Location;

/** ---------- CITY ENVIRONMENT CLASS ----------
 * This class manages the environment (the city) where the agents of the multi-agent system navigate and interact.
 * In particular this class is responsible for the following:
 * - Initialize the city environment
 * - Initialize the city model
 * - Initialize the city view
 *
 * Furthermore this class is responsible for the processing of the path of the police agents
 * which are the only agents moving in the city, exploring it.
 * This movement of the police agents is managed by using threads such that is possible to
 * visualize the steps of the agents, cell after cell, in the GUI by updating the position of the police icon.
 */

public class CityEnvironment extends jason.environment.Environment {
    private static CityEnvironment instance;
    private Logger logger = Logger.getLogger("criminals_and_agents.mas2j." + CityEnvironment.class.getName());

    CityModel city_model;
    CityView city_view;
    boolean continueExploration = true;
    int cityType;
    boolean gui = true;


    // Override the init method to initialize the city
    @Override
    public void init(String[] args) {
        initCity(Integer.parseInt(args[0]));
    }

    // Method to initialize an instance of the city environment
    public CityEnvironment() {
        instance = this;
    }

    // Method to gen an instance of the city environment
    public static CityEnvironment getInstance() {
        return instance;
    }

    // Method to get the city type
    // Actually right now the city type is always 1 but the original idea was to have different types of cities
    // so this method could be useful in the future
    public int getCityType() {
        return cityType;
    }

    // Method to get the city model
    public CityModel getCityModel() {
        return this.city_model;
    }

    // Method to get the city view
    public CityView getView() {
        return city_view;
    }

    // Method to set the continue exploration flag
    public void setContinueExploration(boolean continueEpxloration) {
        this.continueExploration = continueExploration;
    }


    // Method to initialize the city
    public void initCity(int x) {
        cityType = x;

        try {
            // Clear obstacles if city_model is already initialized
            if (city_model != null) {
                city_model.clearObstacles();
                city_model.clearAgents();
                city_model.clearJail();
            }

            // Initialize CityModel as city1
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


    /**
     * Method to process the path of the police agent called by FindPath.java (internal action).
     * Responsible for the visualization of the police agent movement in the city by updating the position of the icon.
     * It also implement the recursive exploration of the city by computing randomly the X and the Y of the new end position.
     *
     * @param agId: the ID of the police agent
     * @param path: the path to be processed
     */
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
    } // processPath

} // CityEnvironment
