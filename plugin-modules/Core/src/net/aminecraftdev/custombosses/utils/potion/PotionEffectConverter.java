package net.aminecraftdev.custombosses.utils.potion;

import net.aminecraftdev.custombosses.utils.IConverter;
import net.aminecraftdev.custombosses.utils.potion.converters.PotionEffectTypeConverter;
import net.aminecraftdev.custombosses.utils.potion.holder.PotionEffectHolder;
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

        if(potionEffectType != null && duration != null && level != null) return new PotionEffect(this.potionEffectTypeConverter.from(potionEffectType), level-1, (duration*20));

        return null;
    }
}
