package com.songoda.epicbosses.panel;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.SkillsFileManager;
import com.songoda.epicbosses.panel.handlers.MainListPanelHandler;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.itemstack.ItemStackConverter;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 10-Oct-18
 */
public class CustomSkillsPanel extends MainListPanelHandler {

    private ItemStackConverter itemStackConverter;
    private SkillsFileManager skillsFileManager;
    private CustomBosses plugin;

    public CustomSkillsPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.plugin = plugin;
        this.skillsFileManager = plugin.getSkillsFileManager();
        this.itemStackConverter = new ItemStackConverter();
    }

    @Override
    public void fillPanel(Panel panel) {
        Map<String, Skill> currentSkills = this.skillsFileManager.getSkillMap();
        List<String> entryList = new ArrayList<>(currentSkills.keySet());
        int maxPage = panel.getMaxPage(entryList);

        panel.setOnPageChange(((player, currentPage, requestedPage) -> {
            if(requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(panel, requestedPage, currentSkills, entryList);
            return true;
        }));

        loadPage(panel, 0, currentSkills, entryList);
    }

    private void loadPage(Panel panel, int requestedPage, Map<String, Skill> currentSkills, List<String> entryList) {
        panel.loadPage(requestedPage, (slot, realisticSlot) -> {
            if(slot >= currentSkills.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e -> {});
            } else {
                String name = entryList.get(slot);
                Skill skill = currentSkills.get(name);
                ItemStackHolder itemStackHolder = BossAPI.getStoredItemStack("DefaultSkillMenuItem");
                ItemStack itemStack = this.itemStackConverter.from(itemStackHolder);

                Map<String, String> replaceMap = new HashMap<>();

                replaceMap.put("{name}", name);
                replaceMap.put("{type}", skill.getType());
                replaceMap.put("{displayName}", skill.getDisplayName());
                replaceMap.put("{customMessage}", skill.getCustomMessage());
                replaceMap.put("{radius}", NumberUtils.get().formatDouble(skill.getRadius()));

                ItemStackUtils.applyDisplayName(itemStack, this.plugin.getConfig().getString("Display.Skills.name"), replaceMap);
                ItemStackUtils.applyDisplayLore(itemStack, this.plugin.getConfig().getStringList("Display.Skills.lore"), replaceMap);


                panel.setItem(realisticSlot, itemStack, e -> {
                    //TODO: Add Edit Skill
                });
            }
        });
    }
}
