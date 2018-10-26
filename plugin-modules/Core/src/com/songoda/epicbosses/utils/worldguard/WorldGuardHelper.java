package com.songoda.epicbosses.utils.worldguard;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.songoda.epicbosses.utils.IWorldGuardHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 16-Oct-18
 */
public class WorldGuardHelper implements IWorldGuardHelper {

    @Override
    public boolean isPvpAllowed(Location loc) {
        if(Bukkit.getServer().getPluginManager().getPlugin("WorldEdit") != null
                && Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            ApplicableRegionSet set = WGBukkit.getPlugin().getRegionManager(loc.getWorld()).getApplicableRegions(loc);

            if(set.queryState(null, DefaultFlag.PVP) == StateFlag.State.DENY) return false;
        }

        return true;
    }

    @Override
    public boolean isBreakAllowed(Location loc) {
        if(Bukkit.getServer().getPluginManager().getPlugin("WorldEdit") != null
                && Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            ApplicableRegionSet set = WGBukkit.getPlugin().getRegionManager(loc.getWorld()).getApplicableRegions(loc);

            if(set.queryState(null, DefaultFlag.BLOCK_BREAK) == StateFlag.State.DENY) return false;
        }

        return true;
    }

    @Override
    public boolean isExplosionsAllowed(Location loc) {
        if(Bukkit.getServer().getPluginManager().getPlugin("WorldEdit") != null
                && Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            ApplicableRegionSet set = WGBukkit.getPlugin().getRegionManager(loc.getWorld()).getApplicableRegions(loc);

            if(set.queryState(null, DefaultFlag.OTHER_EXPLOSION) == StateFlag.State.DENY) return false;
        }

        return true;
    }

    @Override
    public List<String> getRegionNames(Location loc) {
        if(Bukkit.getServer().getPluginManager().getPlugin("WorldEdit") != null
                && Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null) {

            ApplicableRegionSet set = WGBukkit.getPlugin().getRegionManager(loc.getWorld()).getApplicableRegions(loc);
            LinkedList<String> parentNames = new LinkedList<String>();
            LinkedList<String> regions = new LinkedList<String>();

            for(ProtectedRegion region : set) {
                String id = region.getId();

                regions.add(id);

                ProtectedRegion parent = region.getParent();

                while(parent != null) {
                    parentNames.add(parent.getId());
                    parent = parent.getParent();
                }
            }

            for(String name : parentNames) {
                regions.remove(name);
            }

            return regions;
        }

        return null;
    }

    @Override
    public boolean isMobSpawningAllowed(Location loc) {
        if(Bukkit.getServer().getPluginManager().getPlugin("WorldEdit") != null
                && Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            ApplicableRegionSet set = WGBukkit.getPlugin().getRegionManager(loc.getWorld()).getApplicableRegions(loc);

            if(set.queryState(null, DefaultFlag.MOB_SPAWNING) == StateFlag.State.DENY) return false;
        }

        return true;
    }
}
