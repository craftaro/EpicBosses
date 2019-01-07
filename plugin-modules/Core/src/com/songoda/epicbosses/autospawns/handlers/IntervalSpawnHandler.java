package com.songoda.epicbosses.autospawns.handlers;

import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.autospawns.AutoSpawn;
import com.songoda.epicbosses.autospawns.settings.AutoSpawnSettings;
import com.songoda.epicbosses.autospawns.types.IntervalSpawnElement;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.holder.autospawn.ActiveIntervalAutoSpawnHolder;
import com.songoda.epicbosses.listeners.IBossDeathHandler;
import com.songoda.epicbosses.utils.MessageUtils;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.ObjectUtils;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import org.bukkit.Location;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 07-Jan-19
 */
public class IntervalSpawnHandler {

    public boolean attemptSpawn(ActiveIntervalAutoSpawnHolder activeAutoSpawnHolder, IntervalSpawnElement intervalSpawnElement) {
        IBossDeathHandler bossDeathHandler = activeAutoSpawnHolder.getPostDeathHandler();
        AutoSpawn autoSpawn = activeAutoSpawnHolder.getAutoSpawn();
        AutoSpawnSettings autoSpawnSettings = autoSpawn.getAutoSpawnSettings();
        boolean customSpawnMessage = ObjectUtils.getValue(autoSpawnSettings.getOverrideDefaultSpawnMessage(), false);
        String spawnMessage = autoSpawnSettings.getSpawnMessage();
        int amountToSpawn = ObjectUtils.getValue(autoSpawnSettings.getAmountPerSpawn(), 1);
        boolean shuffleList = ObjectUtils.getValue(autoSpawnSettings.getShuffleEntitiesList(), false);
        List<String> bosses = autoSpawn.getEntities();
        Location location = intervalSpawnElement.getSpawnLocation();

        if(bosses == null || bosses.isEmpty()) return false;

        if(shuffleList) Collections.shuffle(bosses);

        Queue<String> queue = new LinkedList<>(bosses);

        for(int i = 1; i <= amountToSpawn; i++) {
            if(queue.isEmpty()) queue = new LinkedList<>(bosses);

            BossEntity bossEntity = BossAPI.getBossEntity(queue.poll());
            ActiveBossHolder activeBossHolder = BossAPI.spawnNewBoss(bossEntity, location, null, null, customSpawnMessage);

            if(activeBossHolder == null) continue;

            activeBossHolder.getPostBossDeathHandlers().add(bossDeathHandler);
            activeAutoSpawnHolder.getActiveBossHolders().add(activeBossHolder);
        }

        if(customSpawnMessage && spawnMessage != null) {
            String x = NumberUtils.get().formatDouble(location.getBlockX());
            String y = NumberUtils.get().formatDouble(location.getBlockY());
            String z = NumberUtils.get().formatDouble(location.getBlockZ());
            String world = StringUtils.get().formatString(location.getWorld().getName());

            List<String> spawnMessages = BossAPI.getStoredMessages(spawnMessage);

            if(spawnMessages != null) {
                spawnMessages.replaceAll(s -> s.replace("{x}", x).replace("{y}", y).replace("{z}", z).replace("{world}", world));

                MessageUtils.get().sendMessage(location, -1, spawnMessages);
            }
        }

        return true;
    }

    public ClickAction getSpawnAfterLastBossIsKilledAction(IntervalSpawnElement intervalSpawnElement) {
        return event -> {};
    }

    public ClickAction getLocationAction(IntervalSpawnElement intervalSpawnElement) {
        return event -> {};
    }

    public ClickAction getPlaceholderAction(IntervalSpawnElement intervalSpawnElement) {
        return event -> {};
    }

    public ClickAction getSpawnRateAction(IntervalSpawnElement intervalSpawnElement) {
        return event -> {};
    }

}
