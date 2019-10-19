package com.songoda.epicbosses.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.managers.files.BossesFileManager;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.StringUtils;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Oct-18
 */
public class CommandSpawn extends AbstractCommand {

    private BossesFileManager bossesFileManager;

    public CommandSpawn(BossesFileManager bossesFileManager) {
        super(false, "spawn");

        this.bossesFileManager = bossesFileManager;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        if (args.length == 0)
            return ReturnType.SYNTAX_ERROR;

        Location spawnLocation;

        if (args.length == 2) {
            Location input = StringUtils.get().fromStringToLocation(args[1]);

            if (input == null) {
                Message.Boss_Spawn_InvalidLocation.msg(sender);
                return ReturnType.FAILURE;
            }

            spawnLocation = input;
        } else {
            if (!(sender instanceof Player)) {
                Message.Boss_Spawn_MustBePlayer.msg(sender);
                return ReturnType.FAILURE;
            }

            spawnLocation = ((Player) sender).getLocation();
        }

        String bossInput = args[0];
        BossEntity bossEntity = this.bossesFileManager.getBossEntity(bossInput);

        if (bossEntity == null) {
            Message.Boss_Spawn_InvalidBoss.msg(sender);
            return ReturnType.FAILURE;
        }

        BossAPI.spawnNewBoss(bossEntity, spawnLocation, null, null, false);
        Message.Boss_Spawn_Spawned.msg(sender, bossInput, StringUtils.get().translateLocation(spawnLocation));
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender commandSender, String... args) {
        if (args.length == 1) {
            return new ArrayList<>(bossesFileManager.getBossEntitiesMap().keySet());
        } else if (args.length == 2) {
            return Collections.singletonList("world,0,100,0");
        }
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "boss.admin";
    }

    @Override
    public String getSyntax() {
        return "spawn <name> [location]";
    }

    @Override
    public String getDescription() {
        return "Spawns a specific boss at the defined location.";
    }
}
