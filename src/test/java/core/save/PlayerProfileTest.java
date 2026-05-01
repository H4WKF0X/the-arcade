package core.save;

import org.junit.jupiter.api.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class PlayerProfileTest {

    private PlayerProfile profile(String name) {
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("name", name);
        return new PlayerProfile(data);
    }

    @Test void getName_returnsConstructedName() {
        assertEquals("alice", profile("alice").getName());
    }

    @Test void getInt_defaultWhenAbsent() {
        assertEquals(99, profile("p").getInt("missing", 99));
    }

    @Test void getInt_roundTrip() {
        PlayerProfile p = profile("p");
        p.set("score", 42);
        assertEquals(42, p.getInt("score", 0));
    }

    @Test void getInt_gsonDoubleHandled() {
        // Gson deserializes JSON integers as Double; getInt must handle this
        Map<String, Object> data = new LinkedHashMap<>();
        data.put("name", "p");
        data.put("score", 7.0);
        assertEquals(7, new PlayerProfile(data).getInt("score", 0));
    }

    @Test void getString_defaultWhenAbsent() {
        assertEquals("default", profile("p").getString("missing", "default"));
    }

    @Test void getString_roundTrip() {
        PlayerProfile p = profile("p");
        p.set("alias", "Alice");
        assertEquals("Alice", p.getString("alias", ""));
    }

    @Test void getBoolean_defaultWhenAbsent() {
        assertTrue(profile("p").getBoolean("missing", true));
        assertFalse(profile("p").getBoolean("missing", false));
    }

    @Test void getBoolean_roundTrip() {
        PlayerProfile p = profile("p");
        p.set("unlocked", true);
        assertTrue(p.getBoolean("unlocked", false));
    }

    @Test void set_overwritesPreviousValue() {
        PlayerProfile p = profile("p");
        p.set("score", 1);
        p.set("score", 2);
        assertEquals(2, p.getInt("score", 0));
    }
}