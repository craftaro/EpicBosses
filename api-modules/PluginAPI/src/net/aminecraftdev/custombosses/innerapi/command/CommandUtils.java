package net.aminecraftdev.custombosses.innerapi.command;

import org.boostednetwork.factionscore.utils.NumberUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/**
 * Created by charl on 28-Apr-17.
 */
public class CommandUtils {

    public static final Player strAsPlayer(String s) {
        if(Bukkit.getPlayer(s) == null) return null;

        return Bukkit.getPlayer(s);
    }

    public static final Integer strAsInteger(String s) {
        if(NumberUtils.isStringInteger(s)) return Integer.valueOf(s);

        return null;
    }

    public static final Double strAsDouble(String s) {
        if(NumberUtils.isStringDouble(s)) return Double.valueOf(s);

        return null;
    }

    public static final boolean isIntegerWithinItemStackSize(int i) {
        if(i > 64) return false;
        if(i < 1) return false;

        return true;
    }

    public static final boolean isCommandSenderPlayer(CommandSender commandSender) {
        if(!(commandSender instanceof Player)) return false;

        return true;
    }

}