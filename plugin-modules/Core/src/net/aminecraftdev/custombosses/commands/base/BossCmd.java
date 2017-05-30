package net.aminecraftdev.custombosses.commands.base;

import net.aminecraftdev.custombosses.innerapi.command.SubCommand;
import net.aminecraftdev.custombosses.innerapi.command.SubCommandBuilder;
import net.aminecraftdev.custombosses.innerapi.command.builder.CommandService;
import net.aminecraftdev.custombosses.innerapi.command.builder.attributes.*;
import net.aminecraftdev.custombosses.utils.Message;
import org.bukkit.command.CommandSender;

import java.util.HashSet;
import java.util.Set;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 31-May-17
 */
@Name("boss")
@Alias({"bosses", "custombosses", "customboss"})
@Permission("bosses.command.use")
@Description("Access all boss related features of CustomBosses.")
@NoPermission("&cI'm sorry, but you do not have permission to perform this command. Please contact the server administrators if you believe that this is in error.")
public class BossCmd extends CommandService<CommandSender> implements SubCommandBuilder<CommandSender> {

    private final Set<SubCommand<CommandSender>> subCommands = new HashSet<>();

    public BossCmd() {
        super(BossCmd.class);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            Message.BossCommand_Help.msg(sender);
            return;
        }

        for(SubCommand<CommandSender> subCommand : subCommands) {
            if(subCommand.getSubCommand().equalsIgnoreCase(args[0])) {
                subCommand.execute(sender, args);
                return;
            }
        }

        Message.BossCommand_Help.msg(sender);
        return;
    }


    @Override
    public void register(SubCommand<CommandSender> subCommand) {
        subCommands.add(subCommand);
    }
}
