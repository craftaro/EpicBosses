package com.songoda.epicbosses.panel.bosses;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.managers.BossEntityManager;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.BossesFileManager;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import com.songoda.epicbosses.utils.panel.base.handlers.VariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 18-Nov-18
 */
public class MainBossEditPanel extends VariablePanelHandler<BossEntity> {

    private BossesFileManager bossesFileManager;
    private BossEntityManager bossEntityManager;

    public MainBossEditPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.bossesFileManager = plugin.getBossesFileManager();
        this.bossEntityManager = plugin.getBossEntityManager();
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {
        PanelBuilderCounter panelBuilderCounter = panelBuilder.getPanelBuilderCounter();

        panelBuilderCounter
                .addSlotCounter("Drops")
                .addSlotCounter("Equipment")
                .addSlotCounter("Targeting")
                .addSlotCounter("Weapon")
                .addSlotCounter("Skill")
                .addSlotCounter("Stats")
                .addSlotCounter("Particle")
                .addSlotCounter("Spawning")
                .addSlotCounter("Text")
                .addSlotCounter("Editing")
                .addSlotCounter("Command");
    }

    @Override
    public void fillPanel(Panel panel, BossEntity bossEntity) {

    }

    @Override
    public void openFor(Player player, BossEntity bossEntity) {
        Map<String, String> replaceMap = new HashMap<>();

        replaceMap.put("{name}", BossAPI.getBossEntityName(bossEntity));
        replaceMap.put("{mode}", bossEntity.getEditingValue());

        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();

        panelBuilder.addReplaceData(replaceMap);

        Panel panel = panelBuilder.getPanel()
                .setDestroyWhenDone(true)
                .setCancelClick(true)
                .setCancelLowerClick(true);
        PanelBuilderCounter counter = panel.getPanelBuilderCounter();

        counter.getSlotsWith("Editing").forEach(slot -> panel.setOnClick(slot, getEditingAction(bossEntity)));
        counter.getSlotsWith("Drops").forEach(slot -> panel.setOnClick(slot, e -> this.bossPanelManager.getDropsEditMenu().openFor((Player) e.getWhoClicked(), bossEntity)));
        counter.getSlotsWith("Targeting").forEach(slot -> panel.setOnClick(slot, e -> this.bossPanelManager.getTargetingEditMenu().openFor((Player) e.getWhoClicked(), bossEntity)));
        counter.getSlotsWith("Equipment").forEach(slot -> panel.setOnClick(slot, e -> this.bossPanelManager.getEquipmentListEditMenu().openFor((Player) e.getWhoClicked(), bossEntity)));
        counter.getSlotsWith("Weapon").forEach(slot -> panel.setOnClick(slot, e -> this.bossPanelManager.getWeaponListEditMenu().openFor((Player) e.getWhoClicked(), bossEntity)));
        counter.getSlotsWith("Skill").forEach(slot -> panel.setOnClick(slot, e -> this.bossPanelManager.getSkillsBossEditMenu().openFor((Player) e.getWhoClicked(), bossEntity)));
        counter.getSlotsWith("Stats").forEach(slot -> panel.setOnClick(slot, e -> this.bossPanelManager.getStatisticListEditMenu().openFor((Player) e.getWhoClicked(), bossEntity)));

        panel.openFor(player);
    }

    private ClickAction getEditingAction(BossEntity bossEntity) {
        return event -> {
            Player player = (Player) event.getWhoClicked();

            if(bossEntity.isCompleteEnoughToSpawn()) {
                bossEntity.setEditing(!bossEntity.isEditing());
                this.bossesFileManager.save();
                Message.Boss_Edit_Toggled.msg(player, BossAPI.getBossEntityName(bossEntity), bossEntity.getEditingValue());
                player.closeInventory();

                if(bossEntity.isEditing()) {
                    this.bossEntityManager.killAllHolders(bossEntity);
                }
            } else {
                Message.Boss_Edit_NotCompleteEnough.msg(player);
            }
        };
    }

}
