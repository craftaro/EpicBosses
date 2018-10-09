package net.aminecraftdev.custombosses.managers.files;

import net.aminecraftdev.custombosses.CustomBosses;
import net.aminecraftdev.custombosses.container.BossEntityContainer;
import net.aminecraftdev.custombosses.entity.BossEntity;
import net.aminecraftdev.custombosses.file.BossesFileHandler;
import net.aminecraftdev.custombosses.utils.ILoadable;
import net.aminecraftdev.custombosses.utils.IReloadable;
import net.aminecraftdev.custombosses.utils.ISavable;

import java.io.File;
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

    public BossesFileManager(CustomBosses customBosses) {
        File file = new File(customBosses.getDataFolder(), "bosses.json");

        this.bossesFileHandler = new BossesFileHandler(customBosses, true, file);
        this.bossEntityContainer = customBosses.getBossEntityContainer();
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

    public BossEntity getBossEntity(String name) {
        return this.bossEntityContainer.getData().getOrDefault(name, null);
    }

    public Map<String, BossEntity> getBossEntities() {
        return this.bossEntityContainer.getData();
    }
}
