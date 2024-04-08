package helper;

import city.CityModel;
import city.CityEnvironment;
import city.CityView;

import java.util.ArrayList;
import java.util.List;

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
                        // find the agent id
                        int agId = cityModel.getAgentId(CityModel.CLUE_AGENT, nx, ny);
                        System.out.println("Found clue agent " + agId + " at " + nx + ", " + ny);

                        System.out.println("POLICE ID: " + policeId + "FOUND AGENT ID: " + agId + " X: " + x + " Y: " + y);
                        AgentPercept.foundAgent(CityEnvironment.getInstance(), policeId, agId, x, y);
                    }
                    if (cityModel.hasObject(CityModel.CIVILIAN_AGENT, nx, ny)) {
                        foundAgents.add("Civilian Agent");
                        int agId = cityModel.getAgentId(CityModel.CIVILIAN_AGENT, nx, ny);
                        System.out.println("Found civilian agent " + agId + " at " + nx + ", " + ny);

                        System.out.println("POLICE ID: " + policeId + "FOUND AGENT ID: " + agId + " X: " + x + " Y: " + y);
                        AgentPercept.foundAgent(CityEnvironment.getInstance(), policeId, agId, x, y);
                    }
                    if (cityModel.hasObject(CityModel.CRIMINAL_AGENT, nx, ny)) {
                        foundAgents.add("Criminal Agent");
                        int agId = cityModel.getAgentId(CityModel.CRIMINAL_AGENT, nx, ny);
                        System.out.println("Found criminal agent " + agId + " at " + nx + ", " + ny);

                        System.out.println("POLICE ID: " + policeId + "FOUND AGENT ID: " + agId + " X: " + x + " Y: " + y);
                        AgentPercept.foundAgent(CityEnvironment.getInstance(), policeId, agId, x, y);
                    }
                    if (cityModel.hasObject(CityModel.POLICE_AGENT, nx, ny)) {
                        foundAgents.add("Police Agent");
                    }
                }
            }
        }
        return foundAgents;
    }
}
