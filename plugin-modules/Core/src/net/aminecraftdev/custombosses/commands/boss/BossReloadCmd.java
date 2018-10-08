package net.aminecraftdev.custombosses.commands.boss;

import net.aminecraftdev.custombosses.utils.IReloadable;
import net.aminecraftdev.custombosses.utils.Message;
import net.aminecraftdev.custombosses.utils.Permission;
import net.aminecraftdev.custombosses.utils.command.SubCommand;
import org.bukkit.command.CommandSender;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Oct-18
 */
public class BossReloadCmd extends SubCommand {

    private IReloadable masterReloadable;

    public BossReloadCmd(IReloadable reloadable) {
        super("reload");

        this.masterReloadable = reloadable;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!Permission.reload.hasPermission(sender)) {
            Message.Boss_Reload_NoPermission.msg(sender);
            return;
        }

        long currentMs = System.currentTimeMillis();

        this.masterReloadable.reload();
        Message.Boss_Reload_Successful.msg(sender, (System.currentTimeMillis() - currentMs));
    }
}
