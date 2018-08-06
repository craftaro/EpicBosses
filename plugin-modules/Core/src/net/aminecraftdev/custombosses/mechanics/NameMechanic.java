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
        if(activeBossHolder.getLivingEntityMap().getOrDefault(0, null) == null) return false;
        if(bossEntity.getMainStats().isEmpty() || bossEntity.getMainStats().get(0) == null) return false;

        LivingEntity livingEntity = activeBossHolder.getLivingEntityMap().getOrDefault(0, null);

        String customName = bossEntity.getMainStats().get(0).getDisplayName();

        if(customName != null) {
            livingEntity.setCustomName(StringUtils.get().translateColor(customName));
            livingEntity.setCustomNameVisible(true);
        }

        return true;
    }
}
