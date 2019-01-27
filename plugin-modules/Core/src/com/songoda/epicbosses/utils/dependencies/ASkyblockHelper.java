package com.songoda.epicbosses.utils.dependencies;

import com.wasteofplastic.askyblock.ASkyBlock;
import com.wasteofplastic.askyblock.Island;
import com.songoda.epicbosses.utils.IASkyblockHelper;
import org.bukkit.entity.Player;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 16-Oct-18
 */
public class ASkyblockHelper implements IASkyblockHelper {

    @Override
    public boolean isOnOwnIsland(Player player) {
        Island island = ASkyBlock.getPlugin().getGrid().getProtectedIslandAt(player.getLocation());

        if(island == null) return false;

        return island.getMembers().contains(player.getUniqueId());
    }
}
