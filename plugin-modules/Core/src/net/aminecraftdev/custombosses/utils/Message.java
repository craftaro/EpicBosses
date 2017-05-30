package net.aminecraftdev.custombosses.utils;

import net.aminecraftdev.custombosses.innerapi.message.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 31-May-17
 */
public enum Message {

    BossCommand_Help(
            " &4\n" +
            "&7&m-----------------&b&l[ &a&lCustomBosses&r &b&l]&7&m-----------------&r\n" +
            " &4\n" +
            "&6/boss spawn (type) (x) (y) (z) (world) &e&m-&f&o Spawn a boss at the coordinates.\n" +
            "&7/boss spawn (type) (player) &e&m-&f&o Spawn a boss on a player.\n" +
            "&6/boss time &e&m-&f&o View the time until the next boss auto-spawn.\n" +
            "&6/boss reload &e&m-&f&o Reloads the YML files for CustomBosses.\n" +
            "&6/boss killall &e&m-&f&o Kills all currently spawned bosses/minions.\n" +
            "&6/boss list &e&m-&f&o Opens a GUI with all the available boss eggs.\n" +
            "&6/giveegg (player) (boss) [amount] &e&m-&f&o Gives the specified player a boss egg.\n" +
            " &4\n" +
            "&7&m-----------------&b&l[ &a&lCustomBosses&r &b&l]&7&m-----------------&r\n" +
            " &4"),
    BossCommand_KillAll_Blocked("&c&l[!] &cThe KillAll command is blocked!"),
    BossCommand_KillAll_Killed("&a&l[!] &fYou've killed &e{0}&f bosses/minions."),
    BossCommand_List("&a&l[!] &fYou are now viewing all custom bosses."),
    BossCommand_Reload("&a&l[!] &fYou just reloaded the files for CustomBosses."),
    BossCommand_Spawn_InvalidBoss("&c&l[!] &cThe specified boss doesn't exist!"),
    BossCommand_Spawn_InvalidLocation("&c&l[!] &cThe specified location is invalid!"),
    BossCommand_Spawn_InvalidWorld("&c&l[!] &cThe specified world doesn't exist!"),
    BossCommand_Spawn_Spawned("&a&l[!] &fYou have spawned a &e{0}&f boss at &e{1}&f."),
    BossCommand_Time_Display("&a&l[!] &fThere is currently &e{0}&f left till next spawn."),
    BossCommand_Time_NotEnabled("&c&l[!] &cThe boss time command has been disabled."),

    GiveEggCommand_Given_Receiver("&a&l[!] &fYou have received &e{0}x&f boss egg(s)."),
    GiveEggCommand_Given_Sender("&a&l[!] &fYou have given &e{0}x&f boss egg(s) to &e{1}&f."),
    GiveEggCommand_Help(
            " &4\n" +
            "&7&m-----------------&b&l[ &a&lCustomBosses&r &b&l]&7&m-----------------&r\n" +
            " &4\n" +
            "&6/boss spawn (type) (x) (y) (z) (world) &e&m-&f&o Spawn a boss at the coordinates.\n" +
            "&7/boss spawn (type) (player) &e&m-&f&o Spawn a boss on a player.\n" +
            "&6/boss time &e&m-&f&o View the time until the next boss auto-spawn.\n" +
            "&6/boss reload &e&m-&f&o Reloads the YML files for CustomBosses.\n" +
            "&6/boss killall &e&m-&f&o Kills all currently spawned bosses/minions.\n" +
            "&6/boss list &e&m-&f&o Opens a GUI with all the available boss eggs.\n" +
            "&6/giveegg (player) (boss) [amount] &e&m-&f&o Gives the specified player a boss egg.\n" +
            " &4\n" +
            "&7&m-----------------&b&l[ &a&lCustomBosses&r &b&l]&7&m-----------------&r\n" +
            " &4"),
    GiveEggCommand_InvalidBoss("&c&l[!] &cThe specified boss doesn't exist!"),

    Settings_InvalidNumber("&c&l[!] &cYou have specified an invalid number!"),
    Settings_InventorySpace("&c&l[!] &cYou do not have enough inventory space for that!"),
    Settings_ItemStack_Max("&c&l[!] &cThe maximum ItemStack size is 64!"),
    Settings_ItemStack_Min("&c&l[!] &cThe minimum ItemStack size is 1!"),
    Settings_ItemStack_Null("&c&l[!] &cThe specified ItemStack cannot be null or air!"),
    Settings_MustBePlayer("&c&l[!] &cYou must be a player to use that feature!"),
    Settings_NotOnline("&c&l[!] &cThe player you specified is offline or doesn't exist!");

    private String path;
    private String msg;
    private static FileConfiguration LANG;

    Message(String string) {
        this.path = "Messages." + this.name().replace("_", ".");
        this.msg = string;
    }

    public static void setFile(FileConfiguration configuration) {
        LANG = configuration;
    }

    @Override
    public String toString() {
        String s = ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, msg));

        return s;
    }

    public String toString(Object... args) {
        String s = ChatColor.translateAlternateColorCodes('&', LANG.getString(this.path, msg));

        return getMessageReplaced(s, args);
    }

    public String getDefault() {
        return this.msg;
    }

    public String getPath() {
        return this.path;
    }

    public void msg(CommandSender p, Object... order) {
        String s = toString();

        if(s.contains("\n")) {
            String[] split = s.split("\n");

            for(String inner : split) {
                messageString(p, inner, order);
            }
        } else {
            messageString(p, s, order);
        }
    }

    public void broadcast(Object... order) {
        String s = toString();

        if(s.contains("\n")) {
            String[] split = s.split("\n");

            for(String s1 : split) {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    messageString(player, s1, order);
                }
            }
        } else {
            for(Player player : Bukkit.getOnlinePlayers()) {
                messageString(player, s, order);
            }
        }
    }



    /**
     *
     * Private Enum methods
     *
     *
     */
    private void messageString(CommandSender player, String s, Object... order) {
        if(s.equalsIgnoreCase("")) return;

        if(s.contains("{c}")) {
            MessageUtils.sendCenteredMessage(player, getMessageReplaced(s, order).replace("{c}", ""));
        } else {
            player.sendMessage(getMessageReplaced(s, order));
        }
    }

    private String getMessageReplaced(String s, Object... order) {
        int current = 0;

        for(Object object : order) {
            String placeholder = "{" + current + "}";

            if(s.contains(placeholder)) {
                if(object instanceof CommandSender) {
                    s = s.replace(placeholder, ((CommandSender) object).getName());
                }

                if(object instanceof Location) {
                    Location location = (Location) object;
                    String repl = location.getWorld().getName() + ", " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ();

                    s = s.replace(placeholder, repl);
                }

                if(object instanceof String) {
                    s = s.replace(placeholder, MessageUtils.translateString((String) object));
                }

                if(object instanceof Integer) {
                    s = s.replace(placeholder, ""+object);
                }

                if(object instanceof ItemStack) {
                    s = s.replace(placeholder, getItemStackName((ItemStack) object));
                }
            }

            current++;
        }

        return s;
    }

    private String getItemStackName(ItemStack itemStack) {
        String name = itemStack.getType().toString().replace("_", " ");

        if(itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
            return itemStack.getItemMeta().getDisplayName();
        }

        return name;
    }

}
