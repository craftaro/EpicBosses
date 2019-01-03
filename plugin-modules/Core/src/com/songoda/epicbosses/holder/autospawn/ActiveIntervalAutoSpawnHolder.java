package com.songoda.epicbosses.holder.autospawn;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.autospawns.AutoSpawn;
import com.songoda.epicbosses.autospawns.SpawnType;
import com.songoda.epicbosses.holder.ActiveAutoSpawnHolder;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.time.TimeUnit;
import lombok.Getter;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 03-Jan-19
 */
public class ActiveIntervalAutoSpawnHolder extends ActiveAutoSpawnHolder {

    @Getter private BukkitTask intervalTask = null;
    @Getter private long nextCompletedTime = 0L;

    public ActiveIntervalAutoSpawnHolder(SpawnType spawnType, AutoSpawn autoSpawn) {
        super(spawnType, autoSpawn);
    }

    public void restartInterval() {
        stopInterval();

        if(getAutoSpawn().getEditing() != null && getAutoSpawn().getEditing()) return;

        Integer delay = getAutoSpawn().getIntervalSpawnData().getSpawnRate();

        if(delay == null) {
            Debug.AUTOSPAWN_INTERVALNOTREAL.debug("null", BossAPI.getAutoSpawnName(getAutoSpawn()));
            return;
        }

        long currentMs = System.currentTimeMillis();
        long delayMs = (long) TimeUnit.SECONDS.to(TimeUnit.MILLISECONDS, delay);

        this.nextCompletedTime = currentMs + delayMs;
        this.intervalTask = new BukkitRunnable() {
            private int timerTime = delay;

            public void run() {
                this.timerTime -= 1;

                if(this.timerTime <= 0) {

                }
            }

        }.runTaskTimer(CustomBosses.get(), 20L, 20L);
    }

    public long getRemainingMs() {
        long currentMs = System.currentTimeMillis();

        if(currentMs > this.nextCompletedTime) return 0;

        return this.nextCompletedTime - currentMs;
    }

    public void stopInterval() {
        if(this.intervalTask != null) ServerUtils.get().cancelTask(this.intervalTask);
    }



}
