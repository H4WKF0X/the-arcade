package core.tui;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ColorTest {

    private static final String ESC = "";

    @Test void resetConstant() { assertEquals(ESC + "[0m", Color.RESET); }
    @Test void redConstant()   { assertEquals(ESC + "[31m", Color.RED); }
    @Test void greenConstant() { assertEquals(ESC + "[32m", Color.GREEN); }
    @Test void yellowConstant(){ assertEquals(ESC + "[33m", Color.YELLOW); }
    @Test void boldConstant()  { assertEquals(ESC + "[1m",  Color.BOLD); }
    @Test void dimConstant()   { assertEquals(ESC + "[2m",  Color.DIM); }
    @Test void cyanConstant()  { assertEquals(ESC + "[36m", Color.CYAN); }

    @Test void redHelper()    { assertEquals(ESC + "[31mhello" + ESC + "[0m", Color.red("hello")); }
    @Test void greenHelper()  { assertEquals(ESC + "[32mfoo"   + ESC + "[0m", Color.green("foo")); }
    @Test void yellowHelper() { assertEquals(ESC + "[33mbar"   + ESC + "[0m", Color.yellow("bar")); }
    @Test void boldHelper()   { assertEquals(ESC + "[1mX"      + ESC + "[0m", Color.bold("X")); }
    @Test void dimHelper()    { assertEquals(ESC + "[2mX"      + ESC + "[0m", Color.dim("X")); }
    @Test void cyanHelper()   { assertEquals(ESC + "[36mX"     + ESC + "[0m", Color.cyan("X")); }

    @Test void emptyStringProducesOnlyEscapeCodes() {
        String result = Color.red("");
        assertTrue(result.startsWith(ESC + "[31m"));
        assertTrue(result.endsWith(ESC + "[0m"));
        // no text between the codes
        assertEquals(ESC + "[31m" + ESC + "[0m", result);
    }

    @Test void composedWrappersStack() {
        // bold(red("x")) should open bold, then red, then text, then two resets
        String result = Color.bold(Color.red("x"));
        assertTrue(result.startsWith(ESC + "[1m"));
        assertTrue(result.endsWith(ESC + "[0m"));
        assertTrue(result.contains(ESC + "[31m"));
        assertTrue(result.contains("x"));
    }
}