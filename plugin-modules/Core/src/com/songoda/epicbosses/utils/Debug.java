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

    NULL_CHECK("An object was found as null when it should not be null."),

    MAX_HEALTH("You cannot set the max health higher than {0}. You can adjust your max health in the spigot.yml file and restart your server to increase this."),
    MECHANIC_APPLICATION_FAILED("Some mechanics have failed to be applied. It got stuck at {0} mechanic."),
    BOSS_NAME_EXISTS("A boss was attempted to be created with the name {0} but there is already a boss with that name."),
    BOSS_CONTAINER_SAVE("The BossEntity map was saved in, {0} succeeded, and {1} failed. Listed below are the saved data which already existed in the container: \n{2}"),

    ATTEMPTED_TO_UPDATE_PLUGIN("Something has attempted to update the PLUGIN variable in the BossAPI class while it is already initialized."),
    ATTEMPTED_TO_SPAWN_WHILE_DISABLED("A boss/minion attempted to spawn while editing is enabled."),
    FAILED_TO_FIND_DROP_TABLE("The {0} boss has been killed however the specified drop table {1} wasn't found."),
    FAILED_TO_FIND_DROP_TABLE_TYPE("The {0} drop table type was not a valid drop table. Valid types are 'SPRAY', 'DROP', 'GIVE'."),
    FAILED_ATTEMPT_TO_SPAWN_BOSS("A boss has attempted to spawn but cannot spawn for the following reason: \n{0}"),
    FAILED_ATTEMPT_TO_STACK_BOSSES("A boss has failed to stack on top of another boss under the {0} boss configuration."),
    FAILED_TO_SAVE_THE_NEW_BOSS("The {0} with EntityType boss was successfully created but failed to save."),
    FAILED_TO_LOAD_BOSSCOMMANDMANAGER("The boss command manager tried to load again, but it has already loaded previously."),
    FAILED_TO_LOAD_BOSSLISTENERMANAGER("The boss listener manager tried to load again, but it has already loaded previously."),
    FAILED_TO_LOAD_CUSTOM_ITEM("The itemstack name that is provided ({0}) for the spawn item doesn't exist or wasn't found."),
    FAILED_TO_LOAD_MESSAGES("The messages name that is provided ({0}) doesn't exist or wasn't found."),
    FAILED_TO_LOAD_COMMANDS("The commands name that is provided ({0}) doesn't exist or wasn't found."),
    FAILED_TO_CREATE_ACTIVE_BOSS_HOLDER("Something went wrong while trying to create an active boss holder for someone who is trying to spawn a boss."),

    DROP_TABLE_FAILED_INVALID_NUMBER("The specified position ({0}) on the drop table is not a valid number."),
    DROP_TABLE_FAILED_TO_GET_ITEM("The drop table failed to get the specific item for the list."),

    MECHANIC_TYPE_NOT_STORED("This mechanic type is not stored, therefore will not be applied. Valid mechanic types are IOptionalMechanic and IPrimaryMechanic."),

    SKILL_COMMANDS_ARE_EMPTY("The commands list for the skill {0} is empty."),
    SKILL_POTIONS_ARE_EMPTY("The potions list for the skill {0} is empty."),
    SKILL_CAGE_INVALID_MATERIAL("Invalid block type {0} for the skill {1}.");

    private static CustomBosses PLUGIN;

    private String message;

    Debug(String message) {
        this.message = message;
    }

    public void debug(Object... objects) {
        int current = 0;
        String message = this.message;

        for(Object object : objects) {
            String placeholder = "{" + current + "}";

            message = message.replace(placeholder, object.toString());
            current += 1;
        }

        String finalMsg = message;

//        if(PLUGIN.isDebug()) {
            ServerUtils.get().logDebug(finalMsg);
//        }

        PLUGIN.getDebugManager().getToggledPlayers().forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);

            if(player == null) return;

            player.sendMessage(finalMsg);
        });
    }

    public static void setPlugin(CustomBosses plugin) {
        PLUGIN = plugin;
    }

}
