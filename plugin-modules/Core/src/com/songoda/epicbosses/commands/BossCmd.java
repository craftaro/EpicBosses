package com.songoda.epicbosses.commands;

import com.songoda.epicbosses.utils.command.SubCommandService;
import com.songoda.epicbosses.utils.command.attributes.Alias;
import com.songoda.epicbosses.utils.command.attributes.Description;
import com.songoda.epicbosses.utils.command.attributes.Name;
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
