package net.aminecraftdev.custombosses.innerapi.command.builder;

import net.aminecraftdev.custombosses.innerapi.command.builder.attributes.*;
import net.aminecraftdev.custombosses.innerapi.message.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.lang.reflect.Field;
import java.util.Arrays;

/**
 * Created by charl on 03-May-17.
 */
public abstract class CommandService<T extends CommandSender> extends BukkitCommand {

    private static CommandMap _commandMap = null;

    private String _command, _description, _usage, _noPermMessage, _permission;
    private String[] _aliases;

    /**
     * Construct a new command.
     *
     * @param command     The command label
     * @param description The command description
     * @param aliases     Aliases of the command
     */
    public CommandService(String command, String description, String[] aliases) {
        super(command);

        _command = command;
        _description = description;
        _aliases = aliases;
    }

    /**
     * Construct a new command.
     *
     * @param command     The command label
     * @param description The command description
     */
    public CommandService(String command, String description) {
        this(command, description, new String[]{});
    }

    /**
     * Construct a new command.
     *
     * @param cmd The command
     */
    public CommandService(Class<? extends CommandService> cmd) {
        this(cmd.getAnnotation(Name.class).value(), cmd.getAnnotation(Description.class).value());

        if(cmd.isAnnotationPresent(Alias.class))
            _aliases = cmd.getAnnotation(Alias.class).value();

        if(cmd.isAnnotationPresent(Permission.class))
            _permission = cmd.getAnnotation(Permission.class).value();

        if(cmd.isAnnotationPresent(Usage.class))
            _usage = cmd.getAnnotation(Usage.class).value();

        if(cmd.isAnnotationPresent(NoPermission.class))
            _noPermMessage = cmd.getAnnotation(NoPermission.class).value();

        register();
    }

    @Override
    public final boolean execute(CommandSender commandSender, String s, String[] args) {
        if(testPermission(commandSender)) return false;

        try {
            execute((T) commandSender, args);
        } catch (ClassCastException e) {
            commandSender.sendMessage(MessageUtils.translateString("&4You cannot use that command."));
            return false;
        }
        return true;
    }

    /**
     * This is fired when the command is executed.
     *
     * @param sender Sender of the command
     * @param args   Command arguments
     */
    public abstract void execute(T sender, String[] args);


    /**
     * This method will register a command without the need of plugin.yml
     */
    private void register() {
        if (_commandMap != null) {
            setFields();
            _commandMap.register(_command, this);
            return;
        }

        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            _commandMap = (CommandMap) field.get(Bukkit.getServer());

            setFields();

            _commandMap.register(_command, this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFields() {
        setDescription(_description);

        if(_aliases != null) setAliases(Arrays.asList(_aliases));
        if(_usage != null) setUsage(_usage);
        if(_permission != null) setPermission(_permission);

        if(_noPermMessage != null) {
            setPermissionMessage(MessageUtils.translateString(_noPermMessage));
        } else {
            setPermissionMessage(MessageUtils.translateString("&4You do not have <permission> for that command."));
        }
    }

    @Override
    public String getDescription() {
        return _description;
    }
}
