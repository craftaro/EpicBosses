package net.aminecraftdev.custombosses.managers;

import net.aminecraftdev.custombosses.CustomBosses;
import net.aminecraftdev.custombosses.entity.BossEntity;
import net.aminecraftdev.custombosses.holder.ActiveBossHolder;
import net.aminecraftdev.custombosses.mechanics.*;
import net.aminecraftdev.custombosses.utils.Debug;
import net.aminecraftdev.custombosses.utils.ILoadable;
import net.aminecraftdev.custombosses.utils.IMechanic;

import java.util.LinkedList;
import java.util.Queue;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Jun-18
 */
public class BossMechanicManager implements ILoadable {

    private final CustomBosses customBosses;
    private Queue<IMechanic> mechanicQueue;

    public BossMechanicManager(CustomBosses customBosses) {
        this.customBosses = customBosses;
    }

    @Override
    public void load() {
        this.mechanicQueue = new LinkedList<>();

        this.mechanicQueue.add(new EntityTypeMechanic());
        this.mechanicQueue.add(new NameMechanic());
        this.mechanicQueue.add(new HealthMechanic());
        this.mechanicQueue.add(new EquipmentMechanic(this.customBosses.getItemStackManager()));
        this.mechanicQueue.add(new WeaponMechanic(this.customBosses.getItemStackManager()));
        this.mechanicQueue.add(new PotionMechanic());
        this.mechanicQueue.add(new SettingsMechanic());
    }

    public boolean handleMechanicApplication(BossEntity bossEntity, ActiveBossHolder activeBossHolder) {
        if(this.mechanicQueue != null && bossEntity != null && activeBossHolder != null) {
            Queue<IMechanic> queue = new LinkedList<>(this.mechanicQueue);

            while(!queue.isEmpty()) {
                IMechanic mechanic = queue.poll();

                if(mechanic == null) continue;

                if(!mechanic.applyMechanic(bossEntity, activeBossHolder)) {
                    Debug.MECHANIC_APPLICATION_FAILED.debug(mechanic.getClass().getSimpleName());
                    return false;
                }
            }
        }

        return true;
    }
}
