package com.songoda.epicbosses.mechanics.minions;

import com.songoda.epicbosses.entity.MinionEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.entity.elements.MainStatsElement;
import com.songoda.epicbosses.holder.ActiveMinionHolder;
import com.songoda.epicbosses.mechanics.IMinionMechanic;
import com.songoda.epicbosses.utils.version.VersionHandler;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Jun-18
 */
public class SettingsMechanic implements IMinionMechanic {

    private VersionHandler versionHandler;

    public SettingsMechanic() {
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

            livingEntity.setRemoveWhenFarAway(false);
            livingEntity.setCanPickupItems(false);
            entityEquipment.setHelmetDropChance(0.0F);
            entityEquipment.setChestplateDropChance(0.0F);
            entityEquipment.setLeggingsDropChance(0.0F);
            entityEquipment.setBootsDropChance(0.0F);

            if(this.versionHandler.canUseOffHand()) {
                entityEquipment.setItemInMainHandDropChance(0.0F);
                entityEquipment.setItemInOffHandDropChance(0.0F);
            } else {
                entityEquipment.setItemInHandDropChance(0.0F);
            }
        }

        return true;
    }
}
