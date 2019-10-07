package com.songoda.epicbosses.managers.files;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.container.BossEntityContainer;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.file.BossesFileHandler;
import com.songoda.epicbosses.utils.ILoadable;
import com.songoda.epicbosses.utils.IReloadable;
import com.songoda.epicbosses.utils.ISavable;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 01-Jul-18
 */
public class BossesFileManager implements ILoadable, ISavable, IReloadable {

    private BossEntityContainer bossEntityContainer;
    private BossesFileHandler bossesFileHandler;

    public BossesFileManager(EpicBosses epicBosses) {
        File file = new File(epicBosses.getDataFolder(), "bosses.json");

        this.bossesFileHandler = new BossesFileHandler(epicBosses, true, file);
        this.bossEntityContainer = epicBosses.getBossEntityContainer();
    }

    @Override
    public void load() {
        this.bossEntityContainer.clearContainer();
        this.bossEntityContainer.saveData(this.bossesFileHandler.loadFile());
    }

    @Override
    public void reload() {
        load();
    }

    @Override
    public void save() {
        this.bossesFileHandler.saveFile(this.bossEntityContainer.getData());
    }

    public void saveBossEntity(String name, BossEntity bossEntity) {
        if(this.bossEntityContainer.exists(name)) return;

        this.bossEntityContainer.saveData(name, bossEntity);
    }

    public BossEntity getBossEntity(String name) {
        return this.bossEntityContainer.getData().getOrDefault(name, null);
    }

    public Map<String, BossEntity> getBossEntitiesMap() {
        return new HashMap<>(this.bossEntityContainer.getData());
    }

    public List<BossEntity> getBossEntities() {
        return new ArrayList<>(this.bossEntityContainer.getData().values());
    }
}
