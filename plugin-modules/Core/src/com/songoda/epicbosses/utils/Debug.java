package com.songoda.epicbosses.utils;

import com.songoda.epicbosses.CustomBosses;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 08-Mar-18
 */
public enum Debug {

    PLAIN("{0}"),

    NULL_CHECK("An object was found as null when it should not be null."),

    MAX_HEALTH("You cannot set the max health higher than {0}. You can adjust your max health in the spigot.yml file and restart your server to increase this."),
    MECHANIC_APPLICATION_FAILED("Some mechanics have failed to be applied. It got stuck at {0} mechanic."),
    BOSS_NAME_EXISTS("A boss was attempted to be created with the name {0} but there is already a boss with that name."),
    DROPTABLE_NAME_EXISTS("A droptable was attempted to be created with the name {0} but there is already a drop table with that name."),
    AUTOSPAWN_NAME_EXISTS("A autospawn was attempted to be created with the name {0} but there is already an auto spawn with that name."),
    SKILL_NAME_EXISTS("A skill was attempted to be created with the name {0} but there is already a skill with that name."),
    MINION_NAME_EXISTS("A minion was attempted to be created with the name {0} but there is already a minion with that name."),
    BOSS_CONTAINER_SAVE("The BossEntity map was saved in, {0} succeeded, and {1} failed. Listed below are the saved data which already existed in the container: \n{2}"),
    MINION_CONTAINER_SAVE("The MinionEntity map was saved in, {0} succeeded, and {1} failed. Listed below are the saved data which already existed in the container: \n{2}"),

    ATTEMPTED_TO_UPDATE_PLUGIN("Something has attempted to update the PLUGIN variable in the BossAPI class while it is already initialized."),
    ATTEMPTED_TO_SPAWN_WHILE_DISABLED("A boss/minion attempted to spawn while editing is enabled."),
    FAILED_TO_APPLY_MECHANIC("The {0} mechanic failed to be applied to the entity."),
    FAILED_TO_FIND_DROP_TABLE("The {0} boss has been killed however the specified drop table {1} wasn't found."),
    FAILED_TO_FIND_DROP_TABLE_TYPE("The {0} drop table type was not a valid drop table. Valid types are 'SPRAY', 'DROP', 'GIVE'."),
    FAILED_ATTEMPT_TO_SPAWN_BOSS("A boss has attempted to spawn but cannot spawn for the following reason: \n{0}"),
    FAILED_ATTEMPT_TO_STACK_BOSSES("A boss has failed to stack on top of another boss under the {0} boss configuration."),
    FAILED_TO_SAVE_THE_NEW_BOSS("The {0} with EntityType boss was successfully created but failed to save."),
    FAILED_TO_SAVE_THE_NEW_MINION("The {0} with EntityType minion was successfully created but failed to save."),
    FAILED_TO_SPAWN_MINIONS_FROM_SKILL("The skill {0} failed to spawn minions as the minionsToSpawn list was empty or null. Please make sure this has got at least one minion in it before trying to call from the skill."),
    FAILED_TO_FIND_MINION("The skill {0} has failed to find the specified minion {1}."),
    FAILED_TO_SPAWN_MINION("The skill {0} has failed to spawn the minion {1}."),
    FAILED_TO_LOAD_BOSSCOMMANDMANAGER("The boss command manager tried to load again, but it has already loaded previously."),
    FAILED_TO_LOAD_BOSSLISTENERMANAGER("The boss listener manager tried to load again, but it has already loaded previously."),
    FAILED_TO_LOAD_CUSTOM_ITEM("The itemstack name that is provided ({0}) for the spawn item doesn't exist or wasn't found."),
    FAILED_TO_LOAD_MESSAGES("The messages name that is provided ({0}) doesn't exist or wasn't found."),
    FAILED_TO_CREATE_NEWPOSITION("A new position with the number of {0} was attempted to be added to the {1} droptable, however there was already a position filled there."),
    FAILED_TO_LOAD_COMMANDS("The commands name that is provided ({0}) doesn't exist or wasn't found."),
    FAILED_TO_CREATE_ACTIVE_BOSS_HOLDER("Something went wrong while trying to create an active boss holder for someone who is trying to spawn a boss."),
    FAILED_TO_GIVE_SPAWN_EGG("{0} tried to obtain a spawn egg for the boss {1} but it has not been set yet."),
    FAILED_TO_CONNECT_TO_VAULT("Something went wrong while trying to connect to Vault. Please make sure you have an Economy and Permission plugin connected to your Vault plugin such as Essentials and GroupManager."),
    FAILED_TO_OBTAIN_THE_SKILL_HANDLER("Something went wrong when trying to detect the skill handler for {0}."),
    FAILED_TO_GIVE_CUSTOM_ITEM("{0} tried to obtain a custom item from the Custom Items list but failed due to the item not being set properly."),
    FAILED_TO_FIND_ASSIGNED_CUSTOMSKILLHANDLER("Failed to find the assigned custom skill handler that matches the skill name {0}."),

    DROP_TABLE_FAILED_INVALID_NUMBER("The specified position ({0}) on the drop table is not a valid number."),
    DROP_TABLE_FAILED_TO_GET_ITEM("The drop table failed to get the specific item for the list."),
    DROP_TABLE_FAILED_TO_GET_TYPE("The drop table that was attempted to be made with type {0} failed as it's not a valid drop table type."),

    MECHANIC_TYPE_NOT_STORED("This mechanic type is not stored, therefore will not be applied. Valid mechanic types are IOptionalMechanic and IPrimaryMechanic."),

    SKILL_COMMANDS_ARE_EMPTY("The commands list for the skill {0} is empty."),
    SKILL_POTIONS_ARE_EMPTY("The potions list for the skill {0} is empty."),
    SKILL_CAGE_INVALID_MATERIAL("Invalid block type {0} for the skill {1}."),
    SKILL_EMPTY_SKILLS("The skills list for the {0} boss is empty."),
    SKILL_NOT_FOUND("The specified skill was not found!"),

    AUTOSPAWN_INTERVALNOTREAL("The specified interval of {0} is not a valid integer for the auto spawn interval table {1}.");

    private static CustomBosses PLUGIN;

    private String message;

    Debug(String message) {
        this.message = message;
    }

    public void debug(Object... objects) {
        int current = 0;
        String message = this.message;

        for(Object object : objects) {
            if(object == null) continue;

            String placeholder = "{" + current + "}";

            message = message.replace(placeholder, object.toString());
            current += 1;
        }

        String finalMsg = message;

        if(PLUGIN.isDebug()) {
            ServerUtils.get().logDebug(finalMsg);
        }

        PLUGIN.getDebugManager().getToggledPlayers().forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);

            if(player == null) return;

            player.sendMessage(finalMsg);
        });
    }

    public static void debugMessage(String message) {
        PLAIN.debug(message);
    }

    public static void setPlugin(CustomBosses plugin) {
        PLUGIN = plugin;
    }

}
