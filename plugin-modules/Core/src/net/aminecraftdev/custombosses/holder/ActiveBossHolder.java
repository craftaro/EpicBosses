package net.aminecraftdev.custombosses.holder;

import lombok.Getter;
import net.aminecraftdev.custombosses.entity.BossEntity;
import net.aminecraftdev.custombosses.exception.AlreadySetException;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 03-Jun-18
 */
public class ActiveBossHolder {

    @Getter private final BossEntity bossEntity;
    @Getter private final Location location;

    @Getter private Map<Integer, LivingEntity> livingEntityMap = new HashMap<>();

    public ActiveBossHolder(BossEntity bossEntity, Location spawnLocation) {
        this.location = spawnLocation;
        this.bossEntity = bossEntity;
    }

    public void setLivingEntity(int position, LivingEntity livingEntity) {
        if(getLivingEntityMap().containsKey(position)) {
            throw new AlreadySetException("Tried to set a new LivingEntity while it's already set.");
        } else {
            this.livingEntityMap.put(position, livingEntity);
        }
    }
}
