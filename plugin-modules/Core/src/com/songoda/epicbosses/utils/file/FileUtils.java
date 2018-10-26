package com.songoda.epicbosses.utils.file;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 28-Apr-18
 */
public class FileUtils {

    private static FileUtils INSTANCE = new FileUtils();

    public void saveFile(File file, FileConfiguration fileConfiguration) {
        try {
            fileConfiguration.save(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public void createFile(File file) {
        try {
            file.createNewFile();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public FileConfiguration loadFile(File file) {
        return YamlConfiguration.loadConfiguration(file);
    }

    public static FileUtils get() {
        return INSTANCE;
    }

}
