package com.songoda.epicbosses.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.epicbosses.managers.BossPanelManager;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 04-Oct-18
 */
public class CommandDropTable extends AbstractCommand {

    private BossPanelManager bossPanelManager;

    public CommandDropTable(BossPanelManager bossPanelManager) {
        super(true, "droptable");
        this.bossPanelManager = bossPanelManager;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        this.bossPanelManager.getDropTables().openFor((Player) sender);
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
        return "/boss droptable";
    }

    @Override
    public String getDescription() {
        return "Shows the current drop table";
    }
}
