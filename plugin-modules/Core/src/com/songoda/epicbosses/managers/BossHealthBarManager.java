package com.songoda.epicbosses.managers;

import com.songoda.core.compatibility.ServerVersion;
import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.entity.elements.HealthBarElement;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.utils.MessageUtils;
import com.songoda.epicbosses.utils.NumberUtils;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

public class BossHealthBarManager {

    private final EpicBosses epicBosses;

    public BossHealthBarManager(EpicBosses epicBosses) {
        this.epicBosses = epicBosses;
    }

    public void handleHealthBar(ActiveBossHolder activeBossHolder) {
        if (activeBossHolder.getBossEntity().getHealthBar() == null || activeBossHolder.getBossEntity().getHealthBar().getText() == null)
            return;

        createNewRunnable(activeBossHolder);
    }

    public void createNewRunnable(ActiveBossHolder activeBossHolder) {
        HealthBarElement healthBarElement = activeBossHolder.getBossEntity().getHealthBar();

        new BukkitRunnable() {
            @Override
            public void run() {
                if (activeBossHolder.isDead()) {
                    cancel();
                    return;
                }

                if (activeBossHolder.getLivingEntity() == null) return;

                // Parse placeholders
                String formattedText = healthBarElement.getText().replaceAll("(?i)%currenthealth%", String.valueOf(NumberUtils.get().formatDouble(activeBossHolder.getLivingEntity().getHealth())))
                        .replaceAll("(?i)%maxhealth%", String.valueOf(NumberUtils.get().formatDouble(getMaxHealth(activeBossHolder.getLivingEntity()))));

                MessageUtils.get().sendActionBar(activeBossHolder.getLocation(), NumberUtils.get().getSquared(healthBarElement.getRadius()), formattedText);
            }
        }.runTaskTimer(epicBosses, 10, 20);
    }

    private double getMaxHealth(LivingEntity entity) {
        if (ServerVersion.isServerVersionAbove(ServerVersion.V1_8)) {
            return entity.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue();
        } else {
            return entity.getMaxHealth();
        }
    }
}