package net.aminecraftdev.custombosses.mechanics;

import net.aminecraftdev.custombosses.entity.elements.MainStatsElement;
import net.aminecraftdev.custombosses.utils.EntityTypeUtil;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 01-Jun-18
 */
public class EntityTypeMechanic {

    private static final EntityTypeMechanic instance = new EntityTypeMechanic();

    public LivingEntity getBaseEntity(MainStatsElement mainStatsElement, Location location) {
        String bossEntityType = mainStatsElement.getEntityType();
        LivingEntity livingEntity;

        try {
            livingEntity = EntityTypeUtil.get(bossEntityType, location);
        } catch (NullPointerException ex) {
            return null;
        }

        if(livingEntity == null) {
            EntityType entityType = EntityType.valueOf(bossEntityType.toUpperCase());

            try {
                livingEntity = (LivingEntity) location.getWorld().spawnEntity(location, entityType);
            } catch (Exception ex) {
                return null;
            }
        }

        return livingEntity;
    }

    public static EntityTypeMechanic get() {
        return instance;
    }

}
