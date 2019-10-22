package com.songoda.epicbosses.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.holder.ActiveAutoSpawnHolder;
import com.songoda.epicbosses.holder.autospawn.ActiveIntervalAutoSpawnHolder;
import com.songoda.epicbosses.managers.AutoSpawnManager;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.time.TimeUnit;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Oct-18
 */
public class CommandTime extends AbstractCommand {

    private AutoSpawnManager autoSpawnManager;

    public CommandTime(EpicBosses plugin) {
        super(false, "time");
        this.autoSpawnManager = plugin.getAutoSpawnManager();
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        if (args.length != 1)
            return ReturnType.SYNTAX_ERROR;

        String section = args[0];
        boolean exists = this.autoSpawnManager.exists(section);
        List<String> currentActive = this.autoSpawnManager.getIntervalAutoSpawns();

        if (!exists) {
            Message.Boss_Time_DoesntExist.msg(sender, StringUtils.get().appendList(currentActive));
            return ReturnType.FAILURE;
        }

        ActiveAutoSpawnHolder activeAutoSpawnHolder = this.autoSpawnManager.getActiveAutoSpawnHolder(section);
        ActiveIntervalAutoSpawnHolder activeIntervalAutoSpawnHolder = (ActiveIntervalAutoSpawnHolder) activeAutoSpawnHolder;
        long remainingMs = activeIntervalAutoSpawnHolder.getRemainingMs();

        if (remainingMs == 0 && activeIntervalAutoSpawnHolder.isSpawnAfterLastBossIsKilled()) {
            Message.Boss_Time_CurrentlyActive.msg(sender);
            return ReturnType.FAILURE;
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
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender commandSender, String... args) {
        if (args.length == 1) {
            return new ArrayList<>(this.autoSpawnManager.getAutoSpawns().keySet());
        }
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "boss.time";
    }

    @Override
    public String getSyntax() {
        return "time <section>";
    }

    @Override
    public String getDescription() {
        return "Shows the time left till next auto spawn.";
    }
}
