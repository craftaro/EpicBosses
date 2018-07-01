package net.aminecraftdev.custombosses.utils.file;

import lombok.Getter;
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
    @Getter private final File file;

    public FileHandler(JavaPlugin javaPlugin, boolean saveResource, File file) {
        this.javaPlugin = javaPlugin;
        this.saveResource = saveResource;
        this.file = file;
    }

    public void createFile() {
        if(!this.file.exists()) {
            if(this.saveResource) {
                String path = this.file.getName();

                if(this.javaPlugin.getResource(path) != null) {
                    this.javaPlugin.saveResource(path, false);
                    return;
                }
            }

            FileUtils.get().createFile(this.file);
        }
    }

}
