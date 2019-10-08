package com.songoda.epicbosses.panel;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.SkillsFileManager;
import com.songoda.epicbosses.panel.handlers.MainListPanelHandler;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.itemstack.ItemStackConverter;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import com.songoda.epicbosses.utils.panel.Panel;
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
 * @since 10-Oct-18
 */
public class CustomSkillsPanel extends MainListPanelHandler {

    private ItemStackConverter itemStackConverter;
    private SkillsFileManager skillsFileManager;
    private EpicBosses plugin;

    public CustomSkillsPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, EpicBosses plugin) {
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
            if (requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(panel, requestedPage, currentSkills, entryList);
            return true;
        }));

        loadPage(panel, 0, currentSkills, entryList);
    }

    private void loadPage(Panel panel, int requestedPage, Map<String, Skill> currentSkills, List<String> entryList) {
        panel.loadPage(requestedPage, (slot, realisticSlot) -> {
            if (slot >= currentSkills.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e -> {
                });
            } else {
                String name = entryList.get(slot);
                Skill skill = currentSkills.get(name);
                ItemStackHolder itemStackHolder = BossAPI.getStoredItemStack("DefaultSkillMenuItem");
                ItemStack itemStack = this.itemStackConverter.from(itemStackHolder);

                Map<String, String> replaceMap = new HashMap<>();
                String displayName = skill.getDisplayName();
                String customMessage = skill.getCustomMessage();
                Double radius = skill.getRadius();
                String type = skill.getType();

                if (customMessage == null || customMessage.equals("")) customMessage = "N/A";
                if (radius == null) radius = 100.0;
                if (displayName == null || displayName.equals("")) displayName = "N/A";
                if (type == null || type.equals("")) type = "N/A";

                replaceMap.put("{name}", name);
                replaceMap.put("{type}", type);
                replaceMap.put("{displayName}", displayName);
                replaceMap.put("{customMessage}", customMessage);
                replaceMap.put("{radius}", NumberUtils.get().formatDouble(radius));

                ItemStackUtils.applyDisplayName(itemStack, this.plugin.getDisplay().getString("Display.Skills.Main.name"), replaceMap);
                ItemStackUtils.applyDisplayLore(itemStack, this.plugin.getDisplay().getStringList("Display.Skills.Main.lore"), replaceMap);


                panel.setItem(realisticSlot, itemStack, e -> this.bossPanelManager.getMainSkillEditMenu().openFor((Player) e.getWhoClicked(), skill));
            }
        });
    }
}
