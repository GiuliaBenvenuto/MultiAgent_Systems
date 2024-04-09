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


//    public void updateAgentPercepts(int globalId, int x, int y) {
//        AgentIdMapper mapper = new AgentIdMapper();
//        String agentType = mapper.getType(globalId);
//        int localId = mapper.getLocalId(globalId);
//
//        // Position
//        Literal positionPercept = ASSyntax.createLiteral("at",
//                ASSyntax.createNumber(x),
//                ASSyntax.createNumber(y));
//
//        //System.out.println("(agent type: " + agentType + String.valueOf(localId) + ", position: " + positionPercept + ")");
//        addPercept(agentType + String.valueOf(localId + 1), positionPercept);
//
//        // Id
//        Literal idPercept = ASSyntax.createLiteral("myId",
//                ASSyntax.createNumber(localId));
//        addPercept(agentType + String.valueOf(localId + 1), idPercept);
//
//
//
//        initCity(1);
//        if(agentType.equals("police")) {
//            // Add final end position for police agents
//            int endX, endY;
//            Random random = new Random();
//            do {
//                endX = random.nextInt(39) + 1; // Generates a number between 1 and 39
//                endY = random.nextInt(39) + 1;
//            } while (!city_model.isFree(endX, endY));
//            System.out.println("POLICE " + localId + " MOVING TO: " + endX + ", " + endY);
//
//            Literal policeStart = ASSyntax.createLiteral("startPos",
//                    ASSyntax.createNumber(x),
//                    ASSyntax.createNumber(y));
//
//            Literal policeEnd = ASSyntax.createLiteral("endPos",
//                    ASSyntax.createNumber(endX),
//                    ASSyntax.createNumber(endY));
//
//            addPercept(agentType + String.valueOf(localId + 1), policeStart);
//            addPercept(agentType + String.valueOf(localId + 1), policeEnd);
//
//            // Add jail position for police agents
//            Location jail = city_model.getJail();
//            Literal jailPercept = ASSyntax.createLiteral("jailPos",
//                    ASSyntax.createNumber(jail.x),
//                    ASSyntax.createNumber(jail.y));
//            addPercept(agentType + String.valueOf(localId + 1), jailPercept);
//
//        }
//    } //updateAgentPercepts


    // Process the path to move the police agent icons
//    public void processPath(int agId, List<Location> path) {
//        new Thread(() -> {
//            for (Location step : path) {
//                try {
//                    // Wait before moving to the next step to visualize the movement
//                    Thread.sleep(500); // 500 milliseconds delay for visualization
//                    SwingUtilities.invokeLater(() -> city_model.updatePoliceAgentPosition(agId, step.x, step.y));
//                    city_view.updateView(city_model); // Update the view with the new agent position
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//    }
    public void processPath(int agId, List<Location> path) {
        new Thread(() -> {
            for (Location step : path) {
                try {
                    Thread.sleep(500); // Delay for visualization
                    SwingUtilities.invokeLater(() -> {
                        city_model.updatePoliceAgentPosition(agId, step.x, step.y);
                        city_view.updateView(city_model);

                        // Look around at the new position
                        List<String> agentsAround = LookAround.checkSurroundings(city_model, step.x, step.y, agId);
//                        if (!agentsAround.isEmpty()) {
//                            System.out.println("Police Agent " + agId + " at (" + step.x + ", " + step.y + ") found: " + String.join(", ", agentsAround));
//                        }
                    });
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            // ------- RECURSION OF EXPLORATION -------
            SwingUtilities.invokeLater(() -> {
                // add percept to inform that the police agent has arrived at the destination
                instance.addPercept("police" + (agId + 1) , ASSyntax.createLiteral("arrivedAtDestination"));
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

        }).start();
    }


} //CityEnvironment
