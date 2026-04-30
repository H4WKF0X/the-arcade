package core.tui;

import java.io.IOException;
import java.util.List;

public class Menu {
    private final List<MenuOption> options;
    private int selectedIndex;

    public Menu(List<MenuOption> options) {
        if (options == null || options.isEmpty()) throw new IllegalArgumentException("options must not be empty");
        this.options = List.copyOf(options);
        this.selectedIndex = 0;
    }

    // ── State (headless, testable) ────────────────────────────────────────────

    public int getSelectedIndex() { return selectedIndex; }
    public MenuOption getSelected() { return options.get(selectedIndex); }
    public int size() { return options.size(); }

    public void moveUp() {
        if (selectedIndex > 0) selectedIndex--;
    }

    public void moveDown() {
        if (selectedIndex < options.size() - 1) selectedIndex++;
    }

    public void setSelectedIndex(int index) {
        if (index >= 0 && index < options.size()) selectedIndex = index;
    }

    // ── Rendering ─────────────────────────────────────────────────────────────

    /** Blocks until the user picks an option (returns its index) or presses ESC (returns -1). */
    public int show(Terminal terminal) throws IOException {
        terminal.hideCursor();
        try {
            while (true) {
                render(terminal);
                Terminal.KeyPress key = terminal.readKey();
                switch (key.key()) {
                    case UP    -> moveUp();
                    case DOWN  -> moveDown();
                    case ENTER -> { return selectedIndex; }
                    case ESC   -> { return -1; }
                    default    -> {}
                }
            }
        } finally {
            terminal.showCursor();
        }
    }

    private void render(Terminal terminal) {
        // clearScreen() homes the cursor to (1,1) — no separate moveCursor needed.
        // This is simpler than moveCursor+clearLine per row, and clearScreen() is the
        // one capability confirmed to work correctly on this terminal.
        terminal.clearScreen();
        for (int i = 0; i < options.size(); i++) {
            if (i == selectedIndex) {
                terminal.println(Color.yellow("▶ ") + Color.bold(options.get(i).title()));
            } else {
                terminal.println("  " + Color.dim(options.get(i).title()));
            }
        }
        terminal.println();
        String detail = options.get(selectedIndex).detail();
        if (detail != null && !detail.isEmpty()) {
            terminal.print(Color.cyan("  " + detail));
        }
    }
}