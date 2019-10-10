package com.songoda.epicbosses.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.managers.BossEntityManager;
import com.songoda.epicbosses.managers.files.BossesFileManager;
import com.songoda.epicbosses.utils.Message;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Oct-18
 */
public class CommandInfo extends AbstractCommand {

    private BossEntityManager bossEntityManager;
    private BossesFileManager bossesFileManager;

    public CommandInfo(BossesFileManager bossesFileManager, BossEntityManager bossEntityManager) {
        super(false, "info");
        this.bossesFileManager = bossesFileManager;
        this.bossEntityManager = bossEntityManager;
    }

    @Override
    protected AbstractCommand.ReturnType runCommand(CommandSender sender, String... args) {
        if (args.length != 1) {
            Message.Boss_Info_InvalidArgs.msg(sender);
            return ReturnType.FAILURE;
        }

        String input = args[0];
        BossEntity bossEntity = this.bossesFileManager.getBossEntity(input);

        if (bossEntity == null) {
            Message.Boss_Info_CouldntFindBoss.msg(sender);
            return ReturnType.FAILURE;
        }

        boolean editing = bossEntity.isEditing();
        int active = this.bossEntityManager.getCurrentlyActive(bossEntity);
        boolean complete = bossEntity.isCompleteEnoughToSpawn();

        Message.Boss_Info_Display.msg(sender, input, editing, active, complete);
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender commandSender, String... args) {
        if (args.length == 1) {
            return new ArrayList<>(bossesFileManager.getBossEntitiesMap().keySet());
        }
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "boss.admin";
    }

    @Override
    public String getSyntax() {
        return "/boss info <name>";
    }

    @Override
    public String getDescription() {
        return "Displays info on the specified boss.";
    }
}
