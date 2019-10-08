package com.songoda.epicbosses.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.container.BossEntityContainer;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.utils.EntityFinder;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.Permission;
import com.songoda.epicbosses.utils.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Oct-18
 */
public class CommandCreate extends AbstractCommand {

    private BossEntityContainer bossEntityContainer;

    public CommandCreate(BossEntityContainer bossEntityContainer) {
        super(true, "create");
        this.bossEntityContainer = bossEntityContainer;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        switch (args.length) {
            case 1:
                List<EntityFinder> availableEntities = new ArrayList<>(Arrays.asList(EntityFinder.values()));
                String list = StringUtils.get().appendList(availableEntities);

                Message.Boss_Create_NoEntitySpecified.msg(sender, list);
                return ReturnType.FAILURE;
            case 2:
                String name = args[0];
                String entityTypeInput = args[1];

                if (this.bossEntityContainer.exists(name)) {
                    Message.Boss_Create_NameAlreadyExists.msg(sender, name);
                    return ReturnType.FAILURE;
                }

                EntityFinder entityFinder = EntityFinder.get(entityTypeInput);

                if (entityFinder == null) {
                    Message.Boss_Create_EntityTypeNotFound.msg(sender, entityTypeInput);
                    return ReturnType.FAILURE;
                }

                BossEntity bossEntity = BossAPI.createBaseBossEntity(name, entityTypeInput);

                if (bossEntity == null) {
                    Message.Boss_Create_SomethingWentWrong.msg(sender);
                    return ReturnType.FAILURE;
                }

                Message.Boss_Create_SuccessfullyCreated.msg(sender, name, entityFinder.getFancyName());
                return ReturnType.SUCCESS;
            default:
                Message.Boss_Create_InvalidArgs.msg(sender);
                return ReturnType.FAILURE;
        }
    }

    @Override
    protected List<String> onTab(CommandSender commandSender, String... args) {
        if (args.length == 1) {
            return Collections.singletonList("name");
        } else if (args.length == 2) {
            return Arrays.stream(EntityType.values()).map(Enum::name).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "boss.create";
    }

    @Override
    public String getSyntax() {
        return "/boss create <[>name> <entity>";
    }

    @Override
    public String getDescription() {
        return "Start the creation of a boss.";
    }
}
