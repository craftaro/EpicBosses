package net.aminecraftdev.custombosses.commands.boss;

import net.aminecraftdev.custombosses.managers.BossPanelManager;
import net.aminecraftdev.custombosses.utils.Message;
import net.aminecraftdev.custombosses.utils.Permission;
import net.aminecraftdev.custombosses.utils.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 04-Oct-18
 */
public class BossSkillsCmd extends SubCommand {

    private BossPanelManager bossPanelManager;

    public BossSkillsCmd(BossPanelManager bossPanelManager) {
        super("skills", "skill");

        this.bossPanelManager = bossPanelManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!Permission.admin.hasPermission(sender)) {
            Message.Boss_Skills_NoPermission.msg(sender);
            return;
        }

        if(!(sender instanceof Player)) {
            Message.General_MustBePlayer.msg(sender);
            return;
        }

        Player player = (Player) sender;

        this.bossPanelManager.getCustomSkills().openFor(player);
    }
}