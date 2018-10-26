package com.songoda.epicbosses.utils.command;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 28-Apr-18
 */
public abstract class SubCommand {

    private List<String> aliases = new ArrayList<>();
    private String subCommand;

    public SubCommand(String subCommand) {
        this.subCommand = subCommand;
    }

    public SubCommand(String subCommand, String... subCommands) {
        this(subCommand);

        this.aliases.addAll(Arrays.asList(subCommands));
    }

    public String getSubCommand() {
        return this.subCommand;
    }

    public List<String> getAliases() {
        return this.aliases;
    }

    public boolean isSubCommand(String input) {
        input = input.toLowerCase();

        return (input.equals(this.subCommand) || this.aliases.contains(input));
    }

    public abstract void execute(CommandSender sender, String[] args);

}
