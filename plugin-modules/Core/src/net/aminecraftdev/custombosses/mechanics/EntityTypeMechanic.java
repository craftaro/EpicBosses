package net.aminecraftdev.custombosses.mechanics;

import net.aminecraftdev.custombosses.api.BossAPI;
import net.aminecraftdev.custombosses.container.BossEntityContainer;
import net.aminecraftdev.custombosses.entity.BossEntity;
import net.aminecraftdev.custombosses.entity.elements.MainStatsElement;
import net.aminecraftdev.custombosses.holder.ActiveBossHolder;
import net.aminecraftdev.custombosses.utils.Debug;
import net.aminecraftdev.custombosses.utils.EntityFinder;
import net.aminecraftdev.custombosses.utils.IMechanic;
import net.aminecraftdev.custombosses.utils.mechanics.IPrimaryMechanic;
import org.bukkit.entity.LivingEntity;

import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 01-Jun-18
 */
public class EntityTypeMechanic implements IPrimaryMechanic {

    @Override
    public boolean applyMechanic(BossEntity bossEntity, ActiveBossHolder activeBossHolder) {
        for(MainStatsElement mainStatsElement : bossEntity.getMainStats()) {
            String bossEntityType = mainStatsElement.getEntityType();
            String input = bossEntityType.split(":")[0];
            EntityFinder entityFinder = EntityFinder.get(input);
            Integer position = mainStatsElement.getPosition();

            if(position == null) position = 1;
            if(entityFinder == null) return false;

            LivingEntity livingEntity = entityFinder.spawnNewLivingEntity(bossEntityType, activeBossHolder.getLocation());

            if(livingEntity == null) return false;

            activeBossHolder.setLivingEntity(position, livingEntity);

            if(position > 1) {
                int lowerPosition = position - 1;
                LivingEntity lowerLivingEntity = activeBossHolder.getLivingEntityMap().getOrDefault(lowerPosition, null);

                if(lowerLivingEntity == null) {
                    Debug.FAILED_ATTEMPT_TO_STACK_BOSSES.debug(BossAPI.getBossEntityName(bossEntity));
                    return false;
                }

                lowerLivingEntity.setPassenger(livingEntity);
            }
        }

        return true;
    }
}
