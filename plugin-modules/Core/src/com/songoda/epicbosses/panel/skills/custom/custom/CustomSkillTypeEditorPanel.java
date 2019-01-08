package com.songoda.epicbosses.panel.skills.custom.custom;

import com.google.gson.JsonObject;
import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.BossSkillManager;
import com.songoda.epicbosses.skills.CustomSkillHandler;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.interfaces.IOtherSkillDataElement;
import com.songoda.epicbosses.skills.types.CustomSkillElement;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.itemstack.ItemStackConverter;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.handlers.SubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 13-Dec-18
 */
public class CustomSkillTypeEditorPanel extends SubVariablePanelHandler<Skill, CustomSkillElement> {

    private ItemStackConverter itemStackConverter;
    private BossSkillManager bossSkillManager;
    private CustomBosses plugin;

    public CustomSkillTypeEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.plugin = plugin;
        this.itemStackConverter = new ItemStackConverter();
        this.bossSkillManager = plugin.getBossSkillManager();
    }

    @Override
    public void openFor(Player player, Skill skill, CustomSkillElement customSkillElement) {
        ServerUtils.get().runTaskAsync(() -> {
            Map<String, String> replaceMap = new HashMap<>();
            PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();

            replaceMap.put("{name}", BossAPI.getSkillName(skill));
            replaceMap.put("{selected}", customSkillElement.getCustom().getType());
            panelBuilder.addReplaceData(replaceMap);

            Panel panel = panelBuilder.getPanel()
                    .setParentPanelHandler(this.bossPanelManager.getCustomSkillEditorPanel(), skill);

            fillPanel(panel, skill, customSkillElement);

            panel.openFor(player);
        });
    }

    @Override
    public void fillPanel(Panel panel, Skill skill, CustomSkillElement customSkillElement) {
        List<CustomSkillHandler> customSkillHandlers = this.bossSkillManager.getSkills();
        int maxPage = panel.getMaxPage(customSkillHandlers);

        panel.setOnPageChange(((player, currentPage, requestedPage) -> {
            if(requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(panel, requestedPage, skill, customSkillElement, customSkillHandlers);
            return true;
        }));

        loadPage(panel, 0, skill, customSkillElement, customSkillHandlers);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private void loadPage(Panel panel, int page, Skill skill, CustomSkillElement customSkillElement, List<CustomSkillHandler> customSkillHandlers) {
        String current = customSkillElement.getCustom().getType();

        panel.loadPage(page, ((slot, realisticSlot) -> {
            if(slot >= customSkillHandlers.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e -> {});
            } else {
                CustomSkillHandler customSkillHandler = customSkillHandlers.get(slot);
                String name = customSkillHandler.getSkillName();
                Map<String, String> replaceMap = new HashMap<>();
                String hasCustomData = customSkillHandler.getOtherSkillData() == null? "false" : "true";

                replaceMap.put("{name}", name);
                replaceMap.put("{multiplier}", ""+customSkillHandler.doesUseMultiplier());
                replaceMap.put("{customData}", hasCustomData);

                ItemStack itemStack;

                if(name.equalsIgnoreCase(current)) {
                    itemStack = this.itemStackConverter.from(this.plugin.getItemStackManager().getItemStackHolder("DefaultSelectedCustomSkillTypeItem"));

                    ItemStackUtils.applyDisplayName(itemStack, this.plugin.getConfig().getString("Display.Skills.CustomType.selectedName"), replaceMap);
                } else {
                    itemStack = this.itemStackConverter.from(this.plugin.getItemStackManager().getItemStackHolder("DefaultCustomSkillTypeItem"));

                    ItemStackUtils.applyDisplayName(itemStack, this.plugin.getConfig().getString("Display.Skills.CustomType.name"), replaceMap);
                }

                ItemStackUtils.applyDisplayLore(itemStack, this.plugin.getConfig().getStringList("Display.Skills.CustomType.lore"), replaceMap);

                panel.setItem(realisticSlot, itemStack, event -> {
                    IOtherSkillDataElement otherSkillDataElement = customSkillHandler.getOtherSkillData();
                    JsonObject otherData = otherSkillDataElement == null? null : BossAPI.convertObjectToJsonObject(otherSkillDataElement);

                    customSkillElement.getCustom().setType(name);
                    customSkillElement.getCustom().setOtherSkillData(otherData);

                    JsonObject jsonObject = BossAPI.convertObjectToJsonObject(customSkillElement);

                    skill.setCustomData(jsonObject);
                    this.plugin.getSkillsFileManager().save();

                    openFor((Player) event.getWhoClicked(), skill, customSkillElement);
                });
            }
        }));
    }
}
