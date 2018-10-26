package com.songoda.epicbosses;

import lombok.Getter;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.commands.BossCmd;
import com.songoda.epicbosses.container.BossEntityContainer;
import com.songoda.epicbosses.file.ConfigFileHandler;
import com.songoda.epicbosses.file.EditorFileHandler;
import com.songoda.epicbosses.file.LangFileHandler;
import com.songoda.epicbosses.managers.*;
import com.songoda.epicbosses.managers.files.*;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.IReloadable;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.file.YmlFileHandler;
import com.songoda.epicbosses.utils.version.VersionHandler;
import org.bstats.bukkit.Metrics;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 06-Sep-17
 *
 * TODO: In menu when toggling Edit mode, make sure that it has all needed mechanics
 */
public class CustomBosses extends JavaPlugin implements IReloadable {

    @Getter private MessagesFileManager bossMessagesFileManager;
    @Getter private CommandsFileManager bossCommandFileManager;
    @Getter private DropTableFileManager dropTableFileManager;
    @Getter private BossesFileManager bossesFileManager;
    @Getter private ItemsFileManager itemStackManager;

    @Getter private BossEntityContainer bossEntityContainer;
    @Getter private BossMechanicManager bossMechanicManager;
    @Getter private BossLocationManager bossLocationManager;
    @Getter private BossListenerManager bossListenerManager;
    @Getter private BossCommandManager bossCommandManager;
    @Getter private BossEntityManager bossEntityManager;
    @Getter private BossPanelManager bossPanelManager;
    @Getter private BossHookManager bossHookManager;
    @Getter private VersionHandler versionHandler;
    @Getter private DebugManager debugManager;

    @Getter private YmlFileHandler langFileHandler, editorFileHandler, configFileHandler;
    @Getter private FileConfiguration lang, editor, config;

    @Getter private boolean debug = false;

    @Override
    public void onEnable() {
        long beginMs = System.currentTimeMillis();

        Debug.setPlugin(this);
        new BossAPI(this);
        new Metrics(this);
        new ServerUtils(this);

        this.debugManager = new DebugManager();
        this.versionHandler = new VersionHandler();
        this.bossEntityContainer = new BossEntityContainer();
        this.bossMechanicManager = new BossMechanicManager(this);
        this.bossHookManager = new BossHookManager(this);
        this.bossLocationManager = new BossLocationManager(this);

        loadFileManagersAndHandlers();

        //Managers that rely on Files
        this.bossPanelManager = new BossPanelManager(this);
        this.bossEntityManager = new BossEntityManager(this);

        createFiles();
        reloadFiles();

        this.itemStackManager.reload();
        this.bossesFileManager.reload();
        this.bossCommandFileManager.reload();
        this.bossMessagesFileManager.reload();
        this.dropTableFileManager.reload();

        this.bossCommandManager = new BossCommandManager(new BossCmd(), this);
        this.bossListenerManager = new BossListenerManager(this);

        this.bossPanelManager.load();

        //RELOAD/LOAD ALL MANAGERS
        this.bossHookManager.reload();
        this.bossLocationManager.reload();
        this.bossMechanicManager.load();

        saveMessagesToFile();

        this.bossCommandManager.load();
        this.bossListenerManager.load();

        ServerUtils.get().logDebug("Loaded all fields and managers, saved messages and plugin is initialized and ready to go. (took " + (System.currentTimeMillis() - beginMs) + "ms).");
    }

    @Override
    public void reload() {
        this.bossMessagesFileManager.reload();
        this.bossCommandFileManager.reload();
        this.bossesFileManager.reload();
        this.itemStackManager.reload();
        this.dropTableFileManager.reload();

        this.bossMechanicManager.load();

        reloadFiles();

        this.bossPanelManager.reload();
        this.bossHookManager.reload();
        this.bossLocationManager.reload();
        this.debug = getConfig().getBoolean("Settings.debug", false);

        Message.setFile(getLang());
    }

    private void loadFileManagersAndHandlers() {
        this.itemStackManager = new ItemsFileManager(this);
        this.bossesFileManager = new BossesFileManager(this);
        this.bossCommandFileManager = new CommandsFileManager(this);
        this.bossMessagesFileManager = new MessagesFileManager(this);
        this.dropTableFileManager = new DropTableFileManager(this);

        this.langFileHandler = new LangFileHandler(this);
        this.editorFileHandler = new EditorFileHandler(this);
        this.configFileHandler = new ConfigFileHandler(this);
    }

    private void reloadFiles() {
        this.lang = this.langFileHandler.loadFile();
        this.editor = this.editorFileHandler.loadFile();
        this.config = this.configFileHandler.loadFile();
    }

    private void createFiles() {
        this.editorFileHandler.createFile();
        this.langFileHandler.createFile();
        this.configFileHandler.createFile();
    }

    private void saveMessagesToFile() {
        FileConfiguration lang = getLang();

        for(Message message : Message.values()) {
            if(!lang.contains(message.getPath())) {
                lang.set(message.getPath(), message.getDefault());
            }
        }

        this.langFileHandler.saveFile(lang);
        Message.setFile(lang);
    }
}
