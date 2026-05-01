package core.game;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GameResultTest {

    @Test void win_setsStatusAndMessage() {
        GameResult r = GameResult.win("great job");
        assertEquals("win", r.getStatus());
        assertEquals("great job", r.getMessage());
        assertNull(r.getScore());
    }

    @Test void loss_setsStatusAndMessage() {
        GameResult r = GameResult.loss("too bad");
        assertEquals("loss", r.getStatus());
        assertEquals("too bad", r.getMessage());
        assertNull(r.getScore());
    }

    @Test void quit_hasNullMessageAndScore() {
        GameResult r = GameResult.quit();
        assertEquals("quit", r.getStatus());
        assertNull(r.getMessage());
        assertNull(r.getScore());
    }

    @Test void score_setsScoreAndNullMessage() {
        GameResult r = GameResult.score(42);
        assertEquals("score", r.getStatus());
        assertEquals(42, r.getScore());
        assertNull(r.getMessage());
    }

    @Test void custom_setsArbitraryStatus() {
        GameResult r = GameResult.custom("draw", "nobody wins");
        assertEquals("draw", r.getStatus());
        assertEquals("nobody wins", r.getMessage());
        assertNull(r.getScore());
    }
}