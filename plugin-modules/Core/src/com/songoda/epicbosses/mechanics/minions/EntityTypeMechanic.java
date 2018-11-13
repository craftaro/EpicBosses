package com.songoda.epicbosses.mechanics.minions;

import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.MinionEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.entity.elements.MainStatsElement;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.EntityFinder;
import com.songoda.epicbosses.utils.mechanics.IPrimaryMechanic;
import org.bukkit.entity.LivingEntity;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 01-Jun-18
 */
public class EntityTypeMechanic implements IPrimaryMechanic<MinionEntity> {

    @Override
    public boolean applyMechanic(MinionEntity minionEntity, ActiveBossHolder activeBossHolder) {
        for(EntityStatsElement entityStatsElement : minionEntity.getEntityStats()) {
            MainStatsElement mainStatsElement = entityStatsElement.getMainStats();

            String bossEntityType = mainStatsElement.getEntityType();
            String input = bossEntityType.split(":")[0];
            EntityFinder entityFinder = EntityFinder.get(input);
            Integer position = mainStatsElement.getPosition();

            if(position == null) position = 1;
            if(entityFinder == null) return false;

            LivingEntity livingEntity = entityFinder.spawnNewLivingEntity(bossEntityType, activeBossHolder.getLivingEntity().getLocation());

            if(livingEntity == null) return false;

            activeBossHolder.getMinionEntityMap().put(position, livingEntity);

            if(position > 1) {
                int lowerPosition = position - 1;
                LivingEntity lowerLivingEntity = activeBossHolder.getLivingEntityMap().getOrDefault(lowerPosition, null);

                if(lowerLivingEntity == null) {
                    Debug.FAILED_ATTEMPT_TO_STACK_BOSSES.debug(BossAPI.getMinionEntityName(minionEntity));
                    return false;
                }

                lowerLivingEntity.setPassenger(livingEntity);
            }
        }

        return true;
    }
}
