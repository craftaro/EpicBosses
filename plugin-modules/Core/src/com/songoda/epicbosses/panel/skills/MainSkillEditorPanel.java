package com.songoda.epicbosses.panel.skills;

import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.handlers.VariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Dec-18
 */
public class MainSkillEditorPanel extends VariablePanelHandler<Skill> {

    public MainSkillEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder) {
        super(bossPanelManager, panelBuilder);
    }

    @Override
    public void fillPanel(Panel panel, Skill skill) {

    }

    @Override
    public void openFor(Player player, Skill skill) {
        Map<String, String> replaceMap = new HashMap<>();
        String customMessage = skill.getCustomMessage();
        Double radius = skill.getRadius();
        String mode = skill.getMode();
        String displayName = skill.getDisplayName();
        String type = skill.getType();
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();

        if(customMessage == null || customMessage.equals("")) customMessage = "N/A";
        if(radius == null) radius = 100.0;
        if(mode == null || mode.equals("")) mode = "N/A";
        if(displayName == null || displayName.equals("")) displayName = "N/A";
        if(type == null || type.equals("")) type = "N/A";

        replaceMap.put("{name}", BossAPI.getSkillName(skill));
        replaceMap.put("{customMessage}", customMessage);
        replaceMap.put("{radius}", NumberUtils.get().formatDouble(radius));
        replaceMap.put("{mode}", mode);
        replaceMap.put("{displayName}", displayName);
        replaceMap.put("{type}", type);
        panelBuilder.addReplaceData(replaceMap);

        Panel panel = panelBuilder.getPanel()
                .setDestroyWhenDone(true)
                .setCancelClick(true)
                .setCancelLowerClick(true);
        PanelBuilderCounter counter = panel.getPanelBuilderCounter();

        counter.getSlotsWith("Radius").forEach(slot -> {});
        counter.getSlotsWith("CustomData").forEach(slot -> {});
        counter.getSlotsWith("Mode").forEach(slot -> {});
        counter.getSlotsWith("DisplayName").forEach(slot -> {});
        counter.getSlotsWith("CustomMessage").forEach(slot -> {});
        counter.getSlotsWith("Type").forEach(slot -> {});

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {
        PanelBuilderCounter panelBuilderCounter = panelBuilder.getPanelBuilderCounter();

        panelBuilderCounter
                .addSlotCounter("CustomMessage")
                .addSlotCounter("Radius")
                .addSlotCounter("CustomData")
                .addSlotCounter("Mode")
                .addSlotCounter("DisplayName")
                .addSlotCounter("Type");
    }
}
