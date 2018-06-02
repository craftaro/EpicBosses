package net.aminecraftdev.custombosses.utils;

import net.aminecraftdev.custombosses.utils.entity.ICustomEntityHandler;
import net.aminecraftdev.custombosses.utils.entity.handlers.*;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 01-Jun-18
 */
public enum EntityTypeUtil {

    WITHER_SKELETON(new WitherSkeletonHandler()),
    ELDER_GUARDIAN(new ElderGuardianHandler()),
    KILLER_BUNNY(new KillerBunnyHandler()),
    ZOMBIE(new ZombieBabyHandler()),
    BABY_ZOMBIE(new ZombieBabyHandler()),
    PIG_ZOMBIE(new PigZombieHandler()),
    BABY_PIG_ZOMBIE(new PigZombieBabyHandler()),
    SLIME(new SlimeHandler()),
    MAGMA_CUBE(new MagmaCubeHandler()),
    VILLAGER(new VillagerHandler());

    private ICustomEntityHandler entityHandler;

    EntityTypeUtil(ICustomEntityHandler customEntityHandler) {
        this.entityHandler = customEntityHandler;
    }

    private ICustomEntityHandler getEntityHandler() {
        return entityHandler;
    }

    public static LivingEntity get(String entityType, Location spawnLocation) {
        for(EntityTypeUtil entityTypeUtil : values()) {
            if(entityType.toUpperCase().startsWith(entityTypeUtil.name())) return entityTypeUtil.getEntityHandler().getBaseEntity(entityType, spawnLocation);
        }

        return null;
    }

}
