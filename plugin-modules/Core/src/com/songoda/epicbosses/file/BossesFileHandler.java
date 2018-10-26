package com.songoda.epicbosses.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.songoda.epicbosses.entity.BossEntity;
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
 * @since 01-Jul-18
 */
public class BossesFileHandler extends FileHandler<Map<String, BossEntity>> {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    public BossesFileHandler(JavaPlugin javaPlugin, boolean saveResource, File file) {
        super(javaPlugin, saveResource, file);
    }

    @Override
    public Map<String, BossEntity> loadFile() {
        Map<String, BossEntity> bossEntityMap = new HashMap<>();

        createFile();

        try {
            FileReader fileReader = new FileReader(getFile());
            JsonObject jsonObject = GSON.fromJson(fileReader, JsonObject.class);

            fileReader.close();

            if(jsonObject != null) {
                jsonObject.entrySet().forEach(entry -> {
                    String id = entry.getKey();
                    BossEntity bossEntity = GSON.fromJson(entry.getValue(), BossEntity.class);

                    bossEntityMap.put(id, bossEntity);
                });
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return bossEntityMap;
    }

    @Override
    public void saveFile(Map<String, BossEntity> map) {
        try {
            FileWriter fileWriter = new FileWriter(getFile());
            Type type = new TypeToken<Map<String, BossEntity>>(){}.getType();

            fileWriter.write(GSON.toJson(new HashMap<>(map), type));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
