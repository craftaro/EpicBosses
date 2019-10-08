package com.songoda.epicbosses.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.utils.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 10-Oct-18
 */
public class CommandShop extends AbstractCommand {

    private EpicBosses plugin;

    public CommandShop(EpicBosses plugin) {
        super(true, "shop", "buy", "store");

        this.plugin = plugin;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        if (!this.plugin.getConfig().getBoolean("Toggles.bossShop", true)) {
            Message.Boss_Shop_Disabled.msg(sender);
            return ReturnType.FAILURE;
        }

        plugin.getBossPanelManager().getShopPanel().openFor((Player) sender);
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender commandSender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "boss.shop";
    }

    @Override
    public String getSyntax() {
        return "/boss shop";
    }

    @Override
    public String getDescription() {
        return "Opens the shop for a player to purchase boss eggs themselves.";
    }
}
