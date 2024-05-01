package helper;

import java.util.HashMap;
import java.util.Map;

/** ----------- AgentIdMapper Class -----------
 * This is a utility class used in several parts of the project since it allows to map global agent identifiers
 * to their respective types and local identifiers within a multi-agent simulation environment.
 *
 * This class is needed since in the java part the agent ids go from 0 to 12 and in the
 * jason part the agent ids go from 0 to x (where x is the number of agents of a particular type in the simulation).
 * So while in java I have agents from 0 to 12 in jason I have from 0 to 2 for police agents, from 0 to 1 for criminals,
 * from 0 to 3 for clue agents and from 0 to 3 for civilians.
 */

public class AgentIdMapper {
    private final Map<Integer, String> idToType;
    private final Map<Integer, Integer> idToLocalId;

    public AgentIdMapper() {
        idToType = new HashMap<>();
        idToLocalId = new HashMap<>();

        // Mapping for police agents
        for (int i = 0; i < 3; i++) {
            idToType.put(i, "police");
            idToLocalId.put(i, i);
        }
        // Mapping for criminal agents
        for (int i = 3; i < 5; i++) {
            idToType.put(i, "criminal");
            idToLocalId.put(i, i - 3);
        }
        // Mapping for clue agents
        for (int i = 5; i < 9; i++) {
            idToType.put(i, "clue");
            idToLocalId.put(i, i - 5);
        }
        // Mapping for civilian agents
        for (int i = 9; i < 13; i++) {
            idToType.put(i, "civilian");
            idToLocalId.put(i, i - 9);
        }
    }


    // Method to get the type of an agent
    public String getType(int globalId) {
        return idToType.get(globalId);
    }


    // Method to get the local id of an agent
    public int getLocalId(int globalId) {
        Integer localId = idToLocalId.get(globalId);
        if (localId == null) {
            // Handle the case where the mapping doesn't exist, e.g., throw an exception or log a warning
            throw new IllegalStateException("No local ID found for global ID: " + globalId);
        }
        return localId.intValue();
    }

}