package com.songoda.epicbosses;

import com.songoda.core.SongodaCore;
import com.songoda.core.SongodaPlugin;
import com.songoda.core.commands.CommandManager;
import com.songoda.core.compatibility.CompatibleMaterial;
import com.songoda.core.configuration.Config;
import com.songoda.core.hooks.EconomyManager;
import com.songoda.core.hooks.WorldGuardHook;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.commands.*;
import com.songoda.epicbosses.container.BossEntityContainer;
import com.songoda.epicbosses.container.MinionEntityContainer;
import com.songoda.epicbosses.file.DisplayFileHandler;
import com.songoda.epicbosses.file.EditorFileHandler;
import com.songoda.epicbosses.file.LangFileHandler;
import com.songoda.epicbosses.managers.*;
import com.songoda.epicbosses.managers.files.*;
import com.songoda.epicbosses.settings.Settings;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.IReloadable;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.file.YmlFileHandler;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.ArrayList;
import java.util.List;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 06-Sep-17
 */
public class EpicBosses extends SongodaPlugin implements IReloadable {

    private static EpicBosses INSTANCE;

    private MessagesFileManager bossMessagesFileManager;
    private CommandsFileManager bossCommandFileManager;
    private AutoSpawnFileManager autoSpawnFileManager;
    private DropTableFileManager dropTableFileManager;
    private MinionsFileManager minionsFileManager;
    private BossesFileManager bossesFileManager;
    private SkillsFileManager skillsFileManager;
    private ItemsFileManager itemStackManager;

    private BossDropTableManager bossDropTableManager;
    private BossEntityContainer bossEntityContainer;
    private BossMechanicManager bossMechanicManager;
    private BossLocationManager bossLocationManager;
    private BossListenerManager bossListenerManager;
    private BossEntityManager bossEntityManager;
    private BossTargetManager bossTargetManager;
    private BossPanelManager bossPanelManager;
    private BossSkillManager bossSkillManager;
    private BossTauntManager bossTauntManager;
    private BossHookManager bossHookManager;

    private AutoSpawnManager autoSpawnManager;
    private PlaceholderManager placeholderManager;

    private MinionMechanicManager minionMechanicManager;
    private MinionEntityContainer minionEntityContainer;

    private CommandManager commandManager;

    private DebugManager debugManager = new DebugManager();

    private YmlFileHandler langFileHandler, editorFileHandler, displayFileHandler;
    private FileConfiguration lang, editor, display;

    private boolean debug = true;

    public static EpicBosses getInstance() {
        return INSTANCE;
    }

    @Override
    public void onPluginLoad() {
        INSTANCE = this;

        // Register WorldGuard
        WorldGuardHook.addHook("boss-spawn-region", true);
        WorldGuardHook.addHook("boss-blocked-region", false);
    }

    @Override
    public void onPluginDisable() {
        this.autoSpawnManager.stopIntervalSystems();
        this.bossEntityManager.killAllHolders((World) null);
    }

    @Override
    public void onPluginEnable() {
        // Run Songoda Updater
        SongodaCore.registerPlugin(this, 19, CompatibleMaterial.ZOMBIE_SPAWN_EGG);

        // Load Economy
        EconomyManager.load();

        // Setup Config
        Settings.setupConfig();

        // Set economy preference
        EconomyManager.getManager().setPreferredHook(Settings.ECONOMY_PLUGIN.getString());

        if (!this.getDataFolder().exists())
            this.getDataFolder().mkdir();

        Debug.setPlugin(this);

        long beginMs = System.currentTimeMillis();

        new BossAPI(this);
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

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            this.placeholderManager = new PlaceholderManager(this);
            this.placeholderManager.register();
        }

        createFiles();
        reloadFiles();

        this.debug = getConfig().getBoolean("Settings.debug", false);

        this.itemStackManager.reload();
        this.bossesFileManager.reload();
        this.minionsFileManager.reload();
        this.skillsFileManager.reload();
        this.bossCommandFileManager.reload();
        this.bossMessagesFileManager.reload();
        this.dropTableFileManager.reload();
        this.autoSpawnFileManager.reload();

        // Register commands
        this.commandManager = new CommandManager(this);
        this.commandManager.addCommand(new CommandBoss())
                .addSubCommands(
                        new CommandCreate(bossEntityContainer),
                        new CommandDebug(debugManager),
                        new CommandDropTable(bossPanelManager),
                        new CommandEdit(bossPanelManager, bossEntityContainer),
                        new CommandGiveEgg(bossesFileManager, bossEntityManager),
                        new CommandInfo(bossesFileManager, bossEntityManager),
                        new CommandItems(bossPanelManager),
                        new CommandKillAll(bossEntityManager),
                        new CommandList(bossPanelManager),
                        new CommandMenu(bossPanelManager),
                        new CommandNearby(this),
                        new CommandNewSkill(skillsFileManager, bossSkillManager),
                        new CommandNewAutoSpawn(autoSpawnFileManager),
                        new CommandNewCommand(bossCommandFileManager),
                        new CommandNewMessage(bossCommandFileManager, bossMessagesFileManager),
                        new CommandNewDropTable(dropTableFileManager, bossDropTableManager),
                        new CommandReload(this, bossEntityManager),
                        new CommandShop(this),
                        new CommandSkills(bossPanelManager),
                        new CommandSpawn(bossesFileManager),
                        new CommandTime(this)
                );

        this.bossListenerManager = new BossListenerManager(this);

        this.bossPanelManager.load();

        //RELOAD/LOAD ALL MANAGERS
        this.bossSkillManager.load();
        this.bossHookManager.reload();
        this.bossLocationManager.reload();
        this.bossMechanicManager.load();
        this.minionMechanicManager.load();

        saveMessagesToFile();

        this.bossListenerManager.load();

        this.autoSpawnManager.startIntervalSystems();

        ServerUtils.get().logDebug("Loaded all fields and managers, saved messages and plugin is initialized and ready to go. (took " + (System.currentTimeMillis() - beginMs) + "ms).");
    }

    @Override
    public List<Config> getExtraConfig() {
        return new ArrayList<>();
    }

    @Override
    public void onConfigReload() {
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
        this.displayFileHandler = new DisplayFileHandler(this);
    }

    private void reloadFiles() {
        this.lang = this.langFileHandler.loadFile();
        this.editor = this.editorFileHandler.loadFile();
        this.display = this.displayFileHandler.loadFile();
    }

    private void createFiles() {
        this.editorFileHandler.createFile();
        this.langFileHandler.createFile();
        this.displayFileHandler.createFile();
    }

    private void saveMessagesToFile() {
        FileConfiguration lang = getLang();

        for (Message message : Message.values()) {
            if (!lang.contains(message.getPath())) {
                lang.set(message.getPath(), message.getDefault());
            }
        }

        this.langFileHandler.saveFile(lang);
        Message.setFile(lang);
    }

    public MessagesFileManager getBossMessagesFileManager() {
        return this.bossMessagesFileManager;
    }

    public CommandsFileManager getBossCommandFileManager() {
        return this.bossCommandFileManager;
    }

    public AutoSpawnFileManager getAutoSpawnFileManager() {
        return this.autoSpawnFileManager;
    }

    public DropTableFileManager getDropTableFileManager() {
        return this.dropTableFileManager;
    }

    public MinionsFileManager getMinionsFileManager() {
        return this.minionsFileManager;
    }

    public BossesFileManager getBossesFileManager() {
        return this.bossesFileManager;
    }

    public SkillsFileManager getSkillsFileManager() {
        return this.skillsFileManager;
    }

    public ItemsFileManager getItemStackManager() {
        return this.itemStackManager;
    }

    public BossDropTableManager getBossDropTableManager() {
        return this.bossDropTableManager;
    }

    public BossEntityContainer getBossEntityContainer() {
        return this.bossEntityContainer;
    }

    public BossMechanicManager getBossMechanicManager() {
        return this.bossMechanicManager;
    }

    public BossLocationManager getBossLocationManager() {
        return this.bossLocationManager;
    }

    public BossListenerManager getBossListenerManager() {
        return this.bossListenerManager;
    }

    public BossEntityManager getBossEntityManager() {
        return this.bossEntityManager;
    }

    public BossTargetManager getBossTargetManager() {
        return this.bossTargetManager;
    }

    public BossPanelManager getBossPanelManager() {
        return this.bossPanelManager;
    }

    public BossSkillManager getBossSkillManager() {
        return this.bossSkillManager;
    }

    public BossTauntManager getBossTauntManager() {
        return this.bossTauntManager;
    }

    public BossHookManager getBossHookManager() {
        return this.bossHookManager;
    }

    public AutoSpawnManager getAutoSpawnManager() {
        return this.autoSpawnManager;
    }

    public PlaceholderManager getPlaceholderManager() {
        return this.placeholderManager;
    }

    public MinionMechanicManager getMinionMechanicManager() {
        return this.minionMechanicManager;
    }

    public MinionEntityContainer getMinionEntityContainer() {
        return this.minionEntityContainer;
    }

    public CommandManager getCommandManager() {
        return commandManager;
    }

    public DebugManager getDebugManager() {
        return this.debugManager;
    }

    public YmlFileHandler getLangFileHandler() {
        return this.langFileHandler;
    }

    public YmlFileHandler getEditorFileHandler() {
        return this.editorFileHandler;
    }

    public YmlFileHandler getDisplayFileHandler() {
        return this.displayFileHandler;
    }

    public FileConfiguration getLang() {
        return this.lang;
    }

    public FileConfiguration getEditor() {
        return this.editor;
    }

    public FileConfiguration getDisplay() {
        return this.display;
    }

    public boolean isDebug() {
        return this.debug;
    }

    @Override
    public void reload() {
        reloadConfig();
    }
}
