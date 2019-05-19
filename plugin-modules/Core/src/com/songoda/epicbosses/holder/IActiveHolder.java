package com.songoda.epicbosses.holder;

import com.songoda.epicbosses.targeting.TargetHandler;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import java.util.Map;
import java.util.UUID;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 19-Nov-18
 */
public interface IActiveHolder {

    Location getLocation();

    String getName();

    Map<Integer, UUID> getLivingEntityMap();

    LivingEntity getLivingEntity(int position);

    Map<UUID, Double> getMapOfDamagingUsers();

    TargetHandler getTargetHandler();

    void setLivingEntity(int position, LivingEntity livingEntity);

    void killAll();

    boolean isDead();

    boolean hasAttacked(UUID uuid);

}
