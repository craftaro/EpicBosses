package net.aminecraftdev.custombosses.utils.potion.converters;

import net.aminecraftdev.custombosses.utils.IConverter;
import org.bukkit.potion.PotionEffectType;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Jun-18
 */
public class PotionEffectTypeConverter implements IConverter<String, PotionEffectType> {

    @Override
    public String to(PotionEffectType potionEffectType) {
        return potionEffectType.getName().toUpperCase();
    }

    @Override
    public PotionEffectType from(String s) {
        return PotionEffectType.getByName(s);
    }
}
