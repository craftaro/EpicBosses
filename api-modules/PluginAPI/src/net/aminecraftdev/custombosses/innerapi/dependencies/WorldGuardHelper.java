package net.aminecraftdev.custombosses.innerapi.dependencies;

import com.sk89q.worldguard.bukkit.WGBukkit;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.flags.DefaultFlag;
import com.sk89q.worldguard.protection.flags.StateFlag;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.LinkedList;
import java.util.List;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 25-May-17
 */
public class WorldGuardHelper {

    private static Boolean isLoaded = null;

    private static boolean isPluginLoaded() {
        return isLoaded != null? isLoaded : (isLoaded = Bukkit.getServer().getPluginManager().getPlugin("WorldEdit") != null
                && Bukkit.getServer().getPluginManager().getPlugin("WorldGuard") != null);
    }

    public static boolean isPvPAllowed(Location loc) {
        if(isPluginLoaded()) {
            ApplicableRegionSet set = WGBukkit.getPlugin().getRegionManager(loc.getWorld()).getApplicableRegions(loc);

            if(set.queryState(null, DefaultFlag.PVP) == StateFlag.State.DENY) return false;
        }

        return true;
    }

    public static boolean isBreakAllowed(Location loc) {
        if(isPluginLoaded()) {
            ApplicableRegionSet set = WGBukkit.getPlugin().getRegionManager(loc.getWorld()).getApplicableRegions(loc);

            if(set.queryState(null, DefaultFlag.BLOCK_BREAK) == StateFlag.State.DENY) return false;
        }

        return true;
    }

    public static boolean allowsExplosions(Location loc) {
        if(isPluginLoaded()) {
            ApplicableRegionSet set = WGBukkit.getPlugin().getRegionManager(loc.getWorld()).getApplicableRegions(loc);

            if(set.queryState(null, DefaultFlag.OTHER_EXPLOSION) == StateFlag.State.DENY) return false;
        }

        return true;
    }

    public static List<String> getRegionNames(Location loc) {
        if(isPluginLoaded()) {
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

    public static boolean isMobSpawningAllowed(Location loc) {
        if(isPluginLoaded()) {
            ApplicableRegionSet set = WGBukkit.getPlugin().getRegionManager(loc.getWorld()).getApplicableRegions(loc);

            if(set.queryState(null, DefaultFlag.MOB_SPAWNING) == StateFlag.State.DENY) return false;
        }

        return true;
    }

}
