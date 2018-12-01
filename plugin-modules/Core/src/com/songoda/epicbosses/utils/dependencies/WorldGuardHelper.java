package com.songoda.epicbosses.utils.dependencies;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldedit.world.World;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.bukkit.BukkitUtil;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.Flags;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import com.songoda.epicbosses.utils.IWorldGuardHelper;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 16-Oct-18
 */
public class WorldGuardHelper implements IWorldGuardHelper {

    private WorldGuard worldGuard;

    @Override
    public boolean isPvpAllowed(Location loc) {
        if(Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            if(worldGuard == null) {
                this.worldGuard = WorldGuard.getInstance();
            }

            RegionQuery regionQuery = this.worldGuard.getPlatform().getRegionContainer().createQuery();
            ApplicableRegionSet applicableRegionSet = regionQuery.getApplicableRegions(BukkitAdapter.adapt(loc));
            StateFlag.State state = applicableRegionSet.queryState(null, Flags.PVP);

            return state != StateFlag.State.DENY;
        }

        return true;
    }

    @Override
    public boolean isBreakAllowed(Location loc) {
        if(Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            if(worldGuard == null) {
                this.worldGuard = WorldGuard.getInstance();
            }

            RegionQuery regionQuery = this.worldGuard.getPlatform().getRegionContainer().createQuery();
            ApplicableRegionSet applicableRegionSet = regionQuery.getApplicableRegions(BukkitAdapter.adapt(loc));
            StateFlag.State state = applicableRegionSet.queryState(null, Flags.BLOCK_BREAK);

            return state != StateFlag.State.DENY;
        }

        return true;
    }

    @Override
    public boolean isExplosionsAllowed(Location loc) {
        if(Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            if(worldGuard == null) {
                this.worldGuard = WorldGuard.getInstance();
            }

            RegionQuery regionQuery = this.worldGuard.getPlatform().getRegionContainer().createQuery();
            ApplicableRegionSet applicableRegionSet = regionQuery.getApplicableRegions(BukkitAdapter.adapt(loc));
            StateFlag.State state = applicableRegionSet.queryState(null, Flags.OTHER_EXPLOSION);

            return state != StateFlag.State.DENY;
        }

        return true;
    }

    @Override
    public List<String> getRegionNames(Location loc) {
        if(Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null) {
            if (worldGuard == null) {
                this.worldGuard = WorldGuard.getInstance();
            }

            List<String> regions = new ArrayList<>();
            List<String> parentNames = new ArrayList<>();
            World world = BukkitAdapter.adapt(loc.getWorld());
            RegionManager regionManager = this.worldGuard.getPlatform().getRegionContainer().get(world);

            if (regionManager == null) return null;

            ApplicableRegionSet set = regionManager.getApplicableRegions(BukkitAdapter.asVector(loc));

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
//        if(Bukkit.getServer().getPluginManager().getPlugin("WorldEdit") != null
//                && Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null) {
//            ApplicableRegionSet set = WGBukkit.getPlugin().getRegionManager(loc.getWorld()).getApplicableRegions(loc);
//
//            if(set.queryState(null, DefaultFlag.MOB_SPAWNING) == StateFlag.State.DENY) return false;
//        }

        return true;
    }
}
