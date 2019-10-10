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
public class CommandSkills extends AbstractCommand {

    private BossPanelManager bossPanelManager;

    public CommandSkills(BossPanelManager bossPanelManager) {
        super(true, "skills", "skill");
        this.bossPanelManager = bossPanelManager;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        this.bossPanelManager.getCustomSkills().openFor((Player) sender);
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
        return "/boss skills";
    }

    @Override
    public String getDescription() {
        return "Shows all current configured skills.";
    }
}
