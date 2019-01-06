package com.songoda.epicbosses.managers;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.autospawns.AutoSpawn;
import com.songoda.epicbosses.autospawns.SpawnType;
import com.songoda.epicbosses.holder.ActiveAutoSpawnHolder;
import com.songoda.epicbosses.holder.autospawn.ActiveIntervalAutoSpawnHolder;
import com.songoda.epicbosses.managers.files.AutoSpawnFileManager;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Jan-19
 */
public class AutoSpawnManager {

    private final Map<String, ActiveAutoSpawnHolder> activeAutoSpawnHolders = new HashMap<>();

    private AutoSpawnFileManager autoSpawnFileManager;

    public AutoSpawnManager(CustomBosses plugin) {
        this.autoSpawnFileManager = plugin.getAutoSpawnFileManager();
    }

    public void startIntervalSystems() {
        Map<String, AutoSpawn> autoSpawnMap = this.autoSpawnFileManager.getAutoSpawnMap();

        autoSpawnMap.forEach((name, autoSpawn) -> {
            String autoSpawnType = autoSpawn.getType();
            SpawnType spawnType = SpawnType.getCurrent(autoSpawnType);

            if(spawnType == SpawnType.INTERVAL) {
                ActiveIntervalAutoSpawnHolder autoSpawnHolder = new ActiveIntervalAutoSpawnHolder(spawnType, autoSpawn);

                if(!autoSpawn.isLocked()) {
                    autoSpawnHolder.restartInterval();
                }
                this.activeAutoSpawnHolders.put(name, autoSpawnHolder);
            }
        });
    }

    public List<String> getIntervalAutoSpawns() {
        Map<String, ActiveAutoSpawnHolder> autoSpawnHolderMap = new HashMap<>(this.activeAutoSpawnHolders);
        List<String> intervalAutoSpawns = new ArrayList<>();

        autoSpawnHolderMap.forEach((name, autoSpawnHolder) -> {
            if(autoSpawnHolder.getSpawnType() == SpawnType.INTERVAL) {
                intervalAutoSpawns.add(name);
            }
        });

        return intervalAutoSpawns;
    }

    public boolean exists(String name) {
        List<String> keyList = new ArrayList<>(this.activeAutoSpawnHolders.keySet());

        for (String s : keyList) {
            if(s.equalsIgnoreCase(name)) return true;
        }

        return false;
    }

    public ActiveAutoSpawnHolder getAutoSpawnHolder(String name) {
        if(!exists(name)) return null;

        List<String> keyList = new ArrayList<>(this.activeAutoSpawnHolders.keySet());

        for (String s : keyList) {
            if(s.equalsIgnoreCase(name)) return this.activeAutoSpawnHolders.get(s);
        }

        return null;
    }

    public void addActiveAutoSpawnHolder(String name, ActiveAutoSpawnHolder autoSpawnHolder) {
        if(this.activeAutoSpawnHolders.containsKey(name)) return;

        this.activeAutoSpawnHolders.put(name, autoSpawnHolder);
    }

    public ActiveAutoSpawnHolder getActiveAutoSpawnHolder(AutoSpawn autoSpawn) {
        return getActiveAutoSpawnHolder(BossAPI.getAutoSpawnName(autoSpawn));
    }

    public ActiveAutoSpawnHolder getActiveAutoSpawnHolder(String name) {
        return this.activeAutoSpawnHolders.getOrDefault(name, null);
    }

    public void removeActiveAutoSpawnHolder(String name) {
        ActiveAutoSpawnHolder autoSpawnHolder = this.activeAutoSpawnHolders.getOrDefault(name, null);

        if(autoSpawnHolder != null) {
            stopInterval(autoSpawnHolder);
            this.activeAutoSpawnHolders.remove(name);
        }
    }

    public void stopIntervalSystems() {
        Map<String, ActiveAutoSpawnHolder> autoSpawnHolderMap = new HashMap<>(this.activeAutoSpawnHolders);

        autoSpawnHolderMap.forEach((name, autoSpawnHolder) -> removeActiveAutoSpawnHolder(name));
    }

    public void stopInterval(AutoSpawn autoSpawn) {
        String name = BossAPI.getAutoSpawnName(autoSpawn);

        stopInterval(name);
    }


    public void stopInterval(String name) {
        ActiveAutoSpawnHolder autoSpawnHolder = this.activeAutoSpawnHolders.getOrDefault(name, null);

        stopInterval(autoSpawnHolder);
    }

    public void stopInterval(ActiveAutoSpawnHolder autoSpawnHolder) {
        if(autoSpawnHolder != null) {
            if(autoSpawnHolder.getSpawnType() == SpawnType.INTERVAL) {
                ActiveIntervalAutoSpawnHolder intervalAutoSpawnHolder = (ActiveIntervalAutoSpawnHolder) autoSpawnHolder;

                intervalAutoSpawnHolder.stopInterval();
            }
        }
    }

}
