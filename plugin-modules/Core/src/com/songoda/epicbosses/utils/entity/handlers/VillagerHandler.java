package com.songoda.epicbosses.utils.entity.handlers;

import com.songoda.epicbosses.utils.entity.ICustomEntityHandler;
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

        if (split.length == 2) {
            String type = split[1];
            Villager.Profession profession;

            try {
                profession = Villager.Profession.valueOf(type.toUpperCase());
            } catch (Exception ex) {
                throw new NullPointerException("Profession value is too high and does not represent an actual villager profession.");
            }

            villager.setProfession(profession);
        }

        return villager;
    }
}
