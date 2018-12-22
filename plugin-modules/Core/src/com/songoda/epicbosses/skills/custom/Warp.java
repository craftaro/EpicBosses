package com.songoda.epicbosses.skills.custom;

import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.skills.CustomSkillHandler;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.interfaces.ICustomSkillAction;
import com.songoda.epicbosses.skills.types.CustomSkillElement;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import org.bukkit.entity.LivingEntity;

import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 11-Nov-18
 */
public class Warp extends CustomSkillHandler {

    @Override
    public boolean doesUseMultiplier() {
        return false;
    }

    @Override
    public Map<String, Object> getOtherSkillData() {
        return null;
    }

    @Override
    public List<ICustomSkillAction> getOtherSkillDataActions(Skill skill, CustomSkillElement customSkillElement) {
        return null;
    }

    @Override
    public void castSkill(Skill skill, CustomSkillElement customSkillElement, ActiveBossHolder activeBossHolder, List<LivingEntity> nearbyEntities) {
        LivingEntity target = nearbyEntities.get(0);

        activeBossHolder.getLivingEntity().teleport(target);
    }
}
