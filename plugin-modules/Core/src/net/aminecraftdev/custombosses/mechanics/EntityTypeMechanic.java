package net.aminecraftdev.custombosses.mechanics;

import net.aminecraftdev.custombosses.entity.BossEntity;
import net.aminecraftdev.custombosses.holder.ActiveBossHolder;
import net.aminecraftdev.custombosses.utils.EntityTypeUtil;
import net.aminecraftdev.custombosses.utils.IMechanic;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 01-Jun-18
 */
public class EntityTypeMechanic implements IMechanic {

    @Override
    public boolean applyMechanic(BossEntity bossEntity, ActiveBossHolder activeBossHolder) {
        String bossEntityType = bossEntity.getMainStats().getEntityType();
        LivingEntity livingEntity;

        try {
            livingEntity = EntityTypeUtil.get(bossEntityType, activeBossHolder.getLocation());
        } catch (NullPointerException ex) {
            return false;
        }

        if(livingEntity == null) {
            EntityType entityType = EntityType.valueOf(bossEntityType.toUpperCase());

            try {
                livingEntity = (LivingEntity) activeBossHolder.getLocation().getWorld().spawnEntity(activeBossHolder.getLocation(), entityType);
            } catch (Exception ex) {
                return false;
            }
        }

        activeBossHolder.setLivingEntity(livingEntity);
        return true;
    }
}
