package net.aminecraftdev.custombosses.utils.reader;

import net.aminecraftdev.custombosses.utils.IYmlReader;
import net.aminecraftdev.custombosses.utils.file.FileUtils;
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

    @Override
    public Object getObject(String path) {
        return this.spigotConfig.get(path);
    }

    public static SpigotYmlReader get() {
        return instance;
    }

}
