package net.aminecraftdev.custombosses;

import lombok.Getter;
import net.aminecraftdev.custombosses.api.BossAPI;
import net.aminecraftdev.custombosses.commands.BossCmd;
import net.aminecraftdev.custombosses.container.BossEntityContainer;
import net.aminecraftdev.custombosses.file.BossesFileHandler;
import net.aminecraftdev.custombosses.file.LangFileHandler;
import net.aminecraftdev.custombosses.managers.BossCommandManager;
import net.aminecraftdev.custombosses.managers.files.BossItemFileManager;
import net.aminecraftdev.custombosses.managers.BossMechanicManager;
import net.aminecraftdev.custombosses.managers.files.BossesFileManager;
import net.aminecraftdev.custombosses.utils.IReloadable;
import net.aminecraftdev.custombosses.utils.Message;
import net.aminecraftdev.custombosses.utils.command.SubCommandService;
import org.bstats.bukkit.Metrics;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 06-Sep-17
 */
public class CustomBosses extends JavaPlugin implements IReloadable {

    @Getter private BossEntityContainer bossEntityContainer;
    @Getter private BossMechanicManager bossMechanicManager;
    @Getter private BossCommandManager bossCommandManager;
    @Getter private BossItemFileManager itemStackManager;
    @Getter private BossesFileManager bossesFileManager;

    @Getter private LangFileHandler langFileHandler;
    @Getter private FileConfiguration lang;

    @Override
    public void onEnable() {
        final long startMs = System.currentTimeMillis();
        long beginMs = System.currentTimeMillis();

        new BossAPI(this);
        new Metrics(this);

        System.out.println("Boss API loaded (took " + (System.currentTimeMillis() - beginMs) + "ms)");
        beginMs = System.currentTimeMillis();


        this.bossEntityContainer = new BossEntityContainer();
        this.bossMechanicManager = new BossMechanicManager(this);

        System.out.println("Managers and Containers loaded (took " + (System.currentTimeMillis() - beginMs) + "ms)");
        beginMs = System.currentTimeMillis();

        loadFileManagersAndHandlers();

        System.out.println("File Handlers and File Managers loaded (took " + (System.currentTimeMillis() - beginMs) + "ms)");
        beginMs = System.currentTimeMillis();

        this.bossCommandManager = new BossCommandManager(new BossCmd(), this);
        this.bossCommandManager.load();

        System.out.println("All commands and listeners loaded (took " + (System.currentTimeMillis() - beginMs) + "ms)");
        beginMs = System.currentTimeMillis();

        reload();
        saveMessagesToFile();

        System.out.println("Reloaded all fields, saved messages (took " + (System.currentTimeMillis() - beginMs) + "ms) and plugin is now loaded. Took a total of " + (System.currentTimeMillis() - startMs) + "ms.");
    }

    @Override
    public void reload() {
        this.itemStackManager.reload();
        this.bossesFileManager.reload();
        this.bossMechanicManager.load();

        this.lang = this.langFileHandler.loadFile();
        Message.setFile(getLang());
    }

    private void saveMessagesToFile() {
        FileConfiguration lang = getLang();

        for(Message message : Message.values()) {
            if(!lang.contains(message.getPath())) {
                lang.set(message.getPath(), message.getDefault());
            }
        }

        this.langFileHandler.saveFile(lang);
    }

    private void loadFileManagersAndHandlers() {
        this.itemStackManager = new BossItemFileManager(this);
        this.bossesFileManager = new BossesFileManager(this);

        this.langFileHandler = new LangFileHandler(this);
    }
}
