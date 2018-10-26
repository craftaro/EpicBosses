package com.songoda.epicbosses.managers.files;

import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.file.DropTableFileHandler;
import com.songoda.epicbosses.utils.ILoadable;
import com.songoda.epicbosses.utils.IReloadable;
import com.songoda.epicbosses.utils.ISavable;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 25-Oct-18
 */
public class DropTableFileManager implements ILoadable, ISavable, IReloadable {

    private Map<String, DropTable> dropTableMap = new HashMap<>();
    private DropTableFileHandler dropTableFileHandler;

    public DropTableFileManager(JavaPlugin javaPlugin) {
        File file = new File(javaPlugin.getDataFolder(), "droptables.json");

        this.dropTableFileHandler = new DropTableFileHandler(javaPlugin, true, file);
    }

    @Override
    public void load() {
        this.dropTableMap = this.dropTableFileHandler.loadFile();
    }

    @Override
    public void reload() {
        load();
    }

    @Override
    public void save() {
        this.dropTableFileHandler.saveFile(this.dropTableMap);
    }

    public DropTable getDropTable(String name) {
        return this.dropTableMap.getOrDefault(name, null);
    }

    public Map<String, DropTable> getDropTables() {
        return new HashMap<>(this.dropTableMap);
    }
}
