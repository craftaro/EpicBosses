package com.songoda.epicbosses.utils.file;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 01-Jul-18
 */
public abstract class FileHandler<T> implements IFileHandler<T> {

    private final JavaPlugin javaPlugin;
    private final boolean saveResource;

    private final File file;

    public FileHandler(JavaPlugin javaPlugin, boolean saveResource, File file) {
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

    public File getFile() {
        return this.file;
    }
}
