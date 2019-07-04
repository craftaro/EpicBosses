package com.songoda.epicbosses.mechanics.boss;

import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.entity.elements.MainStatsElement;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.mechanics.IBossMechanic;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.potion.PotionEffectConverter;
import com.songoda.epicbosses.utils.potion.holder.PotionEffectHolder;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Jun-18
 */
public class PotionMechanic implements IBossMechanic {

    private PotionEffectConverter potionEffectConverter;

    public PotionMechanic() {
        this.potionEffectConverter = new PotionEffectConverter();
    }

    @Override
    public boolean applyMechanic(BossEntity bossEntity, ActiveBossHolder activeBossHolder) {
        if(activeBossHolder.getLivingEntityMap().getOrDefault(1, null) == null) return false;

        for(EntityStatsElement entityStatsElement : bossEntity.getEntityStats()) {
            MainStatsElement mainStatsElement = entityStatsElement.getMainStats();
            LivingEntity livingEntity = activeBossHolder.getLivingEntity(mainStatsElement.getPosition());
            List<PotionEffectHolder> potionElements = entityStatsElement.getPotions();

            if(livingEntity == null) return false;

            if(potionElements != null && !potionElements.isEmpty()) {
                potionElements.forEach(potionElement -> {
                    PotionEffect potionEffect = this.potionEffectConverter.from(potionElement);
                    if (potionEffect == null) {
                        ServerUtils.get().logDebug("Failed to apply potion effect to boss: " +
                                "[type=" + potionElement.getType() + ",duration=" + potionElement.getDuration() + ",level=" + potionElement.getLevel() + "]");
                        return;
                    }

                    livingEntity.addPotionEffect(potionEffect);
                });
            }
        }

        return true;
    }
}
