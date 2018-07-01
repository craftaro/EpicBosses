package net.aminecraftdev.custombosses.mechanics;

import net.aminecraftdev.custombosses.entity.BossEntity;
import net.aminecraftdev.custombosses.entity.elements.MainStatsElement;
import net.aminecraftdev.custombosses.holder.ActiveBossHolder;
import net.aminecraftdev.custombosses.utils.EntityFinder;
import net.aminecraftdev.custombosses.utils.IMechanic;
import org.bukkit.entity.LivingEntity;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 01-Jun-18
 */
public class EntityTypeMechanic implements IMechanic {

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
        }

        return true;
    }
}
