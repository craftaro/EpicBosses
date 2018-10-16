package net.aminecraftdev.custombosses.managers;

import lombok.Getter;
import net.aminecraftdev.custombosses.CustomBosses;
import net.aminecraftdev.custombosses.utils.IASkyblockHelper;
import net.aminecraftdev.custombosses.utils.IFactionHelper;
import net.aminecraftdev.custombosses.utils.IReloadable;
import net.aminecraftdev.custombosses.utils.IWorldGuardHelper;
import net.aminecraftdev.custombosses.utils.askyblock.ASkyblockHelper;
import net.aminecraftdev.custombosses.utils.factions.FactionsM;
import net.aminecraftdev.custombosses.utils.factions.FactionsOne;
import net.aminecraftdev.custombosses.utils.factions.FactionsUUID;
import net.aminecraftdev.custombosses.utils.factions.LegacyFactions;
import net.aminecraftdev.custombosses.utils.worldguard.WorldGuardHelper;
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

    @Getter private boolean askyblockEnabled, factionsEnabled, stackmobEnabled, worldguardEnabld;
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
        this.worldguardEnabld = worldGuard.getBoolean("enabled", true);

        this.worldGuardSpawnRegions = worldGuard.getStringList("spawnRegions");
        this.worldguardBlockedRegions = worldGuard.getStringList("blockedRegions");

        this.askyblockOwnIsland = askyblock.getBoolean("onOwnIsland", false);
        this.factionWarzone = factions.getBoolean("useWarzoneSpawnRegion", false);

        setupFactions();
        setupWorldGuard();
    }

    private void setupAskyblock() {
        if(!isAskyblockEnabled()) return;

        this.aSkyblockHelper = new ASkyblockHelper();
    }

    private void setupWorldGuard() {
        if(!isWorldguardEnabld()) return;

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
