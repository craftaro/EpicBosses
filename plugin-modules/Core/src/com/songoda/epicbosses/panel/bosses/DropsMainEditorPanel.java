package com.songoda.epicbosses.panel.bosses;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.BossesFileManager;
import com.songoda.epicbosses.utils.NumberUtils;
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
 * @since 28-Nov-18
 */
public class DropsMainEditorPanel extends VariablePanelHandler<BossEntity> {

    private BossesFileManager bossesFileManager;

    public DropsMainEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.bossesFileManager = plugin.getBossesFileManager();
    }

    @Override
    public void fillPanel(Panel panel, BossEntity bossEntity) {

    }

    @Override
    public void openFor(Player player, BossEntity bossEntity) {
        Map<String, String> replaceMap = new HashMap<>();
        Boolean naturalDrops = bossEntity.getDrops().getNaturalDrops();
        Boolean naturalExp = bossEntity.getDrops().getDropExp();
        String dropTable = bossEntity.getDrops().getDropTable();
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();

        if(naturalDrops == null) naturalDrops = true;
        if(naturalExp == null) naturalExp = true;
        if(dropTable == null) dropTable = "N/A";

        replaceMap.put("{name}", BossAPI.getBossEntityName(bossEntity));
        replaceMap.put("{naturalDrops}", ""+naturalDrops);
        replaceMap.put("{naturalExp}", ""+naturalExp);
        replaceMap.put("{dropTable}", dropTable);
        panelBuilder.addReplaceData(replaceMap);

        Panel panel = panelBuilder.getPanel()
                .setDestroyWhenDone(true)
                .setCancelClick(true)
                .setCancelLowerClick(true)
                .setParentPanelHandler(this.bossPanelManager.getMainBossEditMenu(), bossEntity);
        PanelBuilderCounter counter = panel.getPanelBuilderCounter();

        Boolean finalNaturalDrops = naturalDrops;
        Boolean finalNaturalEXP = naturalExp;

        counter.getSlotsWith("NaturalDrops").forEach(slot -> panel.setOnClick(slot, getNaturalDropsAction(bossEntity, finalNaturalDrops)));
        counter.getSlotsWith("DropTable").forEach(slot -> panel.setOnClick(slot, event -> this.bossPanelManager.getDropsEditMenu().openFor((Player) event.getWhoClicked(), bossEntity)));
        counter.getSlotsWith("NaturalEXP").forEach(slot -> panel.setOnClick(slot, getNaturalExpAction(bossEntity, finalNaturalEXP)));

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private ClickAction getNaturalDropsAction(BossEntity bossEntity, Boolean current) {
        return event -> {
            bossEntity.getDrops().setNaturalDrops(!current);
            this.bossesFileManager.save();

            openFor((Player) event.getWhoClicked(), bossEntity);
        };
    }

    private ClickAction getNaturalExpAction(BossEntity bossEntity, Boolean current) {
        return event -> {
            bossEntity.getDrops().setDropExp(!current);
            this.bossesFileManager.save();

            openFor((Player) event.getWhoClicked(), bossEntity);
        };
    }
}
