package com.songoda.epicbosses.mechanics.boss;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.entity.elements.MainStatsElement;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.mechanics.IBossMechanic;
import com.songoda.epicbosses.utils.StringUtils;
import org.bukkit.entity.LivingEntity;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Jun-18
 */
public class NameMechanic implements IBossMechanic {

    private EpicBosses plugin = EpicBosses.getInstance();

    @Override
    public boolean applyMechanic(BossEntity bossEntity, ActiveBossHolder activeBossHolder) {
        if (activeBossHolder.getLivingEntityMap().getOrDefault(1, null) == null) return false;

        for (EntityStatsElement entityStatsElement : bossEntity.getEntityStats()) {
            MainStatsElement mainStatsElement = entityStatsElement.getMainStats();
            LivingEntity livingEntity = activeBossHolder.getLivingEntity(mainStatsElement.getPosition());
            String customName = mainStatsElement.getDisplayName();

            if (livingEntity == null || customName == null) continue;
            String formattedName = StringUtils.get().translateColor(customName);

            livingEntity.setCustomName(formattedName);
            livingEntity.setCustomNameVisible(true);
        }

        return true;
    }
}
