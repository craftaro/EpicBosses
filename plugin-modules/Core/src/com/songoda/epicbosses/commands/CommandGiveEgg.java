package com.songoda.epicbosses.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.managers.BossEntityManager;
import com.songoda.epicbosses.managers.files.BossesFileManager;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 14-Nov-18
 */
public class CommandGiveEgg extends AbstractCommand {

    private BossEntityManager bossEntityManager;
    private BossesFileManager bossesFileManager;

    public CommandGiveEgg(BossesFileManager bossesFileManager, BossEntityManager bossEntityManager) {
        super(false, "give", "giveegg");

        this.bossesFileManager = bossesFileManager;
        this.bossEntityManager = bossEntityManager;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {

        if (args.length < 2) {
            Message.Boss_GiveEgg_InvalidArgs.msg(sender);
            return ReturnType.FAILURE;
        }

        int amount = 1;

        if (args.length == 3) {
            String amountInput = args[2];

            if (NumberUtils.get().isInt(amountInput)) {
                amount = Integer.valueOf(amountInput);
            } else {
                Message.General_NotNumber.msg(sender);
                return ReturnType.FAILURE;
            }
        }

        String playerInput = args[1];
        Player player = Bukkit.getPlayer(playerInput);

        if (player == null) {
            Message.General_NotOnline.msg(sender, playerInput);
            return ReturnType.FAILURE;
        }

        String bossInput = args[0];
        BossEntity bossEntity = this.bossesFileManager.getBossEntity(bossInput);

        if (bossEntity == null) {
            Message.Boss_GiveEgg_InvalidBoss.msg(sender);
            return ReturnType.FAILURE;
        }

        ItemStack spawnItem = this.bossEntityManager.getSpawnItem(bossEntity);

        if (spawnItem == null) {
            Message.Boss_GiveEgg_NotSet.msg(sender);
            return ReturnType.FAILURE;
        }

        spawnItem.setAmount(amount);
        player.getInventory().addItem(spawnItem);

        Message.Boss_GiveEgg_Given.msg(sender, player.getName(), amount, bossInput);
        Message.Boss_GiveEgg_Received.msg(player, amount, bossInput);
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender commandSender, String... args) {
        if (args.length == 1) {
            return new ArrayList<>(bossesFileManager.getBossEntitiesMap().keySet());
        } else if (args.length == 2) {
            return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList());
        } else if (args.length == 3) {
            return Arrays.asList("1", "2", "3", "4", "5");
        }
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "boss.give";
    }

    @Override
    public String getSyntax() {
        return "give/giveegg <name> <player> [amount]";
    }

    @Override
    public String getDescription() {
        return "Gives you the spawn egg of a boss.";
    }
}
