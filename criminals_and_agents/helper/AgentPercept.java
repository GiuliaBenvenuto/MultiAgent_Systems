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
                    ASSyntax.createNumber(jail.x),
                    ASSyntax.createNumber(jail.y));
            environment.addPercept(agentType + (localId + 1), jailPercept);
        }
    }
}