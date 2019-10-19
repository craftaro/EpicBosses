package com.songoda.epicbosses.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.autospawns.AutoSpawn;
import com.songoda.epicbosses.managers.BossSkillManager;
import com.songoda.epicbosses.managers.files.AutoSpawnFileManager;
import com.songoda.epicbosses.managers.files.SkillsFileManager;
import com.songoda.epicbosses.skills.SkillMode;
import com.songoda.epicbosses.utils.Message;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CommandNewAutoSpawn extends AbstractCommand {

    private AutoSpawnFileManager autoSpawnFileManager;

    public CommandNewAutoSpawn(AutoSpawnFileManager autoSpawnFileManager) {
        super(false, " ");
        this.autoSpawnFileManager = autoSpawnFileManager;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        if (args.length != 1)
            return ReturnType.SYNTAX_ERROR;
        String nameInput = args[0];

        if (this.autoSpawnFileManager.getAutoSpawn(nameInput) != null) {
            Message.Boss_New_AlreadyExists.msg(sender, "AutoSpawn");
            return ReturnType.FAILURE;
        }

        AutoSpawn autoSpawn = BossAPI.createBaseAutoSpawn(nameInput);

        if (autoSpawn == null) {
            Message.Boss_New_SomethingWentWrong.msg(sender, "AutoSpawn");
        } else {
            Message.Boss_New_AutoSpawn.msg(sender, nameInput);
        }

        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender commandSender, String... args) {
        if (args.length == 1) {
            return Collections.singletonList("name");
        }
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "boss.admin";
    }

    @Override
    public String getSyntax() {
        return "new autospawn <name>";
    }

    @Override
    public String getDescription() {
        return "Create a new auto spawn section.";
    }
}
