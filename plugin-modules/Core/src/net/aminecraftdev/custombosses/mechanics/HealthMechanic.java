package net.aminecraftdev.custombosses.mechanics;

import net.aminecraftdev.custombosses.entity.BossEntity;
import net.aminecraftdev.custombosses.holder.ActiveBossHolder;
import net.aminecraftdev.custombosses.utils.Debug;
import net.aminecraftdev.custombosses.utils.IMechanic;
import net.aminecraftdev.custombosses.utils.file.reader.SpigotYmlReader;
import org.bukkit.entity.LivingEntity;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Jun-18
 */
public class HealthMechanic implements IMechanic {

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
        return true;
    }
}
