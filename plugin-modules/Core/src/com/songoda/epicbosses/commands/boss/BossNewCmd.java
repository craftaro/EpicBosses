package com.songoda.epicbosses.commands.boss;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.autospawns.AutoSpawn;
import com.songoda.epicbosses.autospawns.SpawnType;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.managers.BossDropTableManager;
import com.songoda.epicbosses.managers.BossSkillManager;
import com.songoda.epicbosses.managers.files.*;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.SkillMode;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.Permission;
import com.songoda.epicbosses.utils.command.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 19-Nov-18
 *
 * boss new droptable [name] [type]
 * boss new skill [name] [type] [mode]
 * boss new command [name] [commands]
 * boss new message [name] [message]
 * boss new autospawn [name]
 */
public class BossNewCmd extends SubCommand {

    private AutoSpawnFileManager autoSpawnFileManager;
    private DropTableFileManager dropTableFileManager;
    private BossDropTableManager bossDropTableManager;
    private MessagesFileManager messagesFileManager;
    private CommandsFileManager commandsFileManager;
    private SkillsFileManager skillsFileManager;
    private BossSkillManager bossSkillManager;

    public BossNewCmd(CustomBosses plugin) {
        super("new");

        this.bossSkillManager = plugin.getBossSkillManager();
        this.skillsFileManager = plugin.getSkillsFileManager();
        this.dropTableFileManager = plugin.getDropTableFileManager();
        this.bossDropTableManager = plugin.getBossDropTableManager();
        this.messagesFileManager = plugin.getBossMessagesFileManager();
        this.commandsFileManager = plugin.getBossCommandFileManager();
        this.autoSpawnFileManager = plugin.getAutoSpawnFileManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!Permission.admin.hasPermission(sender)) {
            Message.Boss_New_NoPermission.msg(sender);
            return;
        }

        //--------------------
        // A U T O   S P A W N
        //--------------------
        if(args.length == 3 && args[1].equalsIgnoreCase("autospawn")) {
            String nameInput = args[2];

            if(this.autoSpawnFileManager.getAutoSpawn(nameInput) != null) {
                Message.Boss_New_AlreadyExists.msg(sender, "AutoSpawn");
                return;
            }

            AutoSpawn autoSpawn = BossAPI.createBaseAutoSpawn(nameInput);

            if(autoSpawn == null) {
                Message.Boss_New_SomethingWentWrong.msg(sender, "AutoSpawn");
            } else {
                Message.Boss_New_AutoSpawn.msg(sender, nameInput);
            }

            return;
        }


        //-------------------
        // C O M M A N D
        //-------------------
        if(args.length >= 4 && args[1].equalsIgnoreCase("command")) {
            String nameInput = args[2];

            if(this.commandsFileManager.getCommands(nameInput) != null) {
                Message.Boss_New_AlreadyExists.msg(sender, "Command");
                return;
            }

            List<String> commands = appendList(args);

            this.commandsFileManager.addNewCommand(nameInput, commands);
            this.commandsFileManager.save();

            Message.Boss_New_Command.msg(sender, nameInput);
            return;
        }

        //-------------------
        // M E S S A G E
        //-------------------
        if(args.length >= 4 && args[1].equalsIgnoreCase("message")) {
            String nameInput = args[2];

            if(this.commandsFileManager.getCommands(nameInput) != null) {
                Message.Boss_New_AlreadyExists.msg(sender, "Message");
                return;
            }

            List<String> messages = appendList(args);

            this.messagesFileManager.addNewMessage(nameInput, messages);
            this.messagesFileManager.save();

            Message.Boss_New_Message.msg(sender, nameInput);
            return;
        }

        //----------------------
        // D R O P   T A B L E
        //----------------------
        if(args.length == 4 && args[1].equalsIgnoreCase("droptable")) {
            String nameInput = args[2];
            String typeInput = args[3];
            boolean validType = false;

            if(this.dropTableFileManager.getDropTable(nameInput) != null) {
                Message.Boss_New_AlreadyExists.msg(sender, "DropTable");
                return;
            }

            for(String s : this.bossDropTableManager.getValidDropTableTypes()) {
                if(s.equalsIgnoreCase(typeInput)) {
                    validType = true;
                    break;
                }
            }

            if(!validType) {
                Message.Boss_New_InvalidDropTableType.msg(sender);
                return;
            }

            DropTable dropTable = BossAPI.createBaseDropTable(nameInput, typeInput);

            if(dropTable == null) {
                Message.Boss_New_SomethingWentWrong.msg(sender, "DropTable");
            } else {
                Message.Boss_New_DropTable.msg(sender, nameInput, typeInput);
            }

            return;
        }

        //-------------------
        // S K I L L
        //-------------------
        if(args.length == 5 && args[1].equalsIgnoreCase("skill")) {
            String nameInput = args[2];
            String typeInput = args[3];
            String modeInput = args[4];
            boolean validType = false, validMode = false;
            List<SkillMode> skillModes = SkillMode.getSkillModes();

            if(this.skillsFileManager.getSkill(nameInput) != null) {
                Message.Boss_New_AlreadyExists.msg(sender, "Skill");
                return;
            }

            for(String s : this.bossSkillManager.getValidSkillTypes()) {
                if(s.equalsIgnoreCase(typeInput)) {
                    validType = true;
                    break;
                }
            }

            for(SkillMode skillMode : skillModes) {
                if(skillMode.name().equalsIgnoreCase(modeInput)) {
                    validMode = true;
                    break;
                }
            }

            if(!validType) {
                Message.Boss_New_InvalidSkillType.msg(sender);
                return;
            }

            if(!validMode) {
                Message.Boss_New_InvalidSkillMode.msg(sender);
                return;
            }

            Skill skill = BossAPI.createBaseSkill(nameInput, typeInput, modeInput);

            if(skill == null) {
                Message.Boss_New_SomethingWentWrong.msg(sender, "Skill");
            } else {
                Message.Boss_New_Skill.msg(sender, nameInput, typeInput);
            }

            return;
        }

        Message.Boss_New_InvalidArgs.msg(sender);
    }

    private List<String> appendList(String[] args) {
        String[] params = Arrays.copyOfRange(args, 3, args.length);

        List<String> sections = new ArrayList<>();
        StringBuilder currentSection = new StringBuilder();

        for (String param : params) {
            String[] split = param.split("\\|");
            if (split.length == 1) {
                currentSection.append(split[0]).append(" ");
                continue;
            }

            boolean firstAdded = false;
            for (String piece : split) {
                currentSection.append(piece).append(" ");

                if (!firstAdded) {
                    sections.add(currentSection.toString().trim());
                    currentSection = new StringBuilder();
                    firstAdded = true;
                }
            }
        }

        if (!currentSection.toString().trim().isEmpty())
            sections.add(currentSection.toString().trim());

        return sections;
    }
}
