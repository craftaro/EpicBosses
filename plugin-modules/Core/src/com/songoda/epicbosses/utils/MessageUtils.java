package com.songoda.epicbosses.utils;

import com.songoda.core.compatibility.ServerVersion;
import com.songoda.core.utils.NMSUtils;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 04-Nov-18
 */
public class MessageUtils {

    private static MessageUtils INSTANCE = new MessageUtils();

    public static MessageUtils get() {
        return INSTANCE;
    }

    public void sendMessage(LivingEntity player, String... messages) {
        sendMessage(player, Arrays.asList(messages));
    }

    public void sendMessage(LivingEntity player, List<String> messages) {
        for (String s : messages) {
            player.sendMessage(StringUtils.get().translateColor(s));
        }
    }

    public void sendMessage(Location center, int radius, String... messages) {
        sendMessage(center, radius, Arrays.asList(messages));
    }

    public void sendMessage(Location center, int radius, List<String> messages) {
        messages.replaceAll(s -> s.replace('&', '§'));

        if (radius == -1) {
            Bukkit.getOnlinePlayers().forEach(player -> messages.forEach(string -> player.sendMessage(string)));
        } else {
            Bukkit.getOnlinePlayers().forEach(player -> {
                if ((player.getWorld().equals(center.getWorld())) && (player.getLocation().distanceSquared(center) <= radius)) {
                    messages.forEach(player::sendMessage);
                }
            });
        }
    }

    public void sendActionBar(Location center, int radius, String message) {
        if (radius == -1) {
            if (center.getWorld() == null) return;
            center.getWorld().getPlayers().forEach(p -> sendActionBar(p, message));
        } else {
            Bukkit.getOnlinePlayers().forEach(player -> {
                if ((player.getWorld().equals(center.getWorld())) && (player.getLocation().distanceSquared(center) <= radius)) {
                    sendActionBar(player, message);
                }
            });
        }
    }

    private Class<?> clazzPacketPlayOutChat, clazzChatSerializer, clazzIChatBaseComponent;
    private Method methodA;

    public void sendActionBar(Player player, String message) {
        message = ChatColor.translateAlternateColorCodes('&', message);

        if (ServerVersion.isServerVersionAbove(ServerVersion.V1_8)) {
            player.spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
        } else {
            try {
                // Cache nms
                if (clazzPacketPlayOutChat == null) {
                    clazzPacketPlayOutChat = NMSUtils.getNMSClass("PacketPlayOutChat");

                    clazzChatSerializer = NMSUtils.getNMSClass("IChatBaseComponent$ChatSerializer");
                    clazzIChatBaseComponent = NMSUtils.getNMSClass("IChatBaseComponent");

                    methodA = clazzChatSerializer.getDeclaredMethod("a", String.class);
                }

                Object chatBaseComponent = clazzIChatBaseComponent.cast(methodA.invoke(clazzChatSerializer, "{\"text\": \"" + message + "\"}"));
                Object packetPlayOutChat = clazzPacketPlayOutChat.getConstructor(new Class<?>[]{clazzIChatBaseComponent, byte.class}).newInstance(chatBaseComponent, (byte) 2);

                NMSUtils.sendPacket(player, packetPlayOutChat);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}