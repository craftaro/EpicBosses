package net.aminecraftdev.custombosses.utils;

import org.bukkit.ChatColor;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 28-Apr-18
 */
public class StringUtils {

    private static StringUtils INSTANCE = new StringUtils();

    public String stripColor(String string) {
        return ChatColor.stripColor(string);
    }

    public String translateColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public static StringUtils get() {
        return INSTANCE;
    }

}
