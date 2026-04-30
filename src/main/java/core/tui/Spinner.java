package core.tui;

public class Spinner {
    private static final String[] FRAMES = {"⠋", "⠙", "⠹", "⠸", "⠼", "⠴", "⠦", "⠧", "⠇", "⠏"};
    private static final int FRAME_MS = 80;

    private final Terminal terminal;
    private volatile boolean running;
    private Thread thread;

    public Spinner(Terminal terminal) {
        this.terminal = terminal;
    }

    public void start(String message) {
        terminal.hideCursor();
        running = true;
        thread = new Thread(() -> {
            int i = 0;
            while (running) {
                terminal.print("\r" + Color.cyan(FRAMES[i % FRAMES.length]) + " " + message);
                i++;
                try { Thread.sleep(FRAME_MS); } catch (InterruptedException e) { Thread.currentThread().interrupt(); break; }
            }
        }, "spinner");
        thread.setDaemon(true);
        thread.start();
    }

    public void stop(String finalMessage) {
        running = false;
        if (thread != null) {
            try { thread.join(500); } catch (InterruptedException ignored) { Thread.currentThread().interrupt(); }
        }
        terminal.clearLine();
        terminal.showCursor();
        if (finalMessage != null && !finalMessage.isEmpty()) {
            terminal.println(finalMessage);
        }
    }

    public void stop() {
        stop(null);
    }

    /** Convenience: spin for {@code millis} ms then clear. */
    public static void pause(Terminal terminal, String message, long millis) {
        Spinner s = new Spinner(terminal);
        s.start(message);
        try { Thread.sleep(millis); } catch (InterruptedException ignored) {}
        s.stop();
    }
}