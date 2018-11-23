package com.songoda.epicbosses.panel.bosses;

import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.BossesFileManager;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.VariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 22-Nov-18
 */
public class TargetingEditorPanel extends VariablePanelHandler<BossEntity> {

    private BossesFileManager bossesFileManager;

    public TargetingEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, BossesFileManager bossesFileManager) {
        super(bossPanelManager, panelBuilder);

        this.bossesFileManager = bossesFileManager;
    }

    @Override
    public void fillPanel(Panel panel, BossEntity bossEntity) {

    }

    @Override
    public void openFor(Player player, BossEntity bossEntity) {
        Map<String, String> replaceMap = new HashMap<>();

        replaceMap.put("{name}", BossAPI.getBossEntityName(bossEntity));
        replaceMap.put("{selected}", bossEntity.getTargetingValue());

        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();

        panelBuilder.addReplaceData(replaceMap);

        Panel panel = panelBuilder.getPanel()
                .setDestroyWhenDone(true)
                .setCancelLowerClick(true)
                .setCancelClick(true)
                .setParentPanelHandler(this.bossPanelManager.getMainBossEditMenu(), bossEntity);
        PanelBuilderCounter panelBuilderCounter = panel.getPanelBuilderCounter();

        panelBuilderCounter.getSpecialSlotsWith("TargetingSystem").forEach((slot, returnValue) -> panel.setOnClick(slot, event -> {
            bossEntity.setTargeting((String) returnValue);
            this.bossesFileManager.save();

            openFor(player, bossEntity);

        }));

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {
        PanelBuilderCounter panelBuilderCounter = panelBuilder.getPanelBuilderCounter();

        panelBuilderCounter.addSpecialCounter("TargetingSystem");
    }
}
