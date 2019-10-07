package com.songoda.epicbosses.utils.file.reader;

import com.songoda.epicbosses.utils.IYmlReader;
import com.songoda.epicbosses.utils.file.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;

import java.io.File;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Jun-18
 */
public class SpigotYmlReader implements IYmlReader {

    private static SpigotYmlReader instance = new SpigotYmlReader();

    private FileConfiguration spigotConfig;

    private SpigotYmlReader() {
        File serverFile = Bukkit.getWorldContainer().getParentFile();
        File spigotFile = new File(serverFile, "spigot.yml");

        this.spigotConfig = FileUtils.get().loadFile(spigotFile);
    }

    public static SpigotYmlReader get() {
        return instance;
    }

    @Override
    public Object getObject(String path) {
        return this.spigotConfig.get(path);
    }

}
