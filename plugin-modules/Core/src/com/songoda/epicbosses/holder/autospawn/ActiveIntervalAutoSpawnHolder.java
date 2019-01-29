package com.songoda.epicbosses.holder.autospawn;

import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.autospawns.AutoSpawn;
import com.songoda.epicbosses.autospawns.SpawnType;
import com.songoda.epicbosses.autospawns.types.IntervalSpawnElement;
import com.songoda.epicbosses.events.BossDeathEvent;
import com.songoda.epicbosses.events.PreBossDeathEvent;
import com.songoda.epicbosses.holder.ActiveAutoSpawnHolder;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.listeners.IBossDeathHandler;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.ObjectUtils;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.time.TimeUnit;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 03-Jan-19
 */
public class ActiveIntervalAutoSpawnHolder extends ActiveAutoSpawnHolder {

    @Getter private final IntervalSpawnElement intervalSpawnElement;

    @Getter private BukkitTask intervalTask = null;
    @Getter private long nextCompletedTime = 0L;

    public ActiveIntervalAutoSpawnHolder(SpawnType spawnType, AutoSpawn autoSpawn) {
        super(spawnType, autoSpawn);

        this.intervalSpawnElement = autoSpawn.getIntervalSpawnData();
    }

    @Override
    public boolean canSpawn() {
        if(getAutoSpawn().isEditing()) return false;
        if(!getAutoSpawn().getType().equalsIgnoreCase("INTERVAL")) return false;

        int currentActiveAmount = getCurrentActiveBossHolders();
        int maxAmount = getAutoSpawn().getAutoSpawnSettings().getMaxAliveAtOnce();

        Location location = this.intervalSpawnElement.getSpawnLocation();
        boolean spawnIfChunkNotLoaded = ObjectUtils.getValue(getAutoSpawn().getAutoSpawnSettings().getSpawnWhenChunkIsntLoaded(), false);

        if(location == null) return false;
        if(!spawnIfChunkNotLoaded && !location.getChunk().isLoaded()) return false;
        if(isSpawnAfterLastBossIsKilled() && !getActiveBossHolders().isEmpty()) return false;

        return currentActiveAmount < maxAmount;
    }

    public boolean isSpawnAfterLastBossIsKilled() {
        return ObjectUtils.getValue(this.intervalSpawnElement.getSpawnAfterLastBossIsKilled(), false);
    }

    public void restartInterval() {
        stopInterval();

        if(getAutoSpawn().isEditing()) return;

        Integer delay = this.intervalSpawnElement.getSpawnRate();

        if(delay == null) {
            Debug.AUTOSPAWN_INTERVALNOTREAL.debug("null", BossAPI.getAutoSpawnName(getAutoSpawn()));
            return;
        }

        int delaySec = (int) TimeUnit.MINUTES.to(TimeUnit.SECONDS, delay);

        this.intervalTask = ServerUtils.get().runTimer(delaySec*20, delaySec*20, () -> {
            boolean canSpawn = canSpawn();

            if(!canSpawn) return;

            this.intervalSpawnElement.attemptSpawn(this);

            if(isSpawnAfterLastBossIsKilled()) {
                cancelCurrentInterval();
                return;
            }

            updateNextCompleteTime();
        });

        updateNextCompleteTime();
    }

    public void stopInterval() {
        cancelCurrentInterval();
        getActiveBossHolders().forEach(ActiveBossHolder::killAll);
        getActiveBossHolders().clear();
    }

    public long getRemainingMs() {
        long currentMs = System.currentTimeMillis();

        if(currentMs > this.nextCompletedTime) return 0;

        return this.nextCompletedTime - currentMs;
    }

    public IBossDeathHandler getPostDeathHandler() {
        return event -> {
            boolean spawnAfterLastBossIsKilled = ObjectUtils.getValue(this.intervalSpawnElement.getSpawnAfterLastBossIsKilled(), false);
            ActiveBossHolder activeBossHolder = event.getActiveBossHolder();

            if(getActiveBossHolders().contains(activeBossHolder)) {
                getActiveBossHolders().remove(activeBossHolder);

                if(spawnAfterLastBossIsKilled) {
                    restartInterval();
                    updateNextCompleteTime();
                }
            }
        };
    }

    private void cancelCurrentInterval() {
        if(this.intervalTask != null) ServerUtils.get().cancelTask(this.intervalTask);

        this.nextCompletedTime = 0;
    }

    private void updateNextCompleteTime() {
        Integer delay = this.intervalSpawnElement.getSpawnRate();

        if(delay == null) {
            Debug.AUTOSPAWN_INTERVALNOTREAL.debug("null", BossAPI.getAutoSpawnName(getAutoSpawn()));
            return;
        }

        long delayMs = (long) TimeUnit.MINUTES.to(TimeUnit.MILLISECONDS, delay);

        this.nextCompletedTime = System.currentTimeMillis() + delayMs;
    }
}
