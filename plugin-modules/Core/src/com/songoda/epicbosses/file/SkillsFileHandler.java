package com.songoda.epicbosses.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.songoda.epicbosses.skills.Skill;
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
 * @since 13-Nov-18
 */
public class SkillsFileHandler extends FileHandler<Map<String, Skill>> {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    public SkillsFileHandler(JavaPlugin javaPlugin, boolean saveResource, File file) {
        super(javaPlugin, saveResource, file);
    }

    @Override
    public Map<String, Skill> loadFile() {
        Map<String, Skill> skillMap = new HashMap<>();

        createFile();

        try {
            FileReader fileReader = new FileReader(getFile());
            JsonObject jsonObject = GSON.fromJson(fileReader, JsonObject.class);

            fileReader.close();

            if(jsonObject != null) {
                jsonObject.entrySet().forEach(entry -> {
                    String id = entry.getKey();
                    Skill bossEntity = GSON.fromJson(entry.getValue(), Skill.class);

                    skillMap.put(id, bossEntity);
                });
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return skillMap;
    }

    @Override
    public void saveFile(Map<String, Skill> skillMap) {
        try {
            FileWriter fileWriter = new FileWriter(getFile());
            Type type = new TypeToken<Map<String, Skill>>(){}.getType();

            fileWriter.write(GSON.toJson(new HashMap<>(skillMap), type));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
