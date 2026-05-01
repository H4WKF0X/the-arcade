package core.save;

public interface SaveManager {
    PlayerProfile getProfile(String playerName);
    void save();
}