package com.songoda.epicbosses.commands.boss;

import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.managers.files.BossesFileManager;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.Permission;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.command.SubCommand;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Oct-18
 */
public class BossSpawnCmd extends SubCommand {

    private BossesFileManager bossesFileManager;

    public BossSpawnCmd(BossesFileManager bossesFileManager) {
        super("spawn");

        this.bossesFileManager = bossesFileManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!Permission.admin.hasPermission(sender)) {
            Message.Boss_Spawn_NoPermission.msg(sender);
            return;
        }

        if(args.length < 2) {
            Message.Boss_Spawn_InvalidArgs.msg(sender);
            return;
        }

        Location spawnLocation;

        if(args.length == 3) {
            Location input = StringUtils.get().fromStringToLocation(args[2]);

            if(input == null) {
                Message.Boss_Spawn_InvalidLocation.msg(sender);
                return;
            }

            spawnLocation = input;
        } else {
            if(!(sender instanceof Player)) {
                Message.Boss_Spawn_MustBePlayer.msg(sender);
                return;
            }

            spawnLocation = ((Player) sender).getLocation();
        }

        String bossInput = args[1];
        BossEntity bossEntity = this.bossesFileManager.getBossEntity(bossInput);

        if(bossEntity == null) {
            Message.Boss_Spawn_InvalidBoss.msg(sender);
            return;
        }

        BossAPI.spawnNewBoss(bossEntity, spawnLocation, null, null);
        Message.Boss_Spawn_Spawned.msg(sender, bossInput, StringUtils.get().translateLocation(spawnLocation));
    }
}
