package com.songoda.epicbosses.panel.skills.custom.custom;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.types.CustomSkillElement;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.ISubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.base.IVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.base.handlers.SubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 16-Dec-18
 */
public abstract class MaterialTypeEditorPanel extends SubVariablePanelHandler<Skill, CustomSkillElement> {

    private CustomBosses plugin;

    public MaterialTypeEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.plugin = plugin;
    }

    public abstract void saveSetting(Skill skill, CustomSkillElement customSkillElement, String newValue);

    public abstract String getCurrentSetting(CustomSkillElement customSkillElement);

    public abstract ISubVariablePanelHandler<Skill, CustomSkillElement> getParentHolder();

    @Override
    public void fillPanel(Panel panel, Skill skill, CustomSkillElement customSkillElement) {
        List<Material> materials = Arrays.asList(Material.values());
        List<Material> filteredList = getFilteredList(materials);
        int maxPage = panel.getMaxPage(filteredList);

        panel.setOnPageChange((player, currentPage, requestedPage) -> {
            if(requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(panel, requestedPage, filteredList, skill, customSkillElement);
            return true;
        });

        loadPage(panel, 0, filteredList, skill, customSkillElement);
    }

    @Override
    public void openFor(Player player, Skill skill, CustomSkillElement customSkillElement) {
        Panel panel = getPanelBuilder().getPanel()
                .setParentPanelHandler(getParentHolder(), skill, customSkillElement);

        fillPanel(panel, skill, customSkillElement);
        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private void loadPage(Panel panel, int page, List<Material> filteredList, Skill skill, CustomSkillElement customSkillElement) {
        String current = getCurrentSetting(customSkillElement);

        panel.loadPage(page, (slot, realisticSlot) -> {
            if(slot >= filteredList.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e->{});
            } else {
                Material material = filteredList.get(slot);
                ItemStack itemStack;

                if(material == Material.AIR) itemStack = new ItemStack(Material.GLASS);
                else itemStack = new ItemStack(material);

                Map<String, String> replaceMap = new HashMap<>();

                if(itemStack.getType() == Material.AIR) return;

                String name = material.name();

                replaceMap.put("{type}", StringUtils.get().formatString(name));

                if(current.equalsIgnoreCase(name)) {
                    ItemStackUtils.applyDisplayName(itemStack, this.plugin.getConfig().getString("Display.Skills.Material.selectedName"), replaceMap);
                } else {
                    ItemStackUtils.applyDisplayName(itemStack, this.plugin.getConfig().getString("Display.Skills.Material.name"), replaceMap);
                }

                panel.setItem(realisticSlot, itemStack, event -> {
                    saveSetting(skill, customSkillElement, name);
                    loadPage(panel, page, filteredList, skill, customSkillElement);
                });
            }
        });
    }

    private List<Material> getFilteredList(List<Material> masterList) {
        List<Material> materials = new ArrayList<>();

        masterList.forEach(material -> {
            if((material.isBlock() && material.isSolid() && material.isItem()) || (material == Material.AIR)) {
                materials.add(material);
            }
        });

        return materials;
    }
}
