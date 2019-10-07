package com.songoda.epicbosses.commands.boss;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.Permission;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.command.SubCommand;
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
        if(Permission.admin.hasPermission(sender) || Permission.help.hasPermission(sender)) {
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
                case 3:
                    Message.Boss_Help_Page3.msg(sender);
                    break;
                case 4:
                    Message.Boss_Help_Page4.msg(sender);
                    break;
            }

            return;
        }

        sender.sendMessage(StringUtils.get().translateColor("EpicBosses &7Version " + EpicBosses.getInstance().getDescription().getVersion() + " Created with <3 by &5&l&oSongoda"));
        Message.Boss_Help_NoPermission.msg(sender);
    }
}
