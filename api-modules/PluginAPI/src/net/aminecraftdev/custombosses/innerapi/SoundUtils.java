package net.aminecraftdev.custombosses.innerapi;

import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Player;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 31-May-17
 */
public class SoundUtils {

    public static final void playSound(ConfigurationSection configurationSection, Player player) {
        boolean enabled = configurationSection.getBoolean("enabled", true);
        String type = configurationSection.getString("type");
        int volume = configurationSection.getInt("volume");
        int pitch = configurationSection.getInt("pitch");

        if(!enabled) return;

        player.playSound(player.getLocation(), Sound.valueOf(type.toUpperCase()), (float) volume, (float) pitch);
    }

    public static final Sound getSound(ConfigurationSection configurationSection) {
        boolean enabled = configurationSection.getBoolean("enabled", true);
        String type = configurationSection.getString("type");

        if(!enabled) return null;

        return Sound.valueOf(type.toUpperCase());
    }


}
