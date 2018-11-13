package com.songoda.epicbosses.mechanics.minions;

import com.songoda.epicbosses.entity.MinionEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.entity.elements.MainStatsElement;
import com.songoda.epicbosses.holder.ActiveBossHolder;
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
public class NameMechanic implements IOptionalMechanic<MinionEntity> {

    @Override
    public boolean applyMechanic(MinionEntity minionEntity, ActiveBossHolder activeBossHolder) {
        if(activeBossHolder.getMinionEntityMap() == null || activeBossHolder.getMinionEntityMap().isEmpty()) return false;

        for(EntityStatsElement entityStatsElement : minionEntity.getEntityStats()) {
            MainStatsElement mainStatsElement = entityStatsElement.getMainStats();
            LivingEntity livingEntity = activeBossHolder.getMinionEntityMap().getOrDefault(mainStatsElement.getPosition(), null);
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
