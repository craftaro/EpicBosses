package com.songoda.epicbosses;

import com.songoda.epicbosses.container.MinionEntityContainer;
import com.songoda.epicbosses.utils.*;
import com.songoda.epicbosses.utils.dependencies.HolographicDisplayHelper;
import com.songoda.epicbosses.utils.dependencies.VaultHelper;
import lombok.Getter;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.commands.BossCmd;
import com.songoda.epicbosses.container.BossEntityContainer;
import com.songoda.epicbosses.file.ConfigFileHandler;
import com.songoda.epicbosses.file.EditorFileHandler;
import com.songoda.epicbosses.file.LangFileHandler;
import com.songoda.epicbosses.managers.*;
import com.songoda.epicbosses.managers.files.*;
import com.songoda.epicbosses.utils.file.YmlFileHandler;
import com.songoda.epicbosses.utils.version.VersionHandler;
import org.bstats.bukkit.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 06-Sep-17
 */
public class CustomBosses extends JavaPlugin implements IReloadable {

    private static CustomBosses instance;

    @Getter private MessagesFileManager bossMessagesFileManager;
    @Getter private CommandsFileManager bossCommandFileManager;
    @Getter private AutoSpawnFileManager autoSpawnFileManager;
    @Getter private DropTableFileManager dropTableFileManager;
    @Getter private MinionsFileManager minionsFileManager;
    @Getter private BossesFileManager bossesFileManager;
    @Getter private SkillsFileManager skillsFileManager;
    @Getter private ItemsFileManager itemStackManager;

    @Getter private BossDropTableManager bossDropTableManager;
    @Getter private BossEntityContainer bossEntityContainer;
    @Getter private BossMechanicManager bossMechanicManager;
    @Getter private BossLocationManager bossLocationManager;
    @Getter private BossListenerManager bossListenerManager;
    @Getter private BossCommandManager bossCommandManager;
    @Getter private BossEntityManager bossEntityManager;
    @Getter private BossTargetManager bossTargetManager;
    @Getter private BossPanelManager bossPanelManager;
    @Getter private BossSkillManager bossSkillManager;
    @Getter private BossTauntManager bossTauntManager;
    @Getter private BossHookManager bossHookManager;

    @Getter private AutoSpawnManager autoSpawnManager;

    @Getter private MinionMechanicManager minionMechanicManager;
    @Getter private MinionEntityContainer minionEntityContainer;

    @Getter private VersionHandler versionHandler = new VersionHandler();
    @Getter private DebugManager debugManager = new DebugManager();

    @Getter private YmlFileHandler langFileHandler, editorFileHandler, configFileHandler;
    @Getter private FileConfiguration lang, editor, config;

    @Getter private HolographicDisplayHelper holographicDisplayHelper;
    @Getter private VaultHelper vaultHelper;

    @Getter private boolean debug = false;

    @Override
    public void onDisable() {
        ConsoleCommandSender console = Bukkit.getConsoleSender();

        console.sendMessage(StringUtils.get().translateColor("&a============================="));
        console.sendMessage(StringUtils.get().translateColor("&7EpicBosses " + getDescription().getVersion() + " by &5Songoda <3&7!"));
        console.sendMessage(StringUtils.get().translateColor("&7Action: &aDisabling&7..."));
        console.sendMessage(StringUtils.get().translateColor("&a============================="));

        this.autoSpawnManager.stopIntervalSystems();
        this.bossEntityManager.killAllHolders((World) null);
    }

    @Override
    public void onEnable() {
        ConsoleCommandSender console = Bukkit.getConsoleSender();

        console.sendMessage(StringUtils.get().translateColor("&a============================="));
        console.sendMessage(StringUtils.get().translateColor("&7EpicBosses " + getDescription().getVersion() + " by &5Songoda <3&7!"));
        console.sendMessage(StringUtils.get().translateColor("&7Action: &aEnabling&7..."));
        console.sendMessage(StringUtils.get().translateColor("&a============================="));

        Debug.setPlugin(this);

        instance = this;

        this.vaultHelper = new VaultHelper();
        this.holographicDisplayHelper = new HolographicDisplayHelper();

        long beginMs = System.currentTimeMillis();

        if(!this.vaultHelper.isConnected()) {
            Debug.FAILED_TO_CONNECT_TO_VAULT.debug();
            return;
        }

        new BossAPI(this);
        new Metrics(this);
        new ServerUtils(this);

        this.bossSkillManager = new BossSkillManager(this);
        this.bossHookManager = new BossHookManager(this);
        this.bossTauntManager = new BossTauntManager(this);
        this.bossTargetManager = new BossTargetManager(this);
        this.bossEntityContainer = new BossEntityContainer();
        this.minionEntityContainer = new MinionEntityContainer();
        this.bossMechanicManager = new BossMechanicManager(this);
        this.minionMechanicManager = new MinionMechanicManager(this);
        this.bossLocationManager = new BossLocationManager(this);

        loadFileManagersAndHandlers();

        //Managers that rely on Files
        this.bossDropTableManager = new BossDropTableManager(this);
        this.bossPanelManager = new BossPanelManager(this);
        this.bossEntityManager = new BossEntityManager(this);

        this.autoSpawnManager = new AutoSpawnManager(this);

        createFiles();
        reloadFiles();

        this.itemStackManager.reload();
        this.bossesFileManager.reload();
        this.minionsFileManager.reload();
        this.skillsFileManager.reload();
        this.bossCommandFileManager.reload();
        this.bossMessagesFileManager.reload();
        this.dropTableFileManager.reload();
        this.autoSpawnFileManager.reload();

        this.bossCommandManager = new BossCommandManager(new BossCmd(), this);
        this.bossListenerManager = new BossListenerManager(this);

        this.bossPanelManager.load();

        //RELOAD/LOAD ALL MANAGERS
        this.bossSkillManager.load();
        this.bossHookManager.reload();
        this.bossLocationManager.reload();
        this.bossMechanicManager.load();
        this.minionMechanicManager.load();

        saveMessagesToFile();

        this.bossCommandManager.load();
        this.bossListenerManager.load();

        this.autoSpawnManager.startIntervalSystems();

        ServerUtils.get().logDebug("Loaded all fields and managers, saved messages and plugin is initialized and ready to go. (took " + (System.currentTimeMillis() - beginMs) + "ms).");
    }

    @Override
    public void reload() {
        this.itemStackManager.reload();
        this.bossesFileManager.reload();
        this.minionsFileManager.reload();
        this.skillsFileManager.reload();
        this.bossCommandFileManager.reload();
        this.bossMessagesFileManager.reload();
        this.dropTableFileManager.reload();
        this.autoSpawnFileManager.reload();

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
        this.minionsFileManager = new MinionsFileManager(this);
        this.bossCommandFileManager = new CommandsFileManager(this);
        this.bossMessagesFileManager = new MessagesFileManager(this);
        this.dropTableFileManager = new DropTableFileManager(this);
        this.skillsFileManager = new SkillsFileManager(this);
        this.autoSpawnFileManager = new AutoSpawnFileManager(this);

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

    public static CustomBosses get() {
        return instance;
    }
}
