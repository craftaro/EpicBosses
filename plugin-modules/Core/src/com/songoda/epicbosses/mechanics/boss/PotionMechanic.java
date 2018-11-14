package com.songoda.epicbosses.mechanics.boss;

import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.entity.elements.MainStatsElement;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.utils.IMechanic;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.mechanics.IOptionalMechanic;
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
public class PotionMechanic implements IOptionalMechanic<BossEntity> {

    private PotionEffectConverter potionEffectConverter;

    public PotionMechanic() {
        this.potionEffectConverter = new PotionEffectConverter();
    }

    @Override
    public boolean applyMechanic(BossEntity bossEntity, ActiveBossHolder activeBossHolder) {
        if(activeBossHolder.getLivingEntityMap().getOrDefault(1, null) == null) return false;

        for(EntityStatsElement entityStatsElement : bossEntity.getEntityStats()) {
            MainStatsElement mainStatsElement = entityStatsElement.getMainStats();
            LivingEntity livingEntity = activeBossHolder.getLivingEntityMap().getOrDefault(mainStatsElement.getPosition(), null);
            List<PotionEffectHolder> potionElements = entityStatsElement.getPotions();

            if(livingEntity == null) return false;

            if(potionElements != null && !potionElements.isEmpty()) {
                potionElements.forEach(potionElement -> {
                    PotionEffect potionEffect = this.potionEffectConverter.from(potionElement);

                    System.out.println(potionEffect);
                    System.out.println(potionEffect.getAmplifier());
                    System.out.println(potionEffect.getDuration());
                    System.out.println(livingEntity);
                    livingEntity.addPotionEffect(potionEffect);
                });
            }
        }

        return true;
    }
}
