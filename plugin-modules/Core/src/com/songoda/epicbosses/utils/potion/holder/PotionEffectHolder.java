package com.songoda.epicbosses.utils.potion.holder;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Jun-18
 */
public class PotionEffectHolder {

    @Expose @Getter @Setter private String type;
    @Expose @Getter @Setter private Integer level, duration;

    public PotionEffectHolder(String type, Integer level, Integer duration) {
        this.type = type;
        this.level = level;
        this.duration = duration;
    }

}
