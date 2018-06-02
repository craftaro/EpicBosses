package net.aminecraftdev.custombosses.utils.entity.handlers;

import net.aminecraftdev.custombosses.utils.entity.ICustomEntityHandler;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Villager;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Jun-18
 */
public class VillagerHandler implements ICustomEntityHandler {

    @Override
    public LivingEntity getBaseEntity(String entityType, Location spawnLocation) {
        Villager villager = (Villager) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.VILLAGER);
        String[] split = entityType.split(":");
        int size = Integer.valueOf(split[1]);
        Villager.Profession profession = Villager.Profession.getProfession(size);

        if(profession == null) {
            throw new NullPointerException("Profession value is too high and does not represent an actual villager profession.");
        }

        villager.setProfession(profession);
        return villager;
    }
}
