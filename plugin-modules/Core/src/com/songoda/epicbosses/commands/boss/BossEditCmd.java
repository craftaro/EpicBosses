package com.songoda.epicbosses.commands.boss;

import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.container.BossEntityContainer;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.Permission;
import com.songoda.epicbosses.utils.command.SubCommand;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Oct-18
 */
public class BossEditCmd extends SubCommand {

    private BossEntityContainer bossEntityContainer;
    private BossPanelManager bossPanelManager;

    public BossEditCmd(BossPanelManager bossPanelManager, BossEntityContainer bossEntityContainer) {
        super("edit");

        this.bossPanelManager = bossPanelManager;
        this.bossEntityContainer = bossEntityContainer;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!Permission.admin.hasPermission(sender)) {
            Message.Boss_Edit_NoPermission.msg(sender);
            return;
        }

        if(!(sender instanceof Player)) {
            Message.General_MustBePlayer.msg(sender);
            return;
        }

        Player player = (Player) sender;

        switch(args.length) {
            default:
            case 1:
                this.bossPanelManager.getBosses().openFor(player);
                break;
            case 2:
                String input = args[1];

                if(!this.bossEntityContainer.exists(input)) {
                    Message.Boss_Edit_DoesntExist.msg(sender);
                    return;
                }

                BossEntity bossEntity = this.bossEntityContainer.getData().get(input);

                this.bossPanelManager.getMainBossEditMenu().openFor(player, bossEntity);
                break;
        }
    }
}
