package com.songoda.epicbosses.utils.dependencies;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.songoda.epicbosses.utils.IWorldGuardHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 16-Oct-18
 */
public class WorldGuardLegacyHelper implements IWorldGuardHelper {

    private WorldGuardPlugin worldGuard;

    @Override
    public boolean isPvpAllowed(Location loc) {
        if(Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            if(worldGuard == null) {
                this.worldGuard = WGBukkit.getPlugin();
            }
            ApplicableRegionSet applicableRegionSet = this.worldGuard.getRegionManager(loc.getWorld()).getApplicableRegions(loc);
            StateFlag.State state = applicableRegionSet.queryState(null, DefaultFlag.PVP);

            return state != StateFlag.State.DENY;
        }

        return true;
    }

    @Override
    public boolean isBreakAllowed(Location loc) {
        if(Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            if(worldGuard == null) {
                this.worldGuard = WGBukkit.getPlugin();
            }

            ApplicableRegionSet applicableRegionSet = this.worldGuard.getRegionManager(loc.getWorld()).getApplicableRegions(loc);
            StateFlag.State state = applicableRegionSet.queryState(null, DefaultFlag.BLOCK_BREAK);

            return state != StateFlag.State.DENY;
        }

        return true;
    }

    @Override
    public boolean isExplosionsAllowed(Location loc) {
        if(Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            if(worldGuard == null) {
                this.worldGuard = WGBukkit.getPlugin();
            }

            ApplicableRegionSet applicableRegionSet = this.worldGuard.getRegionManager(loc.getWorld()).getApplicableRegions(loc);
            StateFlag.State state = applicableRegionSet.queryState(null, DefaultFlag.OTHER_EXPLOSION);

            return state != StateFlag.State.DENY;
        }

        return true;
    }

    @Override
    public List<String> getRegionNames(Location loc) {
        if(Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            if(worldGuard == null) {
                this.worldGuard = WGBukkit.getPlugin();
            }

            List<String> regions = new ArrayList<>();
            List<String> parentNames = new ArrayList<>();
            // this prototype is different from v5 to v6, but they're both Iterable
            Iterable<ProtectedRegion> set = (Iterable<ProtectedRegion>) this.worldGuard.getRegionManager(loc.getWorld()).getApplicableRegions(loc);

            for (ProtectedRegion region : set) {
                String id = region.getId();

                regions.add(id);

                ProtectedRegion parent = region.getParent();

                while (parent != null) {
                    parentNames.add(parent.getId());
                    parent = parent.getParent();
                }
            }

            for (String name : parentNames) {
                regions.remove(name);
            }

            return regions;
        }

        return null;
    }

    @Override
    public boolean isMobSpawningAllowed(Location loc) {
        if(Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            if(worldGuard == null) {
                this.worldGuard = WGBukkit.getPlugin();
            }

            ApplicableRegionSet applicableRegionSet = this.worldGuard.getRegionManager(loc.getWorld()).getApplicableRegions(loc);
            StateFlag.State state = applicableRegionSet.queryState(null, DefaultFlag.MOB_SPAWNING);

            return state != StateFlag.State.DENY;
        }

        return true;
    }
}
