package com.songoda.epicbosses.utils.file;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

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
        if (!this.file.exists()) {
            if (this.saveResource) {
                String name = this.file.getName();
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
