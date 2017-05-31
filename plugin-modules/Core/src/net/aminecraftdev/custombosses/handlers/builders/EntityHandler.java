package net.aminecraftdev.custombosses.handlers.builders;

import net.aminecraftdev.custombosses.innerapi.reflection.ReflectionUtils;
import org.bukkit.Location;
import org.bukkit.entity.*;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 31-May-17
 */
public class EntityHandler extends ReflectionUtils {

    public final LivingEntity getBaseEntity(String type, Location location) {
        LivingEntity livingEntity = null;
        EntityType entityType = null;

        if(type.equalsIgnoreCase("WITHER_SKELETON")) livingEntity = getWitherSkeleton(location);
        else if(type.equalsIgnoreCase("ELDER_GUARDIAN")) livingEntity = getElderGuardian(location);
        else if(type.equalsIgnoreCase("KILLER_BUNNY")) livingEntity = getKillerBunny(location);
        else if(type.equalsIgnoreCase("ZOMBIE")) livingEntity = getZombie(location);
        else if(type.equalsIgnoreCase("BABY_ZOMBIE")) livingEntity = getBabyZombie(location);
        else if(type.equalsIgnoreCase("PIG_ZOMBIE")) livingEntity = getPigZombie(location);
        else if(type.equalsIgnoreCase("BABY_PIG_ZOMBIE")) livingEntity = getBabyPigZombie(location);
        else if(type.contains(":")) {
            String[] split = type.split(":");

            if(split[0].equalsIgnoreCase("SLIME")) {
                livingEntity = getSlime(location, Integer.valueOf(split[1]));
            } else if(split[0].equalsIgnoreCase("MAGMA_CUBE")) {
                livingEntity = getMagmaCube(location, Integer.valueOf(split[1]));
            } else {
                if(EntityType.valueOf(type).equals(null)) {
                    /* ADD A DEBUG MESSAGE HERE */
                    return null;
                }
            }
        } else {
            if(EntityType.valueOf(type).equals(null)) {
                    /* ADD A DEBUG MESSAGE HERE */
                return null;
            }

            entityType = EntityType.valueOf(type);
        }

        if(livingEntity == null) {
            if(entityType == EntityType.SLIME) getSlime(location, 10);
            else if(entityType == EntityType.MAGMA_CUBE) getSlime(location, 10);
            else {
                livingEntity = (LivingEntity) location.getWorld().spawn(location, entityType.getEntityClass());
            }
        }

        return livingEntity;
    }

    private LivingEntity getWitherSkeleton(Location location) {
        if(getAPIVersion().startsWith("v1_11_R")) {
            return (LivingEntity) location.getWorld().spawn(location, EntityType.WITHER_SKELETON.getEntityClass());
        } else {
            Skeleton skeleton = (Skeleton) location.getWorld().spawn(location, EntityType.SKELETON.getEntityClass());
            skeleton.setSkeletonType(Skeleton.SkeletonType.WITHER);

            return skeleton;
        }
    }

    private LivingEntity getElderGuardian(Location location) {
        if(getAPIVersion().startsWith("v1_11_R")) {
            return (LivingEntity) location.getWorld().spawn(location, EntityType.ELDER_GUARDIAN.getEntityClass());
        } else {
            Guardian guardian = (Guardian) location.getWorld().spawn(location, EntityType.GUARDIAN.getEntityClass());
            guardian.setElder(true);

            return guardian;
        }
    }

    private LivingEntity getKillerBunny(Location location) {
        Rabbit rabbit = (Rabbit) location.getWorld().spawn(location, EntityType.RABBIT.getEntityClass());
        rabbit.setRabbitType(Rabbit.Type.THE_KILLER_BUNNY);

        return rabbit;
    }

    private LivingEntity getBabyZombie(Location location) {
        Zombie zombie = (Zombie)  location.getWorld().spawn(location, EntityType.ZOMBIE.getEntityClass());
        zombie.setBaby(true);

        return zombie;
    }

    private LivingEntity getZombie(Location location) {
        Zombie zombie = (Zombie)  location.getWorld().spawn(location, EntityType.ZOMBIE.getEntityClass());
        zombie.setBaby(false);

        return zombie;
    }

    private LivingEntity getBabyPigZombie(Location location) {
        PigZombie pigZombie = (PigZombie)  location.getWorld().spawn(location, EntityType.PIG_ZOMBIE.getEntityClass());
        pigZombie.setBaby(true);

        return pigZombie;
    }

    private LivingEntity getPigZombie(Location location) {
        PigZombie pigZombie = (PigZombie)  location.getWorld().spawn(location, EntityType.PIG_ZOMBIE.getEntityClass());
        pigZombie.setBaby(false);

        return pigZombie;
    }

    private LivingEntity getSlime(Location location, int size) {
        Slime slime = (Slime) location.getWorld().spawn(location, EntityType.SLIME.getEntityClass());
        slime.setSize(size);

        return slime;
    }

    private LivingEntity getMagmaCube(Location location, int size) {
        MagmaCube magmaCube = (MagmaCube) location.getWorld().spawn(location, EntityType.MAGMA_CUBE.getEntityClass());
        magmaCube.setSize(size);

        return magmaCube;
    }

}
