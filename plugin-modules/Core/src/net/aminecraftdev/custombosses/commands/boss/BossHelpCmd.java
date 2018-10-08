package net.aminecraftdev.custombosses.commands.boss;

import net.aminecraftdev.custombosses.utils.Message;
import net.aminecraftdev.custombosses.utils.NumberUtils;
import net.aminecraftdev.custombosses.utils.command.SubCommand;
import org.bukkit.command.CommandSender;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Oct-18
 */
public class BossHelpCmd extends SubCommand {

    public BossHelpCmd() {
        super("help");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        int pageNumber = 0;

        if(args.length > 1) {
            Integer newNumber = NumberUtils.get().getInteger(args[1]);

            if(newNumber != null) pageNumber = newNumber;
        }

        switch (pageNumber) {
            default:
            case 1:
                Message.Boss_Help_Page1.msg(sender);
                break;
            case 2:
                Message.Boss_Help_Page2.msg(sender);
                break;
        }

        return;
    }
}
