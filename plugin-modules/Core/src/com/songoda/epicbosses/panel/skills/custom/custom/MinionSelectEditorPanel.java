package com.songoda.epicbosses.panel.skills.custom.custom;

import com.google.gson.JsonObject;
import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.MinionEntity;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.ItemsFileManager;
import com.songoda.epicbosses.managers.files.MinionsFileManager;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.elements.CustomMinionSkillElement;
import com.songoda.epicbosses.skills.types.CustomSkillElement;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.itemstack.ItemStackConverter;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.handlers.SubVariablePanelHandler;
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
 * @since 22-Dec-18
 */
public class MinionSelectEditorPanel extends SubVariablePanelHandler<Skill, CustomSkillElement> {

    private MinionsFileManager minionsFileManager;
    private ItemStackConverter itemStackConverter;
    private ItemsFileManager itemsFileManager;
    private CustomBosses plugin;

    public MinionSelectEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.plugin = plugin;
        this.itemStackConverter = new ItemStackConverter();
        this.itemsFileManager = plugin.getItemStackManager();
        this.minionsFileManager = plugin.getMinionsFileManager();
    }

    @Override
    public void fillPanel(Panel panel, Skill skill, CustomSkillElement customSkillElement) {
        Map<String, MinionEntity> currentEntities = this.minionsFileManager.getMinionEntities();
        List<String> entryList = new ArrayList<>(currentEntities.keySet());
        int maxPage = panel.getMaxPage(entryList);

        panel.setOnPageChange(((player, currentPage, requestedPage) -> {
            if(requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(panel, requestedPage, currentEntities, entryList, skill, customSkillElement);
            return true;
        }));

        loadPage(panel, 0, currentEntities, entryList, skill, customSkillElement);
    }

    @Override
    public void openFor(Player player, Skill skill, CustomSkillElement customSkillElement) {
        Panel panel = getPanelBuilder().getPanel()
                .setParentPanelHandler(this.bossPanelManager.getSpecialSettingsEditorMenu(), skill, customSkillElement);

        ServerUtils.get().runTaskAsync(() -> fillPanel(panel, skill, customSkillElement));
        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private void loadPage(Panel panel, int page, Map<String, MinionEntity> currentEntities, List<String> entryList, Skill skill, CustomSkillElement customSkillElement) {
        CustomMinionSkillElement customMinionSkillElement = customSkillElement.getCustom().getCustomMinionSkillData();
        String current = customMinionSkillElement.getMinionToSpawn();

        panel.loadPage(page, ((slot, realisticSlot) -> {
            if(slot >= entryList.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e->{});
            } else {
                String name = entryList.get(slot);
                MinionEntity minionEntity = currentEntities.get(name);
                ItemStackHolder itemStackHolder = this.itemsFileManager.getItemStackHolder("DefaultMinionMenuSpawnItem");
                ItemStack itemStack = this.itemStackConverter.from(itemStackHolder);
                Map<String, String> replaceMap = new HashMap<>();

                replaceMap.put("{name}", name);
                replaceMap.put("{editing}", ""+minionEntity.isEditing());
                replaceMap.put("{targeting}", minionEntity.getTargeting());

                if(current.equalsIgnoreCase(name)) {
                    ItemStackUtils.applyDisplayName(itemStack, this.plugin.getConfig().getString("Display.Skills.MinionList.selectedName"), replaceMap);
                } else {
                    ItemStackUtils.applyDisplayName(itemStack, this.plugin.getConfig().getString("Display.Skills.MinionList.name"), replaceMap);
                }

                ItemStackUtils.applyDisplayLore(itemStack, this.plugin.getConfig().getStringList("Display.Skills.MinionList.lore"), replaceMap);

                panel.setItem(realisticSlot, itemStack, event -> {
                    customMinionSkillElement.setMinionToSpawn(name);

                    JsonObject minionElement = BossAPI.convertObjectToJsonObject(customMinionSkillElement);

                    customSkillElement.getCustom().setOtherSkillData(minionElement);

                    JsonObject customElement = BossAPI.convertObjectToJsonObject(customSkillElement);

                    skill.setCustomData(customElement);
                    this.plugin.getSkillsFileManager().save();

                    loadPage(panel, page, currentEntities, entryList, skill, customSkillElement);
                });
            }
        }));
    }
}
