package com.songoda.epicbosses.commands.boss;

import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.managers.BossEntityManager;
import com.songoda.epicbosses.managers.files.BossesFileManager;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.Permission;
import com.songoda.epicbosses.utils.command.SubCommand;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 14-Nov-18
 */
public class BossGiveEggCmd extends SubCommand {

    private BossEntityManager bossEntityManager;
    private BossesFileManager bossesFileManager;

    public BossGiveEggCmd(BossesFileManager bossesFileManager, BossEntityManager bossEntityManager) {
        super("give", "giveegg");

        this.bossesFileManager = bossesFileManager;
        this.bossEntityManager = bossEntityManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!Permission.give.hasPermission(sender)) {
            Message.Boss_GiveEgg_NoPermission.msg(sender);
            return;
        }

        if(args.length < 3) {
            Message.Boss_GiveEgg_InvalidArgs.msg(sender);
            return;
        }

        int amount = 1;

        if(args.length == 4) {
            String amountInput = args[3];

            if(NumberUtils.get().isInt(amountInput)) {
                amount = Integer.valueOf(amountInput);
            } else {
                Message.General_NotNumber.msg(sender);
                return;
            }
        }

        String playerInput = args[2];
        Player player = Bukkit.getPlayer(playerInput);

        if(player == null) {
            Message.General_NotOnline.msg(sender, playerInput);
            return;
        }

        String bossInput = args[1];
        BossEntity bossEntity = this.bossesFileManager.getBossEntity(bossInput);

        if(bossEntity == null) {
            Message.Boss_GiveEgg_InvalidBoss.msg(sender);
            return;
        }

        ItemStack spawnItem = this.bossEntityManager.getSpawnItem(bossEntity);

        if(spawnItem == null) {
            Message.Boss_GiveEgg_NotSet.msg(sender);
            return;
        }

        spawnItem.setAmount(amount);
        player.getInventory().addItem(spawnItem);

        Message.Boss_GiveEgg_Given.msg(sender, player.getName(), amount, bossInput);
        Message.Boss_GiveEgg_Received.msg(player, amount, bossInput);
    }
}
