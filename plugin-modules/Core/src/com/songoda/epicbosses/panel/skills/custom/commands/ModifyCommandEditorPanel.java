package com.songoda.epicbosses.panel.skills.custom.commands;

import com.google.gson.JsonObject;
import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.BossSkillManager;
import com.songoda.epicbosses.managers.files.SkillsFileManager;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.elements.SubCommandSkillElement;
import com.songoda.epicbosses.skills.types.CommandSkillElement;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import com.songoda.epicbosses.utils.panel.base.handlers.SubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 05-Dec-18
 */
public class ModifyCommandEditorPanel extends SubVariablePanelHandler<Skill, SubCommandSkillElement> {

    private SkillsFileManager skillsFileManager;
    private BossSkillManager bossSkillManager;

    public ModifyCommandEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.bossSkillManager = plugin.getBossSkillManager();
        this.skillsFileManager = plugin.getSkillsFileManager();
    }

    @Override
    public void fillPanel(Panel panel, Skill skill, SubCommandSkillElement subCommandSkillElement) {

    }

    @Override
    public void openFor(Player player, Skill skill, SubCommandSkillElement subCommandSkillElement) {
        ServerUtils.get().runTaskAsync(() -> {
            Map<String, String> replaceMap = new HashMap<>();
            List<String> commands = subCommandSkillElement.getCommands();
            Double chance = subCommandSkillElement.getChance();
            PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();

            if(commands == null) commands = new ArrayList<>();
            if(chance == null) chance = 100.0;

            replaceMap.put("{commands}", StringUtils.get().appendList(commands));
            replaceMap.put("{chance}", NumberUtils.get().formatDouble(chance));
            panelBuilder.addReplaceData(replaceMap);

            PanelBuilderCounter counter = panelBuilder.getPanelBuilderCounter();
            Panel panel = panelBuilder.getPanel()
                    .setParentPanelHandler(this.bossPanelManager.getCommandSkillEditorPanel(), skill);

            counter.getSlotsWith("Chance").forEach(slot -> panel.setOnClick(slot, getChanceAction(skill, subCommandSkillElement)));
            counter.getSlotsWith("Commands").forEach(slot -> panel.setOnClick(slot, event -> this.bossPanelManager.getCommandListSkillEditMenu().openFor((Player) event.getWhoClicked(), skill, subCommandSkillElement)));

            panel.openFor(player);
        });
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private ClickAction getChanceAction(Skill skill, SubCommandSkillElement subCommandSkillElement) {
        return event -> {
            ClickType clickType = event.getClick();
            double amountToModify;

            if(clickType == ClickType.SHIFT_LEFT) {
                amountToModify = 0.1;
            } else if(clickType == ClickType.RIGHT) {
                amountToModify = -1.0;
            } else if(clickType == ClickType.SHIFT_RIGHT) {
                amountToModify = -0.1;
            } else {
                amountToModify = 1.0;
            }

            String modifyValue = amountToModify > 0.0? "increased" : "decreased";
            Double currentValue = subCommandSkillElement.getChance();

            if(currentValue == null) currentValue = 100.0;

            double newValue = currentValue + amountToModify;

            if(newValue < 0.0) {
                newValue = 0.0;
            }

            if(newValue > 100.0) {
                newValue = 100.0;
            }

            subCommandSkillElement.setChance(newValue);

            CommandSkillElement commandSkillElement = this.bossSkillManager.getCommandSkillElement(skill);
            List<SubCommandSkillElement> subCommandSkillElements = commandSkillElement.getCommands();
            List<SubCommandSkillElement> newElements = new ArrayList<>();

            for(SubCommandSkillElement subElement : subCommandSkillElements) {
                if(subElement.getName().equals(subCommandSkillElement.getName())) {
                    newElements.add(subCommandSkillElement);
                } else {
                    newElements.add(subElement);
                }
            }

            commandSkillElement.setCommands(newElements);
            saveSkill(skill, commandSkillElement);

            openFor((Player) event.getWhoClicked(), skill, subCommandSkillElement);
            Message.Boss_Skills_SetCommandChance.msg(event.getWhoClicked(), modifyValue, NumberUtils.get().formatDouble(newValue));
        };
    }

    private void saveSkill(Skill skill, CommandSkillElement commandSkillElement) {
        JsonObject jsonObject = BossAPI.convertObjectToJsonObject(commandSkillElement);

        skill.setCustomData(jsonObject);
        this.skillsFileManager.save();
    }
}
