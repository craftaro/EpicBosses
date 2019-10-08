package com.songoda.epicbosses.mechanics.boss;

import com.songoda.core.compatibility.ServerVersion;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.entity.elements.MainStatsElement;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.mechanics.IBossMechanic;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Jun-18
 */
public class SettingsMechanic implements IBossMechanic {

    @Override
    public boolean applyMechanic(BossEntity bossEntity, ActiveBossHolder activeBossHolder) {
        if (activeBossHolder.getLivingEntityMap().getOrDefault(1, null) == null) return false;

        for (EntityStatsElement entityStatsElement : bossEntity.getEntityStats()) {
            MainStatsElement mainStatsElement = entityStatsElement.getMainStats();
            LivingEntity livingEntity = activeBossHolder.getLivingEntity(mainStatsElement.getPosition());

            if (livingEntity == null) return false;

            EntityEquipment entityEquipment = livingEntity.getEquipment();

            livingEntity.setRemoveWhenFarAway(false);
            livingEntity.setCanPickupItems(false);
            entityEquipment.setHelmetDropChance(0.0F);
            entityEquipment.setChestplateDropChance(0.0F);
            entityEquipment.setLeggingsDropChance(0.0F);
            entityEquipment.setBootsDropChance(0.0F);

            if (ServerVersion.isServerVersionAtLeast(ServerVersion.V1_9)) {
                entityEquipment.setItemInMainHandDropChance(0.0F);
                entityEquipment.setItemInOffHandDropChance(0.0F);
            } else {
                entityEquipment.setItemInHandDropChance(0.0F);
            }
        }

        return true;
    }
}
