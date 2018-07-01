package net.aminecraftdev.custombosses;

import lombok.Getter;
import net.aminecraftdev.custombosses.api.BossAPI;
import net.aminecraftdev.custombosses.file.BossesFileHandler;
import net.aminecraftdev.custombosses.managers.files.BossItemFileManager;
import net.aminecraftdev.custombosses.managers.BossMechanicManager;
import net.aminecraftdev.custombosses.managers.files.BossesFileManager;
import net.aminecraftdev.custombosses.utils.IReloadable;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 06-Sep-17
 */
public class CustomBosses extends JavaPlugin implements IReloadable {

    @Getter private BossMechanicManager bossMechanicManager;
    @Getter private BossItemFileManager itemStackManager;
    @Getter private BossesFileManager bossesFileManager;

    @Override
    public void onEnable() {
        BossAPI.setPlugin(this);

        this.itemStackManager = new BossItemFileManager(this);
        this.bossesFileManager = new BossesFileManager(this);
        this.bossMechanicManager = new BossMechanicManager(this);

        reload();
    }


    @Override
    public void reload() {
        this.itemStackManager.reload();
        this.bossesFileManager.reload();
        this.bossMechanicManager.load();
    }
}
