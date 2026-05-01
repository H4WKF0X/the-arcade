package core.game;

import org.reflections.Reflections;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class GameRegistry {

    private final List<Game> games = new ArrayList<>();

    public GameRegistry() {
        loadViaReflections();
        if (games.isEmpty()) {
            loadViaRegistryFile();
        }
    }

    private void loadViaReflections() {
        try {
            Reflections reflections = new Reflections("games");
            Set<Class<?>> annotated = reflections.getTypesAnnotatedWith(ArcadeGame.class);
            for (Class<?> cls : annotated) {
                instantiate(cls);
            }
        } catch (Exception e) {
            // fall through to registry file
        }
    }

    private void loadViaRegistryFile() {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream("games/registry.txt")) {
            if (in == null) return;
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;
                try {
                    instantiate(Class.forName(line));
                } catch (Exception e) {
                    System.err.println("[GameRegistry] Could not load: " + line + " — " + e.getMessage());
                }
            }
        } catch (Exception e) {
            System.err.println("[GameRegistry] Could not read registry.txt: " + e.getMessage());
        }
    }

    private void instantiate(Class<?> cls) {
        if (!Game.class.isAssignableFrom(cls)) return;
        if (!cls.isAnnotationPresent(ArcadeGame.class)) return;
        try {
            games.add((Game) cls.getDeclaredConstructor().newInstance());
        } catch (Exception e) {
            System.err.println("[GameRegistry] Could not instantiate " + cls.getName() + ": " + e.getMessage());
        }
    }

    public List<Game> getGames() {
        return List.copyOf(games);
    }
}