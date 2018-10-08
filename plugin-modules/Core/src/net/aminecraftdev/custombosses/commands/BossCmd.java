package net.aminecraftdev.custombosses.commands;

import net.aminecraftdev.custombosses.utils.command.SubCommandService;
import net.aminecraftdev.custombosses.utils.command.attributes.Alias;
import net.aminecraftdev.custombosses.utils.command.attributes.Description;
import net.aminecraftdev.custombosses.utils.command.attributes.Name;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 18-Jul-18
 */
@Name("boss")
@Alias({"bosses", "b", "bs"})
@Description("Used to handle all CustomBosses related commands.")

public class BossCmd extends SubCommandService<CommandSender> {

    public BossCmd() {
        super(BossCmd.class);
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            Bukkit.dispatchCommand(sender, "boss help");
            return;
        }

        if(handleSubCommand(sender, args)) return;

        Bukkit.dispatchCommand(sender, "boss help");
    }
}
