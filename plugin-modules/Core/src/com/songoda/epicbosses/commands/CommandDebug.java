package com.songoda.epicbosses.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.epicbosses.managers.DebugManager;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.Permission;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 09-Oct-18
 */
public class CommandDebug extends AbstractCommand {

    private DebugManager debugManager;

    public CommandDebug(DebugManager debugManager) {
        super(true, "debug");
        this.debugManager = debugManager;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
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
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender commandSender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "boss.reload";
    }

    @Override
    public String getSyntax() {
        return "/boss reload";
    }

    @Override
    public String getDescription() {
        return "Reloads EpicBosses and its configurations.";
    }
}
