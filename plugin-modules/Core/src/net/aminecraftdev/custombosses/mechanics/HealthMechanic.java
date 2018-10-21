package net.aminecraftdev.custombosses.mechanics;

import net.aminecraftdev.custombosses.entity.BossEntity;
import net.aminecraftdev.custombosses.entity.elements.EntityStatsElement;
import net.aminecraftdev.custombosses.entity.elements.MainStatsElement;
import net.aminecraftdev.custombosses.holder.ActiveBossHolder;
import net.aminecraftdev.custombosses.utils.Debug;
import net.aminecraftdev.custombosses.utils.IMechanic;
import net.aminecraftdev.custombosses.utils.ServerUtils;
import net.aminecraftdev.custombosses.utils.file.reader.SpigotYmlReader;
import net.aminecraftdev.custombosses.utils.mechanics.IPrimaryMechanic;
import org.bukkit.entity.LivingEntity;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Jun-18
 */
public class HealthMechanic implements IPrimaryMechanic {

    @Override
    public boolean applyMechanic(BossEntity bossEntity, ActiveBossHolder activeBossHolder) {
        if(activeBossHolder.getLivingEntityMap().getOrDefault(1, null) == null) return false;

        double maxHealthSetting = (double) SpigotYmlReader.get().getObject("settings.attribute.maxHealth.max");

        for(EntityStatsElement entityStatsElement : bossEntity.getEntityStats()) {
            MainStatsElement mainStatsElement = entityStatsElement.getMainStats();
            LivingEntity livingEntity = activeBossHolder.getLivingEntityMap().getOrDefault(mainStatsElement.getPosition(), null);
            double maxHealth = mainStatsElement.getHealth();

            if(livingEntity == null) return false;

            if(maxHealth > maxHealthSetting) {
                Debug.MAX_HEALTH.debug(maxHealthSetting);
                return false;
            }


            livingEntity.setMaxHealth(maxHealth);
            livingEntity.setHealth(maxHealth);
        }

        return true;
    }
}
