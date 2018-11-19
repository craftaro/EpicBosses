package com.songoda.epicbosses.holder;

import com.songoda.epicbosses.entity.MinionEntity;
import com.songoda.epicbosses.targeting.TargetHandler;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 19-Nov-18
 */
public class ActiveMinionHolder implements IActiveHolder {

    @Getter @Setter private TargetHandler<ActiveMinionHolder> targetHandler = null;

    @Getter private Map<Integer, LivingEntity> livingEntityMap = new HashMap<>();
    @Getter private ActiveBossHolder activeBossHolder;
    @Getter private final MinionEntity minionEntity;
    @Getter private final Location location;
    @Getter private final String name;



    public ActiveMinionHolder(ActiveBossHolder activeBossHolder, MinionEntity minionEntity, Location spawnLocation, String name) {
        this.activeBossHolder = activeBossHolder;
        this.name = name;
        this.location = spawnLocation;
        this.minionEntity = minionEntity;
    }

    @Override
    public Map<UUID, Double> getMapOfDamagingUsers() {
        return getActiveBossHolder().getMapOfDamagingUsers();
    }

    @Override
    public void setLivingEntity(int position, LivingEntity livingEntity) {
        if(getLivingEntityMap().containsKey(position)) {
            this.livingEntityMap.get(position).remove();
            this.livingEntityMap.remove(position);
        }

        this.livingEntityMap.put(position, livingEntity);
    }

    @Override
    public void killAll() {
        this.livingEntityMap.values().forEach(LivingEntity::remove);
        this.livingEntityMap.clear();
    }

    @Override
    public boolean isDead() {
        if(this.livingEntityMap.isEmpty()) return true;

        for(LivingEntity livingEntity : this.livingEntityMap.values()) {
            if(livingEntity == null || livingEntity.isDead()) return true;
        }

        return false;
    }

    @Override
    public boolean hasAttacked(UUID uuid) {
        return getActiveBossHolder().hasAttacked(uuid);
    }

}
