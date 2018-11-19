package com.songoda.epicbosses.mechanics.minions;

import com.songoda.epicbosses.entity.MinionEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.entity.elements.MainStatsElement;
import com.songoda.epicbosses.holder.ActiveMinionHolder;
import com.songoda.epicbosses.mechanics.IMinionMechanic;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.file.reader.SpigotYmlReader;
import org.bukkit.entity.LivingEntity;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Jun-18
 */
public class HealthMechanic implements IMinionMechanic {

    @Override
    public boolean applyMechanic(MinionEntity minionEntity, ActiveMinionHolder activeMinionHolder) {
        if(activeMinionHolder.getLivingEntityMap() == null || activeMinionHolder.getLivingEntityMap().isEmpty()) return false;

        double maxHealthSetting = (double) SpigotYmlReader.get().getObject("settings.attribute.maxHealth.max");

        for(EntityStatsElement entityStatsElement : minionEntity.getEntityStats()) {
            MainStatsElement mainStatsElement = entityStatsElement.getMainStats();
            LivingEntity livingEntity = activeMinionHolder.getLivingEntityMap().getOrDefault(mainStatsElement.getPosition(), null);
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
