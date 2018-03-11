package net.aminecraftdev.custombosses.utils.parser;

import net.aminecraftdev.custombosses.entities.components.PotionEffectComponent;
import net.aminecraftdev.custombosses.utils.base.BaseParser;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 11-Mar-18
 */
public class PotionParser extends BaseParser<PotionEffectComponent, PotionEffect> {

    public PotionParser(PotionEffectComponent potionEffectComponent) {
        super(potionEffectComponent);
    }

    @Override
    public PotionEffect parse() {
        int duration = this.input.getDuration();
        int level = this.input.getLevel();
        PotionEffectType potionEffectType = this.input.getPotionEffectType();

        if(duration == -1) {
            duration = 15000;
        }

        return new PotionEffect(potionEffectType, duration*20, level-1);
    }
}
