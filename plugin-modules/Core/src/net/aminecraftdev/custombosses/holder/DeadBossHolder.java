package net.aminecraftdev.custombosses.holder;

import lombok.Getter;
import net.aminecraftdev.custombosses.entity.BossEntity;
import org.bukkit.Location;

import java.util.Map;
import java.util.UUID;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 25-Oct-18
 */
public class DeadBossHolder {

    @Getter private final Map<UUID, Double> sortedDamageMap;
    @Getter private final BossEntity bossEntity;
    @Getter private final Location location;

    public DeadBossHolder(BossEntity bossEntity, Location deathLocation, Map<UUID, Double> sortedDamageMap) {
        this.location = deathLocation;
        this.bossEntity = bossEntity;
        this.sortedDamageMap = sortedDamageMap;
    }

}
