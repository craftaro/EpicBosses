package com.songoda.epicbosses.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.epicbosses.managers.BossPanelManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 10-Oct-18
 */
public class CommandMenu extends AbstractCommand {

    private BossPanelManager bossPanelManager;

    public CommandMenu(BossPanelManager bossPanelManager) {
        super(true, "menu");
        this.bossPanelManager = bossPanelManager;
    }

    @Override
    protected AbstractCommand.ReturnType runCommand(CommandSender sender, String... args) {
        this.bossPanelManager.getMainMenu().openFor((Player) sender);
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender commandSender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "menu";
    }

    @Override
    public String getSyntax() {
        return null;
    }

    @Override
    public String getDescription() {
        return "Opens up the menu to edit all current created bosses.";
    }
}
