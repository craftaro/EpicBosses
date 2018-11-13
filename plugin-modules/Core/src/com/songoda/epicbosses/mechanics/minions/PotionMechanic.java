package com.songoda.epicbosses.mechanics.minions;

import com.songoda.epicbosses.entity.MinionEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.entity.elements.MainStatsElement;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.utils.mechanics.IOptionalMechanic;
import com.songoda.epicbosses.utils.potion.PotionEffectConverter;
import com.songoda.epicbosses.utils.potion.holder.PotionEffectHolder;
import org.bukkit.entity.LivingEntity;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Jun-18
 */
public class PotionMechanic implements IOptionalMechanic<MinionEntity> {

    private PotionEffectConverter potionEffectConverter;

    public PotionMechanic() {
        this.potionEffectConverter = new PotionEffectConverter();
    }

    @Override
    public boolean applyMechanic(MinionEntity minionEntity, ActiveBossHolder activeBossHolder) {
        if(activeBossHolder.getLivingEntityMap().getOrDefault(1, null) == null) return false;

        for(EntityStatsElement entityStatsElement : minionEntity.getEntityStats()) {
            MainStatsElement mainStatsElement = entityStatsElement.getMainStats();
            LivingEntity livingEntity = activeBossHolder.getMinionEntityMap().getOrDefault(mainStatsElement.getPosition(), null);
            List<PotionEffectHolder> potionElements = entityStatsElement.getPotions();

            if(livingEntity == null) return false;

            if(potionElements != null && !potionElements.isEmpty()) {
                potionElements.forEach(potionElement -> livingEntity.addPotionEffect(this.potionEffectConverter.from(potionElement)));
            }
        }

        return true;
    }
}
