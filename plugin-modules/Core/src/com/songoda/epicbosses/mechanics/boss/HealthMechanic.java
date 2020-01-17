package com.songoda.epicbosses.mechanics.boss;

import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.entity.elements.MainStatsElement;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.mechanics.IBossMechanic;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.file.reader.SpigotYmlReader;
import org.bukkit.entity.LivingEntity;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Jun-18
 */
public class HealthMechanic implements IBossMechanic {

    @Override
    public boolean applyMechanic(BossEntity bossEntity, ActiveBossHolder activeBossHolder) {
        if (activeBossHolder.getLivingEntityMap().getOrDefault(1, null) == null) return false;

        double maxHealthSetting = Double.valueOf( SpigotYmlReader
                .get().getObject("settings.attribute.maxHealth.max").toString());

        for (EntityStatsElement entityStatsElement : bossEntity.getEntityStats()) {
            MainStatsElement mainStatsElement = entityStatsElement.getMainStats();
            LivingEntity livingEntity = activeBossHolder.getLivingEntity(mainStatsElement.getPosition());
            double maxHealth = mainStatsElement.getHealth();

            if (livingEntity == null) return false;

            if (maxHealth > maxHealthSetting) {
                Debug.MAX_HEALTH.debug(maxHealthSetting);
                return false;
            }


            livingEntity.setMaxHealth(maxHealth);
            livingEntity.setHealth(maxHealth);
        }

        return true;
    }
}
