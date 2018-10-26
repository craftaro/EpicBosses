package com.songoda.epicbosses.mechanics;

import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.entity.elements.MainStatsElement;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.IMechanic;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.mechanics.IOptionalMechanic;
import org.bukkit.entity.LivingEntity;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Jun-18
 *
 * TODO: Make a hologram above name instead of using default CustomName
 */
public class NameMechanic implements IOptionalMechanic {

    @Override
    public boolean applyMechanic(BossEntity bossEntity, ActiveBossHolder activeBossHolder) {
        if(activeBossHolder.getLivingEntityMap().getOrDefault(1, null) == null) return false;

        for(EntityStatsElement entityStatsElement : bossEntity.getEntityStats()) {
            MainStatsElement mainStatsElement = entityStatsElement.getMainStats();
            LivingEntity livingEntity = activeBossHolder.getLivingEntityMap().getOrDefault(mainStatsElement.getPosition(), null);
            String customName = mainStatsElement.getDisplayName();

            if(livingEntity == null) return false;

            if(customName != null) {
                livingEntity.setCustomName(StringUtils.get().translateColor(customName));
                livingEntity.setCustomNameVisible(true);
            }
        }

        return true;
    }
}
