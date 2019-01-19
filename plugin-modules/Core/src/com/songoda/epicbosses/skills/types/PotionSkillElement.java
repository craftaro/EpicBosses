package com.songoda.epicbosses.skills.types;

import com.google.gson.annotations.Expose;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.skills.interfaces.ISkillHandler;
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
public class PotionSkillElement implements ISkillHandler<PotionSkillElement> {

    @Expose @Getter @Setter private List<PotionEffectHolder> potions;

    private PotionEffectConverter potionEffectConverter;

    public PotionSkillElement(List<PotionEffectHolder> potions) {
        this.potions = potions;
        this.potionEffectConverter = new PotionEffectConverter();
    }

    @Override
    public void castSkill(Skill skill, PotionSkillElement customSkillElement, ActiveBossHolder activeBossHolder, List<LivingEntity> nearbyEntities) {
        List<PotionEffectHolder> potionElements = getPotions();

        if(this.potionEffectConverter == null) this.potionEffectConverter = new PotionEffectConverter();

        if(nearbyEntities == null || nearbyEntities.isEmpty()) return;
        if(potionElements == null) return;
        if(potionElements.isEmpty()) {
            Debug.SKILL_POTIONS_ARE_EMPTY.debug();
            return;
        }

        nearbyEntities.forEach(livingEntity -> potionElements.forEach(potionElement -> livingEntity.addPotionEffect(this.potionEffectConverter.from(potionElement))));
    }
}
