package core.save;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

public class JsonSaveManager implements SaveManager {
    private static final String DEFAULT_PATH = "save/arcade_save.json";

    private final File file;
    private final Gson gson;
    private final SaveFile saveFile;

    public JsonSaveManager() {
        this(new File(DEFAULT_PATH));
    }

    public JsonSaveManager(File file) {
        this.file = file;
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.saveFile = load();
    }

    private SaveFile load() {
        if (!file.exists()) return new SaveFile();
        try (Reader reader = new FileReader(file)) {
            SaveFile loaded = gson.fromJson(reader, SaveFile.class);
            return loaded != null ? loaded : new SaveFile();
        } catch (IOException e) {
            return new SaveFile();
        }
    }

    @Override
    public PlayerProfile getProfile(String playerName) {
        Map<String, Map<String, Object>> players = saveFile.getPlayers();
        players.computeIfAbsent(playerName, k -> {
            Map<String, Object> data = new LinkedHashMap<>();
            data.put("name", playerName);
            return data;
        });
        return new PlayerProfile(players.get(playerName));
    }

    @Override
    public void save() {
        try (Writer writer = new FileWriter(file)) {
            gson.toJson(saveFile, writer);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save arcade data: " + e.getMessage(), e);
        }
    }
}