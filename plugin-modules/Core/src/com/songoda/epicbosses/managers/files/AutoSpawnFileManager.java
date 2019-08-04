package com.songoda.epicbosses.managers.files;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.autospawns.AutoSpawn;
import com.songoda.epicbosses.file.AutoSpawnFileHandler;
import com.songoda.epicbosses.utils.ILoadable;
import com.songoda.epicbosses.utils.IReloadable;
import com.songoda.epicbosses.utils.ISavable;
import com.songoda.epicbosses.utils.Versions;
import com.songoda.epicbosses.utils.version.VersionHandler;

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

    public AutoSpawnFileManager(CustomBosses plugin) {
        File file = new File(plugin.getDataFolder(), new VersionHandler().getVersion().isHigherThanOrEqualTo(Versions.v1_13_R1) ? "current" : "legacy" + "/autospawns.json");

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
        if(this.autoSpawnMap.containsKey(name)) return;

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
