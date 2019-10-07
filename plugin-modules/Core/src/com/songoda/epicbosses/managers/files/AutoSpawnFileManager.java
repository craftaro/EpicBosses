package com.songoda.epicbosses.managers.files;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.autospawns.AutoSpawn;
import com.songoda.epicbosses.file.AutoSpawnFileHandler;
import com.songoda.epicbosses.utils.ILoadable;
import com.songoda.epicbosses.utils.IReloadable;
import com.songoda.epicbosses.utils.ISavable;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Jan-19
 */
public class AutoSpawnFileManager implements ILoadable, ISavable, IReloadable {

    private Map<String, AutoSpawn> autoSpawnMap = new HashMap<>();
    private AutoSpawnFileHandler autoSpawnFileHandler;

    public AutoSpawnFileManager(EpicBosses plugin) {
        File file = new File(plugin.getDataFolder(), "autospawns.json");

        this.autoSpawnFileHandler = new AutoSpawnFileHandler(plugin, true, file);
    }

    @Override
    public void load() {
        this.autoSpawnMap = this.autoSpawnFileHandler.loadFile();
    }

    @Override
    public void reload() {
        load();
    }

    @Override
    public void save() {
        this.autoSpawnFileHandler.saveFile(this.autoSpawnMap);
    }

    public void saveAutoSpawn(String name, AutoSpawn autoSpawn) {
        if (this.autoSpawnMap.containsKey(name)) return;

        this.autoSpawnMap.put(name, autoSpawn);
        save();
    }

    public AutoSpawn getAutoSpawn(String name) {
        return this.autoSpawnMap.getOrDefault(name, null);
    }

    public Map<String, AutoSpawn> getAutoSpawnMap() {
        return new HashMap<>(this.autoSpawnMap);
    }
}
