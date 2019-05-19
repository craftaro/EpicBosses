package com.songoda.epicbosses.managers;

import com.songoda.epicbosses.utils.ServerUtils;
import lombok.Getter;
import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.utils.IReloadable;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 16-Oct-18
 */
public class BossLocationManager implements IReloadable {

    @Getter private boolean useBlockedWorlds;
    @Getter private List<String> blockedWorlds;

    private final BossHookManager bossHookManager;
    private final CustomBosses plugin;

    public BossLocationManager(CustomBosses customBosses) {
        this.bossHookManager = customBosses.getBossHookManager();
        this.plugin = customBosses;
    }

    @Override
    public void reload() {
        FileConfiguration config = this.plugin.getConfig();

        this.useBlockedWorlds = config.getBoolean("Settings.BlockedWorlds.enabled");
        this.blockedWorlds = config.getStringList("Settings.BlockedWorlds.worlds");
    }

    public boolean canSpawnBoss(Player player, Location location) {
        for(int x = location.getBlockX() - 2; x <= location.getBlockX() + 2; x++) {
            for(int y = location.getBlockY(); y <= location.getBlockY() + 2; y++) {
                for(int z = location.getBlockZ() - 2; z <= location.getBlockZ() + 2; z++) {
                    Location l = new Location(location.getWorld(), x, y, z);
                    Block block = l.getBlock();

                    if(block.getType().isSolid()) {
                        ServerUtils.get().logDebug("Unable to spawn boss due to needing a 5x3x5 area to spawn");
                        return false;
                    }
                }
            }
        }

        if(isUseBlockedWorlds()) {
            if(getBlockedWorlds().contains(location.getWorld().getName())) {
                ServerUtils.get().logDebug("Unable to spawn boss due to world being in blocked worlds list");
                return false;
            }
        }

        if(this.bossHookManager.isWorldguardEnabled() && this.bossHookManager.getWorldGuardHelper() != null) {
            List<String> currentRegions = this.bossHookManager.getWorldGuardHelper().getRegionNames(location);
            boolean blocked = false;

            if(currentRegions != null) {
                for(String s : this.bossHookManager.getWorldguardBlockedRegions()) {
                    if(currentRegions.contains(s)) {
                        blocked = true;
                        break;
                    }
                }
            }

                if(blocked) {
                    ServerUtils.get().logDebug("Unable to spawn boss due to worldguard region being in blocked list");
                    return false;
                }
            }
        }

        if(this.bossHookManager.isFactionsEnabled() && this.bossHookManager.getFactionHelper() != null) {
            if(!this.bossHookManager.getFactionHelper().isInWarzone(location)) {
                ServerUtils.get().logDebug("Unable to spawn boss due to being outside a factions warzone");
                return false;
            }
        }

        if(this.bossHookManager.isWorldguardEnabled() && this.bossHookManager.getWorldGuardHelper() != null) {
            List<String> currentRegions = this.bossHookManager.getWorldGuardHelper().getRegionNames(location);
            boolean allowed = false;

            if(currentRegions != null) {
                for(String s : this.bossHookManager.getWorldGuardSpawnRegions()) {
                    if(currentRegions.contains(s)) {
                        allowed = true;
                        break;
                    }
                }

                if(!allowed) {
                    ServerUtils.get().logDebug("Unable to spawn boss due to worldguard region not being in the spawnable regions list");
                    return false;
                }
            }
        }

        if(this.bossHookManager.isAskyblockEnabled() && this.bossHookManager.getASkyblockHelper() != null) {
            if(this.bossHookManager.isAskyblockOwnIsland()) {
                boolean canSpawn = this.bossHookManager.getASkyblockHelper().isOnOwnIsland(player);
                if (!canSpawn)
                    ServerUtils.get().logDebug("Unable to spawn boss due to not being on own ASkyblock island");
                return canSpawn;
            }
        }

        return true;
    }

}
