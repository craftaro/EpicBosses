package net.aminecraftdev.custombosses.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 04-Oct-18
 */
public enum Message {

    General_MustBePlayer("&c&l(!) &cYou must be a player to use this command."),

    Boss_Create_EntityTypeNotFound("&c&l(!) &cThe specified entity type {0} was not found. If you think this is an error please contact &fAMinecraftDev&c."),
    Boss_Create_InvalidArgs("&c&l(!) &cYou must use &n/boss create [name] [entity] &c to create a boss."),
    Boss_Create_NameAlreadyExists("&c&l(!) &cA boss already exists with the name {0}."),
    Boss_Create_NoEntitySpecified("&c&l(!) &cNo entity type was specified. Make sure to add an entity type! Possible entity types are: \n&7{0}"),
    Boss_Create_NoPermission("&c&l(!) &cYou do not have access to this command."),
    Boss_Create_SomethingWentWrong("&c&l(!) &cSomething went wrong in the API class while finalising the boss creation."),
    Boss_Create_SuccessfullyCreated("&e&lCustomBosses &8» &7A boss has successfully been created with the name &f{0}&7 and the entity type &f{1}&7."),

    Boss_Debug_NoPermission("&c&l(!) &cYou do not have access to this command."),
    Boss_Debug_Toggled("&e&lCustomBosses &8» &7You have toggled debug mode for &fCustomBosses &7to {0}."),

    Boss_Help_Page1(
            "&8&m----*--------&6&l[ &e&lBoss Help &7(Page 1) &6&l]&8&m--------*----\n" +
            "&e/boss help (page) &8» &7Displays boss commands.\n" +
            "&e/boss create [name] [entity] &8» &7Start the creation of a boss.\n" +
            "&e/boss edit (name) &8» &7Edit the specified boss.\n" +
            "&e/boss info [name] &8» &7Shows information on the specified boss.\n" +
            "&e/boss nearby (radius) &8» &7Shows the nearby bosses.\n" +
            "&e/boss reload &8» &7Reloads the boss plugin.\n" +
            "&7\n" +
            "&7Use /boss help 2 to view the next page.\n" +
            "&8&m----*-----------------------------------*----"),
    Boss_Help_Page2(
            "&8&m----*--------&6&l[ &e&lBoss Help &7(Page 2) &6&l]&8&m--------*----\n" +
            "&e/boss spawn [name] (location) &8» &7Spawns a boss at your location or the specified location.\n" +
            " &7&o(Separate location with commas, an example is: world,0,100,0)\n" +
            "&e/boss time (section) &8» &7Shows the time left till next auto spawn.\n" +
            "&e/boss droptable &8» &7Shows all current drop tables.\n" +
            "&e/boss items &8» &7Shows all current custom items.\n" +
            "&e/boss skills &8» &7Shows all current set skills.\n" +
            "&7\n" +
            "&8&m----*-----------------------------------*----"),

    Boss_Reload_NoPermission("&c&l(!) &cYou do not have access to this command."),
    Boss_Reload_Successful("&e&lCustomBosses &8» &7All boss data has been reloaded. The process took &f{0}ms&7.");

    private static FileConfiguration LANG;

    private final String path, msg;

    Message(String string) {
        this.path = this.name().replace("_", ".");
        this.msg = string;
    }

    public static void setFile(FileConfiguration configuration) {
        LANG = configuration;
    }

    @Override
    public String toString() {
        return StringUtils.get().translateColor(LANG.getString(this.path, this.msg));
    }

    public String toString(Object... args) {
        return getFinalized(StringUtils.get().translateColor(LANG.getString(this.path, this.msg)), args);
    }

    public String getDefault() {
        return this.msg;
    }

    public String getPath() {
        return this.path;
    }

    public void msg(CommandSender p, Object... order) {
        String s = toString(order);

        if(s.contains("\n")) {
            String[] split = s.split("\n");

            for(String inner : split) {
                sendMessage(p, inner, order);
            }
        } else {
            sendMessage(p, s, order);
        }
    }

    public void broadcast(Object... order) {
        String s = toString();

        if(s.contains("\n")) {
            String[] split = s.split("\n");

            for(String inner : split) {
                for(Player player : Bukkit.getOnlinePlayers()) {
                    sendMessage(player, inner, order);
                }
            }
        } else {
            for(Player player : Bukkit.getOnlinePlayers()) {
                sendMessage(player, s, order);
            }
        }
    }

    private String getFinalized(String string, Object... order) {
        int current = 0;

        for(Object object : order) {
            String placeholder = "{" + current + "}";

            if(string.contains(placeholder)) {
                if(object instanceof CommandSender) {
                    string = string.replace(placeholder, ((CommandSender) object).getName());
                }
                else if(object instanceof OfflinePlayer) {
                    string = string.replace(placeholder, ((OfflinePlayer) object).getName());
                }
                else if(object instanceof Location) {
                    Location location = (Location) object;
                    String repl = location.getWorld().getName() + ", " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ();

                    string = string.replace(placeholder, repl);
                }
                else if(object instanceof String) {
                    string = string.replace(placeholder, StringUtils.get().translateColor((String) object));
                }
                else if(object instanceof Long) {
                    string = string.replace(placeholder, ""+object);
                }
                else if(object instanceof Double) {
                    string = string.replace(placeholder, ""+object);
                }
                else if(object instanceof Integer) {
                    string = string.replace(placeholder, ""+object);
                }
                else if(object instanceof ItemStack) {
                    string = string.replace(placeholder, getItemStackName((ItemStack) object));
                } else if(object instanceof Boolean) {
                    string = string.replace(placeholder, ""+object);
                }
            }

            current++;
        }

        return string;
    }

    private void sendMessage(CommandSender target, String string, Object... order) {
        target.sendMessage(getFinalized(string, order));
    }

    private String getItemStackName(ItemStack itemStack) {
        String name = itemStack.getType().toString().replace("_", " ");

        if(itemStack.hasItemMeta() && itemStack.getItemMeta().hasDisplayName()) {
            return itemStack.getItemMeta().getDisplayName();
        }

        return name;
    }

}
