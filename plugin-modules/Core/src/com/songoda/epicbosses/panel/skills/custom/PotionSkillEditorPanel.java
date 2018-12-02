package com.songoda.epicbosses.panel.skills.custom;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.BossSkillManager;
import com.songoda.epicbosses.managers.files.SkillsFileManager;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.types.PotionSkillElement;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.handlers.VariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import com.songoda.epicbosses.utils.potion.PotionEffectConverter;
import com.songoda.epicbosses.utils.potion.holder.PotionEffectHolder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Dec-18
 */
public class PotionSkillEditorPanel extends VariablePanelHandler<Skill> {

    private PotionEffectConverter potionEffectConverter;
    private SkillsFileManager skillsFileManager;
    private BossSkillManager bossSkillManager;

    public PotionSkillEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.bossSkillManager = plugin.getBossSkillManager();
        this.skillsFileManager = plugin.getSkillsFileManager();
        this.potionEffectConverter = new PotionEffectConverter();
    }

    @Override
    public void fillPanel(Panel panel, Skill skill) {
        PotionSkillElement potionSkillElement = this.bossSkillManager.getPotionSkillElement(skill);
        List<PotionEffectHolder> potionEffectHolders = potionSkillElement.getPotions();
        int maxPage = panel.getMaxPage(potionEffectHolders);

        panel.setOnPageChange(((player, currentPage, requestedPage) -> {
            if(requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(panel, requestedPage, potionEffectHolders, potionSkillElement);
            return true;
        }));

        loadPage(panel, 0, potionEffectHolders, potionSkillElement);
    }

    @Override
    public void openFor(Player player, Skill skill) {
        Map<String, String> replaceMap = new HashMap<>();
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();

        replaceMap.put("{name}", BossAPI.getSkillName(skill));
        panelBuilder.addReplaceData(replaceMap);

        PanelBuilderCounter counter = panelBuilder.getPanelBuilderCounter();
        PotionSkillElement potionSkillElement = this.bossSkillManager.getPotionSkillElement(skill);
        Panel panel = panelBuilder.getPanel()
                .setDestroyWhenDone(true)
                .setCancelClick(true)
                .setCancelLowerClick(true)
                .setParentPanelHandler(this.bossPanelManager.getMainSkillEditMenu(), skill);

        fillPanel(panel, skill);
        counter.getSlotsWith("PotionEffect").forEach(slot -> panel.setOnClick(slot, event -> this.bossPanelManager.getCreatePotionEffectMenu().openFor((Player) event.getWhoClicked(), skill, potionSkillElement)));

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {
        PanelBuilderCounter panelBuilderCounter = panelBuilder.getPanelBuilderCounter();

        panelBuilderCounter
                .addSlotCounter("PotionEffect");
    }

    private void loadPage(Panel panel, int page, List<PotionEffectHolder> potionEffectHolders, PotionSkillElement potionSkillElement) {
        panel.loadPage(page, (slot, realisticSlot) -> {
            if(slot >= potionEffectHolders.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e -> {});
            } else {
                PotionEffectHolder potionEffectHolder = potionEffectHolders.get(slot);
                PotionEffect potionEffect = this.potionEffectConverter.from(potionEffectHolder);

                ItemStack itemStack = new ItemStack(Material.POTION);
                PotionMeta potionMeta = (PotionMeta) itemStack.getItemMeta();

                potionMeta.addCustomEffect(potionEffect, true);
                itemStack.setItemMeta(potionMeta);

                panel.setItem(realisticSlot, itemStack, e -> {
                    potionEffectHolders.remove(potionEffectHolder);
                    potionSkillElement.setPotions(potionEffectHolders);
                    this.skillsFileManager.save();

                    loadPage(panel, page, potionEffectHolders, potionSkillElement);
                });
            }
        });
    }
}
