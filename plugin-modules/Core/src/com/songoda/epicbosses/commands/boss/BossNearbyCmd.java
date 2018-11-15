package com.songoda.epicbosses.commands.boss;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.file.ConfigFileHandler;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.utils.*;
import com.songoda.epicbosses.utils.command.SubCommand;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

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
        double maxRadius = this.plugin.getConfig().getDouble("Limits.maxNearbyRadius", 500.0);
        String nearbyFormat = this.plugin.getConfig().getString("Settings.nearbyFormat", "{name} ({distance}m)");

        if(args.length == 2) {
            Integer newNumber = NumberUtils.get().getInteger(args[1]);

            if(newNumber != null) radius = newNumber;

            if(radius > maxRadius) {
                Message.Boss_Nearby_MaxRadius.msg(player, maxRadius);
                return;
            }
        }

        Map<ActiveBossHolder, Double> nearbyBosses = this.plugin.getBossEntityManager().getActiveBossHoldersWithinRadius(radius, location);
        Map<ActiveBossHolder, Double> sortedMap = MapUtils.get().sortByValue(nearbyBosses);

        if(sortedMap.isEmpty()) {
            Message.Boss_Nearby_NoneNearby.msg(player);
            return;
        }

        List<String> input = new LinkedList<>();

        sortedMap.forEach(((activeBossHolder, distance) -> {
            input.add(nearbyFormat.replace("{name}", activeBossHolder.getName()).replace("{distance}", NumberUtils.get().formatDouble(distance)));
        }));

        Message.Boss_Nearby_Near.msg(player, StringUtils.get().appendList(input));
    }
}
