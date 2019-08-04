package com.songoda.epicbosses.utils.file;

import com.songoda.epicbosses.utils.Versions;
import com.songoda.epicbosses.utils.version.VersionHandler;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.regex.Pattern;

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

    @Override
    public void createFile() {
        if(!this.file.exists()) {
            if(this.saveResource) {
                String name = this.file.getName();
                String folder = new VersionHandler().getVersion().isHigherThanOrEqualTo(Versions.v1_13_R1) ? "/current/" : "/legacy/";
                String path = folder + name;

                try (InputStream resourceStream = this.getClass().getResourceAsStream(path)) {
                    Files.copy(resourceStream, new File(this.javaPlugin.getDataFolder(), name).toPath());
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            FileUtils.get().createFile(this.file);
        }
    }

}
