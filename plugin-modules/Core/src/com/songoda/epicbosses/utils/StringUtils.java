package com.songoda.epicbosses.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 28-Apr-18
 */
public class StringUtils {

    private static StringUtils INSTANCE = new StringUtils();

    public List<String> splitString(String input, int splitSize) {
        List<String> messages = new ArrayList<>();
        int index = 0;

        while (index < input.length()) {
            messages.add(input.substring(index, Math.min(index + splitSize, input.length())));
            index += splitSize;
        }

        return messages;
    }

    public String stripColor(String string) {
        return ChatColor.stripColor(string);
    }

    public String translateColor(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }

    public String translateLocation(Location location) {
        String world = location.getWorld().getName();
        int x = location.getBlockX();
        int y = location.getBlockY();
        int z = location.getBlockZ();

        return Message.General_LocationFormat.toString()
                .replace("{world}", world)
                .replace("{x}", ""+x)
                .replace("{y}", ""+y)
                .replace("{z}", ""+z);
    }

    public Location fromStringToLocation(String input) {
        String[] split = input.split(",");

        if(split.length != 4) return null;

        String worldInput = split[0];
        String xInput = split[1];
        String yInput = split[2];
        String zInput = split[3];
        World world = Bukkit.getWorld(worldInput);

        if(NumberUtils.get().isInt(xInput) && NumberUtils.get().isInt(yInput) && NumberUtils.get().isInt(zInput)) {
            return new Location(world, Integer.valueOf(xInput), Integer.valueOf(yInput), Integer.valueOf(zInput));
        }

        return null;
    }

    public <T> String appendList(List<T> list) {
        Queue<T> queue = new LinkedList<>(list);
        StringBuilder stringBuilder = new StringBuilder();

        while(!queue.isEmpty()) {
            T object = queue.poll();

            if(object == null) continue;

            stringBuilder.append(object.toString());

            if(queue.isEmpty()) {
                stringBuilder.append(".");
            } else {
                stringBuilder.append(", ");
            }
        }

        return stringBuilder.toString();
    }

    public String formatString(String string) {
        if(string == null) return "null";

        string = string.toLowerCase();

        StringBuilder stringBuilder = new StringBuilder();

        if(string.contains(" ")) {
            for(String z : string.split(" ")) {
                stringBuilder.append(Character.toUpperCase(z.charAt(0))).append(z.substring(1).toLowerCase());
            }
        } else if(string.contains("_")) {
            String[] split = string.split("_");

            for(int i = 0; i < split.length; i++) {
                String z = split[i];

                stringBuilder.append(Character.toUpperCase(z.charAt(0))).append(z.substring(1).toLowerCase());

                if(i != (split.length - 1)) {
                    stringBuilder.append(" ");
                }
            }
        } else {
            return Character.toUpperCase(string.charAt(0)) + string.substring(1).toLowerCase();
        }

        return stringBuilder.toString();
    }

    public static StringUtils get() {
        return INSTANCE;
    }

}
