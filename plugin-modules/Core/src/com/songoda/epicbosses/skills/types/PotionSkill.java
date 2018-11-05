package com.songoda.epicbosses.skills.types;

import com.google.gson.annotations.Expose;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.utils.potion.holder.PotionEffectHolder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 05-Nov-18
 */
public class PotionSkill extends Skill {

    @Expose @Getter @Setter private List<PotionEffectHolder> potions;

}
