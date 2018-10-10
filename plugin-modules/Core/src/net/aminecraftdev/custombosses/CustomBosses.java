package net.aminecraftdev.custombosses;

import lombok.Getter;
import net.aminecraftdev.custombosses.api.BossAPI;
import net.aminecraftdev.custombosses.commands.BossCmd;
import net.aminecraftdev.custombosses.container.BossEntityContainer;
import net.aminecraftdev.custombosses.file.BossesFileHandler;
import net.aminecraftdev.custombosses.file.EditorFileHandler;
import net.aminecraftdev.custombosses.file.LangFileHandler;
import net.aminecraftdev.custombosses.managers.BossCommandManager;
import net.aminecraftdev.custombosses.managers.BossPanelManager;
import net.aminecraftdev.custombosses.managers.DebugManager;
import net.aminecraftdev.custombosses.managers.files.BossItemFileManager;
import net.aminecraftdev.custombosses.managers.BossMechanicManager;
import net.aminecraftdev.custombosses.managers.files.BossesFileManager;
import net.aminecraftdev.custombosses.utils.IReloadable;
import net.aminecraftdev.custombosses.utils.Message;
import net.aminecraftdev.custombosses.utils.command.SubCommandService;
import net.aminecraftdev.custombosses.utils.file.YmlFileHandler;
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
    @Getter private BossPanelManager bossPanelManager;
    @Getter private DebugManager debugManager;

    @Getter private YmlFileHandler langFileHandler, editorFileHandler;
    @Getter private FileConfiguration lang, editor;

    @Override
    public void onEnable() {
        long beginMs = System.currentTimeMillis();

        new BossAPI(this);
        new Metrics(this);

        this.debugManager = new DebugManager();
        this.bossEntityContainer = new BossEntityContainer();
        this.bossMechanicManager = new BossMechanicManager(this);

        loadFileManagersAndHandlers();

        this.bossPanelManager = new BossPanelManager(this);

        createFiles();
        reloadFiles();

        this.itemStackManager.reload();
        this.bossesFileManager.reload();
        this.bossCommandManager = new BossCommandManager(new BossCmd(), this);
        this.bossPanelManager.load();

        reload();
        saveMessagesToFile();

        this.bossCommandManager.load();

        System.out.println("Loaded all fields and managers, saved messages and plugin is initialized and ready to go. (took " + (System.currentTimeMillis() - beginMs) + "ms).");
    }

    @Override
    public void reload() {
        this.itemStackManager.reload();
        this.bossesFileManager.reload();
        this.bossMechanicManager.load();

        reloadFiles();

        this.bossPanelManager.reload();

        Message.setFile(getLang());
    }

    private void loadFileManagersAndHandlers() {
        this.itemStackManager = new BossItemFileManager(this);
        this.bossesFileManager = new BossesFileManager(this);

        this.langFileHandler = new LangFileHandler(this);
        this.editorFileHandler = new EditorFileHandler(this);
    }

    private void reloadFiles() {
        this.lang = this.langFileHandler.loadFile();
        this.editor = this.editorFileHandler.loadFile();
    }

    private void createFiles() {
        this.editorFileHandler.createFile();
        this.langFileHandler.createFile();
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
}
