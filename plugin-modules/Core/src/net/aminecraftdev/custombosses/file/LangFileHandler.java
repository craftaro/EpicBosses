package net.aminecraftdev.custombosses.file;

import net.aminecraftdev.custombosses.utils.file.YmlFileHandler;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 04-Oct-18
 */
public class LangFileHandler extends YmlFileHandler {

    public LangFileHandler(JavaPlugin javaPlugin) {
        super(javaPlugin, false, new File(javaPlugin.getDataFolder(), "lang.yml"));
    }

}
