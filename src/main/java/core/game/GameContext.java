package core.game;

import core.save.SaveManager;
import core.tui.Terminal;

public class GameContext {
    private final SaveManager saveManager;
    private final String playerName;
    private final Terminal terminal;

    public GameContext(SaveManager saveManager, String playerName, Terminal terminal) {
        this.saveManager = saveManager;
        this.playerName = playerName;
        this.terminal = terminal;
    }

    public SaveManager getSaveManager() { return saveManager; }
    public String getPlayerName()       { return playerName; }
    public Terminal getTerminal()       { return terminal; }
}