package core.game;

public class GameResult {
    private final String status;
    private final String message;
    private final Integer score;

    private GameResult(String status, String message, Integer score) {
        this.status = status;
        this.message = message;
        this.score = score;
    }

    public static GameResult win(String message)                    { return new GameResult("win",    message, null); }
    public static GameResult loss(String message)                   { return new GameResult("loss",   message, null); }
    public static GameResult quit()                                 { return new GameResult("quit",   null,    null); }
    public static GameResult score(int score)                       { return new GameResult("score",  null,    score); }
    public static GameResult custom(String status, String message)  { return new GameResult(status,   message, null); }

    public String  getStatus()  { return status; }
    public String  getMessage() { return message; }
    public Integer getScore()   { return score; }
}