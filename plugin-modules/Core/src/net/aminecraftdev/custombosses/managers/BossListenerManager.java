package net.aminecraftdev.custombosses.managers;

import net.aminecraftdev.custombosses.CustomBosses;
import net.aminecraftdev.custombosses.listeners.pre.BossSpawnListener;
import net.aminecraftdev.custombosses.utils.Debug;
import net.aminecraftdev.custombosses.utils.ILoadable;
import net.aminecraftdev.custombosses.utils.ServerUtils;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 16-Oct-18
 */
public class BossListenerManager implements ILoadable {

    private boolean hasBeenLoaded = false;
    private CustomBosses plugin;

    public BossListenerManager(CustomBosses plugin) {
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


        this.hasBeenLoaded = true;
    }
}
