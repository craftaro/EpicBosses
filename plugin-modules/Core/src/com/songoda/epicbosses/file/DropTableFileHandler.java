package com.songoda.epicbosses.file;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.songoda.epicbosses.droptable.DropTable;
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
 * @since 25-Oct-18
 */
public class DropTableFileHandler extends FileHandler<Map<String, DropTable>> {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    public DropTableFileHandler(JavaPlugin javaPlugin, boolean saveResource, File file) {
        super(javaPlugin, saveResource, file);
    }

    @Override
    public Map<String, DropTable> loadFile() {
        Map<String, DropTable> dropTableMap = new HashMap<>();

        createFile();

        try {
            FileReader fileReader = new FileReader(getFile());
            JsonObject jsonObject = GSON.fromJson(fileReader, JsonObject.class);

            fileReader.close();

            if(jsonObject != null) {
                jsonObject.entrySet().forEach(entry -> {
                    String id = entry.getKey();
                    DropTable dropTable = GSON.fromJson(entry.getValue(), DropTable.class);

                    dropTableMap.put(id, dropTable);
                });
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        }

        return dropTableMap;
    }

    @Override
    public void saveFile(Map<String, DropTable> stringDropTableMap) {
        try {
            FileWriter fileWriter = new FileWriter(getFile());
            Type type = new TypeToken<Map<String, ItemStackHolder>>(){}.getType();

            fileWriter.write(GSON.toJson(new HashMap<>(stringDropTableMap), type));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
