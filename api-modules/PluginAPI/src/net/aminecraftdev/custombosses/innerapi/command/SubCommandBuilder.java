package net.aminecraftdev.custombosses.innerapi.command;

import org.bukkit.command.CommandSender;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 31-May-17
 */
public interface SubCommandBuilder<T extends CommandSender> {

    void register(SubCommand<T> subCommand);

}
