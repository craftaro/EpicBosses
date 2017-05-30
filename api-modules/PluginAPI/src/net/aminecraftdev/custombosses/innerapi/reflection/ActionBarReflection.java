package net.aminecraftdev.custombosses.innerapi.reflection;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by charl on 28-Apr-17.
 */
public class ActionBarReflection extends ReflectionUtils {

    private static Plugin _plugin;
    private static boolean _use1_8_R1Methods = false;
    private static boolean _use1_7Methods = false;

    static {
        if(getAPIVersion().equalsIgnoreCase("v1_8_R1")) {
            _use1_8_R1Methods = true;
        } else if(getAPIVersion().startsWith("v1_7_")) {
            _use1_7Methods = true;
        }
    }

    public static final Plugin getPlugin() {
        return _plugin;
    }

    public static final Object getPlayerConnection(Player player) {
        try {
            Class<?> c1 = getOBCClass("entity.CraftPlayer");
            Object p = c1.cast(player); // (CraftPlayer) player;
            Method m1 = c1.getDeclaredMethod("getHandle"); //CraftPlayer.getHandle();
            Object h = m1.invoke(p); // EntityPlayer h = ((CraftPlayer) player).getHandle();
            Field f1 = h.getClass().getDeclaredField("playerConnection");
            return f1.get(h);

        }catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final void sendActionBar(Player player, String message) {
        if(player == null) {
            return;
        }

        if(message == null) {
            return;
        }

        try {
            Object ppoc;
            Class<?> c4 = getNMSClass("PacketPlayOutChat");
            Class<?> c5 = getNMSClass("Packet");

            if(_use1_8_R1Methods || _use1_7Methods) {
                Class<?> c2 = getNMSClass("ChatSerializer");
                Class<?> c3 = getNMSClass("IChatBaseComponent");
                Method m3 = c2.getDeclaredMethod("a", String.class);
                Object cbc = c3.cast(m3.invoke(c2, "{\"text\": \"" + message + "\"}"));

                if(_use1_8_R1Methods) {
                    ppoc = c4.getConstructor(new Class<?>[] {c3, byte.class}).newInstance(cbc, (byte) 2);
                } else {
                    ppoc = c4.getConstructor(new Class<?>[] {c3}).newInstance(cbc);
                }

                Object playerConnection = getPlayerConnection(player); // PlayerConnection pc = h.playerConnection;
                Method m5 = playerConnection.getClass().getDeclaredMethod("sendPacket", c5); // PlayerConnection.sendPacket(Packet<?> packet);

                m5.invoke(playerConnection, ppoc); // pc.sendPacket(ppoc);
            } else {
                Class<?> c2 = getNMSClass("ChatComponentText");
                Class<?> c3 = getNMSClass("IChatBaseComponent");
                Object o = c2.getConstructor(new Class<?>[] {String.class}).newInstance(message);

                ppoc = c4.getConstructor(new Class<?>[] {c3, byte.class}).newInstance(o, (byte) 2);

                Object playerConnection = getPlayerConnection(player); // PlayerConnection pc = h.playerConnection;
                Method m5 = playerConnection.getClass().getDeclaredMethod("sendPacket", c5); // PlayerConnection.sendPacket(Packet<?> packet);
                m5.invoke(playerConnection, ppoc); // pc.sendPacket(ppoc);
            }
        } catch(Exception e) {
            e.printStackTrace();
        }
    }

    public static final void sendActionBar(final Player player, final String message, int duration, Plugin plugin) {
        sendActionBar(player, message);
        _plugin = plugin;

        if (duration >= 0) {
            // Sends empty message at the end of the duration. Allows messages shorter than 3 seconds, ensures precision.
            new BukkitRunnable() {

                public void run() {
                    sendActionBar(player, "");
                }
            }.runTaskLater(getPlugin(), duration + 1);
        }

        // Re-sends the messages every 3 seconds so it doesn't go away from the player's screen.
        while (duration > 60) {
            duration -= 60;
            int sched = duration % 60;
            new BukkitRunnable() {

                public void run() {
                    sendActionBar(player, message);
                }
            }.runTaskLater(getPlugin(), (long) sched);
        }
    }

    public static final void sendActionBarToAllPlayers(String message, Plugin plugin) {
        sendActionBarToAllPlayers(message, -1, plugin);
    }

    public static final void sendActionBarToAllPlayers(String message, int duration, Plugin plugin) {
        for(Player player : Bukkit.getOnlinePlayers()) {
            sendActionBar(player, message, duration, plugin);
        }
    }


}