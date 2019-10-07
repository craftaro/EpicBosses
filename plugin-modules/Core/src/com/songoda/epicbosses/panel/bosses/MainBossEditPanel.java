package com.songoda.epicbosses.panel.bosses;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.managers.BossEntityManager;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.BossesFileManager;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.ObjectUtils;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import com.songoda.epicbosses.utils.panel.base.handlers.VariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 18-Nov-18
 */
public class MainBossEditPanel extends VariablePanelHandler<BossEntity> {

    private BossesFileManager bossesFileManager;
    private BossEntityManager bossEntityManager;

    public MainBossEditPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, EpicBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.bossesFileManager = plugin.getBossesFileManager();
        this.bossEntityManager = plugin.getBossEntityManager();
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    @Override
    public void fillPanel(Panel panel, BossEntity bossEntity) {

    }

    @Override
    public void openFor(Player player, BossEntity bossEntity) {
        Map<String, String> replaceMap = new HashMap<>();
        String spawnItem = ObjectUtils.getValue(bossEntity.getSpawnItem(), "N/A");

        replaceMap.put("{name}", BossAPI.getBossEntityName(bossEntity));
        replaceMap.put("{mode}", bossEntity.getEditingValue());
        replaceMap.put("{spawnItem}", spawnItem);

        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();

        panelBuilder.addReplaceData(replaceMap);

        Panel panel = panelBuilder.getPanel()
                .setDestroyWhenDone(true)
                .setCancelClick(true)
                .setCancelLowerClick(true);
        PanelBuilderCounter counter = panel.getPanelBuilderCounter();

        ServerUtils.get().runTaskAsync(() -> {
            counter.getSlotsWith("Editing").forEach(slot -> panel.setOnClick(slot, getEditingAction(bossEntity)));
            counter.getSlotsWith("Drops").forEach(slot -> panel.setOnClick(slot, e -> this.bossPanelManager.getMainDropsEditMenu().openFor((Player) e.getWhoClicked(), bossEntity)));
            counter.getSlotsWith("Targeting").forEach(slot -> panel.setOnClick(slot, e -> this.bossPanelManager.getTargetingEditMenu().openFor((Player) e.getWhoClicked(), bossEntity)));
            counter.getSlotsWith("Equipment").forEach(slot -> panel.setOnClick(slot, e -> this.bossPanelManager.getEquipmentListEditMenu().openFor((Player) e.getWhoClicked(), bossEntity)));
            counter.getSlotsWith("Weapon").forEach(slot -> panel.setOnClick(slot, e -> this.bossPanelManager.getWeaponListEditMenu().openFor((Player) e.getWhoClicked(), bossEntity)));
            counter.getSlotsWith("Skill").forEach(slot -> panel.setOnClick(slot, e -> this.bossPanelManager.getSkillsBossEditMenu().openFor((Player) e.getWhoClicked(), bossEntity)));
            counter.getSlotsWith("Stats").forEach(slot -> panel.setOnClick(slot, e -> this.bossPanelManager.getStatisticListEditMenu().openFor((Player) e.getWhoClicked(), bossEntity)));
            counter.getSlotsWith("Command").forEach(slot -> panel.setOnClick(slot, e -> this.bossPanelManager.getCommandsMainEditMenu().openFor((Player) e.getWhoClicked(), bossEntity)));
            counter.getSlotsWith("Text").forEach(slot -> panel.setOnClick(slot, e -> this.bossPanelManager.getMainTextEditMenu().openFor((Player) e.getWhoClicked(), bossEntity)));
            counter.getSlotsWith("SpawnItem").forEach(slot -> panel.setOnClick(slot, e -> this.bossPanelManager.getSpawnItemEditMenu().openFor((Player) e.getWhoClicked(), bossEntity)));
            counter.getSlotsWith("Shop").forEach(slot -> panel.setOnClick(slot, e -> this.bossPanelManager.getBossShopEditMenu().openFor((Player) e.getWhoClicked(), bossEntity)));
        });

        panel.openFor(player);
    }

    private ClickAction getEditingAction(BossEntity bossEntity) {
        return event -> {
            Player player = (Player) event.getWhoClicked();

            if (bossEntity.isCompleteEnoughToSpawn()) {
                bossEntity.setEditing(!bossEntity.isEditing());
                this.bossesFileManager.save();
                Message.Boss_Edit_Toggled.msg(player, BossAPI.getBossEntityName(bossEntity), bossEntity.getEditingValue());

                if (bossEntity.isEditing()) {
                    this.bossEntityManager.killAllHolders(bossEntity);
                }

                openFor(player, bossEntity);
            } else {
                List<String> incompleteThings = bossEntity.getIncompleteSectionsToEnable();

                Message.Boss_Edit_NotCompleteEnough.msg(player, StringUtils.get().appendList(incompleteThings));
            }
        };
    }

}
