package com.songoda.epicbosses.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.epicbosses.managers.files.CommandsFileManager;
import com.songoda.epicbosses.managers.files.MessagesFileManager;
import com.songoda.epicbosses.utils.Message;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandNewMessage extends AbstractCommand {

    private CommandsFileManager commandsFileManager;
    private MessagesFileManager messagesFileManager;

    public CommandNewMessage(CommandsFileManager commandsFileManager, MessagesFileManager messagesFileManager) {
        super(false, "new message");
        this.commandsFileManager = commandsFileManager;
        this.messagesFileManager = messagesFileManager;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        if (args.length < 2)
            return ReturnType.SYNTAX_ERROR;

        String nameInput = args[0];

        if (this.commandsFileManager.getCommands(nameInput) != null) {
            Message.Boss_New_AlreadyExists.msg(sender, "Message");
            return ReturnType.FAILURE;
        }

        List<String> messages = appendList(args);

        this.messagesFileManager.addNewMessage(nameInput, messages);
        this.messagesFileManager.save();

        Message.Boss_New_Message.msg(sender, nameInput);
        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender commandSender, String... args) {
        if (args.length == 1) {
            return Collections.singletonList("name");
        }
        return Collections.singletonList("message");
    }

    @Override
    public String getPermissionNode() {
        return "boss.admin";
    }

    @Override
    public String getSyntax() {
        return "/boss new message <name> <message...>";
    }

    @Override
    public String getDescription() {
        return "Create a new message section.";
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
