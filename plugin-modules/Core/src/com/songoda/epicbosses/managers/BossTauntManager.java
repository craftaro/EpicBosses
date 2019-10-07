package com.songoda.epicbosses.managers;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.elements.TauntElement;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.utils.MessageUtils;
import com.songoda.epicbosses.utils.NumberUtils;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 04-Nov-18
 */
public class BossTauntManager {

    private EpicBosses plugin;

    public BossTauntManager(EpicBosses plugin) {
        this.plugin = plugin;
    }

    public void handleTauntSystem(ActiveBossHolder activeBossHolder) {
        BossEntity bossEntity = activeBossHolder.getBossEntity();

        if (bossEntity.getMessages() == null) return;
        if (bossEntity.getMessages().getTaunts() == null) return;

        TauntElement tauntElement = bossEntity.getMessages().getTaunts();
        Integer delay = tauntElement.getDelay();
        Integer radius = tauntElement.getRadius();
        List<String> taunts = tauntElement.getTaunts();

        if (delay == null) delay = 60;
        if (radius == null) radius = 100;

        if (taunts != null) {
            if (taunts.isEmpty()) return;

            createRunnable(taunts, activeBossHolder, NumberUtils.get().getSquared(radius), delay);
        }
    }

    private void createRunnable(List<String> taunts, ActiveBossHolder activeBossHolder, int radius, int delay) {
        new BukkitRunnable() {
            Queue<String> queue = new LinkedList<>(taunts);

            @Override
            public void run() {
                if (activeBossHolder.isDead() || BossTauntManager.this.plugin.getBossEntityManager().isAllEntitiesDead(activeBossHolder)) {
                    cancel();
                    return;
                }

                if (this.queue.isEmpty()) this.queue = new LinkedList<>(taunts);

                List<String> messages = BossAPI.getStoredMessages(this.queue.poll());

                if (messages != null) {
                    MessageUtils.get().sendMessage(activeBossHolder.getLocation(), radius, messages);
                }
            }
        }.runTaskTimer(this.plugin, delay * 20, delay * 20);
    }
}
