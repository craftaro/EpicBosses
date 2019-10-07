package com.songoda.epicbosses.commands.boss;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.holder.ActiveAutoSpawnHolder;
import com.songoda.epicbosses.holder.autospawn.ActiveIntervalAutoSpawnHolder;
import com.songoda.epicbosses.managers.AutoSpawnManager;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.Permission;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.command.SubCommand;
import com.songoda.epicbosses.utils.time.TimeUnit;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Oct-18
 */
public class BossTimeCmd extends SubCommand {

    private AutoSpawnManager autoSpawnManager;

    public BossTimeCmd(EpicBosses plugin) {
        super("time");

        this.autoSpawnManager = plugin.getAutoSpawnManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!Permission.time.hasPermission(sender)) {
            Message.Boss_Time_NoPermission.msg(sender);
            return;
        }

        if(args.length != 2) {
            Message.Boss_Time_InvalidArgs.msg(sender);
            return;
        }

        String section = args[1];
        boolean exists = this.autoSpawnManager.exists(section);
        List<String> currentActive = this.autoSpawnManager.getIntervalAutoSpawns();

        if(!exists) {
            Message.Boss_Time_DoesntExist.msg(sender, StringUtils.get().appendList(currentActive));
            return;
        }

        ActiveAutoSpawnHolder activeAutoSpawnHolder = this.autoSpawnManager.getActiveAutoSpawnHolder(section);
        ActiveIntervalAutoSpawnHolder activeIntervalAutoSpawnHolder = (ActiveIntervalAutoSpawnHolder) activeAutoSpawnHolder;
        long remainingMs = activeIntervalAutoSpawnHolder.getRemainingMs();

        if(remainingMs == 0 && activeIntervalAutoSpawnHolder.isSpawnAfterLastBossIsKilled()) {
            Message.Boss_Time_CurrentlyActive.msg(sender);
            return;
        }

        String s = Message.General_TimeLayout.toString();
        TimeUnit unit = TimeUnit.MILLISECONDS;
        int remainingHours = (int) unit.to(TimeUnit.HOURS, remainingMs);
        remainingMs -= TimeUnit.HOURS.to(unit, remainingHours);
        int remainingMins = (int) unit.to(TimeUnit.MINUTES, remainingMs);
        remainingMs -= TimeUnit.MINUTES.to(unit, remainingMins);
        int remainingSecs = (int) unit.to(TimeUnit.SECONDS, remainingMs);

        s = s.replace("{hours}", NumberUtils.get().formatDouble(remainingHours));
        s = s.replace("{mins}", NumberUtils.get().formatDouble(remainingMins));
        s = s.replace("{sec}", NumberUtils.get().formatDouble(remainingSecs));

        Message.Boss_Time_GetRemainingTime.msg(sender, s, section);
    }
}
