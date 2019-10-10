package com.songoda.epicbosses.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.utils.MapUtils;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.StringUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Oct-18
 */
public class CommandNearby extends AbstractCommand {

    private EpicBosses plugin;

    public CommandNearby(EpicBosses plugin) {
        super(true, "nearby");
        this.plugin = plugin;
    }

    @Override
    protected AbstractCommand.ReturnType runCommand(CommandSender sender, String... args) {
        Player player = (Player) sender;
        Location location = player.getLocation();
        double radius = this.plugin.getConfig().getDouble("Settings.defaultNearbyRadius", 250.0);
        double maxRadius = this.plugin.getConfig().getDouble("Limits.maxNearbyRadius", 500.0);
        String nearbyFormat = this.plugin.getConfig().getString("Settings.nearbyFormat", "{name} ({distance}m)");

        if (args.length == 1) {
            Integer newNumber = NumberUtils.get().getInteger(args[0]);

            if (newNumber != null) radius = newNumber;

            if (radius > maxRadius) {
                Message.Boss_Nearby_MaxRadius.msg(player, maxRadius);
                return ReturnType.SUCCESS;
            }
        }

        Map<ActiveBossHolder, Double> nearbyBosses = this.plugin.getBossEntityManager().getActiveBossHoldersWithinRadius(radius, location);
        Map<ActiveBossHolder, Double> sortedMap = MapUtils.get().sortByValue(nearbyBosses);

        if (sortedMap.isEmpty()) {
            Message.Boss_Nearby_NoneNearby.msg(player);
            return ReturnType.FAILURE;
        }

        List<String> input = new LinkedList<>();

        sortedMap.forEach(((activeBossHolder, distance) ->
                input.add(nearbyFormat.replace("{name}", activeBossHolder.getName()).replace("{distance}", NumberUtils.get().formatDouble(distance)))));

        Message.Boss_Nearby_Near.msg(player, StringUtils.get().appendList(input));
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender commandSender, String... args) {
        if (args.length == 1) {
            return Arrays.asList("1", "2", "3", "4", "5");
        }
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "boss.nearby";
    }

    @Override
    public String getSyntax() {
        return "/boss nearby [radius]";
    }

    @Override
    public String getDescription() {
        return "Displays all nearby bosses within the specified radius.";
    }
}
