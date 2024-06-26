package helper;

import city.CityModel;
import city.CityView;
import city.CityEnvironment;

import jason.asSyntax.ASSyntax;
import jason.asSyntax.Literal;
import jason.environment.grid.Location;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;

import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;


/**  ---------- AgentPercept Class ----------
 * This is a utility class used in the project to update the percepts of the agents in the environment.
 * Through the addPercept used in all the methods of this class, the agents can perceive new information
 * about the environment and other agents.
 */

public class AgentPercept {

    // Method to add the percepts about the position and id of the agents
    public static void updateAgentPercepts(CityEnvironment environment, int globalId, int x, int y) {
        AgentIdMapper mapper = new AgentIdMapper();
        String agentType = mapper.getType(globalId);
        int localId = mapper.getLocalId(globalId);

        // Position
        Literal positionPercept = ASSyntax.createLiteral("at",
                ASSyntax.createNumber(x),
                ASSyntax.createNumber(y));
        environment.addPercept(agentType + (localId + 1), positionPercept);

        // Id
        Literal idPercept = ASSyntax.createLiteral("myId",
                ASSyntax.createNumber(localId));
        environment.addPercept(agentType + (localId + 1), idPercept);
    }


    // Method to add the percepts about the police agents, start position - end position and jail position
    public static void addPolicePercept(CityEnvironment environment, int globalId, int x, int y) {
        AgentIdMapper mapper = new AgentIdMapper();
        String agentType = mapper.getType(globalId);
        int localId = mapper.getLocalId(globalId);

        environment.initCity(1);
        if (agentType.equals("police")) {
            // Add final end position for police agents
            Random random = new Random();
            int endX, endY;
            do {
                endX = random.nextInt(39) + 1; // Generates a number between 1 and 39
                endY = random.nextInt(39) + 1;
            }
            while (!environment.getCityModel().isFree(endX, endY));

            Literal policeStart = ASSyntax.createLiteral("startPos",
                    ASSyntax.createNumber(x),
                    ASSyntax.createNumber(y));

            Literal policeEnd = ASSyntax.createLiteral("endPos",
                    ASSyntax.createNumber(endX),
                    ASSyntax.createNumber(endY));

            environment.addPercept(agentType + (localId + 1), policeStart);
            environment.addPercept(agentType + (localId + 1), policeEnd);

            // Add jail position for police agents
            Location jail = environment.getCityModel().getJail();
            Literal jailPercept = ASSyntax.createLiteral("jailPos",
                    ASSyntax.createNumber(jail.x - 1),
                    ASSyntax.createNumber(jail.y));
            environment.addPercept(agentType + (localId + 1), jailPercept);
        }
    }


    // Method to add the percepts to civilian agents about the position of the clue agents
    public static void addCivilianPercept(CityEnvironment environment, int civilianId, int x, int y, int clueId, int i, int j, CityModel cityModel) {
        // x,y civilian agent position
        // i,j clue agent position
        AgentIdMapper mapper = new AgentIdMapper();
        String agentType_civ = mapper.getType(civilianId);
        int localCivId = mapper.getLocalId(civilianId);

        String agentType_clue = mapper.getType(clueId);
        int localClueId = mapper.getLocalId(clueId);

        Location closestClue = cityModel.findClosestClueAgent(x, y);
        if (closestClue != null) {
            // System.out.println("Closest clue agent to civilian: " + localCivId + " is at: " + closestClue.x + ", " + closestClue.y);

            Literal cluePositionPercept = ASSyntax.createLiteral("closeClueAgent",
                    ASSyntax.createNumber(closestClue.x + 1), // add +1 such that I'm moving in a neighboring cell
                    ASSyntax.createNumber(closestClue.y),
                    ASSyntax.createNumber(localClueId + 1),
                    ASSyntax.createAtom(agentType_clue));

            environment.addPercept(agentType_civ + (localCivId + 1), cluePositionPercept);
        }
    }


    // Method to add the percepts to clue agents about the position of the X coordinate of criminal agents
    public static void addCluePerceptX(CityEnvironment environment, int clueId, int clueX, int clueY, int criminalID, int criminalX) {
        AgentIdMapper mapper = new AgentIdMapper();
        String agentType = mapper.getType(clueId);
        int localId = mapper.getLocalId(clueId);

        // X coord of criminal agent
        Literal criminalXpercept = ASSyntax.createLiteral("criminalXcoord",
                ASSyntax.createNumber(criminalID),
                ASSyntax.createNumber(criminalX + 1));
        environment.addPercept(agentType + (localId + 1), criminalXpercept);
    }

    // Method to add the percepts to clue agents about the position of the Y coordinate of criminal agents
    public static void addCluePerceptY(CityEnvironment environment, int clueId, int clueX, int clueY, int criminalID, int criminalY) {
        AgentIdMapper mapper = new AgentIdMapper();
        String agentType = mapper.getType(clueId);
        int localId = mapper.getLocalId(clueId);

        // Y coord of criminal agent
        Literal criminalYpercept = ASSyntax.createLiteral("criminalYcoord",
                ASSyntax.createNumber(criminalID),
                ASSyntax.createNumber(criminalY));
        environment.addPercept(agentType + (localId + 1), criminalYpercept);
    }


    // Method to add the percepts to police agents about the position of the neighboring agents foud while exploring
    public static void foundAgent(CityEnvironment environment, int globalPoliceId, int globalAgentId, int x, int y) {
        AgentIdMapper mapper = new AgentIdMapper();
        String agentType = mapper.getType(globalPoliceId);
        int localPoliceId = mapper.getLocalId(globalPoliceId);

        String foundAgentType = mapper.getType(globalAgentId);
        int localAgentId = mapper.getLocalId(globalAgentId);

        // Position of the found agent
        Literal positionPercept = ASSyntax.createLiteral("closeAgentAt",
                ASSyntax.createNumber(x),
                ASSyntax.createNumber(y),
                ASSyntax.createNumber(localAgentId + 1),
                ASSyntax.createAtom(foundAgentType));

        environment.addPercept(agentType + (localPoliceId + 1), positionPercept);
    }


    // Method to add the percepts to police agents about the fact that they have reached the jail with a criminal
    public static void jailWithCriminal(CityEnvironment environment, int globalPoliceId, int x, int y) {
        AgentIdMapper mapper = new AgentIdMapper();
        String agentType = mapper.getType(globalPoliceId);
        int localPoliceId = mapper.getLocalId(globalPoliceId);

        Location jail = environment.getCityModel().getJail();
        Literal jailPercept = ASSyntax.createLiteral("reachedJail",
                ASSyntax.createNumber(jail.x - 1),
                ASSyntax.createNumber(jail.y));
        environment.addPercept(agentType + (localPoliceId + 1), jailPercept);
    }


    // Method to destroy the police agent when at jail with a criminal
    public static void destroyAgent(CityEnvironment environment, String agentName) {
        Literal destroyPercept = ASSyntax.createLiteral("destroyMe",
                ASSyntax.createAtom(agentName));
        environment.addPercept(agentName, destroyPercept);

        CityEnvironment env = CityEnvironment.getInstance();
        CityView view = env.getView();
        CityModel model = CityModel.getCityModel();
        view.updateView(model);

        // Destory all agents if all criminals (2) have been arrested
        int arrestedCriminals = model.getArrestedCriminals();
        if (arrestedCriminals == 2) {
            AgentPercept.destroyAllAgents(env);

            // Display the message about the safety of the city if all the criminals have been arrested
            // Dialog box to display the message
            SwingUtilities.invokeLater(() -> {
                JOptionPane.showMessageDialog(null,
                        "The city is safe: all criminals have been arrested",
                        "City Safety Notification",
                        JOptionPane.INFORMATION_MESSAGE);
            });
        }
    }


    // Method to destroy all agents
    public static void destroyAllAgents(CityEnvironment environment) {
        List<String> agents = Arrays.asList( "police1", "police2", "police3",
                                "criminal1", "criminal2",
                                "civilian1", "civilian2", "civilian3", "civilian4",
                                "clue1", "clue2", "clue3", "clue4");
        // agent names
        for (int i = 0; i < agents.size(); i++) {
            String agentName = agents.get(i);
            Literal destroyPercept = ASSyntax.createLiteral("destroyAllAgents",
                    ASSyntax.createAtom(agentName));
            environment.addPercept(agentName, destroyPercept);
        }
    }


}