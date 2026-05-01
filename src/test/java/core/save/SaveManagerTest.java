package core.save;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

class SaveManagerTest {

    @TempDir Path tempDir;

    private JsonSaveManager manager() {
        return new JsonSaveManager(tempDir.resolve("save.json").toFile());
    }

    @Test void newPlayerCreatedOnDemand() {
        PlayerProfile p = manager().getProfile("alice");
        assertEquals("alice", p.getName());
    }

    @Test void roundTrip_intValue() {
        File saveFile = tempDir.resolve("save.json").toFile();

        JsonSaveManager mgr = new JsonSaveManager(saveFile);
        mgr.getProfile("alice").set("game.score", 42);
        mgr.save();

        JsonSaveManager mgr2 = new JsonSaveManager(saveFile);
        assertEquals(42, mgr2.getProfile("alice").getInt("game.score", 0));
    }

    @Test void roundTrip_stringValue() {
        File saveFile = tempDir.resolve("save.json").toFile();

        JsonSaveManager mgr = new JsonSaveManager(saveFile);
        mgr.getProfile("bob").set("game.level", "expert");
        mgr.save();

        JsonSaveManager mgr2 = new JsonSaveManager(saveFile);
        assertEquals("expert", mgr2.getProfile("bob").getString("game.level", ""));
    }

    @Test void roundTrip_booleanValue() {
        File saveFile = tempDir.resolve("save.json").toFile();

        JsonSaveManager mgr = new JsonSaveManager(saveFile);
        mgr.getProfile("carol").set("game.unlocked", true);
        mgr.save();

        JsonSaveManager mgr2 = new JsonSaveManager(saveFile);
        assertTrue(mgr2.getProfile("carol").getBoolean("game.unlocked", false));
    }

    @Test void namespaceIsolation() {
        File saveFile = tempDir.resolve("save.json").toFile();

        JsonSaveManager mgr = new JsonSaveManager(saveFile);
        mgr.getProfile("alice").set("game.score", 100);
        mgr.getProfile("bob").set("game.score", 200);
        mgr.save();

        JsonSaveManager mgr2 = new JsonSaveManager(saveFile);
        assertEquals(100, mgr2.getProfile("alice").getInt("game.score", 0));
        assertEquals(200, mgr2.getProfile("bob").getInt("game.score", 0));
    }

    @Test void missingFile_returnsEmptySave() {
        File nonexistent = tempDir.resolve("no_such_file.json").toFile();
        JsonSaveManager mgr = new JsonSaveManager(nonexistent);
        assertEquals(0, mgr.getProfile("alice").getInt("any", 0));
    }

    @Test void defaultValues_survivedRoundTrip() {
        File saveFile = tempDir.resolve("save.json").toFile();

        JsonSaveManager mgr = new JsonSaveManager(saveFile);
        mgr.getProfile("alice").set("roulette.survived", 3);
        mgr.save();

        JsonSaveManager mgr2 = new JsonSaveManager(saveFile);
        assertEquals(0, mgr2.getProfile("alice").getInt("roulette.deaths", 0));
    }

    @Test void global_roundTrip() {
        File saveFile = tempDir.resolve("save.json").toFile();

        JsonSaveManager mgr = new JsonSaveManager(saveFile);
        mgr.getGlobal().put("total_sessions", 5);
        mgr.save();

        JsonSaveManager mgr2 = new JsonSaveManager(saveFile);
        assertEquals(5.0, ((Number) mgr2.getGlobal().get("total_sessions")).doubleValue(), 0.001);
    }

    @Test void corruptedFile_returnsEmptySave() throws IOException {
        File saveFile = tempDir.resolve("corrupt.json").toFile();
        try (FileWriter fw = new FileWriter(saveFile)) { fw.write("{not valid json!!!"); }

        JsonSaveManager mgr = new JsonSaveManager(saveFile);
        assertEquals(0, mgr.getProfile("alice").getInt("any", 0));
    }
}