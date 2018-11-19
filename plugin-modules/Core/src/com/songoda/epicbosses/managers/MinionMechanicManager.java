package com.songoda.epicbosses.managers;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.entity.MinionEntity;
import com.songoda.epicbosses.holder.ActiveMinionHolder;
import com.songoda.epicbosses.managers.interfaces.IMechanicManager;
import com.songoda.epicbosses.mechanics.IMinionMechanic;
import com.songoda.epicbosses.mechanics.minions.*;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.ServerUtils;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 12-Nov-18
 */
public class MinionMechanicManager implements IMechanicManager<MinionEntity, ActiveMinionHolder, IMinionMechanic> {

    private Queue<IMinionMechanic> mechanics;
    private final CustomBosses customBosses;

    public MinionMechanicManager(CustomBosses customBosses) {
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
    public void registerMechanic(IMinionMechanic mechanic) {
        this.mechanics.add(mechanic);
    }

    @Override
    public void handleMechanicApplication(MinionEntity minionEntity, ActiveMinionHolder activeMinionHolder) {
        if(minionEntity != null && activeMinionHolder != null) {
            if(minionEntity.isEditing()) {
                Debug.ATTEMPTED_TO_SPAWN_WHILE_DISABLED.debug();
                return;
            }

            Queue<IMinionMechanic> queue = new LinkedList<>(this.mechanics);

            while(!queue.isEmpty()) {
                IMinionMechanic mechanic = queue.poll();

                if(mechanic == null) continue;

                ServerUtils.get().logDebug("Applying " + mechanic.getClass().getSimpleName());

                if(didMechanicApplicationFail(mechanic, minionEntity, activeMinionHolder)) {
                    Debug.FAILED_TO_APPLY_MECHANIC.debug(mechanic.getClass().getSimpleName());
                }
            }
        }
    }

    private boolean didMechanicApplicationFail(IMinionMechanic mechanic, MinionEntity minionEntity, ActiveMinionHolder activeBossHolder) {
        if(mechanic == null) return true;

        if(!mechanic.applyMechanic(minionEntity, activeBossHolder)) {
            Debug.MECHANIC_APPLICATION_FAILED.debug(mechanic.getClass().getSimpleName());
            return true;
        }

        return false;
    }
}
