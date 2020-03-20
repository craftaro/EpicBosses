package com.songoda.epicbosses.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.epicbosses.managers.BossPanelManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Oct-18
 */
public class CommandList extends AbstractCommand {

    private BossPanelManager bossPanelManager;

    public CommandList(BossPanelManager bossPanelManager) {
        super(true, "list", "show");
        this.bossPanelManager = bossPanelManager;
    }

    @Override
    protected AbstractCommand.ReturnType runCommand(CommandSender sender, String... args) {
        this.bossPanelManager.getBosses().openFor((Player) sender);
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender commandSender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "boss.admin";
    }

    @Override
    public String getSyntax() {
        return "list/show";
    }

    @Override
    public String getDescription() {
        return "Shows all the list of current boss entities.";
    }
}
