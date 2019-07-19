package com.songoda.epicbosses.utils;

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

    General_LocationFormat("{world}, {x}, {y}, {z}"),
    General_MustBePlayer("&c&l(!) &cYou must be a player to use this command."),
    General_NotOnline("&c&l(!) &cThe specified player, {0}, is not online or a valid player."),
    General_CannotSpawn("&c&l(!) &cYou cannot spawn a boss at this location! &c&l(!)"),
    General_NotNumber("&c&l(!) &cThe number you have provided is not a proper number."),
    General_Disarmed("&4&l(!) &f&lYOU HAVE BEEN DISARMED! CHECK THE GROUND AROUND YOU FOR YOUR ITEM!"),
    General_CannotBecauseLive("&c&l(!) &cYou cannot do this because the boss is not in editing mode!"),
    General_TimeLayout("{hours}h, {mins}m, {sec}s"),

    Boss_AutoSpawn_ToggleEditing("&b&lEpicBosses &8» &7You have toggled the editing mode for the &f{0}&7 auto spawn to &f{1}&7."),
    Boss_AutoSpawn_MustToggleEditing("&c&l(!) &cIn order to modify aspects of this plugin you must first toggle editing mode so that it disables the current auto spawn system."),
    Boss_AutoSpawn_NotCompleteEnough("&c&l(!) &cThe auto spawn is not set up enough to be enabled. Please make sure it has: &fA Spawn Entity(s) and Type&c before you try and enable the auto spawn."),
    Boss_AutoSpawn_SpawnRate("&b&lEpicBosses &8» &7You have {0} the spawn rate of the auto spawn to &f{1}&7."),
    Boss_AutoSpawn_InvalidLocation("&c&l(!) &cThe specified location string, &f{0}&c, is invalid. A correct example should be &fworld,10,150,-30&c . If you want to cancel the location update type &f- &cand you will be brought back to the settings menu."),
    Boss_AutoSpawn_SetLocation("&b&lEpicBosses &8» &7Your next input in to chat will be the location for the auto spawn. If you enter &f-&7 it will cancel the updating of the location. Use the format &fworld,10,150,-30&7."),
    Boss_AutoSpawn_SetPlaceholder("&b&lEpicBosses &8» &7Your next input in to chat will be the placeholder for the auto spawn. If you enter &f-&7 it will cancel the updating of the placeholder."),

    Boss_Create_EntityTypeNotFound("&c&l(!) &cThe specified entity type {0} was not found. If you think this is an error please contact &fASongoda&c."),
    Boss_Create_InvalidArgs("&c&l(!) &cYou must use &n/boss create [name] [entity] &c to create a boss."),
    Boss_Create_NameAlreadyExists("&c&l(!) &cA boss already exists with the name {0}."),
    Boss_Create_NoEntitySpecified("&c&l(!) &cNo entity type was specified. Make sure to add an entity type! Possible entity types are: \n&7{0}"),
    Boss_Create_NoPermission("&c&l(!) &cYou do not have access to this command."),
    Boss_Create_SomethingWentWrong("&c&l(!) &cSomething went wrong in the API class while finalising the boss creation."),
    Boss_Create_SuccessfullyCreated("&b&lEpicBosses &8» &7A boss has successfully been created with the name &f{0}&7 and the entity type &f{1}&7."),

    Boss_Debug_NoPermission("&c&l(!) &cYou do not have access to this command."),
    Boss_Debug_Toggled("&b&lEpicBosses &8» &7You have toggled debug mode for &fEpicBosses &7to {0}."),

    Boss_DropTable_NoPermission("&c&l(!) &cYou do not have access to this command."),
    Boss_DropTable_AddedNewReward("&b&lEpicBosses &8» &7You have added a new reward to the drop table &f{0}&7. Now opening the editing panel for the new reward."),
    Boss_DropTable_RewardChance("&b&lEpicBosses &8» &7You have {0} the chance for the reward section for &f{1}&7 to &f{2}%&7."),
    Boss_DropTable_RewardRemoved("&b&lEpicBosses &8» &7You have removed the reward section from the drop table."),
    Boss_DropTable_SetMaxDrops("&b&lEpicBosses &8» &7You have {0} the max drops for the drop table to &f{1}&7."),
    Boss_DropTable_SetMaxDistance("&b&lEpicBosses &8» &7You have {0} the max distance for the drop table to &f{1}&7."),
    Boss_DropTable_SetRandomDrops("&b&lEpicBosses &8» &7You have set the random drops mode for the drop table to &f{0}&7."),
    Boss_DropTable_DropAddedNewReward("&b&lEpicBosses &8» &7You have added a new reward to the drop table &f{0}&7. Now opening the editing panel for the new reward."),
    Boss_DropTable_DropRewardChance("&b&lEpicBosses &8» &7You have {0} the chance for the reward section for &f{1}&7 to &f{2}%&7."),
    Boss_DropTable_DropRewardRemoved("&b&lEpicBosses &8» &7You have removed the reward section from the drop drop table."),
    Boss_DropTable_GiveRandomDrops("&b&lEpicBosses &8» &7You have set the random drops for the &f{0}&7 drop table to &f{1}&7."),
    Boss_DropTable_GiveRandomCommands("&b&lEpicBosses &8» &7You have set the random commands for the &f{0}&7 drop table to &f{1}&7."),
    Boss_DropTable_GiveMaxDrops("&b&lEpicBosses &8» &7You have {0} the max drops for the &f{1}&7 damage section to &f{1}&7."),
    Boss_DropTable_GiveMaxCommands("&b&lEpicBosses &8» &7You have {0} the max commands for the &f{1}&7 damage section to &f{1}&7."),
    Boss_DropTable_GiveRequiredPercentage("&b&lEpicBosses &8» &7You have {0} the required percentage for the &f{1}&7 damage section to &f{1}&7."),

    Boss_Edit_NoPermission("&c&l(!) &cYou do not have access to this command."),
    Boss_Edit_ItemStackHolderNull("&c&l(!) &cThe itemstack name that is provided for the spawn item doesn't exist or wasn't found."),
    Boss_Edit_CannotSpawn("&c&l(!) &cYou cannot spawn this boss while editing is enabled. If you think this is a mistake please contact an administrator to disable the editing of the boss."),
    Boss_Edit_CannotBeModified("&c&l(!) &cYou cannot modify this aspect because you do not have editing mode enabled on this boss."),
    Boss_Edit_Toggled("&b&lEpicBosses &8» &7You have toggled the editing mode for &f{0}&7 to &f{1}&7."),
    Boss_Edit_NotCompleteEnough("&c&l(!) &cThe boss is not set up enough to be enabled. Please make sure it has the following things: &f{0}&c Once these things are set try toggling again."),
    Boss_Edit_DoesntExist("&c&l(!) &cThe specified boss does not exist. Please try again with the proper name. If you cannot figure it out please check the bosses.json file to find the one you're looking for."),
    Boss_Edit_Price("&b&lEpicBosses &8» &7Please input the new price of the &f{0}&7 Boss Entity. Please do not add commas and only use numbers. To cancel this input in to chat &f- &7."),
    Boss_Edit_PriceSet("&b&lEpicBosses &8» &7You have set the price of &f{0}&7 to &a$&f{1}&7."),

    Boss_GiveEgg_NoPermission("&c&l(!) &cYou do not have access to this command."),
    Boss_GiveEgg_InvalidArgs("&c&l(!) &cYou must use &n/boss giveegg [name] [player] (amount)&c to give an egg."),
    Boss_GiveEgg_InvalidBoss("&c&l(!) &cThe specified boss is not a valid type."),
    Boss_GiveEgg_NotSet("&c&l(!) &cThe spawn item for the {0} boss has not been set yet."),
    Boss_GiveEgg_Given("&b&lEpicBosses &8» &7You have given {0} {1}x {2}'s boss spawn item."),
    Boss_GiveEgg_Received("&b&lEpicBosses &8» &7You have received {0}x {1} boss spawn item(s)."),

    Boss_Help_NoPermission("&c&l(!) &cYou do not have access to this command."),
    Boss_Help_Page1(
            "&8&m----*--------&3&l[ &b&lBoss Help &7(Page 1/4) &3&l]&8&m--------*----\n" +
            "&b/boss help (page) &8» &7Displays boss commands.\n" +
            "&b/boss create [name] [entity] &8» &7Start the creation of a boss.\n" +
            "&b/boss edit (name) &8» &7Edit the specified boss.\n" +
            "&b/boss info [name] &8» &7Shows information on the specified boss.\n" +
            "&b/boss nearby (radius) &8» &7Shows the nearby bosses.\n" +
            "&b/boss reload &8» &7Reloads the boss plugin.\n" +
            "&7\n" +
            "&7Use /boss help 2 to view the next page.\n" +
            "&8&m----*-----------------------------------*----"),
    Boss_Help_Page2(
            "&8&m----*--------&3&l[ &b&lBoss Help &7(Page 2/4) &3&l]&8&m--------*----\n" +
            "&b/boss spawn [name] (location) &8» &7Spawns a boss at your" +
            " location or the specified location.\n" +
            "&7&o(Separate location with commas, an example is: world,0,100,0)\n" +
            "&b/boss droptable &8» &7Shows all current drop tables.\n" +
            "&b/boss items &8» &7Shows all current custom items.\n" +
                    "&b/boss skills &8» &7Shows all current set skills.\n" +
            "&b/boss killall (world) &8» &7Kills all bosses/minions.\n" +
            "&7\n" +
            "&7Use /boss help 3 to view the next page.\n" +
            "&8&m----*-----------------------------------*----"),
    Boss_Help_Page3(
            "&8&m----*--------&3&l[ &b&lBoss Help &7(Page 3/4) &3&l]&8&m--------*----\n" +
            "&b/boss time [section] &8» &7Shows the time left till next auto spawn.\n" +
            "&b/boss giveegg [name] [player] (amount) &8» &7Used to be given a " +
            "spawn item of the boss.\n" +
            "&b/boss list &8» &7Shows all the list of current boss entities.\n" +
            "&b/boss new skill [name] [type] [mode] &8» &7Create a new skill section.\n" +
            "&b/boss new droptable [name] [type] &8» &7Create a new drop table section.\n" +
            "&7\n" +
            "&7Use /boss help 4 to view the next page.\n" +
            "&8&m----*-----------------------------------*----"),
    Boss_Help_Page4(
            "&8&m----*--------&3&l[ &b&lBoss Help &7(Page 4/4) &3&l]&8&m--------*----\n" +
            "&b/boss new command [name] [commands] &8» &7Used to create a new command section.\n" +
            "&7&o(To add a new line use &7||&7&o in-between the messages.)\n" +
            "&b/boss new message [name] [messages] &8» &7Used to create a new message section.\n" +
            "&7&o(To add a new line use &7||&7&o in-between the messages.)\n" +
            "&7/boss new autospawn [name] &8» &7Used to create a new auto spawn section.\n" +
            "&b/boss debug &8» &7Used to toggle the debug aspect of the plugin.\n" +
            "&7\n" +
            "&7\n" +
            "&7Use /boss help [page] to view the next page.\n" +
            "&8&m----*-----------------------------------*----"),

    Boss_Info_NoPermission("&c&l(!) &cYou do not have access to this command."),
    Boss_Info_InvalidArgs("&c&l(!) &cYou must use &n/boss info [name]&c to view info on a boss."),
    Boss_Info_CouldntFindBoss("&c&l(!) &cThe specified boss was not able to be retrieved, please try again."),
    Boss_Info_Display(
            "&8&m----*--------&3&l[ &b&l{0} Info &3&l]&8&m--------*----\n" +
            "&bEditing: &f{1}\n" +
            "&bCurrently Active: &f{2}\n" +
            "&bComplete enough to spawn: &f{3}"),

    Boss_Items_NoPermission("&c&l(!) &cYou do not have access to this command."),
    Boss_Items_CannotBeRemoved("&c&l(!) &cThe selected item cannot be removed because it is still used in {0} different positions on the bosses."),
    Boss_Items_DefaultCannotBeRemoved("&c&l(!) &cThe selected item cannot be removed because it is the default item for something in one of the boss menu's."),
    Boss_Items_Removed("&b&lEpicBosses &8» &7The selected item has been removed from the EpicBosses custom items database."),
    Boss_Items_Added("&b&lEpicBosses &8» &7You have added an item to the EpicBosses custom items database."),
    Boss_Items_Cloned("&b&lEpicBosses &8» &7You have cloned an item in the EpicBosses custom items database and the clone item now has the name of &f{0}&7."),
    Boss_Items_AlreadySet("&c&l(!) &cYou must take out the item you have set to add before you can add another."),

    Boss_KillAll_WorldNotFound("&c&l(!) &cThe specified world was not found. If you'd like to kill every boss/minion just use &f/boss killall&c without any arguments."),
    Boss_KillAll_KilledAll("&b&lEpicBosses &8» &7You have killed the boss(es) and minion(s) that were currently active on the server."),
    Boss_KillAll_KilledWorld("&b&lEpicBosses &8» &7You have killed the boss(es) and minion(s) that were in the world {1}."),
    Boss_KillAll_NoPermission("&c&l(!) &cYou do not have access to this command."),

    Boss_List_NoPermission("&c&l(!) &cYou do not have access to this command."),

    Boss_Menu_NoPermission("&c&l(!) &cYou do not have access to this command."),

    Boss_Messages_SetRadiusOnSpawn("&b&lEpicBosses &8» &7You have just {0} the radius for the onSpawn message to &f{1}&7."),
    Boss_Messages_SetRadiusOnDeath("&b&lEpicBosses &8» &7You have just {0} the radius for the onDeath message to &f{1}&7."),
    Boss_Messages_SetOnlyShowOnDeath("&b&lEpicBosses &8» &7You have just {0} the only show amount for the onDeath message to &f{1}&7."),
    Boss_Messages_SetTauntRadius("&b&lEpicBosses &8» &7You have just {0} the radius for the taunt message to &f{1}&7."),
    Boss_Messages_SetTauntDelay("&b&lEpicBosses &8» &7You have just {0} the delay for the taunt message to &f{1}&7."),

    Boss_Nearby_NoPermission("&c&l(!) &cYou do not have access to this command."),
    Boss_Nearby_MaxRadius("&c&l(!) &cYou cannot check for bosses any further then &f{0}&c away from your position."),
    Boss_Nearby_NoneNearby("&b&lEpicBosses &8» &7There is currently no nearby bosses."),
    Boss_Nearby_Near("&b&lEpicBosses &8» &7Nearby bosses: &f{0}."),

    Boss_New_NoPermission("&c&l(!) &cYou do not have access to this command."),
    Boss_New_InvalidArgs("&c&l(!) &cInvalid arguments! You must use &n/boss new droptable [name] (type)&c or &n/boss new skill [name]&c!"),
    Boss_New_CreateArgumentsDropTable("&b&lEpicBosses &8» &7Create a new droptable with the command &f/boss new droptable [name] [type]&7."),
    Boss_New_CreateArgumentsSkill("&b&lEpicBosses &8» &7Create a new skill with the command &f/boss new skill [name] [type] [mode]&7."),
    Boss_New_CreateArgumentsMessage("&b&lEpicBosses &8» &7Create a new message with the command &f/boss new message [name] [message(s)]. \n&7&oUse &f|| &7&oto reference a new line."),
    Boss_New_CreateArgumentsCommand("&b&lEpicBosses &8» &7Create a new command with the command &f/boss new command [name] [command(s)]. \n&7&oUse &f|| &7&oto reference a new line."),
    Boss_New_CreateArgumentsAutoSpawn("&b&lEpicBosses &8» &7Create a new auto spawn with the command &f/boss new autospawn [name]."),
    Boss_New_AlreadyExists("&c&l(!) &cThe specified {0} name already exists. Please try another name."),
    Boss_New_InvalidDropTableType("&c&l(!) &cThe specified DropTable type is invalid. Please use &fGive, Drop, Spray&c."),
    Boss_New_InvalidSkillType("&c&l(!) &cThe specified Skill type is invalid. Please use &fCustom, Command, Group, Potion&c."),
    Boss_New_InvalidSkillMode("&c&l(!) &cThe specified Skill mode is invalid. Please use &fAll, Random, One, Boss&c."),
    Boss_New_DropTable("&b&lEpicBosses &8» &7You have created a new drop table with the name &f{0}&7 and type &f{1}&7."),
    Boss_New_AutoSpawn("&b&lEpicBosses &8» &7You have created a new auto spawn with the name &f{0}&7."),
    Boss_New_Skill("&b&lEpicBosses &8» &7You have created a new skill with the name &f{0}&7 and type &f{1}&7."),
    Boss_New_Command("&b&lEpicBosses &8» &7You have created a new command with the name &f{0}&7."),
    Boss_New_Message("&b&lEpicBosses &8» &7You have created a new message with the name &f{0}&7."),
    Boss_New_SomethingWentWrong("&c&l(!) &cSomething went wrong while trying to create a new &f{0}&c."),

    Boss_Reload_NoPermission("&c&l(!) &cYou do not have access to this command."),
    Boss_Reload_Successful("&b&lEpicBosses &8» &7All boss data has been reloaded. The process took &f{0}ms&7."),

    Boss_Shop_Disabled("&c&l(!) &cThe boss shop is currently disabled."),
    Boss_Shop_NoPermission("&c&l(!) &cYou do not have access to this command."),
    Boss_Shop_NotEnoughBalance("&c&l(!) &cYou do not have enough money to make this purchase! You need &a$&f{0}&c more."),
    Boss_Shop_Purchased("&b&lEpicBosses &8» &7You have purchased &f1x {0}&7."),

    Boss_Skills_NoPermission("&c&l(!) &cYou do not have access to this command."),
    Boss_Skills_SetChance("&b&lEpicBosses &8» &7You have {0} the overall chance for the skill map to &f{1}%&7."),
    Boss_Skills_SetMultiplier("&b&lEpicBosses &8» &7You have {0} the multiplier to &f{1}&7."),
    Boss_Skills_SetRadius("&b&lEpicBosses &8» &7You have {0} the radius for the skill to &f{1}&7."),
    Boss_Skills_SetMode("&b&lEpicBosses &8» &7You have set the skill mode to &f{0}&7."),
    Boss_Skills_SetDisplayName("&b&lEpicBosses &8» &7Your next input in to chat will be the display name for the skill. If you enter &f-&7 it will remove/clear the display name of the skill. For color codes use the &f& &7sign."),
    Boss_Skills_NotCompleteEnough("&c&l(!) &cThe potion effect was unable to be created due to it not having enough information. Please make sure that the potion effect type is selected."),
    Boss_Skills_SetCommandChance("&b&lEpicBosses &8» &7You have {0} the chance for the command skill to &f{1}%&7."),
    Boss_Skills_SetMinionAmount("&b&lEpicBosses &8» &7You have {0} the amount of minions to spawn from this skill to &f{1}&7."),

    Boss_Spawn_NoPermission("&c&l(!) &cYou do not have access to this command."),
    Boss_Spawn_InvalidArgs("&c&l(!) &cYou must use &n/boss spawn [name] (location)&c to spawn a boss."),
    Boss_Spawn_InvalidLocation("&c&l(!) &cThe location string you have entered is not a valid location string. A valid location string should look like this: &fworld,100,65,100"),
    Boss_Spawn_MustBePlayer("&c&l(!) &cTo use this command without an input of location you must be a player."),
    Boss_Spawn_InvalidBoss("&c&l(!) &cThe specified boss is not a valid type."),
    Boss_Spawn_Spawned("&c&l(!) &cYou have spawned a {0} boss at {1}."),

    Boss_Statistics_SetChance("&b&lEpicBosses &8» &7You have {0} the health of the entity to &f{1}&7."),
    Boss_Statistics_SetDisplayName("&b&lEpicBosses &8» &7Your next input in to chat will be the display name for the entity. If you enter &f-&7 it will remove/clear the display name of the entity. For color codes use the &f& &7sign."),
    Boss_Statistics_SetEntityFinder("&b&lEpicBosses &8» &7You have selected &f{0}&7 as the entity type for the boss."),

    Boss_Time_NoPermission("&c&l(!) &cYou do not have access to this command."),
    Boss_Time_InvalidArgs("&c&l(!) &cYou must use &n/boss time [section]&c to check the time left on a boss spawn."),
    Boss_Time_DoesntExist("&c&l(!) &cThe specified interval spawn system doesn't exist or editing has been toggled on so the section isn't ticking at the moment. Please use one of the following active system names: &f{0}&c."),
    Boss_Time_CurrentlyActive("&b&lEpicBosses &8» &7There is currently a boss spawned from this section so the countdown will not begin for the next to spawn until the last boss is killed."),
    Boss_Time_GetRemainingTime("&b&lEpicBosses &8» &7There is currently &f{0}&7 remaining on the &f{1}&7 interval spawn system.");

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
