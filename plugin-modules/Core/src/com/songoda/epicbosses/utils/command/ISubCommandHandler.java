package com.songoda.epicbosses.utils.command;

import org.bukkit.command.CommandSender;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 28-Apr-18
 */
public interface ISubCommandHandler {

    void registerSubCommand(SubCommand subCommand);

    boolean handleSubCommand(CommandSender commandSender, String[] args);

}
