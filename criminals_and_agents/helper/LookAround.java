package helper;

import city.CityModel;
import java.util.ArrayList;
import java.util.List;

public class LookAround {

    // Method to check the surrounding cells and return a list of agent types found.
    public static List<String> checkSurroundings(CityModel cityModel, int x, int y) {
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
                    }
                    if (cityModel.hasObject(CityModel.CIVILIAN_AGENT, nx, ny)) {
                        foundAgents.add("Civilian Agent");
                    }
                    if (cityModel.hasObject(CityModel.CRIMINAL_AGENT, nx, ny)) {
                        foundAgents.add("Criminal Agent");
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
