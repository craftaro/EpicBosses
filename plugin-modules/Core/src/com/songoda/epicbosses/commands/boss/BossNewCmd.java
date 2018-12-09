package com.songoda.epicbosses.commands.boss;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.managers.BossDropTableManager;
import com.songoda.epicbosses.managers.files.DropTableFileManager;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.Permission;
import com.songoda.epicbosses.utils.command.SubCommand;
import org.bukkit.command.CommandSender;

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

    public BossNewCmd(CustomBosses plugin) {
        super("new");

        this.dropTableFileManager = plugin.getDropTableFileManager();
        this.bossDropTableManager = plugin.getBossDropTableManager();
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!Permission.admin.hasPermission(sender)) {
            Message.Boss_New_NoPermission.msg(sender);
            return;
        }

        if(args.length == 4 && args[1].equalsIgnoreCase("droptable")) {
            String nameInput = args[2];
            String typeInput = args[3];
            boolean validType = false;

            if(this.dropTableFileManager.getDropTable(nameInput) != null) {
                Message.Boss_New_DropTableAlreadyExists.msg(sender);
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

        if(args.length == 3 && args[1].equalsIgnoreCase("skill")) {
            //TODO: Complete new skill command

            return;
        }

        Message.Boss_New_InvalidArgs.msg(sender);
        return;
    }
}
