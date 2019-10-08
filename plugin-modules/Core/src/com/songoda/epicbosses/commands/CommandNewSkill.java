package com.songoda.epicbosses.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.managers.BossSkillManager;
import com.songoda.epicbosses.managers.files.SkillsFileManager;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.SkillMode;
import com.songoda.epicbosses.utils.Message;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CommandNewSkill extends AbstractCommand {

    private SkillsFileManager skillsFileManager;
    private BossSkillManager bossSkillManager;

    public CommandNewSkill(SkillsFileManager skillsFileManager, BossSkillManager bossSkillManager) {
        super(false, "new skill");
        this.skillsFileManager = skillsFileManager;
        this.bossSkillManager = bossSkillManager;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        if (args.length != 3)
            return ReturnType.SYNTAX_ERROR;
        String nameInput = args[0];
        String typeInput = args[1];
        String modeInput = args[2];
        boolean validType = false, validMode = false;
        List<SkillMode> skillModes = SkillMode.getSkillModes();

        if (this.skillsFileManager.getSkill(nameInput) != null) {
            Message.Boss_New_AlreadyExists.msg(sender, "Skill");
            return ReturnType.FAILURE;
        }

        for (String s : this.bossSkillManager.getValidSkillTypes()) {
            if (s.equalsIgnoreCase(typeInput)) {
                validType = true;
                break;
            }
        }

        for (SkillMode skillMode : skillModes) {
            if (skillMode.name().equalsIgnoreCase(modeInput)) {
                validMode = true;
                break;
            }
        }

        if (!validType) {
            Message.Boss_New_InvalidSkillType.msg(sender);
            return ReturnType.FAILURE;
        }

        if (!validMode) {
            Message.Boss_New_InvalidSkillMode.msg(sender);
            return ReturnType.FAILURE;
        }

        Skill skill = BossAPI.createBaseSkill(nameInput, typeInput, modeInput);

        if (skill == null) {
            Message.Boss_New_SomethingWentWrong.msg(sender, "Skill");
        } else {
            Message.Boss_New_Skill.msg(sender, nameInput, typeInput);
        }

        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender commandSender, String... args) {
        if (args.length == 1) {
            return Collections.singletonList("name");
        } else if (args.length == 2) {
            return this.bossSkillManager.getValidSkillTypes();
        } else if (args.length == 3) {
            return SkillMode.getSkillModes().stream().map(SkillMode::name).collect(Collectors.toList());
        }
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "boss.admin";
    }

    @Override
    public String getSyntax() {
        return "/boss new skill <name> <type> <mods>";
    }

    @Override
    public String getDescription() {
        return "Create a new skill section.";
    }
}
