package com.songoda.epicbosses.mechanics.minions;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.entity.MinionEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.entity.elements.MainStatsElement;
import com.songoda.epicbosses.holder.ActiveMinionHolder;
import com.songoda.epicbosses.mechanics.IMinionMechanic;
import com.songoda.epicbosses.utils.StringUtils;
import org.bukkit.entity.LivingEntity;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Jun-18
 */
public class NameMechanic implements IMinionMechanic {

    private CustomBosses plugin = CustomBosses.get();

    @Override
    public boolean applyMechanic(MinionEntity minionEntity, ActiveMinionHolder activeMinionHolder) {
        if(activeMinionHolder.getLivingEntityMap() == null || activeMinionHolder.getLivingEntityMap().isEmpty()) return false;

        for(EntityStatsElement entityStatsElement : minionEntity.getEntityStats()) {
            MainStatsElement mainStatsElement = entityStatsElement.getMainStats();
            LivingEntity livingEntity = activeMinionHolder.getLivingEntity(mainStatsElement.getPosition());
            String customName = mainStatsElement.getDisplayName();

            if(livingEntity == null) return false;

            if(customName != null) {
                String formattedName = StringUtils.get().translateColor(customName);

                if(CustomBosses.get().getConfig().getBoolean("Hooks.HolographicDisplays.enabled", false) && this.plugin.getHolographicDisplayHelper().isConnected()) {
                    this.plugin.getHolographicDisplayHelper().createHologram(livingEntity, formattedName);
                    livingEntity.setCustomNameVisible(false);
                } else {
                    livingEntity.setCustomName(formattedName);
                    livingEntity.setCustomNameVisible(true);
                }
            }
        }

        return true;
    }
}
