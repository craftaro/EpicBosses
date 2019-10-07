package com.songoda.epicbosses.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;

import java.util.Arrays;
import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 04-Nov-18
 */
public class MessageUtils {

    private static MessageUtils INSTANCE = new MessageUtils();

    public void sendMessage(LivingEntity player, String... messages) {
        sendMessage(player, Arrays.asList(messages));
    }

    public void sendMessage(LivingEntity player, List<String> messages) {
        for(String s : messages) {
            player.sendMessage(StringUtils.get().translateColor(s));
        }
    }

    public void sendMessage(Location center, int radius, String... messages) {
        sendMessage(center, radius, Arrays.asList(messages));
    }

    public void sendMessage(Location center, int radius, List<String> messages) {
        messages.replaceAll(s -> s.replace('&', '§'));

        if(radius == -1) {
            Bukkit.getOnlinePlayers().forEach(player -> messages.forEach(string -> player.sendMessage(string)));
        } else {
            Bukkit.getOnlinePlayers().forEach(player -> {
                if((player.getWorld().equals(center.getWorld())) && (player.getLocation().distanceSquared(center) <= radius)) {
                    messages.forEach(string -> player.sendMessage(string));
                }
            });
        }
    }

    public static MessageUtils get() {
        return INSTANCE;
    }

}
