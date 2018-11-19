package com.songoda.epicbosses.mechanics.minions;

import com.songoda.epicbosses.entity.MinionEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.entity.elements.HandsElement;
import com.songoda.epicbosses.entity.elements.MainStatsElement;
import com.songoda.epicbosses.holder.ActiveMinionHolder;
import com.songoda.epicbosses.managers.files.ItemsFileManager;
import com.songoda.epicbosses.mechanics.IMinionMechanic;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import com.songoda.epicbosses.utils.version.VersionHandler;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Jun-18
 */
public class WeaponMechanic implements IMinionMechanic {

    private ItemsFileManager itemStackManager;
    private VersionHandler versionHandler;

    public WeaponMechanic(ItemsFileManager itemStackManager) {
        this.itemStackManager = itemStackManager;
        this.versionHandler = new VersionHandler();
    }

    @Override
    public boolean applyMechanic(MinionEntity minionEntity, ActiveMinionHolder activeMinionHolder) {
        if(activeMinionHolder.getLivingEntityMap() == null || activeMinionHolder.getLivingEntityMap().isEmpty()) return false;

        for(EntityStatsElement entityStatsElement : minionEntity.getEntityStats()) {
            MainStatsElement mainStatsElement = entityStatsElement.getMainStats();
            LivingEntity livingEntity = activeMinionHolder.getLivingEntityMap().getOrDefault(mainStatsElement.getPosition(), null);

            if(livingEntity == null) return false;

            EntityEquipment entityEquipment = livingEntity.getEquipment();
            HandsElement handsElement = entityStatsElement.getHands();
            String mainHand = handsElement.getMainHand();
            String offHand = handsElement.getOffHand();

            if(mainHand != null) {
                ItemStackHolder itemStackHolder = this.itemStackManager.getItemStackHolder(mainHand);

                if(itemStackHolder != null) {
                    ItemStack itemStack = this.itemStackManager.getItemStackConverter().from(itemStackHolder);

                    if(this.versionHandler.canUseOffHand()) {
                        entityEquipment.setItemInMainHand(itemStack);
                    } else {
                        entityEquipment.setItemInHand(itemStack);
                    }
                }
            }

            if(offHand != null && this.versionHandler.canUseOffHand()) {
                ItemStackHolder itemStackHolder = this.itemStackManager.getItemStackHolder(offHand);

                if(itemStackHolder != null) {
                    ItemStack itemStack = this.itemStackManager.getItemStackConverter().from(itemStackHolder);

                    entityEquipment.setItemInOffHand(itemStack);
                }
            }
        }

        return true;
    }
}
