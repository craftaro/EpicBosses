package net.aminecraftdev.custombosses.holder;

import lombok.Getter;
import net.aminecraftdev.custombosses.entity.BossEntity;
import net.aminecraftdev.custombosses.exception.AlreadySetException;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 03-Jun-18
 */
public class ActiveBossHolder {

    @Getter private final BossEntity bossEntity;
    @Getter private final Location location;

    @Getter private LivingEntity livingEntity;

    public ActiveBossHolder(BossEntity bossEntity, Location spawnLocation) {
        this.location = spawnLocation;
        this.bossEntity = bossEntity;
    }

    public void setLivingEntity(LivingEntity livingEntity) {
        if(getLivingEntity() != null) {
            throw new AlreadySetException("Tried to set a new LivingEntity while it's already set.");
        } else {
            this.livingEntity = livingEntity;
        }
    }
}
