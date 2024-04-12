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


public class AgentPercept {

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


    public static void addPolicePercept(CityEnvironment environment, int globalId, int x, int y) {
        AgentIdMapper mapper = new AgentIdMapper();
        String agentType = mapper.getType(globalId);
        int localId = mapper.getLocalId(globalId);

        // ------ Police agents' percepts ------
        environment.initCity(1);
        if (agentType.equals("police")) {
            // Add final end position for police agents
            Random random = new Random();
            int endX, endY;
            do {
                endX = random.nextInt(39) + 1; // Generates a number between 1 and 39
                endY = random.nextInt(39) + 1;
            } while (!environment.getCityModel().isFree(endX, endY));

            System.out.println("POLICE " + localId + " MOVING TO: " + endX + ", " + endY);
            System.out.println("");

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
            System.out.println("Closest clue agent to civilian " + localCivId + " is at " + closestClue.x + ", " + closestClue.y);

            Literal cluePositionPercept = ASSyntax.createLiteral("closeClueAgent",
                    ASSyntax.createNumber(closestClue.x + 1), // add +1 such that I'm moving in a neighboring cell
                    ASSyntax.createNumber(closestClue.y),
                    ASSyntax.createNumber(localClueId + 1),
                    ASSyntax.createAtom(agentType_clue));

            environment.addPercept(agentType_civ + (localCivId + 1), cluePositionPercept);
        }

    }


    public static void foundAgent(CityEnvironment environment, int globalPoliceId, int globalAgentId, int x, int y) {
        AgentIdMapper mapper = new AgentIdMapper();
        String agentType = mapper.getType(globalPoliceId);
        int localPoliceId = mapper.getLocalId(globalPoliceId);

        String foundAgentType = mapper.getType(globalAgentId);
        int localAgentId = mapper.getLocalId(globalAgentId);

        // System.out.println("FOUND AGENT TYPE: " + foundAgentType);

        // Position
        Literal positionPercept = ASSyntax.createLiteral("closeAgentAt",
                ASSyntax.createNumber(x),
                ASSyntax.createNumber(y),
                ASSyntax.createNumber(localAgentId + 1),
                ASSyntax.createAtom(foundAgentType));

        environment.addPercept(agentType + (localPoliceId + 1), positionPercept);
    }

    public static void jailWithCriminal(CityEnvironment environment, int globalPoliceId, int x, int y) {
        System.out.println("JAIL WITH CRIMINAL");
        AgentIdMapper mapper = new AgentIdMapper();
        String agentType = mapper.getType(globalPoliceId);
        int localPoliceId = mapper.getLocalId(globalPoliceId);

        Location jail = environment.getCityModel().getJail();
        Literal jailPercept = ASSyntax.createLiteral("reachedJail",
                ASSyntax.createNumber(jail.x - 1),
                ASSyntax.createNumber(jail.y));
        environment.addPercept(agentType + (localPoliceId + 1), jailPercept);
    }


}