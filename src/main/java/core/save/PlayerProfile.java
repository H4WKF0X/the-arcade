package core.save;

import java.util.Map;

public class PlayerProfile {
    private final Map<String, Object> data;

    PlayerProfile(Map<String, Object> data) {
        this.data = data;
    }

    public String getName() {
        return (String) data.get("name");
    }

    public int getInt(String key, int defaultValue) {
        Object val = data.get(key);
        if (val == null) return defaultValue;
        if (val instanceof Number) return ((Number) val).intValue();
        try { return Integer.parseInt(val.toString()); }
        catch (NumberFormatException e) { return defaultValue; }
    }

    public String getString(String key, String defaultValue) {
        Object val = data.get(key);
        return val != null ? val.toString() : defaultValue;
    }

    public boolean getBoolean(String key, boolean defaultValue) {
        Object val = data.get(key);
        if (val == null) return defaultValue;
        if (val instanceof Boolean) return (Boolean) val;
        return Boolean.parseBoolean(val.toString());
    }

    public void set(String key, Object value) {
        data.put(key, value);
    }
}