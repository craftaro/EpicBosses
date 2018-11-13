package com.songoda.epicbosses.skills.custom;

import com.google.gson.annotations.Expose;
import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.skills.ISkillHandler;
import com.songoda.epicbosses.skills.elements.CustomMinionSkillElement;
import com.songoda.epicbosses.skills.types.CustomSkill;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 11-Nov-18
 */
public class Minions extends CustomSkill implements ISkillHandler {

    @Expose @Getter @Setter private CustomMinionSkillElement minions;

    public Minions(String mode, String type, Double radius, String displayName, String customMessage) {
        super(mode, type, radius, displayName, customMessage);
    }

    @Override
    public void castSkill(ActiveBossHolder activeBossHolder, List<LivingEntity> nearbyEntities) {
        BossAPI.spawnNewMinion(activeBossHolder, this);
    }
}
