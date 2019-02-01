package com.songoda.epicbosses.managers;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.autospawns.AutoSpawn;
import com.songoda.epicbosses.autospawns.SpawnType;
import com.songoda.epicbosses.holder.ActiveAutoSpawnHolder;
import com.songoda.epicbosses.holder.autospawn.ActiveIntervalAutoSpawnHolder;
import com.songoda.epicbosses.managers.files.AutoSpawnFileManager;
import com.songoda.epicbosses.skills.interfaces.ICustomSettingAction;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import org.bukkit.inventory.ItemStack;

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

        if(!this.activeAutoSpawnHolders.isEmpty()) {
            stopIntervalSystems();
        }

        autoSpawnMap.forEach(this::addAndCreateActiveAutoSpawnHolder);
    }

    public void stopIntervalSystems() {
        Map<String, ActiveAutoSpawnHolder> autoSpawnHolderMap = new HashMap<>(this.activeAutoSpawnHolders);

        autoSpawnHolderMap.forEach((name, autoSpawnHolder) -> removeActiveAutoSpawnHolder(name));
    }

    public void clearAutoSpawns() {
        this.activeAutoSpawnHolders.values().forEach(ActiveAutoSpawnHolder::clear);
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

    public ActiveAutoSpawnHolder getActiveAutoSpawnHolder(String name) {
        return this.activeAutoSpawnHolders.getOrDefault(name, null);
    }

    public void addAndCreateActiveAutoSpawnHolder(AutoSpawn autoSpawn) {
        String name = BossAPI.getAutoSpawnName(autoSpawn);

        addAndCreateActiveAutoSpawnHolder(name, autoSpawn);
    }

    public void removeActiveAutoSpawnHolder(AutoSpawn autoSpawn) {
        String name = BossAPI.getAutoSpawnName(autoSpawn);

        removeActiveAutoSpawnHolder(name);
    }

    private void removeActiveAutoSpawnHolder(String name) {
        ActiveAutoSpawnHolder autoSpawnHolder = this.activeAutoSpawnHolders.getOrDefault(name, null);

        if(autoSpawnHolder != null) {
            stopInterval(autoSpawnHolder);
            this.activeAutoSpawnHolders.remove(name);
        }
    }

    private void stopInterval(ActiveAutoSpawnHolder autoSpawnHolder) {
        if (autoSpawnHolder != null) {
            if (autoSpawnHolder.getSpawnType() == SpawnType.INTERVAL) {
                ActiveIntervalAutoSpawnHolder intervalAutoSpawnHolder = (ActiveIntervalAutoSpawnHolder) autoSpawnHolder;

                intervalAutoSpawnHolder.stopInterval();
            }
        }
    }

    private void addAndCreateActiveAutoSpawnHolder(String name, AutoSpawn autoSpawn) {
        String autoSpawnType = autoSpawn.getType();
        SpawnType spawnType = SpawnType.getCurrent(autoSpawnType);

        if(spawnType == SpawnType.INTERVAL) {
            ActiveIntervalAutoSpawnHolder autoSpawnHolder = new ActiveIntervalAutoSpawnHolder(spawnType, autoSpawn);

            if(autoSpawn.isEditing()) return;

            autoSpawnHolder.restartInterval();
            this.activeAutoSpawnHolders.put(name, autoSpawnHolder);
        }
    }

    public static ICustomSettingAction createAutoSpawnAction(String name, String current, List<String> extraInformation, ItemStack displayStack, ClickAction clickAction) {
        return new CustomAutoSpawnActionCreator(name, current, extraInformation, displayStack, clickAction);
    }

    private static class CustomAutoSpawnActionCreator implements ICustomSettingAction {

        private final List<String> extraInformation;
        private final ClickAction clickAction;
        private final String name, current;
        private final ItemStack itemStack;

        public CustomAutoSpawnActionCreator(String name, String current, List<String> extraInformation, ItemStack itemStack, ClickAction clickAction) {
            this.name = name;
            this.current = current;
            this.itemStack = itemStack;
            this.clickAction = clickAction;
            this.extraInformation = extraInformation;
        }

        @Override
        public ClickAction getAction() {
            return this.clickAction;
        }

        @Override
        public String getSettingName() {
            return this.name;
        }

        @Override
        public ItemStack getDisplayItemStack() {
            return this.itemStack;
        }

        @Override
        public String getCurrent() {
            return this.current;
        }

        @Override
        public List<String> getExtraInformation() {
            return this.extraInformation;
        }
    }

}
