package com.songoda.epicbosses.utils.entity.handlers;

import com.songoda.core.compatibility.ServerVersion;
import com.songoda.epicbosses.utils.entity.ICustomEntityHandler;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

public class FishHandler implements ICustomEntityHandler {

    @Override
    public LivingEntity getBaseEntity(String entityType, Location spawnLocation) {
        if (ServerVersion.isServerVersionBelow(ServerVersion.V1_13))
            throw new NullPointerException("This feature is only implemented in version 1.13 and above of Minecraft.");


        EntityType fishEntityType;
        String type = entityType.toUpperCase().replace("_", "");
        switch (type) {
            case "COD":
                fishEntityType = EntityType.COD;
                break;
            case "PUFFERFISH":
                fishEntityType = EntityType.PUFFERFISH;
                break;
            case "SALMON":
                fishEntityType = EntityType.SALMON;
                break;
            case "FISH":
            case "TROPICALFISH":
            case "CLOWNFISH":
            default:
                fishEntityType = EntityType.TROPICAL_FISH;
                break;
        }

        return (LivingEntity) spawnLocation.getWorld().spawnEntity(spawnLocation, fishEntityType);
    }
}
