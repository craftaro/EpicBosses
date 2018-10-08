package net.aminecraftdev.custombosses.utils.file;

import lombok.Getter;
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

    @Getter private final File file;

    public YmlFileHandler(JavaPlugin javaPlugin, boolean saveResource, File file) {
        this.javaPlugin = javaPlugin;
        this.saveResource = saveResource;
        this.file = file;
    }

    @Override
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

    @Override
    public FileConfiguration loadFile() {
        return FileUtils.get().loadFile(this.file);
    }

    @Override
    public void saveFile(FileConfiguration config) {
        FileUtils.get().saveFile(this.file, config);
    }

}
