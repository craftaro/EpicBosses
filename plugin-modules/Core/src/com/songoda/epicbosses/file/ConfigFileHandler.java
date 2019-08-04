package com.songoda.epicbosses.file;

import com.songoda.epicbosses.utils.file.YmlFileHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 11-Oct-18
 */
public class ConfigFileHandler extends YmlFileHandler {

    public ConfigFileHandler(JavaPlugin javaPlugin) {
        super(javaPlugin, true, new File(javaPlugin.getDataFolder(), "config.yml"));
    }
}
