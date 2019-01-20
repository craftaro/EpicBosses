package com.songoda.epicbosses.skills.custom;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.managers.BossSkillManager;
import com.songoda.epicbosses.skills.CustomSkillHandler;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.elements.CustomMinionSkillElement;
import com.songoda.epicbosses.skills.interfaces.ICustomSettingAction;
import com.songoda.epicbosses.skills.interfaces.IOtherSkillDataElement;
import com.songoda.epicbosses.skills.types.CustomSkillElement;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import org.bukkit.Material;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

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
    public IOtherSkillDataElement getOtherSkillData() {
        return new CustomMinionSkillElement(1, "");
    }

    @Override
    public List<ICustomSettingAction> getOtherSkillDataActions(Skill skill, CustomSkillElement customSkillElement) {
        List<ICustomSettingAction> clickActions = new ArrayList<>();

        clickActions.add(BossSkillManager.createCustomSkillAction("Amount Editor", getAmountCurrent(customSkillElement), new ItemStack(Material.REDSTONE), getAmountAction(skill, customSkillElement)));
        clickActions.add(BossSkillManager.createCustomSkillAction("Minion to Spawn Editor", getMinionToSpawnCurrent(customSkillElement), new ItemStack(Material.MONSTER_EGG), getMinionToSpawnAction(skill, customSkillElement)));

        return clickActions;
    }

    @Override
    public void castSkill(Skill skill, CustomSkillElement customSkillElement, ActiveBossHolder activeBossHolder, List<LivingEntity> nearbyEntities) {
        BossAPI.spawnNewMinion(activeBossHolder, skill);
    }

    private String getMinionToSpawnCurrent(CustomSkillElement customSkillElement) {
        CustomMinionSkillElement customMinionSkillElement = customSkillElement.getCustom().getCustomMinionSkillData();

        return customMinionSkillElement.getMinionToSpawn();
    }

    private ClickAction getMinionToSpawnAction(Skill skill, CustomSkillElement customSkillElement) {
        return event -> this.plugin.getBossPanelManager().getMinionSelectEditorMenu().openFor((Player) event.getWhoClicked(), skill, customSkillElement);
    }

    private String getAmountCurrent(CustomSkillElement customSkillElement) {
        CustomMinionSkillElement customMinionSkillElement = customSkillElement.getCustom().getCustomMinionSkillData();

        return ""+customMinionSkillElement.getAmount();
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
