package net.aminecraftdev.custombosses.utils.entity.handlers;

import net.aminecraftdev.custombosses.utils.entity.ICustomEntityHandler;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.MagmaCube;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 01-Jun-18
 */
public class MagmaCubeHandler implements ICustomEntityHandler {

    @Override
    public LivingEntity getBaseEntity(String entityType, Location spawnLocation) {
        String[] split = entityType.split(":");
        int size = Integer.valueOf(split[1]);

        MagmaCube magmaCube = (MagmaCube) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.MAGMA_CUBE);
        magmaCube.setSize(size);

        return magmaCube;
    }
}
