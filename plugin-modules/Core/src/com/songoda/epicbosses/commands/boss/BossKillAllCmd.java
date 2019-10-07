package com.songoda.epicbosses.commands.boss;

import com.songoda.epicbosses.managers.BossEntityManager;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.Permission;
import com.songoda.epicbosses.utils.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Oct-18
 */
public class BossKillAllCmd extends SubCommand {

    private BossEntityManager bossEntityManager;

    public BossKillAllCmd(BossEntityManager bossEntityManager) {
        super("killall");

        this.bossEntityManager = bossEntityManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!Permission.admin.hasPermission(sender)) {
            Message.Boss_KillAll_NoPermission.msg(sender);
            return;
        }

        World world = null;

        if (args.length == 2) {
            String worldArgs = args[1];

            world = Bukkit.getWorld(worldArgs);

            if (world == null) {
                Message.Boss_KillAll_WorldNotFound.msg(sender);
                return;
            }
        }

        int amount = this.bossEntityManager.killAllHolders(world);

        if (args.length == 2) Message.Boss_KillAll_KilledWorld.msg(sender, amount, world.getName());
        else Message.Boss_KillAll_KilledAll.msg(sender, amount);
    }
}
