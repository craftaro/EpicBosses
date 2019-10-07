package com.songoda.epicbosses.utils.file;

import com.songoda.epicbosses.utils.Versions;
import com.songoda.epicbosses.utils.version.VersionHandler;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 04-Oct-18
 */
public class YmlFileHandler implements IFileHandler<FileConfiguration> {

    private final JavaPlugin javaPlugin;
    private final boolean saveResource;

    private final File file;

    public YmlFileHandler(JavaPlugin javaPlugin, boolean saveResource, File file) {
        this.javaPlugin = javaPlugin;
        this.saveResource = saveResource;
        this.file = file;
    }

    @Override
    public void createFile() {
        if(!this.file.exists()) {
            if(this.saveResource) {
                String name = this.file.getName();

                System.out.println(name);
                javaPlugin.saveResource(name, false);
            }

            FileUtils.get().createFile(this.file);
        }
    }

    @Override
    public FileConfiguration loadFile() {
        return FileUtils.get().loadFile(this.file);
    }

    @Override
    public void saveFile(FileConfiguration config) {
        FileUtils.get().saveFile(this.file, config);
    }

    public File getFile() {
        return this.file;
    }
}
