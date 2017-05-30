package net.aminecraftdev.custombosses.commands;

import net.aminecraftdev.custombosses.innerapi.command.SubCommand;
import net.aminecraftdev.custombosses.utils.Message;
import org.bukkit.command.CommandSender;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 31-May-17
 */
public class BossHelpCmd extends SubCommand<CommandSender> {

    public BossHelpCmd(String subCommand) {
        super(subCommand);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Message.BossCommand_Help.msg(sender);
        return;
    }
}
