package com.songoda.epicbosses.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.songoda.epicbosses.utils.file.FileHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 17-Oct-18
 */
public class CommandsFileHandler extends FileHandler<Map<String, List<String>>> {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    public CommandsFileHandler(JavaPlugin javaPlugin, boolean saveResource, File file) {
        super(javaPlugin, saveResource, file);
    }

    @Override
    public Map<String, List<String>> loadFile() {
        Map<String, List<String>> commandsMap = new HashMap<>();

        createFile();

        try {
            FileReader fileReader = new FileReader(getFile());
            JsonObject jsonObject = GSON.fromJson(fileReader, JsonObject.class);

            fileReader.close();

            if (jsonObject != null) {
                Type listType = new TypeToken<List<String>>() {
                }.getType();

                jsonObject.entrySet().forEach(entry -> {
                    String id = entry.getKey();
                    List<String> commands = GSON.fromJson(entry.getValue(), listType);

                    commandsMap.put(id, commands);
                });
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return commandsMap;
    }

    @Override
    public void saveFile(Map<String, List<String>> stringListMap) {
        try {
            FileWriter fileWriter = new FileWriter(getFile());
            Type type = new TypeToken<Map<String, List<String>>>() {
            }.getType();

            fileWriter.write(GSON.toJson(new HashMap<>(stringListMap), type));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
