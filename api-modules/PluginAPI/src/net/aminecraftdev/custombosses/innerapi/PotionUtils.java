package net.aminecraftdev.custombosses.innerapi;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 31-May-17
 */
public class PotionUtils {

    public static final PotionEffect getPotionEffect(ConfigurationSection configurationSection) {
        if(configurationSection.contains("enabled") && !configurationSection.getBoolean("enabled")) return null;

        int level = configurationSection.getInt("level");
        int duration = configurationSection.getInt("duration");
        String type = configurationSection.getString("type");

        if(PotionEffectType.getByName(type) == null) {
            return null;
        }

        PotionEffectType potionEffectType = PotionEffectType.getByName(type);

        if(duration == -1) {
            duration = 100000;
        } else {
            duration *= 20;
        }

        level -= 1;

        return new PotionEffect(potionEffectType, duration, level);
    }

}
