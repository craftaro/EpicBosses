package net.aminecraftdev.custombosses;

import lombok.Getter;
import net.aminecraftdev.custombosses.api.BossAPI;
import net.aminecraftdev.custombosses.container.BossEntityContainer;
import net.aminecraftdev.custombosses.file.BossesFileHandler;
import net.aminecraftdev.custombosses.managers.files.BossItemFileManager;
import net.aminecraftdev.custombosses.managers.BossMechanicManager;
import net.aminecraftdev.custombosses.managers.files.BossesFileManager;
import net.aminecraftdev.custombosses.utils.IReloadable;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 06-Sep-17
 */
public class CustomBosses extends JavaPlugin implements IReloadable {

    @Getter private BossEntityContainer bossEntityContainer;
    @Getter private BossMechanicManager bossMechanicManager;
    @Getter private BossItemFileManager itemStackManager;
    @Getter private BossesFileManager bossesFileManager;

    @Override
    public void onEnable() {
        final long startMs = System.currentTimeMillis();
        long beginMs = System.currentTimeMillis();

        new BossAPI(this);
        new Metrics(this);

        System.out.println("Boss API loaded (took " + (System.currentTimeMillis() - beginMs) + "ms)");
        beginMs = System.currentTimeMillis();

        this.bossEntityContainer = new BossEntityContainer();
        this.itemStackManager = new BossItemFileManager(this);
        this.bossesFileManager = new BossesFileManager(this);
        this.bossMechanicManager = new BossMechanicManager(this);

        System.out.println("Managers and Containers loaded (took " + (System.currentTimeMillis() - beginMs) + "ms)");
        beginMs = System.currentTimeMillis();

        reload();

        System.out.println("Reloaded all fields (took " + (System.currentTimeMillis() - beginMs) + "ms) and plugin is now loaded. Took a total of " + (System.currentTimeMillis() - startMs) + "ms.");
    }


    @Override
    public void reload() {
        this.itemStackManager.reload();
        this.bossesFileManager.reload();
        this.bossMechanicManager.load();
    }
}
