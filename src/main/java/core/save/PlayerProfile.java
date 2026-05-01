package core.save;

public class PlayerProfile {
    private final String name;

    public PlayerProfile(String name) {
        this.name = name;
    }

    public String getName() { return name; }

    public int     getInt(String key, int defaultValue)         { throw new UnsupportedOperationException("Session 4"); }
    public String  getString(String key, String defaultValue)   { throw new UnsupportedOperationException("Session 4"); }
    public boolean getBoolean(String key, boolean defaultValue) { throw new UnsupportedOperationException("Session 4"); }
    public void    set(String key, Object value)                { throw new UnsupportedOperationException("Session 4"); }
}