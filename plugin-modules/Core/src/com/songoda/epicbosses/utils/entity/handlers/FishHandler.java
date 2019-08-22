package com.songoda.epicbosses.utils.entity.handlers;

import com.songoda.epicbosses.utils.Versions;
import com.songoda.epicbosses.utils.entity.ICustomEntityHandler;
import com.songoda.epicbosses.utils.version.VersionHandler;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Fish;
import org.bukkit.entity.LivingEntity;

public class FishHandler implements ICustomEntityHandler {

    private VersionHandler versionHandler = new VersionHandler();

    @Override
    public LivingEntity getBaseEntity(String entityType, Location spawnLocation) {
        if(this.versionHandler.getVersion().isLessThan(Versions.v1_13_R1)) {
            throw new NullPointerException("This feature is only implemented in version 1.13 and above of Minecraft.");
        }


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
