package com.songoda.epicbosses.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.container.BossEntityContainer;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.utils.Message;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Oct-18
 */
public class CommandEdit extends AbstractCommand {

    private BossEntityContainer bossEntityContainer;
    private BossPanelManager bossPanelManager;

    public CommandEdit(BossPanelManager bossPanelManager, BossEntityContainer bossEntityContainer) {
        super(true, "edit");
        this.bossPanelManager = bossPanelManager;
        this.bossEntityContainer = bossEntityContainer;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        Player player = (Player) sender;

        switch (args.length) {
            default:
            case 0:
                this.bossPanelManager.getBosses().openFor(player);
                break;
            case 1:
                String input = args[0];

                if (!this.bossEntityContainer.exists(input)) {
                    Message.Boss_Edit_DoesntExist.msg(sender);
                    return ReturnType.FAILURE;
                }

                BossEntity bossEntity = bossEntityContainer.getData().entrySet().stream()
                        .filter(e -> e.getKey().equalsIgnoreCase(input)).findFirst().get().getValue();

                this.bossPanelManager.getMainBossEditMenu().openFor(player, bossEntity);
                break;
        }
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender commandSender, String... args) {
        if (args.length == 1) {
            return new ArrayList<>(EpicBosses.getInstance().getBossesFileManager().getBossEntitiesMap().keySet());
        }
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "boss.edit";
    }

    @Override
    public String getSyntax() {
        return "edit <name>";
    }

    @Override
    public String getDescription() {
        return "Edit a specified boss.";
    }
}
