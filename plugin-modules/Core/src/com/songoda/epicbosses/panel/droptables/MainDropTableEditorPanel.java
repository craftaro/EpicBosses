package com.songoda.epicbosses.panel.droptables;

import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.droptable.elements.DropTableElement;
import com.songoda.epicbosses.droptable.elements.GiveTableElement;
import com.songoda.epicbosses.droptable.elements.SprayTableElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.StringUtils;
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
 * @since 23-Dec-18
 */
public class MainDropTableEditorPanel extends VariablePanelHandler<DropTable> {

    public MainDropTableEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder) {
        super(bossPanelManager, panelBuilder);
    }

    @Override
    public void fillPanel(Panel panel, DropTable dropTable) {

    }

    @Override
    public void openFor(Player player, DropTable dropTable) {
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();
        Map<String, String> replaceMap = new HashMap<>();

        replaceMap.put("{name}", BossAPI.getDropTableName(dropTable));
        replaceMap.put("{type}", StringUtils.get().formatString(dropTable.getDropType()));
        panelBuilder.addReplaceData(replaceMap);

        PanelBuilderCounter panelBuilderCounter = panelBuilder.getPanelBuilderCounter();
        Panel panel = panelBuilder.getPanel()
                .setParentPanelHandler(this.bossPanelManager.getDropTables());

        panelBuilderCounter.getSlotsWith("Type").forEach(slot -> panel.setOnClick(slot, event -> this.bossPanelManager.getDropTableTypeEditMenu().openFor(player, dropTable)));
        panelBuilderCounter.getSlotsWith("Rewards").forEach(slot -> panel.setOnClick(slot, getRewardsAction(dropTable)));

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private ClickAction getRewardsAction(DropTable dropTable) {
        return event -> {
            String dropTableType = dropTable.getDropType();
            Player player = (Player) event.getWhoClicked();

            if(dropTableType.equalsIgnoreCase("SPRAY")) {
                SprayTableElement sprayTableElement = dropTable.getSprayTableData();

                this.bossPanelManager.getSprayDropTableMainEditMenu().openFor(player, dropTable, sprayTableElement);
            } else if(dropTableType.equalsIgnoreCase("GIVE")) {
                GiveTableElement giveTableElement = dropTable.getGiveTableData();

                this.bossPanelManager.getGiveDropTableMainEditMenu().openFor(player, dropTable, giveTableElement);
            } else if(dropTableType.equalsIgnoreCase("DROP")) {
                DropTableElement dropTableElement = dropTable.getDropTableData();

                this.bossPanelManager.getDropDropTableMainEditMenu().openFor(player, dropTable, dropTableElement);
            } else {
                Debug.FAILED_TO_FIND_DROP_TABLE_TYPE.debug(dropTableType);
            }
        };
    }
}
