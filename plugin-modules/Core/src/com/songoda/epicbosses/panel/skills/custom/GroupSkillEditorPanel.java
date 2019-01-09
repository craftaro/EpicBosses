package com.songoda.epicbosses.panel.skills.custom;

import com.google.gson.JsonObject;
import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.ItemsFileManager;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.types.GroupSkillElement;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.itemstack.ItemStackConverter;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.handlers.VariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 10-Dec-18
 */
public class GroupSkillEditorPanel extends VariablePanelHandler<Skill> {

    private ItemStackConverter itemStackConverter;
    private ItemsFileManager itemsFileManager;
    private CustomBosses plugin;

    public GroupSkillEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.plugin = plugin;
        this.itemsFileManager = plugin.getItemStackManager();
        this.itemStackConverter = new ItemStackConverter();
    }

    @Override
    public void fillPanel(Panel panel, Skill skill) {
        GroupSkillElement groupSkillElement = this.plugin.getBossSkillManager().getGroupSkillElement(skill);
        Map<String, Skill> skillMap = this.plugin.getSkillsFileManager().getSkillMap();
        List<String> entryList = new ArrayList<>(skillMap.keySet());
        int maxPage = panel.getMaxPage(entryList);

        panel.setOnPageChange(((player, currentPage, requestedPage) -> {
            if(requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(panel, requestedPage, skill, groupSkillElement, skillMap, entryList);
            return true;
        }));

        loadPage(panel, 0, skill, groupSkillElement, skillMap, entryList);
    }

    @Override
    public void openFor(Player player, Skill skill) {
        Map<String, String> replaceMap = new HashMap<>();
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();

        replaceMap.put("{name}", BossAPI.getSkillName(skill));
        panelBuilder.addReplaceData(replaceMap);

        Panel panel = panelBuilder.getPanel()
                .setParentPanelHandler(this.bossPanelManager.getMainSkillEditMenu(), skill);

        ServerUtils.get().runTaskAsync(() -> fillPanel(panel, skill));

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private void loadPage(Panel panel, int page, Skill skill, GroupSkillElement groupSkillElement, Map<String, Skill> skillMap, List<String> entryList) {
        List<String> currentSkills = groupSkillElement.getGroupedSkills();

        panel.loadPage(page, ((slot, realisticSlot) -> {
            if(slot >= skillMap.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e -> {});
            } else {
                String name = entryList.get(slot);
                Skill innerSkill = skillMap.get(name);
                Map<String, String> replaceMap = new HashMap<>();
                String type = innerSkill.getType();
                String displayName = innerSkill.getDisplayName();
                String customMessage = innerSkill.getCustomMessage();
                Double radius = innerSkill.getRadius();
                String mode = innerSkill.getMode();
                ItemStack itemStack = this.itemStackConverter.from(this.itemsFileManager.getItemStackHolder("DefaultSkillMenuItem"));
                boolean isCurrent = currentSkills.contains(name);

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

                if(isCurrent) {
                    ItemStackUtils.applyDisplayName(itemStack, this.plugin.getConfig().getString("Display.Skills.Group.selectedName"), replaceMap);
                } else {
                    ItemStackUtils.applyDisplayName(itemStack, this.plugin.getConfig().getString("Display.Skills.Group.name"), replaceMap);
                }

                ItemStackUtils.applyDisplayLore(itemStack, this.plugin.getConfig().getStringList("Display.Skills.Group.lore"), replaceMap);

                panel.setItem(realisticSlot, itemStack, event -> {
                    if(isCurrent) {
                        currentSkills.remove(name);
                    } else {
                        currentSkills.add(name);
                    }

                    groupSkillElement.setGroupedSkills(currentSkills);

                    JsonObject jsonObject = BossAPI.convertObjectToJsonObject(groupSkillElement);

                    skill.setCustomData(jsonObject);
                    this.plugin.getSkillsFileManager().save();

                    loadPage(panel, page, skill, groupSkillElement, skillMap, entryList);
                });
            }
        }));
    }
}
