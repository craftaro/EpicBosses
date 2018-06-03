package net.aminecraftdev.custombosses.utils.itemstack;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import net.aminecraftdev.custombosses.utils.file.FileUtils;
import net.aminecraftdev.custombosses.utils.file.IFileHandler;
import net.aminecraftdev.custombosses.utils.itemstack.holder.ItemStackHolder;
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
public class ItemStackFileHandler implements IFileHandler<Map<String, ItemStackHolder>> {

    private static final Gson GSON = new GsonBuilder()
            .setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    private final JavaPlugin javaPlugin;
    private final boolean saveResource;
    private final File file;

    public ItemStackFileHandler(JavaPlugin javaPlugin, File file, boolean saveResource) {
        this.saveResource = saveResource;
        this.javaPlugin = javaPlugin;
        this.file = file;
    }

    @Override
    public Map<String, ItemStackHolder> loadFile() {
        Map<String, ItemStackHolder> itemStackHolderMap = new HashMap<>();

        if(!this.file.exists()) {
            if(this.saveResource) {
                String path = this.file.getName();

                if(this.javaPlugin.getResource(path) != null) {
                    this.javaPlugin.saveResource(path, false);
                }
            } else {
                FileUtils.get().createFile(this.file);
            }
        }

        try {
            FileReader fileReader = new FileReader(file);
            JsonObject jsonObject = GSON.fromJson(fileReader, JsonObject.class);

            fileReader.close();

            if(jsonObject != null) {
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
            FileWriter fileWriter = new FileWriter(file);
            Type type = new TypeToken<Map<String, ItemStackHolder>>(){}.getType();

            fileWriter.write(GSON.toJson(new HashMap<>(map), type));
            fileWriter.flush();
            fileWriter.close();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}
