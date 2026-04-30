package core.tui;

import java.io.IOException;
import java.util.List;

/** Temporary manual smoke-test for the TUI components. Delete before v0.1.0. */
public class MenuDemo {
    public static void main(String[] args) throws IOException, InterruptedException {
        Terminal terminal = new Terminal();
        terminal.enterRawMode();
        try {
            // Spinner demo
            terminal.clearScreen();
            terminal.moveCursor(1, 1);
            terminal.println(Color.bold("Loading the arcade..."));
            Spinner.pause(terminal, "Scanning games...", 2000);
            terminal.println(Color.green("Done!"));
            Thread.sleep(600);

            // Menu demo
            var options = List.of(
                MenuOption.of("Snake",    "Classic snake — eat food, don't eat yourself"),
                MenuOption.of("Pong",     "Two players, one keyboard, zero mercy"),
                MenuOption.of("Trivia",   "How smart are you, really?"),
                MenuOption.of("Quit",     "Coward's exit")
            );
            Menu menu = new Menu(options);
            int selected = menu.show(terminal);

            terminal.clearScreen();
            terminal.moveCursor(1, 1);
            if (selected == -1) {
                terminal.println(Color.yellow("Escaped from the menu."));
            } else {
                terminal.println(Color.green("Selected: ") + Color.bold(options.get(selected).title()));
            }
            Thread.sleep(1500);
        } finally {
            terminal.close();
        }
    }
}