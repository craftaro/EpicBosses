package net.aminecraftdev.custombosses.innerapi;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 31-May-17
 */
public class FireworkUtils {

    public static final Color getColour(String s) {
        if(s.equalsIgnoreCase("AQUA")) return Color.AQUA;
        if(s.equalsIgnoreCase("BLACK")) return Color.BLACK;
        if(s.equalsIgnoreCase("BLUE")) return Color.BLUE;
        if(s.equalsIgnoreCase("FUCHSIA")) return Color.FUCHSIA;
        if(s.equalsIgnoreCase("GRAY")) return Color.GRAY;
        if(s.equalsIgnoreCase("GREEN")) return Color.GREEN;
        if(s.equalsIgnoreCase("LIME")) return Color.LIME;
        if(s.equalsIgnoreCase("MAROON")) return Color.MAROON;
        if(s.equalsIgnoreCase("NAVY")) return Color.NAVY;
        if(s.equalsIgnoreCase("OLIVE")) return Color.OLIVE;
        if(s.equalsIgnoreCase("ORANGE")) return Color.ORANGE;
        if(s.equalsIgnoreCase("PURPLE")) return Color.PURPLE;
        if(s.equalsIgnoreCase("RED")) return Color.RED;
        if(s.equalsIgnoreCase("SILVER")) return Color.SILVER;
        if(s.equalsIgnoreCase("TEAL")) return Color.TEAL;
        if(s.equalsIgnoreCase("WHITE")) return Color.WHITE;
        if(s.equalsIgnoreCase("YELLOW")) return Color.YELLOW;

        return null;
    }

    public static final void spawnFirework(Location location, ConfigurationSection configurationSection) {
        Firework firework = (Firework) location.getWorld().spawn(location, Firework.class);
        String colour = configurationSection.getString("color").toUpperCase();
        Color fireworkColor = getColour(colour);
        int fireworkPower = configurationSection.getInt("power");
        boolean fireworkFlicker = configurationSection.getBoolean("flicker");
        boolean fireworkTrail = configurationSection.getBoolean("trail");
        FireworkEffect.Type fireworkType = FireworkEffect.Type.valueOf(configurationSection.getString("type").toUpperCase());

        FireworkMeta fireworkMeta = firework.getFireworkMeta();

        fireworkMeta.addEffect(FireworkEffect.builder()
                .flicker(fireworkFlicker)
                .trail(fireworkTrail)
                .with(fireworkType)
                .withColor(fireworkColor)
                .build());

        fireworkMeta.setPower(fireworkPower);
        firework.setFireworkMeta(fireworkMeta);
    }

    public static final void spawnFirework(Player player, ConfigurationSection configurationSection) {
        spawnFirework(player.getLocation(), configurationSection);
    }

}
