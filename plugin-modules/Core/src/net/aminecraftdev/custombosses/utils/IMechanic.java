package net.aminecraftdev.custombosses.utils;

import net.aminecraftdev.custombosses.entity.BossEntity;
import net.aminecraftdev.custombosses.holder.ActiveBossHolder;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Jun-18
 */
public interface IMechanic {

    boolean applyMechanic(BossEntity bossEntity, ActiveBossHolder activeBossHolder);

}
