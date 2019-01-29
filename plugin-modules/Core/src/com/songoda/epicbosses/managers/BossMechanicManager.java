package com.songoda.epicbosses.managers;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.managers.interfaces.IMechanicManager;
import com.songoda.epicbosses.mechanics.IBossMechanic;
import com.songoda.epicbosses.mechanics.boss.*;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.IMechanic;
import com.songoda.epicbosses.utils.ServerUtils;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Jun-18
 */
public class BossMechanicManager implements IMechanicManager<BossEntity, ActiveBossHolder, IBossMechanic> {

    private Queue<IBossMechanic> mechanics;
    private final CustomBosses customBosses;

    public BossMechanicManager(CustomBosses customBosses) {
        this.customBosses = customBosses;
    }

    @Override
    public void load() {
        this.mechanics = new LinkedList<>();

        registerMechanic(new EntityTypeMechanic());
        registerMechanic(new NameMechanic());
        registerMechanic(new HealthMechanic());
        registerMechanic(new EquipmentMechanic(this.customBosses.getItemStackManager()));
        registerMechanic(new WeaponMechanic(this.customBosses.getItemStackManager()));
        registerMechanic(new PotionMechanic());
        registerMechanic(new SettingsMechanic());
    }

    @Override
    public void registerMechanic(IBossMechanic mechanic) {
        this.mechanics.add(mechanic);
    }

    @Override
    public void handleMechanicApplication(BossEntity bossEntity, ActiveBossHolder activeBossHolder) {
        if(bossEntity != null && activeBossHolder != null) {
            if(bossEntity.isEditing()) {
                Debug.ATTEMPTED_TO_SPAWN_WHILE_DISABLED.debug();
                return;
            }

            Queue<IBossMechanic> queue = new LinkedList<>(this.mechanics);

            while(!queue.isEmpty()) {
                IBossMechanic mechanic = queue.poll();

                if(mechanic == null) continue;

                ServerUtils.get().logDebug("Applying " + mechanic.getClass().getSimpleName());

                if(didMechanicApplicationFail(mechanic, bossEntity, activeBossHolder)) {
                    Debug.FAILED_TO_APPLY_MECHANIC.debug(mechanic.getClass().getSimpleName());
                }
            }
        }
    }

    private boolean didMechanicApplicationFail(IBossMechanic mechanic, BossEntity bossEntity, ActiveBossHolder activeBossHolder) {
        if(mechanic == null) return true;

        if(!mechanic.applyMechanic(bossEntity, activeBossHolder)) {
            Debug.MECHANIC_APPLICATION_FAILED.debug(mechanic.getClass().getSimpleName());
            return true;
        }

        return false;
    }
}
