package com.songoda.epicbosses.commands.boss;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.Permission;
import com.songoda.epicbosses.utils.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 10-Oct-18
 */
public class BossShopCmd extends SubCommand {

    private EpicBosses plugin;

    public BossShopCmd(EpicBosses plugin) {
        super("shop", "buy", "store");

        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!Permission.shop.hasPermission(sender)) {
            Message.Boss_Shop_NoPermission.msg(sender);
            return;
        }

        if (!(sender instanceof Player)) {
            Message.General_MustBePlayer.msg(sender);
            return;
        }

        if (!this.plugin.getConfig().getBoolean("Toggles.bossShop", true)) {
            Message.Boss_Shop_Disabled.msg(sender);
            return;
        }

        Player player = (Player) sender;

        plugin.getBossPanelManager().getShopPanel().openFor(player);
    }
}
