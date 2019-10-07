package com.songoda.epicbosses.commands.boss;

import com.songoda.epicbosses.managers.DebugManager;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.Permission;
import com.songoda.epicbosses.utils.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 09-Oct-18
 */
public class BossDebugCmd extends SubCommand {

    private DebugManager debugManager;

    public BossDebugCmd(DebugManager debugManager) {
        super("debug");

        this.debugManager = debugManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!Permission.debug.hasPermission(sender)) {
            Message.Boss_Debug_NoPermission.msg(sender);
            return;
        }

        if (!(sender instanceof Player)) {
            Message.General_MustBePlayer.msg(sender);
            return;
        }

        Player player = (Player) sender;
        String toggled;

        if (this.debugManager.isToggled(player.getUniqueId())) {
            this.debugManager.togglePlayerOff(player.getUniqueId());
            toggled = "Off";
        } else {
            this.debugManager.togglePlayerOn(player.getUniqueId());
            toggled = "On";
        }

        Message.Boss_Debug_Toggled.msg(player, toggled);
    }
}
