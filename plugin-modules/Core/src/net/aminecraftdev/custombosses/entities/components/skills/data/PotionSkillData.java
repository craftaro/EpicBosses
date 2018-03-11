package net.aminecraftdev.custombosses.entities.components.skills.data;

import com.google.gson.annotations.Expose;
import net.aminecraftdev.custombosses.entities.components.PotionEffectComponent;
import net.aminecraftdev.custombosses.utils.base.BaseSkillData;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 11-Mar-18
 */
public class PotionSkillData extends BaseSkillData {

    @Expose private List<PotionEffectComponent> potionEffects;

    public PotionSkillData(List<PotionEffectComponent> potionEffectComponents) {
        this.potionEffects = potionEffectComponents;
    }

    public List<PotionEffectComponent> getPotionEffects() {
        return potionEffects;
    }

}
