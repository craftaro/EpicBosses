package com.songoda.epicbosses.managers;

import lombok.Getter;
import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.utils.IASkyblockHelper;
import com.songoda.epicbosses.utils.IFactionHelper;
import com.songoda.epicbosses.utils.IReloadable;
import com.songoda.epicbosses.utils.IWorldGuardHelper;
import com.songoda.epicbosses.utils.dependencies.ASkyblockHelper;
import utils.factions.FactionsM;
import utils.factions.FactionsOne;
import utils.factions.FactionsUUID;
import utils.factions.LegacyFactions;
import com.songoda.epicbosses.utils.dependencies.WorldGuardHelper;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 16-Oct-18
 */
public class BossHookManager implements IReloadable {

    @Getter private boolean askyblockEnabled, factionsEnabled, stackmobEnabled, worldguardEnabled;
    @Getter private List<String> worldGuardSpawnRegions, worldguardBlockedRegions;
    @Getter private boolean askyblockOwnIsland, factionWarzone;

    @Getter private IWorldGuardHelper worldGuardHelper;
    @Getter private IASkyblockHelper aSkyblockHelper;
    @Getter private IFactionHelper factionHelper;

    private final CustomBosses plugin;

    public BossHookManager(CustomBosses customBosses) {
        this.plugin = customBosses;
    }

    @Override
    public void reload() {
        FileConfiguration config = this.plugin.getConfig();
        ConfigurationSection askyblock = config.getConfigurationSection("Hooks.ASkyBlock");
        ConfigurationSection factions = config.getConfigurationSection("Hooks.Factions");
        ConfigurationSection stackMob = config.getConfigurationSection("Hooks.StackMob");
        ConfigurationSection worldGuard = config.getConfigurationSection("Hooks.WorldGuard");

        this.askyblockEnabled = askyblock.getBoolean("enabled", false);
        this.factionsEnabled = factions.getBoolean("enabled", false);
        this.stackmobEnabled = stackMob.getBoolean("enabled", false);
        this.worldguardEnabled = worldGuard.getBoolean("enabled", true);

        this.worldGuardSpawnRegions = worldGuard.getStringList("spawnRegions");
        this.worldguardBlockedRegions = worldGuard.getStringList("blockedRegions");

        this.askyblockOwnIsland = askyblock.getBoolean("onOwnIsland", false);
        this.factionWarzone = factions.getBoolean("useWarzoneSpawnRegion", false);

        setupFactions();
        setupWorldGuard();
        setupAskyblock();
    }

    private void setupAskyblock() {
        if(!isAskyblockEnabled()) return;

        this.aSkyblockHelper = new ASkyblockHelper();
    }

    private void setupWorldGuard() {
        if(!isWorldguardEnabled()) return;

        this.worldGuardHelper = new WorldGuardHelper();
    }

    private void setupFactions() {
        if(!isFactionsEnabled()) return;

        if(Bukkit.getServer().getPluginManager().getPlugin("LegacyFactions") != null) {
            this.factionHelper = new LegacyFactions();
        }

        if(Bukkit.getServer().getPluginManager().getPlugin("Factions") == null) return;

        Plugin factions = Bukkit.getServer().getPluginManager().getPlugin("Factions");
        String version = factions.getDescription().getVersion();
        String uuidVer = "1.6.";
        String oneVer = "1.8.";

        if(version.startsWith(uuidVer)) {
            this.factionHelper = new FactionsUUID();
        } else if(version.startsWith(oneVer)) {
            this.factionHelper = new FactionsOne();
        } else {
            this.factionHelper = new FactionsM();
        }
    }
}
