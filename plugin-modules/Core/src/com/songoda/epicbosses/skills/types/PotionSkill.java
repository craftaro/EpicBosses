package com.songoda.epicbosses.skills.types;

import com.google.gson.annotations.Expose;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.potion.PotionEffectConverter;
import com.songoda.epicbosses.utils.potion.holder.PotionEffectHolder;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 05-Nov-18
 */
public class PotionSkill extends Skill {

    @Expose @Getter @Setter private List<PotionEffectHolder> potions;

    private final PotionEffectConverter potionEffectConverter;

    public PotionSkill() {
        this.potionEffectConverter = new PotionEffectConverter();
    }

    @Override
    public void castSkill(ActiveBossHolder activeBossHolder, List<LivingEntity> nearbyEntities) {
        List<PotionEffectHolder> potionElements = getPotions();

        if(potionElements == null) return;
        if(potionElements.isEmpty()) {
            Debug.SKILL_POTIONS_ARE_EMPTY.debug(getDisplayName());
            return;
        }

        nearbyEntities.forEach(livingEntity -> potionElements.forEach(potionElement -> livingEntity.addPotionEffect(this.potionEffectConverter.from(potionElement))));
    }
}
