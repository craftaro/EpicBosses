package com.songoda.epicbosses.panel.skills.custom.potions;

import com.google.gson.JsonObject;
import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.BossSkillManager;
import com.songoda.epicbosses.managers.files.SkillsFileManager;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.types.PotionSkillElement;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.PotionEffectFinder;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import com.songoda.epicbosses.utils.panel.base.handlers.SubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import com.songoda.epicbosses.utils.potion.holder.PotionEffectHolder;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Dec-18
 */
public class CreatePotionEffectEditorPanel extends SubVariablePanelHandler<Skill, PotionEffectHolder> {

    private SkillsFileManager skillsFileManager;
    private BossSkillManager bossSkillManager;

    public CreatePotionEffectEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.bossSkillManager = plugin.getBossSkillManager();
        this.skillsFileManager = plugin.getSkillsFileManager();
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    @Override
    public void fillPanel(Panel panel, Skill skill, PotionEffectHolder potionEffectHolder) {

    }

    @Override
    public void openFor(Player player, Skill skill, PotionEffectHolder potionEffectHolder) {
        Map<String, String> replaceMap = new HashMap<>();
        String effect = potionEffectHolder.getType();
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();

        if(effect == null || effect.isEmpty()) effect = "N/A";

        replaceMap.put("{duration}", NumberUtils.get().formatDouble(potionEffectHolder.getDuration()));
        replaceMap.put("{level}", NumberUtils.get().formatDouble(potionEffectHolder.getLevel()));
        replaceMap.put("{effect}", effect);
        panelBuilder.addReplaceData(replaceMap);

        Panel panel = panelBuilder.getPanel()
                .setCancelClick(true)
                .setCancelLowerClick(true)
                .setDestroyWhenDone(true)
                .setParentPanelHandler(this.bossPanelManager.getPotionSkillEditorPanel(), skill);

        PanelBuilderCounter counter = panel.getPanelBuilderCounter();

        ServerUtils.get().runTaskAsync(() -> {
            counter.getSlotsWith("Cancel").forEach(slot -> panel.setOnClick(slot, event -> this.bossPanelManager.getPotionSkillEditorPanel().openFor((Player) event.getWhoClicked(), skill)));
            counter.getSlotsWith("Confirm").forEach(slot -> panel.setOnClick(slot, getConfirmAction(skill, potionEffectHolder)));
            counter.getSlotsWith("Level").forEach(slot -> panel.setOnClick(slot, getLevelAction(skill, potionEffectHolder)));
            counter.getSlotsWith("Duration").forEach(slot -> panel.setOnClick(slot, getDurationAction(skill, potionEffectHolder)));
            counter.getSlotsWith("Effect").forEach(slot -> panel.setOnClick(slot, event -> this.bossPanelManager.getPotionEffectTypeEditMenu().openFor((Player) event.getWhoClicked(), skill, potionEffectHolder)));
        });

        panel.openFor(player);
    }

    private ClickAction getDurationAction(Skill skill, PotionEffectHolder potionEffectHolder) {
        return event -> {
            ClickType clickType = event.getClick();
            int amountToModifyBy;

            if(clickType == ClickType.RIGHT) {
                amountToModifyBy = -1;
            } else if(clickType == ClickType.SHIFT_RIGHT) {
                amountToModifyBy = -10;
            } else if(clickType == ClickType.SHIFT_LEFT) {
                amountToModifyBy = 10;
            }else {
                amountToModifyBy = 1;
            }

            int currentAmount = potionEffectHolder.getDuration();
            int totalAmount = amountToModifyBy + currentAmount;

            if(totalAmount < 0) {
                totalAmount = -1;
            }

            potionEffectHolder.setDuration(totalAmount);
            openFor((Player) event.getWhoClicked(), skill, potionEffectHolder);
        };
    }

    private ClickAction getLevelAction(Skill skill, PotionEffectHolder potionEffectHolder) {
        return event -> {
            ClickType clickType = event.getClick();
            int amountToModifyBy;

            if(clickType.name().contains("RIGHT")) {
                amountToModifyBy = -1;
            } else {
                amountToModifyBy = 1;
            }

            int currentAmount = potionEffectHolder.getLevel();
            int totalAmount = amountToModifyBy + currentAmount;

            if(totalAmount <= 0) {
                totalAmount = 1;
            }

            if(totalAmount > 255) {
                totalAmount = 255;
            }

            potionEffectHolder.setLevel(totalAmount);
            openFor((Player) event.getWhoClicked(), skill, potionEffectHolder);
        };
    }

    private ClickAction getConfirmAction(Skill skill, PotionEffectHolder potionEffectHolder) {
        return event -> {
            Integer duration = potionEffectHolder.getDuration();
            Integer level = potionEffectHolder.getLevel();
            String type = potionEffectHolder.getType();

            if(duration != null && level != null && type != null && !type.isEmpty()) {
                PotionSkillElement potionSkillElement = this.bossSkillManager.getPotionSkillElement(skill);
                PotionEffectFinder potionEffectFinder = PotionEffectFinder.getByName(type);

                if(potionEffectFinder != null) {
                    List<PotionEffectHolder> currentList = potionSkillElement.getPotions();

                    currentList.add(potionEffectHolder);
                    potionSkillElement.setPotions(currentList);

                    JsonObject jsonObject = BossAPI.convertObjectToJsonObject(potionSkillElement);

                    skill.setCustomData(jsonObject);
                    this.skillsFileManager.save();
                } else {
                    Message.Boss_Skills_NotCompleteEnough.msg(event.getWhoClicked());
                }
            } else {
                Message.Boss_Skills_NotCompleteEnough.msg(event.getWhoClicked());
            }

            this.bossPanelManager.getPotionSkillEditorPanel().openFor((Player) event.getWhoClicked(), skill);
        };
    }
}
