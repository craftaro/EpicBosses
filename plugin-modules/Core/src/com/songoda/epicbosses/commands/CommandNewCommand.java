package com.songoda.epicbosses.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.epicbosses.managers.files.CommandsFileManager;
import com.songoda.epicbosses.utils.Message;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandNewCommand extends AbstractCommand {

    private CommandsFileManager commandsFileManager;

    public CommandNewCommand(CommandsFileManager commandsFileManager) {
        super(false, "new command");
        this.commandsFileManager = commandsFileManager;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        if (args.length < 2)
            return ReturnType.SYNTAX_ERROR;
        String nameInput = args[0];

        if (this.commandsFileManager.getCommands(nameInput) != null) {
            Message.Boss_New_AlreadyExists.msg(sender, "Command");
            return ReturnType.FAILURE;
        }

        List<String> commands = appendList(args);

        this.commandsFileManager.addNewCommand(nameInput, commands);
        this.commandsFileManager.save();

        Message.Boss_New_Command.msg(sender, nameInput);
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender commandSender, String... args) {
        if (args.length == 1) {
            return Collections.singletonList("name");
        }
        return Collections.singletonList("command");
    }

    @Override
    public String getPermissionNode() {
        return "boss.admin";
    }

    @Override
    public String getSyntax() {
        return "/boss new command <name> <commands...>";
    }

    @Override
    public String getDescription() {
        return "Create a new command section.";
    }

    private List<String> appendList(String[] args) {
        String[] params = Arrays.copyOfRange(args, 2, args.length);

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
