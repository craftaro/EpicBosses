package com.songoda.epicbosses.commands.boss;

import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.managers.BossEntityManager;
import com.songoda.epicbosses.managers.files.BossesFileManager;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.Permission;
import com.songoda.epicbosses.utils.command.SubCommand;
import org.bukkit.command.CommandSender;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Oct-18
 */
public class BossInfoCmd extends SubCommand {

    private BossEntityManager bossEntityManager;
    private BossesFileManager bossesFileManager;

    public BossInfoCmd(BossesFileManager bossesFileManager, BossEntityManager bossEntityManager) {
        super("info");

        this.bossesFileManager = bossesFileManager;
        this.bossEntityManager = bossEntityManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!Permission.admin.hasPermission(sender)) {
            Message.Boss_Info_NoPermission.msg(sender);
            return;
        }

        if (args.length != 2) {
            Message.Boss_Info_InvalidArgs.msg(sender);
            return;
        }

        String input = args[1];
        BossEntity bossEntity = this.bossesFileManager.getBossEntity(input);

        if (bossEntity == null) {
            Message.Boss_Info_CouldntFindBoss.msg(sender);
            return;
        }

        boolean editing = bossEntity.isEditing();
        int active = this.bossEntityManager.getCurrentlyActive(bossEntity);
        boolean complete = bossEntity.isCompleteEnoughToSpawn();

        Message.Boss_Info_Display.msg(sender, input, editing, active, complete);
    }
}
