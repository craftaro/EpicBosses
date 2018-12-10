package com.songoda.epicbosses.commands.boss;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.managers.BossDropTableManager;
import com.songoda.epicbosses.managers.files.CommandsFileManager;
import com.songoda.epicbosses.managers.files.DropTableFileManager;
import com.songoda.epicbosses.managers.files.MessagesFileManager;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.Permission;
import com.songoda.epicbosses.utils.command.SubCommand;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
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
 */
public class BossNewCmd extends SubCommand {

    private DropTableFileManager dropTableFileManager;
    private BossDropTableManager bossDropTableManager;
    private MessagesFileManager messagesFileManager;
    private CommandsFileManager commandsFileManager;

    public BossNewCmd(CustomBosses plugin) {
        super("new");

        this.dropTableFileManager = plugin.getDropTableFileManager();
        this.bossDropTableManager = plugin.getBossDropTableManager();
        this.messagesFileManager = plugin.getBossMessagesFileManager();
        this.commandsFileManager = plugin.getBossCommandFileManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!Permission.admin.hasPermission(sender)) {
            Message.Boss_New_NoPermission.msg(sender);
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
        if(args.length == 3 && args[1].equalsIgnoreCase("skill")) {
            //TODO: Complete new skill command

            return;
        }

        Message.Boss_New_InvalidArgs.msg(sender);
        return;
    }

    private List<String> appendList(String[] args) {
        int length = args.length;
        List<String> listOfElement = new ArrayList<>();
        StringBuilder current = new StringBuilder();

        for(int i = 4; i < length; i++) {
            String arg = args[i];

            if(arg.contains("||")) {
                String[] split = arg.split("||");

                current.append(split[0]);
                listOfElement.add(current.toString());

                if(split.length >= 2) {
                    current = new StringBuilder(split[1]);
                } else {
                    current = new StringBuilder();
                }

                continue;
            }

            current.append(arg);
        }

        listOfElement.add(current.toString());
        return listOfElement;
    }
}
