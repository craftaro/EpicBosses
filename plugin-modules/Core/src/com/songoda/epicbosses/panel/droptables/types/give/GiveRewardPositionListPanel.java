package com.songoda.epicbosses.panel.droptables.types.give;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.droptable.elements.GiveTableElement;
import com.songoda.epicbosses.droptable.elements.GiveTableSubElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import com.songoda.epicbosses.utils.panel.base.handlers.SubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 24-Dec-18
 */
public class GiveRewardPositionListPanel extends SubVariablePanelHandler<DropTable, GiveTableElement> {

    public GiveRewardPositionListPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);
    }

    @Override
    public void fillPanel(Panel panel, DropTable dropTable, GiveTableElement giveTableElement) {
        Map<String, Map<String, GiveTableSubElement>> rewardSections = giveTableElement.getGiveRewards();
        List<String> keys = new ArrayList<>(rewardSections.keySet());
        int maxPage = panel.getMaxPage(keys);

    }

    @Override
    public void openFor(Player player, DropTable dropTable, GiveTableElement giveTableElement) {
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();
        Map<String, String> replaceMap = new HashMap<>();

        replaceMap.put("{name}", BossAPI.getDropTableName(dropTable));
        panelBuilder.addReplaceData(replaceMap);

        PanelBuilderCounter panelBuilderCounter = panelBuilder.getPanelBuilderCounter();
        Panel panel = panelBuilder.getPanel()
                .setParentPanelHandler(this.bossPanelManager.getDropTables());

        panelBuilderCounter.getSlotsWith("NewPosition").forEach(slot -> panel.setOnClick(slot, getNewPositionAction(dropTable, giveTableElement)));
        fillPanel(panel, dropTable, giveTableElement);
        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private int getNextAvailablePosition(List<String> keys) {


        return 0;
    }

    private ClickAction getNewPositionAction(DropTable dropTable, GiveTableElement giveTableElement) {
        return event -> {};
    }
}
