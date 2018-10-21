package net.aminecraftdev.custombosses.utils.potion.converters;

import net.aminecraftdev.custombosses.utils.IConverter;
import net.aminecraftdev.custombosses.utils.PotionEffectFinder;
import org.bukkit.potion.PotionEffectType;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Jun-18
 */
public class PotionEffectTypeConverter implements IConverter<String, PotionEffectType> {

    @Override
    public String to(PotionEffectType potionEffectType) {
        PotionEffectFinder potionEffectFinder = PotionEffectFinder.getByEffect(potionEffectType);

        if(potionEffectFinder == null) return null;

        return potionEffectFinder.getFancyName();
    }

    @Override
    public PotionEffectType from(String s) {
        PotionEffectFinder potionEffectFinder = PotionEffectFinder.getByName(s);

        if(potionEffectFinder == null) return null;

        return potionEffectFinder.getPotionEffectType();
    }
}
