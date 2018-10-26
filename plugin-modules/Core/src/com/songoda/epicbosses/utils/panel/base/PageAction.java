package com.songoda.epicbosses.utils.panel.base;

import org.bukkit.entity.Player;

/**
 * @author Debugged
 * @version 1.0
 * @since 13-5-2017
 */
@FunctionalInterface
public interface PageAction {

    boolean onPageAction(Player player, int currentPage, int requestedPage);

}
