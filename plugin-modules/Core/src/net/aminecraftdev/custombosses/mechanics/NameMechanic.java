package net.aminecraftdev.custombosses.mechanics;

import net.aminecraftdev.custombosses.entity.BossEntity;
import net.aminecraftdev.custombosses.entity.elements.EntityStatsElement;
import net.aminecraftdev.custombosses.entity.elements.MainStatsElement;
import net.aminecraftdev.custombosses.holder.ActiveBossHolder;
import net.aminecraftdev.custombosses.utils.Debug;
import net.aminecraftdev.custombosses.utils.IMechanic;
import net.aminecraftdev.custombosses.utils.StringUtils;
import net.aminecraftdev.custombosses.utils.mechanics.IOptionalMechanic;
import org.bukkit.entity.LivingEntity;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Jun-18
 *
 * TODO: Make a hologram above name instead of using default CustomName
 */
public class NameMechanic implements IOptionalMechanic {

    @Override
    public boolean applyMechanic(BossEntity bossEntity, ActiveBossHolder activeBossHolder) {
        if(activeBossHolder.getLivingEntityMap().getOrDefault(1, null) == null) return false;

        for(EntityStatsElement entityStatsElement : bossEntity.getEntityStats()) {
            MainStatsElement mainStatsElement = entityStatsElement.getMainStats();
            LivingEntity livingEntity = activeBossHolder.getLivingEntityMap().getOrDefault(mainStatsElement.getPosition(), null);
            String customName = mainStatsElement.getDisplayName();

            if(livingEntity == null) return false;

            if(customName != null) {
                livingEntity.setCustomName(StringUtils.get().translateColor(customName));
                livingEntity.setCustomNameVisible(true);
            }
        }

        return true;
    }
}
