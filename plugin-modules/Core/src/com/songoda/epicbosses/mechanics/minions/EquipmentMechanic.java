package com.songoda.epicbosses.mechanics.minions;

import com.songoda.epicbosses.entity.MinionEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.entity.elements.EquipmentElement;
import com.songoda.epicbosses.entity.elements.MainStatsElement;
import com.songoda.epicbosses.holder.ActiveMinionHolder;
import com.songoda.epicbosses.managers.files.ItemsFileManager;
import com.songoda.epicbosses.mechanics.IMinionMechanic;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 03-Jun-18
 */
public class EquipmentMechanic implements IMinionMechanic {

    private ItemsFileManager itemStackManager;

    public EquipmentMechanic(ItemsFileManager itemStackManager) {
        this.itemStackManager = itemStackManager;
    }

    @Override
    public boolean applyMechanic(MinionEntity minionEntity, ActiveMinionHolder activeBossHolder) {
        if(activeBossHolder.getLivingEntityMap() == null || activeBossHolder.getLivingEntityMap().isEmpty()) return false;

        for(EntityStatsElement entityStatsElement : minionEntity.getEntityStats()) {
            MainStatsElement mainStatsElement = entityStatsElement.getMainStats();
            LivingEntity livingEntity = activeBossHolder.getLivingEntity(mainStatsElement.getPosition());

            if(livingEntity == null) return false;

            EquipmentElement equipmentElement = entityStatsElement.getEquipment();
            EntityEquipment entityEquipment = livingEntity.getEquipment();
            String helmet = equipmentElement.getHelmet();
            String chestplate = equipmentElement.getChestplate();
            String leggings = equipmentElement.getLeggings();
            String boots = equipmentElement.getBoots();

            if(helmet != null) {
                ItemStackHolder itemStackHolder = this.itemStackManager.getItemStackHolder(helmet);

                if(itemStackHolder != null) {
                    ItemStack itemStack = this.itemStackManager.getItemStackConverter().from(itemStackHolder);

                    entityEquipment.setHelmet(itemStack);
                }
            }

            if(chestplate != null) {
                ItemStackHolder itemStackHolder = this.itemStackManager.getItemStackHolder(chestplate);

                if(itemStackHolder != null) {
                    ItemStack itemStack = this.itemStackManager.getItemStackConverter().from(itemStackHolder);

                    entityEquipment.setChestplate(itemStack);
                }
            }

            if(leggings != null) {
                ItemStackHolder itemStackHolder = this.itemStackManager.getItemStackHolder(leggings);

                if(itemStackHolder != null) {
                    ItemStack itemStack = this.itemStackManager.getItemStackConverter().from(itemStackHolder);

                    entityEquipment.setLeggings(itemStack);
                }
            }

            if(boots != null) {
                ItemStackHolder itemStackHolder = this.itemStackManager.getItemStackHolder(boots);

                if(itemStackHolder != null) {
                    ItemStack itemStack = this.itemStackManager.getItemStackConverter().from(itemStackHolder);

                    entityEquipment.setBoots(itemStack);
                }
            }
        }

        return true;
    }
}
