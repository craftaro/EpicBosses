package com.songoda.epicbosses.utils.dependencies;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.utils.IHelper;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;
import org.bukkit.scheduler.BukkitRunnable;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Jan-19
 */
public class HolographicDisplayHelper implements IHelper {

    @Override
    public boolean isConnected() {
        return Bukkit.getPluginManager().getPlugin("HolographicDisplays") != null;
    }

    public void createHologram(LivingEntity livingEntity, String line) {
        CustomBosses plugin = CustomBosses.get();
        Hologram hologram = HologramsAPI.createHologram(plugin, livingEntity.getEyeLocation());

        hologram.insertTextLine(1, line);

        new BukkitRunnable() {
            @Override
            public void run() {
                if(!livingEntity.isDead()) {
                    hologram.teleport(livingEntity.getEyeLocation());
                } else {
                    hologram.delete();
                    cancel();
                }
            }
        }.runTaskTimer(plugin, 1L, 1L);
    }
}
