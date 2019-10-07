package com.songoda.epicbosses.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.songoda.epicbosses.entity.MinionEntity;
import com.songoda.epicbosses.utils.file.FileHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 12-Nov-18
 */
public class MinionsFileHandler extends FileHandler<Map<String, MinionEntity>> {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    public MinionsFileHandler(JavaPlugin javaPlugin, boolean saveResource, File file) {
        super(javaPlugin, saveResource, file);
    }

    @Override
    public Map<String, MinionEntity> loadFile() {
        Map<String, MinionEntity> minionEntityMap = new HashMap<>();

        createFile();

        try {
            FileReader fileReader = new FileReader(getFile());
            JsonObject jsonObject = GSON.fromJson(fileReader, JsonObject.class);

            fileReader.close();

            if(jsonObject != null) {
                jsonObject.entrySet().forEach(entry -> {
                    String id = entry.getKey();
                    MinionEntity minionEntity = GSON.fromJson(entry.getValue(), MinionEntity.class);

                    minionEntityMap.put(id, minionEntity);
                });
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return minionEntityMap;
    }

    @Override
    public void saveFile(Map<String, MinionEntity> stringMinionEntityMap) {
        try {
            FileWriter fileWriter = new FileWriter(getFile());
            Type type = new TypeToken<Map<String, MinionEntity>>(){}.getType();

            fileWriter.write(GSON.toJson(new HashMap<>(stringMinionEntityMap), type));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
