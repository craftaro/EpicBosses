package com.songoda.epicbosses.commands.boss;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.file.ConfigFileHandler;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.Permission;
import com.songoda.epicbosses.utils.command.SubCommand;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Oct-18
 */
public class BossNearbyCmd extends SubCommand {

    private CustomBosses plugin;

    public BossNearbyCmd(CustomBosses plugin) {
        super("nearby");

        this.plugin = plugin;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!Permission.nearby.hasPermission(sender)) {
            Message.Boss_Nearby_NoPermission.msg(sender);
            return;
        }

        if(!(sender instanceof Player)) {
            Message.General_MustBePlayer.msg(sender);
            return;
        }

        Player player = (Player) sender;
        Location location = player.getLocation();
        double radius = this.plugin.getConfig().getDouble("Settings.defaultNearbyRadius", 250.0);

        if(args.length == 2) {
            Integer newNumber = NumberUtils.get().getInteger(args[1]);

            if(newNumber != null) radius = newNumber;
        }

        List<ActiveBossHolder> nearbyBosses = this.plugin.getBossEntityManager().getActiveBossHoldersWithinRadius(radius, location);

        //TODO Finish
    }
}
