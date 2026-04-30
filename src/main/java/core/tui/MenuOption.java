package core.tui;

public record MenuOption(String title, String detail) {
    public static MenuOption of(String title, String detail) {
        return new MenuOption(title, detail);
    }

    public static MenuOption of(String title) {
        return new MenuOption(title, "");
    }
}