package net.aminecraftdev.custombosses.innerapi;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 31-May-17
 */
public class FileUtils {

    public static final void saveFile(File file, FileConfiguration configuration) {
        try {
            configuration.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static final FileConfiguration getConf(File file) {
        return YamlConfiguration.loadConfiguration(file);
    }

}
