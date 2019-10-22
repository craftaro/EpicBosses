package com.songoda.epicbosses.commands;

import com.songoda.core.commands.AbstractCommand;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.managers.BossDropTableManager;
import com.songoda.epicbosses.managers.BossSkillManager;
import com.songoda.epicbosses.managers.files.DropTableFileManager;
import com.songoda.epicbosses.managers.files.SkillsFileManager;
import com.songoda.epicbosses.skills.SkillMode;
import com.songoda.epicbosses.utils.Message;
import org.bukkit.command.CommandSender;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CommandNewDropTable extends AbstractCommand {

    private DropTableFileManager dropTableFileManager;
    private BossDropTableManager bossDropTableManager;

    public CommandNewDropTable(DropTableFileManager dropTableFileManager, BossDropTableManager bossDropTableManager) {
        super(false, "new droptable");
        this.dropTableFileManager = dropTableFileManager;
        this.bossDropTableManager = bossDropTableManager;
    }

    @Override
    protected ReturnType runCommand(CommandSender sender, String... args) {
        if (args.length != 2)
            return ReturnType.SYNTAX_ERROR;

        String nameInput = args[0];
        String typeInput = args[1];
        boolean validType = false;

        if (this.dropTableFileManager.getDropTable(nameInput) != null) {
            Message.Boss_New_AlreadyExists.msg(sender, "DropTable");
            return ReturnType.FAILURE;
        }

        for (String s : this.bossDropTableManager.getValidDropTableTypes()) {
            if (s.equalsIgnoreCase(typeInput)) {
                validType = true;
                break;
            }
        }

        if (!validType) {
            Message.Boss_New_InvalidDropTableType.msg(sender);
            return ReturnType.FAILURE;
        }

        DropTable dropTable = BossAPI.createBaseDropTable(nameInput, typeInput);

        if (dropTable == null) {
            Message.Boss_New_SomethingWentWrong.msg(sender, "DropTable");
        } else {
            Message.Boss_New_DropTable.msg(sender, nameInput, typeInput);
        }

        return ReturnType.SUCCESS;
    }

    @Override
    protected List<String> onTab(CommandSender commandSender, String... args) {
        if (args.length == 1) {
            return Collections.singletonList("name");
        } else if (args.length == 2) {
            return this.bossDropTableManager.getValidDropTableTypes();
        }
        return null;
    }

    @Override
    public String getPermissionNode() {
        return "boss.admin";
    }

    @Override
    public String getSyntax() {
        return "new droptable <name> <type>";
    }

    @Override
    public String getDescription() {
        return "Create a new drop table section.";
    }
}
