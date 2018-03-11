package net.aminecraftdev.custombosses.entities.components;

import com.google.gson.annotations.Expose;
import org.bukkit.potion.PotionEffectType;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 11-Mar-18
 */
public class PotionEffectComponent {

    @Expose private PotionEffectType potionEffectType;
    @Expose private int duration, level;

    public PotionEffectComponent(PotionEffectType potionEffectType, int duration, int level) {
        this.potionEffectType = potionEffectType;
        this.duration = duration;
        this.level = level;
    }

    public PotionEffectType getPotionEffectType() {
        return potionEffectType;
    }

    public void setPotionEffectType(PotionEffectType potionEffectType) {
        this.potionEffectType = potionEffectType;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }
}
