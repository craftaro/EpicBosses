package com.songoda.epicbosses.file;

import com.songoda.epicbosses.utils.Versions;
import com.songoda.epicbosses.utils.file.YmlFileHandler;
import com.songoda.epicbosses.utils.version.VersionHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 10-Oct-18
 */
public class EditorFileHandler extends YmlFileHandler {

    public EditorFileHandler(JavaPlugin javaPlugin) {
        super(javaPlugin, true, new File(javaPlugin.getDataFolder(), new VersionHandler().getVersion().isHigherThanOrEqualTo(Versions.v1_13_R1) ? "current" : "legacy" + "/editor.yml"));
    }

}