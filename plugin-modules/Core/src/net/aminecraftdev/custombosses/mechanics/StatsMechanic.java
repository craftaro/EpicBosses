package net.aminecraftdev.custombosses.mechanics;

import net.aminecraftdev.custombosses.entity.BossEntity;
import net.aminecraftdev.custombosses.holder.ActiveBossHolder;
import net.aminecraftdev.custombosses.utils.Debug;
import net.aminecraftdev.custombosses.utils.IMechanic;
import net.aminecraftdev.custombosses.utils.StringUtils;
import net.aminecraftdev.custombosses.utils.reader.SpigotYmlReader;
import org.bukkit.entity.LivingEntity;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Jun-18
 */
public class StatsMechanic implements IMechanic {

    @Override
    public boolean applyMechanic(BossEntity bossEntity, ActiveBossHolder activeBossHolder) {
        if(activeBossHolder.getLivingEntity() == null) return false;

        LivingEntity livingEntity = activeBossHolder.getLivingEntity();
        double maxHealthSetting = (double) SpigotYmlReader.get().getObject("settings.attribute.maxHealth.max");
        double maxHealth = bossEntity.getMainStats().getHealth();

        if(maxHealth > maxHealthSetting) {
            Debug.MAX_HEALTH.debug(maxHealthSetting);
            return false;
        }

        livingEntity.setMaxHealth(maxHealth);
        livingEntity.setHealth(maxHealth);

        String customName = bossEntity.getMainStats().getDisplayName();

        if(customName != null) {
            livingEntity.setCustomName(StringUtils.get().translateColor(customName));
            livingEntity.setCustomNameVisible(true);
        }

        livingEntity.setRemoveWhenFarAway(false);
        livingEntity.setCanPickupItems(false);

        return true;
    }
}
