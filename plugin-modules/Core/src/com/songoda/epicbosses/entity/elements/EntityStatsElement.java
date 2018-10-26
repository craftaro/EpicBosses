package com.songoda.epicbosses.entity.elements;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import com.songoda.epicbosses.utils.potion.holder.PotionEffectHolder;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 21-Oct-18
 */
public class EntityStatsElement {

    @Expose @Getter @Setter private MainStatsElement mainStats;
    @Expose @Getter @Setter private EquipmentElement equipment;
    @Expose @Getter @Setter private HandsElement hands;
    @Expose @Getter @Setter private List<PotionEffectHolder> potions;

}
