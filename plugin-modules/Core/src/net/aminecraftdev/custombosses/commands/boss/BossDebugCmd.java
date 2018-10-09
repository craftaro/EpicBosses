package net.aminecraftdev.custombosses.commands.boss;

import net.aminecraftdev.custombosses.managers.files.BossesFileManager;
import net.aminecraftdev.custombosses.utils.command.SubCommand;
import org.bukkit.command.CommandSender;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 09-Oct-18
 */
public class BossDebugCmd extends SubCommand {

    private BossesFileManager bossesFileManager;

    public BossDebugCmd(BossesFileManager bossesFileManager) {
        super("debug");

        this.bossesFileManager = bossesFileManager;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        StringBuilder stringBuilder = new StringBuilder();

        this.bossesFileManager.getBossEntities().forEach((name, entity) -> stringBuilder.append(name).append(", "));

        System.out.println("CURRENT BOSSES: " +
                "\n" + stringBuilder.toString());
    }
}
