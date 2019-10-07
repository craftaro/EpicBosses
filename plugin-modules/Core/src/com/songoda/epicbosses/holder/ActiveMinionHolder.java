package com.songoda.epicbosses.holder;

import com.songoda.epicbosses.entity.MinionEntity;
import com.songoda.epicbosses.targeting.TargetHandler;
import com.songoda.epicbosses.utils.ServerUtils;
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

    private TargetHandler<ActiveMinionHolder> targetHandler = null;

    private Map<Integer, UUID> livingEntityMap = new HashMap<>();
    private ActiveBossHolder activeBossHolder;
    private final MinionEntity minionEntity;
    private final Location location;
    private final String name;

    public ActiveMinionHolder(ActiveBossHolder activeBossHolder, MinionEntity minionEntity, Location spawnLocation, String name) {
        this.activeBossHolder = activeBossHolder;
        this.name = name;
        this.location = spawnLocation;
        this.minionEntity = minionEntity;
    }

    @Override
    public Map<UUID, Double> getMapOfDamagingUsers() {
        return this.activeBossHolder.getMapOfDamagingUsers();
    }

    @Override
    public void setLivingEntity(int position, LivingEntity livingEntity) {
        if(this.livingEntityMap.containsKey(position)) {
            LivingEntity target = (LivingEntity) ServerUtils.get().getEntity(this.livingEntityMap.get(position));
            if (target != null)
                target.remove();
            this.livingEntityMap.remove(position);
        }

        this.livingEntityMap.put(position, livingEntity.getUniqueId());
    }

    @Override
    public LivingEntity getLivingEntity(int position) {
        UUID target = this.livingEntityMap.get(position);
        if (target == null)
            return null;
        return (LivingEntity) ServerUtils.get().getEntity(target);
    }

    @Override
    public void killAll() {
        for (UUID livingEntity : this.livingEntityMap.values()) {
            LivingEntity target = (LivingEntity) ServerUtils.get().getEntity(livingEntity);
            if (target != null)
                target.remove();
        }
        this.livingEntityMap.clear();
    }

    @Override
    public boolean isDead() {
        if(this.livingEntityMap.isEmpty()) return true;

        for(UUID uuid : this.livingEntityMap.values()) {
            LivingEntity livingEntity = (LivingEntity) ServerUtils.get().getEntity(uuid);
            if(livingEntity == null || livingEntity.isDead()) return true;
        }

        return false;
    }

    @Override
    public boolean hasAttacked(UUID uuid) {
        return this.activeBossHolder.hasAttacked(uuid);
    }

    public TargetHandler<ActiveMinionHolder> getTargetHandler() {
        return this.targetHandler;
    }

    public Map<Integer, UUID> getLivingEntityMap() {
        return this.livingEntityMap;
    }

    public ActiveBossHolder getActiveBossHolder() {
        return this.activeBossHolder;
    }

    public MinionEntity getMinionEntity() {
        return this.minionEntity;
    }

    public Location getLocation() {
        return this.location;
    }

    public String getName() {
        return this.name;
    }

    public void setTargetHandler(TargetHandler<ActiveMinionHolder> targetHandler) {
        this.targetHandler = targetHandler;
    }
}
