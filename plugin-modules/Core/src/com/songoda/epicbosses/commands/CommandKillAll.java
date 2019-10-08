package com.songoda.epicbosses.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.epicbosses.managers.BossEntityManager;
import com.songoda.epicbosses.utils.Message;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Oct-18
 */
public class CommandKillAll extends AbstractCommand {

    private BossEntityManager bossEntityManager;

    public CommandKillAll(BossEntityManager bossEntityManager) {
        super(false, "killall");
        this.bossEntityManager = bossEntityManager;
    }

    @Override
    protected AbstractCommand.ReturnType runCommand(CommandSender sender, String... args) {
        World world = null;

        if (args.length == 1) {
            String worldArgs = args[0];

            world = Bukkit.getWorld(worldArgs);

            if (world == null) {
                Message.Boss_KillAll_WorldNotFound.msg(sender);
                return ReturnType.SUCCESS;
            }
        }

        int amount = this.bossEntityManager.killAllHolders(world);

        if (args.length == 1) Message.Boss_KillAll_KilledWorld.msg(sender, amount, world.getName());
        else Message.Boss_KillAll_KilledAll.msg(sender, amount);
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender commandSender, String... args) {
        if (args.length == 1) {
            return Bukkit.getWorlds().stream().map(World::getName).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "boss.admin";
    }

    @Override
    public String getSyntax() {
        return "/boss killall [world]";
    }

    @Override
    public String getDescription() {
        return "Removes all current bosses in the specified world.";
    }
}
