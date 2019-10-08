package com.songoda.epicbosses.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.core.utils.TextUtils;
import com.songoda.epicbosses.EpicBosses;
import org.bukkit.command.CommandSender;

import java.util.List;

public class CommandBoss extends AbstractCommand {

    EpicBosses instance;

    public CommandBoss() {
        super(false, "Boss");
        instance = EpicBosses.getInstance();
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        sender.sendMessage("");
        sender.sendMessage(TextUtils.formatText("&b&lEpicBosses &8» &7Version " + instance.getDescription().getVersion()
                + " Created with <3 by &5&l&oSongoda"));

        for (AbstractCommand command : instance.getCommandManager().getAllCommands()) {
            if (command.getPermissionNode() == null || sender.hasPermission(command.getPermissionNode())) {
                sender.sendMessage(TextUtils.formatText("&8 - &a" + command.getSyntax() + "&7 - " + command.getDescription()));
            }
        }
        sender.sendMessage("");

        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender cs, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return null;
    }

    @Override
    public String getSyntax() {
        return "/boss";
    }

    @Override
    public String getDescription() {
        return "Displays this page.";
    }
}
