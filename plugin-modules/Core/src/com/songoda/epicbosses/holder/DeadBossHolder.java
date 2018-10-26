package com.songoda.epicbosses.holder;

import lombok.Getter;
import com.songoda.epicbosses.entity.BossEntity;
import org.bukkit.Location;

import java.util.Map;
import java.util.UUID;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 25-Oct-18
 */
public class DeadBossHolder {

    @Getter private final Map<UUID, Double> sortedDamageMap, percentageMap;
    @Getter private final BossEntity bossEntity;
    @Getter private final Location location;

    public DeadBossHolder(BossEntity bossEntity, Location deathLocation, Map<UUID, Double> sortedDamageMap, Map<UUID, Double> percentageMap) {
        this.location = deathLocation;
        this.bossEntity = bossEntity;
        this.sortedDamageMap = sortedDamageMap;
        this.percentageMap = percentageMap;
    }

}
