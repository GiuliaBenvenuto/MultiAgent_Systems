package helper;

import city.CityModel;
import city.CityEnvironment;
import city.CityView;

import java.util.ArrayList;
import java.util.List;

/** ----------- LookAround Class ----------
 * This class provides utility methods to detect and respond to the presence of other agents
 * or objects in the immediate vicinity of an agent (police agent that is exploring the grid).
 * This class is essential for enabling agents, particularly police agents, to interact with their environment
 * by identifying nearby agents and act accordingly.
 */

public class LookAround {

    // Method to check the surrounding cells and return a list of agent types found.
    public static List<String> checkSurroundings(CityModel cityModel, int x, int y, int policeId) {
        List<String> foundAgents = new ArrayList<>();
        int[] dx = {-1, 0, 1}; // Relative X positions
        int[] dy = {-1, 0, 1}; // Relative Y positions

        for (int i = 0; i < dx.length; i++) {
            for (int j = 0; j < dy.length; j++) {
                int nx = x + dx[i];
                int ny = y + dy[j];
                // Skip the current cell
                if (dx[i] == 0 && dy[j] == 0) {
                    continue;
                }
                if (cityModel.isInGrid(nx, ny)) {
                    if (cityModel.hasObject(CityModel.CLUE_AGENT, nx, ny)) {
                        foundAgents.add("Clue Agent");
                        int agId = cityModel.getAgentId(CityModel.CLUE_AGENT, nx, ny);
                        AgentPercept.foundAgent(CityEnvironment.getInstance(), policeId, agId, x, y);
                    }
                    if (cityModel.hasObject(CityModel.CIVILIAN_AGENT, nx, ny)) {
                        foundAgents.add("Civilian Agent");
                        int agId = cityModel.getAgentId(CityModel.CIVILIAN_AGENT, nx, ny);
                        AgentPercept.foundAgent(CityEnvironment.getInstance(), policeId, agId, x, y);
                    }
                    if (cityModel.hasObject(CityModel.CRIMINAL_AGENT, nx, ny)) {
                        foundAgents.add("Criminal Agent");
                        int agId = cityModel.getAgentId(CityModel.CRIMINAL_AGENT, nx, ny);
                        AgentPercept.foundAgent(CityEnvironment.getInstance(), policeId, agId, x, y);
                    }
                    if (cityModel.hasObject(CityModel.POLICE_AGENT, nx, ny)) {
                        foundAgents.add("Police Agent");
                    }
                    if(cityModel.hasObject(CityModel.JAIL, nx, ny)){
                        foundAgents.add("FOUND THE JAIL");
                        // If the police agent is escorting a criminal agent, jail them.
                        // The icon of the police agent will return the policeImage icon since the criminal agent is jailed.
                        boolean escorting = cityModel.isEscorting(policeId);
                        if (escorting) {
                            AgentPercept.jailWithCriminal(CityEnvironment.getInstance(), policeId, x, y);
                        }
                    }
                }
            }
        }
        return foundAgents;
    }
} // LookAround
