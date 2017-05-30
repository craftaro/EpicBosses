package net.aminecraftdev.custombosses.innerapi.command;

import org.bukkit.command.CommandSender;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 31-May-17
 */
public abstract class SubCommand<T extends CommandSender> {

    private String subCommand;

    public SubCommand(String subCommand) {
        this.subCommand = subCommand;
    }

    public String getSubCommand() {
        return subCommand;
    }

    public abstract void execute(T sender, String[] args);

}
