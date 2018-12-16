package com.songoda.epicbosses.skills.custom;

import com.google.gson.JsonObject;
import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.skills.CustomSkillHandler;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.elements.CustomMinionSkillElement;
import com.songoda.epicbosses.skills.types.CustomSkillElement;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 11-Nov-18
 */
public class Minions extends CustomSkillHandler {

    private CustomBosses plugin;

    public Minions(CustomBosses plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean doesUseMultiplier() {
        return false;
    }

    @Override
    public Map<String, Class<?>> getOtherSkillData() {
        Map<String, Class<?>> map = new HashMap<>();

        map.put("amount", Integer.class);
        map.put("minionToSpawn", String.class);

        return map;
    }

    @Override
    public Map<Integer, ClickAction> getOtherSkillDataActions(Skill skill, CustomSkillElement customSkillElement) {
        Map<Integer, ClickAction> clickActionMap = new HashMap<>();

        clickActionMap.put(1, getAmountAction(skill, customSkillElement));


        return clickActionMap;
    }

    @Override
    public void castSkill(Skill skill, CustomSkillElement customSkillElement, ActiveBossHolder activeBossHolder, List<LivingEntity> nearbyEntities) {
        BossAPI.spawnNewMinion(activeBossHolder, skill);
    }

    private ClickAction getAmountAction(Skill skill, CustomSkillElement customSkillElement) {
        return event -> {
            CustomMinionSkillElement customMinionSkillElement = customSkillElement.getCustom().getCustomMinionSkillData();
            ClickType clickType = event.getClick();
            Integer amountToModifyBy;

            if(clickType.name().contains("RIGHT")) {
                amountToModifyBy = -1;
            } else {
                amountToModifyBy = 1;
            }

            Integer currentAmount = customMinionSkillElement.getAmount();
            String modifyValue;
            Integer newAmount;

            if(currentAmount == null) currentAmount = 0;

            if(amountToModifyBy > 0.0) {
                modifyValue = "increased";
                newAmount = currentAmount + amountToModifyBy;
            } else {
                modifyValue = "decreased";
                newAmount = currentAmount + amountToModifyBy;
            }

            if(newAmount <= 0) {
                newAmount = 0;
            }

            customMinionSkillElement.setAmount(newAmount);
            customSkillElement.getCustom().setOtherSkillData(BossAPI.convertObjectToJsonObject(customMinionSkillElement));
            skill.setCustomData(BossAPI.convertObjectToJsonObject(customSkillElement));

            this.plugin.getSkillsFileManager().save();
            Message.Boss_Skills_SetMinionAmount.msg(event.getWhoClicked(), modifyValue, NumberUtils.get().formatDouble(newAmount));
        };
    }
}
