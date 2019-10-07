package com.songoda.epicbosses.commands.boss;

import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.container.BossEntityContainer;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.utils.EntityFinder;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.Permission;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.command.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Oct-18
 */
public class BossCreateCmd extends SubCommand {

    private BossEntityContainer bossEntityContainer;

    public BossCreateCmd(BossEntityContainer bossEntityContainer) {
        super("create");

        this.bossEntityContainer = bossEntityContainer;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!Permission.create.hasPermission(sender)) {
            Message.Boss_Create_NoPermission.msg(sender);
            return;
        }

        switch (args.length) {
            case 2:
                List<EntityFinder> availableEntities = new ArrayList<>(Arrays.asList(EntityFinder.values()));
                String list = StringUtils.get().appendList(availableEntities);

                Message.Boss_Create_NoEntitySpecified.msg(sender, list);
                return;
            case 3:
                String name = args[1];
                String entityTypeInput = args[2];

                if (this.bossEntityContainer.exists(name)) {
                    Message.Boss_Create_NameAlreadyExists.msg(sender, name);
                    return;
                }

                EntityFinder entityFinder = EntityFinder.get(entityTypeInput);

                if (entityFinder == null) {
                    Message.Boss_Create_EntityTypeNotFound.msg(sender, entityTypeInput);
                    return;
                }

                BossEntity bossEntity = BossAPI.createBaseBossEntity(name, entityTypeInput);

                if (bossEntity == null) {
                    Message.Boss_Create_SomethingWentWrong.msg(sender);
                    return;
                }

                Message.Boss_Create_SuccessfullyCreated.msg(sender, name, entityFinder.getFancyName());
                return;
            default:
                Message.Boss_Create_InvalidArgs.msg(sender);
        }
    }
}
