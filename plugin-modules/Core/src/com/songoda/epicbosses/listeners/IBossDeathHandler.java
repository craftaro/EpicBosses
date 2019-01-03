package com.songoda.epicbosses.listeners;

import com.songoda.epicbosses.events.PreBossDeathEvent;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 03-Jan-19
 */
public interface IBossDeathHandler {

    void onPreDeath(PreBossDeathEvent event);

}
