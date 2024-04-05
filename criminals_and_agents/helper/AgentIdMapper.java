package helper;

import java.util.HashMap;
import java.util.Map;

public class AgentIdMapper {
    private final Map<Integer, String> idToType;
    private final Map<Integer, Integer> idToLocalId;

    public AgentIdMapper() {
        idToType = new HashMap<>();
        idToLocalId = new HashMap<>();

        // Initialize mappings for police
        for (int i = 0; i < 3; i++) {
            idToType.put(i, "police");
            idToLocalId.put(i, i);
        }

        // Initialize mappings for civilians
        for (int i = 3; i < 7; i++) {
            idToType.put(i, "civilian");
            idToLocalId.put(i, i - 3);
        }

        // Initialize mappings for criminals
        for (int i = 7; i < 9; i++) {
            idToType.put(i, "criminal");
            idToLocalId.put(i, i - 7);
        }

        for (int i = 9; i < 13; i++) {
            idToType.put(i, "clue");
            idToLocalId.put(i, i - 9);
        }

    }

    public String getType(int globalId) {
        return idToType.get(globalId);
    }

    public int getLocalId(int globalId) {
        Integer localId = idToLocalId.get(globalId);
        if (localId == null) {
            // Handle the case where the mapping doesn't exist, e.g., throw an exception or log a warning
            throw new IllegalStateException("No local ID found for global ID: " + globalId);
        }
        return localId.intValue();
    }

}