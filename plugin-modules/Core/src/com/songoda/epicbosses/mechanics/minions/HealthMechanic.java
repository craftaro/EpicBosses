package com.songoda.epicbosses.mechanics.minions;

import com.songoda.epicbosses.entity.MinionEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.entity.elements.MainStatsElement;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.file.reader.SpigotYmlReader;
import com.songoda.epicbosses.utils.mechanics.IPrimaryMechanic;
import org.bukkit.entity.LivingEntity;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Jun-18
 */
public class HealthMechanic implements IPrimaryMechanic<MinionEntity> {

    @Override
    public boolean applyMechanic(MinionEntity minionEntity, ActiveBossHolder activeBossHolder) {
        if(activeBossHolder.getMinionEntityMap() == null || activeBossHolder.getMinionEntityMap().isEmpty()) return false;

        double maxHealthSetting = (double) SpigotYmlReader.get().getObject("settings.attribute.maxHealth.max");

        for(EntityStatsElement entityStatsElement : minionEntity.getEntityStats()) {
            MainStatsElement mainStatsElement = entityStatsElement.getMainStats();
            LivingEntity livingEntity = activeBossHolder.getMinionEntityMap().getOrDefault(mainStatsElement.getPosition(), null);
            double maxHealth = mainStatsElement.getHealth();

            if(livingEntity == null) return false;

            if(maxHealth > maxHealthSetting) {
                Debug.MAX_HEALTH.debug(maxHealthSetting);
                return false;
            }


            livingEntity.setMaxHealth(maxHealth);
            livingEntity.setHealth(maxHealth);
        }

        return true;
    }
}
