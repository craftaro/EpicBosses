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
        if(activeBossHolder.getLivingEntityMap().getOrDefault(0, null) == null) return false;
        if(bossEntity.getMainStats().isEmpty() || bossEntity.getMainStats().get(0) == null) return false;

        LivingEntity livingEntity = activeBossHolder.getLivingEntityMap().getOrDefault(0, null);
        double maxHealthSetting = (double) SpigotYmlReader.get().getObject("settings.attribute.maxHealth.max");
        double maxHealth = bossEntity.getMainStats().get(0).getHealth();

        if(maxHealth > maxHealthSetting) {
            Debug.MAX_HEALTH.debug(maxHealthSetting);
            return false;
        }

        livingEntity.setMaxHealth(maxHealth);
        livingEntity.setHealth(maxHealth);
        return true;
    }
}
