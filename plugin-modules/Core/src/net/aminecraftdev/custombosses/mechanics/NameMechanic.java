package net.aminecraftdev.custombosses.mechanics;

import net.aminecraftdev.custombosses.entity.BossEntity;
import net.aminecraftdev.custombosses.holder.ActiveBossHolder;
import net.aminecraftdev.custombosses.utils.IMechanic;
import net.aminecraftdev.custombosses.utils.StringUtils;
import org.bukkit.entity.LivingEntity;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Jun-18
 */
public class NameMechanic implements IMechanic {

    @Override
    public boolean applyMechanic(BossEntity bossEntity, ActiveBossHolder activeBossHolder) {
        if(activeBossHolder.getLivingEntity() == null) return false;

        LivingEntity livingEntity = activeBossHolder.getLivingEntity();

        String customName = bossEntity.getMainStats().getDisplayName();

        if(customName != null) {
            livingEntity.setCustomName(StringUtils.get().translateColor(customName));
            livingEntity.setCustomNameVisible(true);
        }

        return true;
    }
}
