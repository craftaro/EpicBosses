package com.songoda.epicbosses.panel.skills;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.handlers.SkillDisplayNameHandler;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.SkillsFileManager;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.SkillMode;
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
 * @since 02-Dec-18
 */
public class MainSkillEditorPanel extends VariablePanelHandler<Skill> {

    private SkillsFileManager skillsFileManager;

    public MainSkillEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, EpicBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.skillsFileManager = plugin.getSkillsFileManager();
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

        ServerUtils.get().runTaskAsync(() -> {
            counter.getSlotsWith("Radius").forEach(slot -> panel.setOnClick(slot, getRadiusAction(skill)));
            counter.getSlotsWith("CustomData").forEach(slot -> panel.setOnClick(slot, getCustomDataAction(skill)));
            counter.getSlotsWith("Mode").forEach(slot -> panel.setOnClick(slot, getModeAction(skill)));
            counter.getSlotsWith("DisplayName").forEach(slot -> panel.setOnClick(slot, getDisplayNameAction(skill)));
            counter.getSlotsWith("CustomMessage").forEach(slot -> panel.setOnClick(slot, getCustomMessageAction(skill)));
            counter.getSlotsWith("Type").forEach(slot -> panel.setOnClick(slot, event -> this.bossPanelManager.getSkillTypeEditMenu().openFor((Player) event.getWhoClicked(), skill)));
        });

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private ClickAction getCustomDataAction(Skill skill) {
        return event -> {
            String type = skill.getType();
            Player player = (Player) event.getWhoClicked();

            if(type.equalsIgnoreCase("POTION")) {
                this.bossPanelManager.getPotionSkillEditorPanel().openFor(player, skill);
            } else if(type.equalsIgnoreCase("GROUP")) {
                this.bossPanelManager.getGroupSkillEditorPanel().openFor(player, skill);
            } else if(type.equalsIgnoreCase("CUSTOM")) {
                this.bossPanelManager.getCustomSkillEditorPanel().openFor(player, skill);
            } else if(type.equalsIgnoreCase("COMMAND")) {
                this.bossPanelManager.getCommandSkillEditorPanel().openFor(player, skill);
            }
        };
    }

    private ClickAction getDisplayNameAction(Skill skill) {
        return event -> {
            Player player = (Player) event.getWhoClicked();
            SkillDisplayNameHandler skillDisplayNameHandler = new SkillDisplayNameHandler(player, skill, this.skillsFileManager, this);

            Message.Boss_Skills_SetDisplayName.msg(player);
            skillDisplayNameHandler.handle();
            player.closeInventory();
        };
    }

    private ClickAction getCustomMessageAction(Skill skill) {
        return event -> {
            ClickType clickType = event.getClick();
            Player player = (Player) event.getWhoClicked();

            if(clickType.name().contains("RIGHT")) {
                skill.setCustomMessage("");
                saveSkill(skill, player);
            } else {
                this.bossPanelManager.getCustomMessageEditMenu().openFor(player, skill);
            }
        };
    }

    private ClickAction getModeAction(Skill skill) {
        return event -> {
            String current = skill.getMode();
            SkillMode skillMode = SkillMode.getCurrent(current);
            SkillMode nextMode = skillMode.getNext();

            skill.setMode(nextMode.name());
            saveSkill(skill, (Player) event.getWhoClicked());
            Message.Boss_Skills_SetMode.msg(event.getWhoClicked(), nextMode.name());
        };
    }

    private ClickAction getRadiusAction(Skill skill) {
        return event -> {
            ClickType clickType = event.getClick();
            int radiusToModifyBy = 0;

            if(clickType == ClickType.LEFT) {
                radiusToModifyBy = 1;
            } else if(clickType == ClickType.SHIFT_LEFT) {
                radiusToModifyBy = 10;
            } else if(clickType == ClickType.RIGHT) {
                radiusToModifyBy = -1;
            } else if(clickType == ClickType.SHIFT_RIGHT) {
                radiusToModifyBy = -10;
            }

            String modifyValue = radiusToModifyBy > 0? "increased" : "decreased";
            Double currentRadius = skill.getRadius();

            if(currentRadius == null) currentRadius = 0.0;

            double newRadius = currentRadius + radiusToModifyBy;

            if(newRadius < 0) {
                newRadius = 0;
            }

            skill.setRadius(newRadius);
            saveSkill(skill, (Player) event.getWhoClicked());
            Message.Boss_Skills_SetRadius.msg(event.getWhoClicked(), modifyValue, NumberUtils.get().formatDouble(newRadius));
        };
    }

    private void saveSkill(Skill skill, Player player) {
        this.skillsFileManager.save();
        openFor(player, skill);
    }

}
