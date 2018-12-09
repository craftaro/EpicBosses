package com.songoda.epicbosses.panel.skills;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.SkillsFileManager;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import com.songoda.epicbosses.utils.panel.base.handlers.VariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Dec-18
 */
public class SkillTypeEditorPanel extends VariablePanelHandler<Skill> {

    private SkillsFileManager skillsFileManager;

    public SkillTypeEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.skillsFileManager = plugin.getSkillsFileManager();
    }

    @Override
    public void fillPanel(Panel panel, Skill skill) {

    }

    @Override
    public void openFor(Player player, Skill skill) {
        Map<String, String> replaceMap = new HashMap<>();
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();

        replaceMap.put("{name}", BossAPI.getSkillName(skill));
        panelBuilder.addReplaceData(replaceMap);

        Panel panel = panelBuilder.getPanel()
                .setDestroyWhenDone(true)
                .setCancelClick(true)
                .setCancelLowerClick(true)
                .setParentPanelHandler(this.bossPanelManager.getMainSkillEditMenu(), skill);
        PanelBuilderCounter counter = panel.getPanelBuilderCounter();

        counter.getSlotsWith("Command").forEach(slot -> panel.setOnClick(slot, getCommandAction(skill)));
        counter.getSlotsWith("Custom").forEach(slot -> panel.setOnClick(slot, getCustomAction(skill)));
        counter.getSlotsWith("Potion").forEach(slot -> panel.setOnClick(slot, getPotionAction(skill)));
        counter.getSlotsWith("Group").forEach(slot -> panel.setOnClick(slot, getGroupAction(skill)));

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {
//        PanelBuilderCounter panelBuilderCounter = panelBuilder.getPanelBuilderCounter();
//
//        panelBuilderCounter
//                .addSlotCounter("Command")
//                .addSlotCounter("Custom")
//                .addSlotCounter("Potion")
//                .addSlotCounter("Group");
    }

    private ClickAction getCommandAction(Skill skill) {
        return event -> {
            skill.setType("COMMAND");
            skill.setCustomData(BossAPI.createNewSkillCustomData(skill.getType()));
            save(skill, event);
        };
    }

    private ClickAction getCustomAction(Skill skill) {
        return event -> {
            skill.setType("CUSTOM");
            skill.setCustomData(BossAPI.createNewSkillCustomData(skill.getType()));
            save(skill, event);
        };
    }

    private ClickAction getGroupAction(Skill skill) {
        return event -> {
            skill.setType("GROUP");
            skill.setCustomData(BossAPI.createNewSkillCustomData(skill.getType()));
            save(skill, event);
        };
    }

    private ClickAction getPotionAction(Skill skill) {
        return event -> {
            skill.setType("POTION");
            skill.setCustomData(BossAPI.createNewSkillCustomData(skill.getType()));
            save(skill, event);
        };
    }

    private void save(Skill skill, InventoryClickEvent event) {
        this.skillsFileManager.save();
        this.bossPanelManager.getMainSkillEditMenu().openFor((Player) event.getWhoClicked(), skill);
    }
}
