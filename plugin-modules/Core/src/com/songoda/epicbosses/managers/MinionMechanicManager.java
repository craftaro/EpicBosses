package com.songoda.epicbosses.managers;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.entity.MinionEntity;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.managers.interfaces.IMechanicManager;
import com.songoda.epicbosses.mechanics.*;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.IMechanic;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.mechanics.IOptionalMechanic;
import com.songoda.epicbosses.utils.mechanics.IPrimaryMechanic;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 12-Nov-18
 */
public class MinionMechanicManager implements IMechanicManager<MinionEntity, ActiveBossHolder> {

    private final CustomBosses customBosses;
    private Queue<IOptionalMechanic> optionalMechanics;
    private Queue<IPrimaryMechanic> primaryMechanics;

    public MinionMechanicManager(CustomBosses customBosses) {
        this.customBosses = customBosses;
    }

    @Override
    public void load() {
        this.primaryMechanics = new LinkedList<>();

        registerMechanic(new EntityTypeMechanic());
        registerMechanic(new NameMechanic());
        registerMechanic(new HealthMechanic());
        registerMechanic(new EquipmentMechanic(this.customBosses.getItemStackManager()));
        registerMechanic(new WeaponMechanic(this.customBosses.getItemStackManager()));
        registerMechanic(new PotionMechanic());
        registerMechanic(new SettingsMechanic());
    }

    @Override
    public void registerMechanic(IMechanic mechanic) {
        if(mechanic instanceof IPrimaryMechanic) {
            this.primaryMechanics.add((IPrimaryMechanic) mechanic);
        } else if(mechanic instanceof IOptionalMechanic) {
            this.optionalMechanics.add((IOptionalMechanic) mechanic);
        } else {
            Debug.MECHANIC_TYPE_NOT_STORED.debug();
        }
    }

    @Override
    public boolean handleMechanicApplication(MinionEntity minionEntity, ActiveBossHolder activeBossHolder) {
        if(minionEntity != null && activeBossHolder != null) {
//            if(bossEntity.isEditing()) {
//                Debug.ATTEMPTED_TO_SPAWN_WHILE_DISABLED.debug();
//                return false;
//            }

            Queue<IMechanic> queue = new LinkedList<>(this.primaryMechanics);

            while(!queue.isEmpty()) {
                IMechanic mechanic = queue.poll();

                if(mechanic == null) continue;

                ServerUtils.get().logDebug("Applying " + mechanic.getClass().getSimpleName());

                if(didMechanicApplicationFail(mechanic, minionEntity, activeBossHolder)) return false;
            }

            queue = new LinkedList<>(this.optionalMechanics);

            while(!queue.isEmpty()) {
                IMechanic mechanic = queue.poll();

                if(mechanic == null) continue;
                if(didMechanicApplicationFail(mechanic, minionEntity, activeBossHolder)) continue;
            }
        }

        return true;
    }

    private boolean didMechanicApplicationFail(IMechanic mechanic, MinionEntity minionEntity, ActiveBossHolder activeBossHolder) {
        if(mechanic == null) return true;

        if(!mechanic.applyMechanic(minionEntity, activeBossHolder)) {
            Debug.MECHANIC_APPLICATION_FAILED.debug(mechanic.getClass().getSimpleName());
            return true;
        }

        return false;
    }
}
