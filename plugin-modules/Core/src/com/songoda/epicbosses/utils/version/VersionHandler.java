package com.songoda.epicbosses.utils.version;

import lombok.Getter;
import com.songoda.epicbosses.utils.Versions;
import org.bukkit.Bukkit;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Jun-18
 */
public class VersionHandler {

    @Getter private Versions version;

    public VersionHandler() {
        String v = Bukkit.getServer().getClass().getPackage().getName();

        v = v.substring(v.lastIndexOf(".") + 1);

        this.version = Versions.getVersion(v);
    }

    public boolean canUseOffHand() {
        return this.version.isHigherThanOrEqualTo(Versions.v1_9_R1);
    }

    public ItemStack getItemInHand(HumanEntity humanEntity) {
        if(this.version.isLessThanOrEqualTo(Versions.v1_8_R3)) {
            return humanEntity.getItemInHand();
        } else {
            return humanEntity.getInventory().getItemInMainHand();
        }
    }

    public void setItemInHand(HumanEntity humanEntity, ItemStack itemStack) {
        if(this.version.isLessThanOrEqualTo(Versions.v1_8_R3)) {
            humanEntity.setItemInHand(itemStack);
        } else {
            humanEntity.getInventory().setItemInMainHand(itemStack);
        }
    }

}
