package com.songoda.epicbosses.utils.command;

import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.command.attributes.*;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandMap;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 28-Apr-18
 */
public abstract class CommandService<T extends CommandSender> extends BukkitCommand {

    private static CommandMap _commandMap = null;

    private String permission, noPermissionMsg;
    private String command, description;
    private Class<T> parameterClass;
    private String[] aliases;

    public CommandService(Class<? extends CommandService> cmd) {
        super(cmd.getAnnotation(Name.class).value());

        this.command = cmd.getAnnotation(Name.class).value();
        this.description = cmd.getAnnotation(Description.class).value();
        this.aliases = new String[]{};

        if(cmd.isAnnotationPresent(Alias.class))
            this.aliases = cmd.getAnnotation(Alias.class).value();

        if(cmd.isAnnotationPresent(Permission.class))
            this.permission = cmd.getAnnotation(Permission.class).value();

        if(cmd.isAnnotationPresent(NoPermission.class))
            this.noPermissionMsg = cmd.getAnnotation(NoPermission.class).value();

        getGenericClass();
        register();
    }

    @Override
    public final boolean execute(CommandSender commandSender, String s, String[] args) {
        if(this.permission != null && !testPermission(commandSender)) return false;


        if(!parameterClass.isInstance(commandSender)) {
            commandSender.sendMessage(StringUtils.get().translateColor("&4You cannot use that command."));
            return false;
        }

        execute(parameterClass.cast(commandSender), args);
        return true;
    }

    public abstract void execute(T sender, String[] args);

    public String getCommand() {
        return this.command;
    }

    public String[] getArrayAliases() {
        return this.aliases;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    private void register() {
        if (_commandMap != null) {
            setFields();
            _commandMap.register(command, this);
            return;
        }

        try {
            Field field = Bukkit.getServer().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            _commandMap = (CommandMap) field.get(Bukkit.getServer());

            setFields();

            _commandMap.register(command, this);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFields() {
        if(this.aliases != null) setAliases(Arrays.asList(this.aliases));
        if(this.description != null) setDescription(this.description);
        if(this.permission != null) setPermission(this.permission);
        if(this.noPermissionMsg != null) setPermissionMessage(this.noPermissionMsg);
    }

    private void getGenericClass() {
        if(this.parameterClass == null) {
            Type superClass = getClass().getGenericSuperclass();
            Type tType = ((ParameterizedType) superClass).getActualTypeArguments()[0];
            String className = tType.toString().split(" ")[1];
            try {
                this.parameterClass = (Class<T>) Class.forName(className);
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
