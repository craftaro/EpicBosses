package com.songoda.epicbosses.utils.target;

import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.utils.ITargetHandler;
import com.songoda.epicbosses.utils.ServerUtils;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 16-Oct-18
 */
public abstract class TargetHandler implements ITargetHandler {

    protected ActiveBossHolder activeBossHolder;
    protected LivingEntity currentTarget;

    public TargetHandler(ActiveBossHolder activeBossHolder) {
        this.activeBossHolder = activeBossHolder;
    }

    @Override
    public boolean canTarget(LivingEntity livingEntity) {
        //TODO: Implement feature to allow targetting of PetMaster pet's too

        return (livingEntity instanceof HumanEntity);
    }

    @Override
    public void createAutoTarget() {
        ServerUtils.get().runLaterAsync(100L, () -> {
            updateTarget();

            if(this.currentTarget == null || this.currentTarget.isDead()) return;

            createAutoTarget();
        });
    }



}
