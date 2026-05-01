package core.game;

public interface Game {
    GameMetadata getMetadata();
    GameResult launch(GameContext context);
}