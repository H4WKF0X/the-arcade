package core.tui;

import com.sun.jna.Library;
import com.sun.jna.Native;
import org.jline.keymap.BindingReader;
import org.jline.keymap.KeyMap;
import org.jline.terminal.Attributes;
import org.jline.terminal.TerminalBuilder;
import org.jline.utils.InfoCmp;

import java.io.IOException;
import java.io.PrintWriter;

public class Terminal {

    public enum Key { UP, DOWN, LEFT, RIGHT, ENTER, ESC, CHAR, UNKNOWN }

    public record KeyPress(Key key, char ch) {
        public static KeyPress of(Key key) { return new KeyPress(key, '\0'); }
        public static KeyPress ofChar(char ch) { return new KeyPress(Key.CHAR, ch); }
    }

    private interface Kernel32 extends Library {
        Kernel32 INSTANCE = Native.load("kernel32", Kernel32.class);
        boolean SetConsoleOutputCP(int wCodePageID);
        boolean SetConsoleCP(int wCodePageID);
    }

    private final org.jline.terminal.Terminal jline;
    private final PrintWriter out;
    private Attributes savedAttributes;
    private BindingReader bindingReader;
    private KeyMap<Key> keyMap;

     public Terminal() throws IOException {
        setUtf8CodePage();
        this.jline = TerminalBuilder.builder().system(true).build();
        this.out = jline.writer();
    }

    private static void setUtf8CodePage() {
        if (!System.getProperty("os.name", "").startsWith("Windows")) return;
        try {
            Kernel32.INSTANCE.SetConsoleOutputCP(65001);
            Kernel32.INSTANCE.SetConsoleCP(65001);
        } catch (Throwable ignored) {}
    }

    Terminal(org.jline.terminal.Terminal jline) {
        this.jline = jline;
        this.out = jline.writer();
    }

    public void enterRawMode() {
        savedAttributes = jline.enterRawMode();
        bindingReader = new BindingReader(jline.reader());
        keyMap = buildKeyMap();
    }

    public void exitRawMode() {
        if (savedAttributes != null) {
            jline.setAttributes(savedAttributes);
            savedAttributes = null;
        }
    }

    private KeyMap<Key> buildKeyMap() {
        KeyMap<Key> map = new KeyMap<>();
        map.bind(Key.UP,    "[A", "OA");
        map.bind(Key.DOWN,  "[B", "OB");
        map.bind(Key.LEFT,  "[D", "OD");
        map.bind(Key.RIGHT, "[C", "OC");
        map.bind(Key.ENTER, "\r", "\n");
        map.bind(Key.ESC,   "");
        bindCap(map, Key.UP,    InfoCmp.Capability.key_up);
        bindCap(map, Key.DOWN,  InfoCmp.Capability.key_down);
        bindCap(map, Key.LEFT,  InfoCmp.Capability.key_left);
        bindCap(map, Key.RIGHT, InfoCmp.Capability.key_right);
        map.setNomatch(Key.CHAR);
        return map;
    }

    private void bindCap(KeyMap<Key> map, Key key, InfoCmp.Capability cap) {
        String seq = jline.getStringCapability(cap);
        if (seq != null && !seq.isEmpty()) map.bind(key, seq);
    }

    public KeyPress readKey() throws IOException {
        if (bindingReader == null) {
            bindingReader = new BindingReader(jline.reader());
            keyMap = buildKeyMap();
        }
        Key result = bindingReader.readBinding(keyMap);
        if (result == null)     return KeyPress.of(Key.UNKNOWN);
        if (result != Key.CHAR) return KeyPress.of(result);
        String last = bindingReader.getLastBinding();
        if (last == null || last.isEmpty()) return KeyPress.of(Key.UNKNOWN);
        char ch = last.charAt(0);
        if (ch == 3 || ch == 4) return KeyPress.of(Key.ESC); // Ctrl+C / Ctrl+D
        return KeyPress.ofChar(ch);
    }

    // ── Text output (colours handled by JLine's Windows ANSI layer) ──────────

    public void print(String s)   { out.print(s);   out.flush(); }
    public void println(String s) { out.println(s); out.flush(); }
    public void println()         { out.println();  out.flush(); }

    // ── Cursor/screen control via JLine capabilities ──────────────────────────
    // jline.puts() routes through JLine's own VT-enabled CONOUT$ handle,
    // which works where raw ANSI bytes written to System.out do not.

    public void clearScreen() {
        jline.puts(InfoCmp.Capability.clear_screen);
        jline.flush();
    }

    public void clearLine() {
        // \r to column 0, then erase to end-of-line = erase whole visible line
        out.print("\r");
        out.flush();
        jline.puts(InfoCmp.Capability.clr_eol);
        jline.flush();
    }

    public void moveCursor(int row, int col) {
        // InfoCmp cursor_address uses 0-based row/col
        jline.puts(InfoCmp.Capability.cursor_address, row - 1, col - 1);
        jline.flush();
    }

    public void hideCursor() {
        jline.puts(InfoCmp.Capability.cursor_invisible);
        jline.flush();
    }

    public void showCursor() {
        jline.puts(InfoCmp.Capability.cursor_normal);
        jline.flush();
    }

    public void close() throws IOException {
        exitRawMode();
        jline.close();
    }
}