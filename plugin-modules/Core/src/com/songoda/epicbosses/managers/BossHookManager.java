package com.songoda.epicbosses.managers;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.utils.IASkyblockHelper;
import com.songoda.epicbosses.utils.IFactionHelper;
import com.songoda.epicbosses.utils.dependencies.ASkyblockHelper;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import utils.factions.FactionsM;
import utils.factions.FactionsOne;
import utils.factions.FactionsUUID;
import utils.factions.LegacyFactions;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 16-Oct-18
 */
public class BossHookManager {

    private boolean askyblockEnabled, factionsEnabled;
    private boolean askyblockOwnIsland, factionWarzone;

    private IASkyblockHelper aSkyblockHelper;
    private IFactionHelper factionHelper;

    private final EpicBosses plugin;

    public BossHookManager(EpicBosses epicBosses) {
        this.plugin = epicBosses;
    }

    public void reload() {
        FileConfiguration config = this.plugin.getConfig();
        ConfigurationSection askyblock = config.getConfigurationSection("Hooks.ASkyBlock");
        ConfigurationSection factions = config.getConfigurationSection("Hooks.Factions");

        this.askyblockEnabled = askyblock.getBoolean("enabled", false);
        this.factionsEnabled = factions.getBoolean("enabled", false);

        this.askyblockOwnIsland = askyblock.getBoolean("onOwnIsland", false);
        this.factionWarzone = factions.getBoolean("useWarzoneSpawnRegion", false);

        setupFactions();
        setupAskyblock();
    }

    private void setupAskyblock() {
        if (!isAskyblockEnabled()) return;

        this.aSkyblockHelper = new ASkyblockHelper();
    }

    private void setupFactions() {
        if (!isFactionsEnabled()) return;

        if (Bukkit.getServer().getPluginManager().getPlugin("LegacyFactions") != null) {
            this.factionHelper = new LegacyFactions();
        }

        if (Bukkit.getServer().getPluginManager().getPlugin("Factions") == null) return;

        Plugin factions = Bukkit.getServer().getPluginManager().getPlugin("Factions");
        String version = factions.getDescription().getVersion();
        String uuidVer = "1.6.";
        String oneVer = "1.8.";

        if (version.startsWith(uuidVer)) {
            this.factionHelper = new FactionsUUID();
        } else if (version.startsWith(oneVer)) {
            this.factionHelper = new FactionsOne();
        } else {
            this.factionHelper = new FactionsM();
        }
    }

    public boolean isAskyblockEnabled() {
        return this.askyblockEnabled;
    }

    public boolean isFactionsEnabled() {
        return this.factionsEnabled;
    }

    public boolean isAskyblockOwnIsland() {
        return this.askyblockOwnIsland;
    }

    //ToDo: This isnt even used 0.o
    public boolean isFactionWarzone() {
        return this.factionWarzone;
    }

    public IFactionHelper getFactionHelper() {
        return factionHelper;
    }

    public IASkyblockHelper getASkyblockHelper() {
        return this.aSkyblockHelper;
    }

}
