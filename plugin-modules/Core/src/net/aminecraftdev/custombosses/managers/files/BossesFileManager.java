package net.aminecraftdev.custombosses.managers.files;

import lombok.Getter;
import net.aminecraftdev.custombosses.entity.BossEntity;
import net.aminecraftdev.custombosses.file.BossesFileHandler;
import net.aminecraftdev.custombosses.utils.ILoadable;
import net.aminecraftdev.custombosses.utils.IReloadable;
import net.aminecraftdev.custombosses.utils.ISavable;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 01-Jul-18
 */
public class BossesFileManager implements ILoadable, ISavable, IReloadable {

    private Map<String, BossEntity> bossEntityMap = new HashMap<>();
    private BossesFileHandler bossesFileHandler;

    public BossesFileManager(JavaPlugin javaPlugin) {
        File file = new File(javaPlugin.getDataFolder(), "bosses.json");

        this.bossesFileHandler = new BossesFileHandler(javaPlugin, true, file);
    }

    @Override
    public void load() {
        this.bossEntityMap = this.bossesFileHandler.loadFile();
    }

    @Override
    public void reload() {
        load();
    }

    @Override
    public void save() {
        this.bossesFileHandler.saveFile(this.bossEntityMap);
    }

    public boolean saveNewBossEntity(String name, BossEntity bossEntity) {
        if(this.bossEntityMap.containsKey(name)) {
            return false;
        }

        this.bossEntityMap.put(name, bossEntity);
        return true;
    }

    public BossEntity getBossEntity(String name) {
        return this.bossEntityMap.getOrDefault(name, null);
    }
}
