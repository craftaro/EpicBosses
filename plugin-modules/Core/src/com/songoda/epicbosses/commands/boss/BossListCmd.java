package com.songoda.epicbosses.commands.boss;

import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.Permission;
import com.songoda.epicbosses.utils.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Oct-18
 */
public class BossListCmd extends SubCommand {

    private BossPanelManager bossPanelManager;

    public BossListCmd(BossPanelManager bossPanelManager) {
        super("list", "show");

        this.bossPanelManager = bossPanelManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!Permission.admin.hasPermission(sender)) {
            Message.Boss_List_NoPermission.msg(sender);
            return;
        }

        if(!(sender instanceof Player)) {
            Message.General_MustBePlayer.msg(sender);
            return;
        }

        Player player = (Player) sender;

        this.bossPanelManager.getBosses().openFor(player);
    }
}
