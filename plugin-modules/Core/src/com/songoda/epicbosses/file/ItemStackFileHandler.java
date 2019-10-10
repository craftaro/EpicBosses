package com.songoda.epicbosses.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.songoda.epicbosses.utils.file.FileHandler;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
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
 * @since 28-Apr-18
 */
public class ItemStackFileHandler extends FileHandler<Map<String, ItemStackHolder>> {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    public ItemStackFileHandler(JavaPlugin javaPlugin, File file, boolean saveResource) {
        super(javaPlugin, saveResource, file);
    }

    @Override
    public Map<String, ItemStackHolder> loadFile() {
        Map<String, ItemStackHolder> itemStackHolderMap = new HashMap<>();

        createFile();

        try {
            FileReader fileReader = new FileReader(getFile());
            JsonObject jsonObject = GSON.fromJson(fileReader, JsonObject.class);

            fileReader.close();

            if (jsonObject != null) {
                jsonObject.entrySet().forEach(entry -> {
                    String id = entry.getKey();
                    ItemStackHolder itemStackHolder = GSON.fromJson(entry.getValue(), ItemStackHolder.class);

                    itemStackHolderMap.put(id, itemStackHolder);
                });
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return itemStackHolderMap;
    }

    @Override
    public void saveFile(Map<String, ItemStackHolder> map) {
        try {
            FileWriter fileWriter = new FileWriter(getFile());
            Type type = new TypeToken<Map<String, ItemStackHolder>>() {
            }.getType();

            fileWriter.write(GSON.toJson(new HashMap<>(map), type));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
