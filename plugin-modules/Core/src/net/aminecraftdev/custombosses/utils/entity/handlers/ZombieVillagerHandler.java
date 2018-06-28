package net.aminecraftdev.custombosses.utils.entity.handlers;

import net.aminecraftdev.custombosses.utils.Versions;
import net.aminecraftdev.custombosses.utils.entity.ICustomEntityHandler;
import net.aminecraftdev.custombosses.utils.version.VersionHandler;
import org.bukkit.Location;
import org.bukkit.entity.*;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 28-Jun-18
 */
public class ZombieVillagerHandler implements ICustomEntityHandler {

    private VersionHandler versionHandler = new VersionHandler();

    @Override
    public LivingEntity getBaseEntity(String entityType, Location spawnLocation) {
        if(this.versionHandler.getVersion().isHigherThanOrEqualTo(Versions.v1_11_R1)) {
            ZombieVillager zombieVillager = (ZombieVillager) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.ZOMBIE_VILLAGER);
            String[] split = entityType.split(":");

            if(split.length == 2) {
                String type = split[1];
                Villager.Profession profession;

                try {
                    profession = Villager.Profession.valueOf(type.toUpperCase());
                } catch (Exception ex) {
                    throw new NullPointerException("Profession value is too high and does not represent an actual villager profession.");
                }

                zombieVillager.setVillagerProfession(profession);

                return zombieVillager;
            }
        }

        Zombie zombie = (Zombie) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.ZOMBIE);
        zombie.setVillager(true);

        return zombie;
    }
}
