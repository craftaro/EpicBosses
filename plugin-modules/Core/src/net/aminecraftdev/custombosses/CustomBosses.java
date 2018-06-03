package net.aminecraftdev.custombosses;

import lombok.Getter;
import net.aminecraftdev.custombosses.managers.ItemStackManager;
import net.aminecraftdev.custombosses.utils.IReloadable;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 06-Sep-17
 */
public class CustomBosses extends JavaPlugin implements IReloadable {

    @Getter private ItemStackManager itemStackManager;

    @Override
    public void onEnable() {
        this.itemStackManager = new ItemStackManager(this);

        reload();
    }


    @Override
    public void reload() {
        this.itemStackManager.reload();
    }
}
