package com.songoda.epicbosses.utils.potion;

import com.songoda.epicbosses.utils.IConverter;
import com.songoda.epicbosses.utils.potion.converters.PotionEffectTypeConverter;
import com.songoda.epicbosses.utils.potion.holder.PotionEffectHolder;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Jun-18
 */
public class PotionEffectConverter implements IConverter<PotionEffectHolder, PotionEffect> {

    private PotionEffectTypeConverter potionEffectTypeConverter;

    public PotionEffectConverter() {
        this.potionEffectTypeConverter = new PotionEffectTypeConverter();
    }

    @Override
    public PotionEffectHolder to(PotionEffect potionEffect) {
        PotionEffectType potionEffectType = potionEffect.getType();
        int duration = potionEffect.getDuration();
        int level = potionEffect.getAmplifier();

        return new PotionEffectHolder(this.potionEffectTypeConverter.to(potionEffectType), level+1, duration/20);
    }

    @Override
    public PotionEffect from(PotionEffectHolder potionHolder) {
        String potionEffectType = potionHolder.getType();
        Integer duration = potionHolder.getDuration();
        Integer level = potionHolder.getLevel();
        PotionEffectType potionEffectTypeConverted = this.potionEffectTypeConverter.from(potionEffectType);

        if(duration == null) duration = 5;
        if(level == null) level = 1;

        if(duration < 1) duration = (Integer.MAX_VALUE / 20);

        if(potionEffectTypeConverted == null) return null;
        if(potionEffectType != null) return new PotionEffect(potionEffectTypeConverted, duration*20, level-1);

        return null;
    }
}
