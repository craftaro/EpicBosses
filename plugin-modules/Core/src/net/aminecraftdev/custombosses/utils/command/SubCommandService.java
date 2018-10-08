package net.aminecraftdev.custombosses.utils.command;

import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 28-Apr-18
 */
public abstract class SubCommandService<T extends CommandSender> extends CommandService<T> implements ISubCommandHandler {

    private List<SubCommand> subCommands = new ArrayList<>();

    public SubCommandService(Class<? extends CommandService> cmd) {
        super(cmd);
    }

    @Override
    public void registerSubCommand(SubCommand subCommand) {
        this.subCommands.add(subCommand);
    }

    @Override
    public boolean handleSubCommand(CommandSender commandSender, String[] args) {
        for(SubCommand subCommand : this.subCommands) {
            if(subCommand.isSubCommand(args[0])) {
                subCommand.execute(commandSender, args);
                return true;
            }
        }

        return false;
    }
}
