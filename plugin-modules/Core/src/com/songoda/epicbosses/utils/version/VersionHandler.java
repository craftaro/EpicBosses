package com.songoda.epicbosses.utils.version;

import com.songoda.core.compatibility.ServerVersion;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.ItemStack;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Jun-18
 */
public class VersionHandler {

    public boolean canUseOffHand() {
        return ServerVersion.isServerVersionAtLeast(ServerVersion.V1_9);
    }

    public ItemStack getItemInHand(HumanEntity humanEntity) {
        if (ServerVersion.isServerVersionAtOrBelow(ServerVersion.V1_8)) {
            return humanEntity.getItemInHand();
        } else {
            return humanEntity.getInventory().getItemInMainHand();
        }
    }

    public void setItemInHand(HumanEntity humanEntity, ItemStack itemStack) {
        if (ServerVersion.isServerVersionAtOrBelow(ServerVersion.V1_8)) {
            humanEntity.setItemInHand(itemStack);
        } else {
            humanEntity.getInventory().setItemInMainHand(itemStack);
        }
    }
}
