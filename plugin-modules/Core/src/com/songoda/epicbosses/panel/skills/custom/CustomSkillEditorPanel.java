package com.songoda.epicbosses.panel.skills.custom;

import com.google.gson.JsonObject;
import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.BossSkillManager;
import com.songoda.epicbosses.managers.files.SkillsFileManager;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.types.CustomSkillElement;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import com.songoda.epicbosses.utils.panel.base.handlers.VariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 12-Dec-18
 */
public class CustomSkillEditorPanel extends VariablePanelHandler<Skill> {

    private SkillsFileManager skillsFileManager;
    private BossSkillManager bossSkillManager;
    private CustomBosses plugin;

    public CustomSkillEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.plugin = plugin;
        this.bossSkillManager = plugin.getBossSkillManager();
        this.skillsFileManager = plugin.getSkillsFileManager();
    }

    @Override
    public void openFor(Player player, Skill skill) {
        ServerUtils.get().runTaskAsync(() -> {
            Map<String, String> replaceMap = new HashMap<>();
            PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();
            CustomSkillElement customSkillElement = this.bossSkillManager.getCustomSkillElement(skill);
            Double multiplier = customSkillElement.getCustom().getMultiplier();
            String multiplierValue = multiplier == null? "N/A" : NumberUtils.get().formatDouble(multiplier);

            replaceMap.put("{name}", BossAPI.getSkillName(skill));
            replaceMap.put("{type}", customSkillElement.getCustom().getType());
            replaceMap.put("{multiplier}", multiplierValue);
            panelBuilder.addReplaceData(replaceMap);

            PanelBuilderCounter counter = panelBuilder.getPanelBuilderCounter();
            Panel panel = panelBuilder.getPanel()
                    .setParentPanelHandler(this.bossPanelManager.getMainSkillEditMenu(), skill);

            counter.getSlotsWith("Type").forEach(slot -> panel.setOnClick(slot, event -> this.bossPanelManager.getCustomSkillTypeEditorMenu().openFor((Player) event.getWhoClicked(), skill, customSkillElement)));
            counter.getSlotsWith("SpecialSettings").forEach(slot -> panel.setOnClick(slot, event -> this.bossPanelManager.getSpecialSettingsEditorMenu().openFor((Player) event.getWhoClicked(), skill, customSkillElement)));
            counter.getSlotsWith("Multiplier").forEach(slot -> panel.setOnClick(slot, getMultiplierAction(skill, customSkillElement)));

            panel.openFor(player);
        });
    }

    @Override
    public void fillPanel(Panel panel, Skill skill) {

    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private ClickAction getMultiplierAction(Skill skill, CustomSkillElement customSkillElement) {
        return event -> {
            ClickType clickType = event.getClick();
            Double amountToModifyBy;

            if(clickType == ClickType.SHIFT_LEFT) {
                amountToModifyBy = 0.1;
            } else if(clickType == ClickType.RIGHT) {
                amountToModifyBy = -1.0;
            } else if(clickType == ClickType.SHIFT_RIGHT) {
                amountToModifyBy = -0.1;
            } else if(clickType == ClickType.MIDDLE) {
                amountToModifyBy = null;
            } else {
                amountToModifyBy = 1.0;
            }

            Double currentAmount = customSkillElement.getCustom().getMultiplier();
            String modifyValue;
            Double newAmount;

            if(currentAmount == null) currentAmount = 0.0;

            if(amountToModifyBy == null) {
                modifyValue = "removed";
                newAmount = null;
            } else if(amountToModifyBy > 0.0) {
                modifyValue = "increased";
                newAmount = currentAmount + amountToModifyBy;
            } else {
                modifyValue = "decreased";
                newAmount = currentAmount + amountToModifyBy;
            }

            if(newAmount != null) {
                if(newAmount <= 0.0) {
                    newAmount = null;
                    modifyValue = "removed";
                }
            }

            customSkillElement.getCustom().setMultiplier(newAmount);

            JsonObject jsonObject = BossAPI.convertObjectToJsonObject(customSkillElement);

            skill.setCustomData(jsonObject);
            this.skillsFileManager.save();
            Message.Boss_Skills_SetMultiplier.msg(event.getWhoClicked(), modifyValue, NumberUtils.get().formatDouble((newAmount == null? 0.0 : newAmount)));

            openFor((Player) event.getWhoClicked(), skill);
        };
    }
}
