package com.songoda.epicbosses.mechanics.boss;

import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.entity.elements.HandsElement;
import com.songoda.epicbosses.entity.elements.MainStatsElement;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.managers.files.ItemsFileManager;
import com.songoda.epicbosses.utils.mechanics.IOptionalMechanic;
import com.songoda.epicbosses.utils.version.VersionHandler;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Jun-18
 */
public class WeaponMechanic implements IOptionalMechanic<BossEntity> {

    private ItemsFileManager itemStackManager;
    private VersionHandler versionHandler;

    public WeaponMechanic(ItemsFileManager itemStackManager) {
        this.itemStackManager = itemStackManager;
        this.versionHandler = new VersionHandler();
    }

    @Override
    public boolean applyMechanic(BossEntity bossEntity, ActiveBossHolder activeBossHolder) {
        if(activeBossHolder.getLivingEntityMap().getOrDefault(1, null) == null) return false;

        for(EntityStatsElement entityStatsElement : bossEntity.getEntityStats()) {
            MainStatsElement mainStatsElement = entityStatsElement.getMainStats();
            LivingEntity livingEntity = activeBossHolder.getLivingEntityMap().getOrDefault(mainStatsElement.getPosition(), null);

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
