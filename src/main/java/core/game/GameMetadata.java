package core.game;

public class GameMetadata {
    private final String name;
    private final String description;
    private final String author;

    public GameMetadata(String name, String description, String author) {
        this.name = name;
        this.description = description;
        this.author = author;
    }

    public String getName()        { return name; }
    public String getDescription() { return description; }
    public String getAuthor()      { return author; }
}