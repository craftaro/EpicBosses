package com.songoda.epicbosses.mechanics.minions;

import com.songoda.epicbosses.entity.MinionEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.entity.elements.MainStatsElement;
import com.songoda.epicbosses.holder.ActiveMinionHolder;
import com.songoda.epicbosses.mechanics.IMinionMechanic;
import com.songoda.epicbosses.utils.StringUtils;
import org.bukkit.entity.LivingEntity;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Jun-18
 *
 * TODO: Make a hologram above name instead of using default CustomName
 */
public class NameMechanic implements IMinionMechanic {

    @Override
    public boolean applyMechanic(MinionEntity minionEntity, ActiveMinionHolder activeMinionHolder) {
        if(activeMinionHolder.getLivingEntityMap() == null || activeMinionHolder.getLivingEntityMap().isEmpty()) return false;

        for(EntityStatsElement entityStatsElement : minionEntity.getEntityStats()) {
            MainStatsElement mainStatsElement = entityStatsElement.getMainStats();
            LivingEntity livingEntity = activeMinionHolder.getLivingEntityMap().getOrDefault(mainStatsElement.getPosition(), null);
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
