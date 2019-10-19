package com.songoda.epicbosses.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.epicbosses.managers.BossEntityManager;
import com.songoda.epicbosses.utils.IReloadable;
import com.songoda.epicbosses.utils.Message;
import org.bukkit.World;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Oct-18
 */
public class CommandReload extends AbstractCommand {

    private BossEntityManager bossEntityManager;
    private IReloadable masterReloadable;

    public CommandReload(IReloadable reloadable, BossEntityManager bossEntityManager) {
        super(false, "reload");
        this.masterReloadable = reloadable;
        this.bossEntityManager = bossEntityManager;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        long currentMs = System.currentTimeMillis();

        this.masterReloadable.reload();
        this.bossEntityManager.killAllHolders((World) null);
        Message.Boss_Reload_Successful.msg(sender, (System.currentTimeMillis() - currentMs));
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender sender, String... args) {
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "boss.reload";
    }

    @Override
    public String getSyntax() {
        return "reload";
    }

    @Override
    public String getDescription() {
        return "Reloads EpicBosses and its configurations.";
    }
}
