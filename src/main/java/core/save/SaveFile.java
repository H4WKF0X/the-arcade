package core.save;

import java.util.LinkedHashMap;
import java.util.Map;

public class SaveFile {
    private Map<String, Map<String, Object>> players = new LinkedHashMap<>();
    private Map<String, Object> global = new LinkedHashMap<>();

    public Map<String, Map<String, Object>> getPlayers() { return players; }
    public Map<String, Object> getGlobal() { return global; }
}