package net.aminecraftdev.custombosses.managers;

import lombok.Getter;
import net.aminecraftdev.custombosses.CustomBosses;
import net.aminecraftdev.custombosses.utils.IReloadable;
import org.bukkit.Location;
import org.bukkit.Material;
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

                    if(block.getType().isSolid()) return false;
                }
            }
        }

        if(isUseBlockedWorlds()) {
            if(getBlockedWorlds().contains(location.getWorld().getName())) return false;
        }

        if(this.bossHookManager.isWorldguardEnabld() && this.bossHookManager.getWorldGuardHelper() != null) {
            List<String> currentRegions = this.bossHookManager.getWorldGuardHelper().getRegionNames(location);
            boolean blocked = false;

            if(currentRegions != null) {
                for(String s : this.bossHookManager.getWorldguardBlockedRegions()) {
                    if(currentRegions.contains(s)) {
                        blocked = true;
                        break;
                    }
                }

                if(blocked) return false;
            }
        }

        if(this.bossHookManager.isFactionsEnabled() && this.bossHookManager.getFactionHelper() != null) {
            if(!this.bossHookManager.getFactionHelper().isInWarzone(location)) return false;
        }

        if(this.bossHookManager.isWorldguardEnabld() && this.bossHookManager.getWorldGuardHelper() != null) {
            List<String> currentRegions = this.bossHookManager.getWorldGuardHelper().getRegionNames(location);
            boolean allowed = false;

            if(currentRegions != null) {
                for(String s : this.bossHookManager.getWorldGuardSpawnRegions()) {
                    if(currentRegions.contains(s)) {
                        allowed = true;
                        break;
                    }
                }

                if(!allowed) return false;
            }
        }

        if(this.bossHookManager.isAskyblockEnabled() && this.bossHookManager.getASkyblockHelper() != null) {
            if(this.bossHookManager.isAskyblockOwnIsland()) {
                return this.bossHookManager.getASkyblockHelper().isOnOwnIsland(player);
            }
        }

        return true;
    }

}
