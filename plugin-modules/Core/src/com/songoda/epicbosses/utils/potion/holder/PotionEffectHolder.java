package com.songoda.epicbosses.utils.potion.holder;

import com.google.gson.annotations.Expose;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Jun-18
 */
public class PotionEffectHolder {

    @Expose
    private String type;
    @Expose
    private Integer level, duration;

    public PotionEffectHolder(String type, Integer level, Integer duration) {
        this.type = type;
        this.level = level;
        this.duration = duration;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getLevel() {
        return this.level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

    public Integer getDuration() {
        return this.duration;
    }

    public void setDuration(Integer duration) {
        this.duration = duration;
    }
}
