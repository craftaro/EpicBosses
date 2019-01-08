package com.songoda.epicbosses.panel.bosses;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.SkillsFileManager;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.itemstack.ItemStackConverter;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
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
 * @since 25-Nov-18
 */
public class SkillListEditorPanel extends VariablePanelHandler<BossEntity> {

    private ItemStackConverter itemStackConverter;
    private SkillsFileManager skillsFileManager;
    private CustomBosses plugin;

    public SkillListEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.itemStackConverter = new ItemStackConverter();
        this.skillsFileManager = plugin.getSkillsFileManager();
        this.plugin = plugin;
    }

    @Override
    public void fillPanel(Panel panel, BossEntity bossEntity) {
        Map<String, Skill> currentSkills = this.skillsFileManager.getSkillMap();
        List<String> entryList = new ArrayList<>(currentSkills.keySet());
        int maxPage = panel.getMaxPage(entryList);

        panel.setOnPageChange(((player, currentPage, requestedPage) -> {
            if(requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(panel, requestedPage, currentSkills, entryList, bossEntity);
            return true;
        }));

        loadPage(panel, 0, currentSkills, entryList, bossEntity);
    }

    @Override
    public void openFor(Player player, BossEntity bossEntity) {
        ServerUtils.get().runTaskAsync(() -> {
            Map<String, String> replaceMap = new HashMap<>();
            PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();

            replaceMap.put("{name}", BossAPI.getBossEntityName(bossEntity));
            panelBuilder.addReplaceData(replaceMap);

            Panel panel = panelBuilder.getPanel()
                    .setDestroyWhenDone(true)
                    .setCancelClick(true)
                    .setCancelLowerClick(true)
                    .setParentPanelHandler(this.bossPanelManager.getSkillsBossEditMenu(), bossEntity);

            fillPanel(panel, bossEntity);

            panel.openFor(player);
        });
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private void loadPage(Panel panel, int requestedPage, Map<String, Skill> currentSkills, List<String> entryList, BossEntity bossEntity) {
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
                replaceMap.put("{type}", StringUtils.get().formatString(skill.getType()));
                replaceMap.put("{displayName}", skill.getDisplayName());
                replaceMap.put("{customMessage}", StringUtils.get().formatString(skill.getCustomMessage()));
                replaceMap.put("{radius}", NumberUtils.get().formatDouble(skill.getRadius()));

                if(bossEntity.getSkills().getSkills().contains(name)) {
                    ItemStackUtils.applyDisplayName(itemStack, this.plugin.getConfig().getString("Display.Boss.Skills.selectedName"), replaceMap);
                } else {
                    ItemStackUtils.applyDisplayName(itemStack, this.plugin.getConfig().getString("Display.Boss.Skills.name"), replaceMap);
                }

                ItemStackUtils.applyDisplayLore(itemStack, this.plugin.getConfig().getStringList("Display.Boss.Skills.lore"), replaceMap);

                panel.setItem(realisticSlot, itemStack, e -> {
                    List<String> currentSkillList = bossEntity.getSkills().getSkills();

                    if(currentSkillList.contains(name)) {
                        currentSkillList.remove(name);
                    } else {
                        currentSkillList.add(name);
                    }

                    bossEntity.getSkills().setSkills(currentSkillList);
                    this.plugin.getBossesFileManager().save();
                    loadPage(panel, requestedPage, currentSkills, entryList, bossEntity);
                });
            }
        });
    }
}
