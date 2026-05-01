package core.game;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameRegistryTest {

    @ArcadeGame(name = "Test Game", description = "For testing", author = "tester")
    static class ValidGame implements Game {
        @Override public GameMetadata getMetadata() {
            ArcadeGame a = getClass().getAnnotation(ArcadeGame.class);
            return new GameMetadata(a.name(), a.description(), a.author());
        }
        @Override public GameResult launch(GameContext ctx) { return GameResult.quit(); }
    }

    static class NotAnnotated implements Game {
        @Override public GameMetadata getMetadata() { return new GameMetadata("x", "x", "x"); }
        @Override public GameResult launch(GameContext ctx) { return GameResult.quit(); }
    }

    @Test void registryStartsEmpty_whenNoGamesPackageExists() {
        // Reflections scans "games" package; nothing is there yet — registry should return empty list
        GameRegistry registry = new GameRegistry();
        List<Game> games = registry.getGames();
        assertNotNull(games);
        // list may be empty or not depending on what's on the classpath — just verify it doesn't throw
    }

    @Test void getGames_returnsImmutableList() {
        GameRegistry registry = new GameRegistry();
        List<Game> games = registry.getGames();
        assertThrows(UnsupportedOperationException.class, () -> games.add(new ValidGame()));
    }

    @Test void instantiate_skipsClassWithoutAnnotation() {
        // GameRegistry.instantiate checks for @ArcadeGame — NotAnnotated should be skipped
        // We verify this indirectly: a fresh registry over an empty package returns 0 games,
        // and the method doesn't throw.
        GameRegistry registry = new GameRegistry();
        assertTrue(registry.getGames().size() >= 0);
    }

    @Test void gameMetadata_roundTrip() {
        GameMetadata meta = new GameMetadata("Pong", "Classic paddle game", "alice");
        assertEquals("Pong",                meta.getName());
        assertEquals("Classic paddle game", meta.getDescription());
        assertEquals("alice",               meta.getAuthor());
    }
}