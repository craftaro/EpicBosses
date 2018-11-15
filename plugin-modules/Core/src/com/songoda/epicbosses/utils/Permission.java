package com.songoda.epicbosses.utils;

import lombok.Getter;
import org.bukkit.command.CommandSender;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 04-Oct-18
 */
public enum Permission {

    admin("boss.admin"),
    create("boss.create"),
    debug("boss.debug"),
    give("boss.give"),
    reload("boss.reload"),
    nearby("boss.nearby");

    @Getter private String permission;

    Permission(String permission) {
        this.permission = permission;
    }

    public boolean hasPermission(CommandSender commandSender) {
        return commandSender.hasPermission(getPermission());
    }

}
