package com.songoda.epicbosses.managers;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.listeners.after.BossDeathListener;
import com.songoda.epicbosses.listeners.during.BossDamageListener;
import com.songoda.epicbosses.listeners.during.BossMinionTargetListener;
import com.songoda.epicbosses.listeners.during.BossSkillListener;
import com.songoda.epicbosses.listeners.pre.BossSpawnListener;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.ILoadable;
import com.songoda.epicbosses.utils.ServerUtils;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 16-Oct-18
 */
public class BossListenerManager implements ILoadable {

    private boolean hasBeenLoaded = false;
    private EpicBosses plugin;

    public BossListenerManager(EpicBosses plugin) {
        this.plugin = plugin;
    }

    @Override
    public void load() {
        if(this.hasBeenLoaded) {
            Debug.FAILED_TO_LOAD_BOSSLISTENERMANAGER.debug();
            return;
        }

        ServerUtils serverUtils = ServerUtils.get();

        serverUtils.registerListener(new BossSpawnListener(this.plugin));
        serverUtils.registerListener(new BossDamageListener(this.plugin));
        serverUtils.registerListener(new BossMinionTargetListener(this.plugin));
        serverUtils.registerListener(new BossSkillListener(this.plugin));
        serverUtils.registerListener(new BossDeathListener(this.plugin));

        this.hasBeenLoaded = true;
    }
}
