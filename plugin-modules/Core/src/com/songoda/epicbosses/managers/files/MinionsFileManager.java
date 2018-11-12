package com.songoda.epicbosses.managers.files;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.container.MinionEntityContainer;
import com.songoda.epicbosses.entity.MinionEntity;
import com.songoda.epicbosses.file.MinionsFileHandler;
import com.songoda.epicbosses.utils.ILoadable;
import com.songoda.epicbosses.utils.IReloadable;
import com.songoda.epicbosses.utils.ISavable;

import java.io.File;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 12-Nov-18
 */
public class MinionsFileManager implements ILoadable, ISavable, IReloadable {

    private MinionEntityContainer minionEntityContainer;
    private MinionsFileHandler minionsFileHandler;

    public MinionsFileManager(CustomBosses customBosses) {
        File file = new File(customBosses.getDataFolder(), "minions.json");

        this.minionsFileHandler = new MinionsFileHandler(customBosses, true, file);
        this.minionEntityContainer = customBosses.getMinionEntityContainer();
    }

    @Override
    public void load() {
        this.minionEntityContainer.clearContainer();
        this.minionEntityContainer.saveData(this.minionsFileHandler.loadFile());
    }

    @Override
    public void reload() {
        load();
    }

    @Override
    public void save() {
        this.minionsFileHandler.saveFile(this.minionEntityContainer.getData());
    }

    public MinionEntity getMinionEntity(String name) {
        return this.minionEntityContainer.getData().getOrDefault(name, null);
    }

    public Map<String, MinionEntity> getMinionEntities() {
        return this.minionEntityContainer.getData();
    }
}
