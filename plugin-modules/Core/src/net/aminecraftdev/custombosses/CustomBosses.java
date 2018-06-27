package net.aminecraftdev.custombosses;

import lombok.Getter;
import net.aminecraftdev.custombosses.managers.BossItemFileManager;
import net.aminecraftdev.custombosses.managers.BossMechanicManager;
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

    @Override
    public void onEnable() {
        this.itemStackManager = new BossItemFileManager(this);

        reload();

        this.bossMechanicManager.load();
    }


    @Override
    public void reload() {
        this.itemStackManager.reload();
    }
}
