package core.tui;

public final class Color {
    public static final String RESET   = "[0m";
    public static final String RED     = "[31m";
    public static final String GREEN   = "[32m";
    public static final String YELLOW  = "[33m";
    public static final String BLUE    = "[34m";
    public static final String MAGENTA = "[35m";
    public static final String CYAN    = "[36m";
    public static final String WHITE   = "[37m";
    public static final String BOLD    = "[1m";
    public static final String DIM     = "[2m";

    private Color() {}

    public static String red(String text)     { return RED     + text + RESET; }
    public static String green(String text)   { return GREEN   + text + RESET; }
    public static String yellow(String text)  { return YELLOW  + text + RESET; }
    public static String blue(String text)    { return BLUE    + text + RESET; }
    public static String magenta(String text) { return MAGENTA + text + RESET; }
    public static String cyan(String text)    { return CYAN    + text + RESET; }
    public static String white(String text)   { return WHITE   + text + RESET; }
    public static String bold(String text)    { return BOLD    + text + RESET; }
    public static String dim(String text)     { return DIM     + text + RESET; }
}