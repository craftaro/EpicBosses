package com.songoda.epicbosses.mechanics;

import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.entity.elements.MainStatsElement;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.IMechanic;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.file.reader.SpigotYmlReader;
import com.songoda.epicbosses.utils.mechanics.IPrimaryMechanic;
import com.songoda.epicbosses.utils.version.VersionHandler;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Jun-18
 */
public class SettingsMechanic implements IPrimaryMechanic {

    private VersionHandler versionHandler;

    public SettingsMechanic() {
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
