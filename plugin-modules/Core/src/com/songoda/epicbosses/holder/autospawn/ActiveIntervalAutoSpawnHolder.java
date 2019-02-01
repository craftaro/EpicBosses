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
import com.songoda.epicbosses.utils.time.TimeUtil;
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
        if(getAutoSpawn().isEditing()) {
            ServerUtils.get().logDebug("AutoSpawn failed to spawn due to editing enabled.");
            return false;
        }
        if(!getAutoSpawn().getType().equalsIgnoreCase("INTERVAL")) {
            ServerUtils.get().logDebug("AutoSpawn failed to spawn due to interval type not set.");
            return false;
        }

        int currentActiveAmount = getCurrentActiveBossHolders();
        int maxAmount = getAutoSpawn().getAutoSpawnSettings().getMaxAliveAtOnce();

        Location location = this.intervalSpawnElement.getSpawnLocation();
        boolean spawnIfChunkNotLoaded = ObjectUtils.getValue(getAutoSpawn().getAutoSpawnSettings().getSpawnWhenChunkIsntLoaded(), false);

        if(location == null) {
            ServerUtils.get().logDebug("AutoSpawn failed to spawn due to location is null.");
            return false;
        }
        if(!spawnIfChunkNotLoaded && !location.getChunk().isLoaded()) {
            ServerUtils.get().logDebug("AutoSpawn failed to spawn due to spawnIfChunkNotLoaded was false and chunk wasn't loaded.");
            return false;
        }
        if(isSpawnAfterLastBossIsKilled() && !getActiveBossHolders().isEmpty()) {
            ServerUtils.get().logDebug("AutoSpawn failed due to spawnAfterLastBossKilled is true and activeBossHolders is not empty.");
            return false;
        }

        boolean returnStatement = currentActiveAmount < maxAmount;

        if(!returnStatement) {
            ServerUtils.get().logDebug("AutoSpawn failed to spawn due to currentActiveAmount is greater then maxAmount of bosses.");
        }

        return returnStatement;
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

        int seconds = 10;

        ServerUtils.get().runLater((int) TimeUnit.SECONDS.to(TimeUnit.TICK, seconds), () -> {
            long delayTick = (long) TimeUnit.MINUTES.to(TimeUnit.TICK, delay);

            this.intervalTask = ServerUtils.get().runTimer(delayTick, delayTick, () -> {
                boolean canSpawn = canSpawn();

                if(!canSpawn) {
                    ServerUtils.get().logDebug("--- Failed to AutoSpawn. ---");
                    updateNextCompleteTime();
                    return;
                }

                ServerUtils.get().logDebug("AutoSpawn Spawn Attempt: " + this.intervalSpawnElement.attemptSpawn(this));

                if(isSpawnAfterLastBossIsKilled()) {
                    cancelCurrentInterval();
                    return;
                }

                updateNextCompleteTime();
            });

            ServerUtils.get().logDebug("Task delay: " + TimeUtil.getFormattedTime(TimeUnit.MINUTES, delay));

            updateNextCompleteTime();

            ServerUtils.get().logDebug("Static Delay: " + TimeUtil.getFormattedTime(TimeUnit.MILLISECONDS, (int) getRemainingMs()));
        });
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
