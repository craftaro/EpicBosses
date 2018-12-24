package com.songoda.epicbosses.panel.droptables.types.drop;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.droptable.elements.DropTableElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.handlers.SubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 24-Dec-18
 */
public class DropDropTableMainEditorPanel extends SubVariablePanelHandler<DropTable, DropTableElement> {

    public DropDropTableMainEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);
    }

    @Override
    public void fillPanel(Panel panel, DropTable dropTable, DropTableElement dropTableElement) {

    }

    @Override
    public void openFor(Player player, DropTable dropTable, DropTableElement dropTableElement) {
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();
        Map<String, String> replaceMap = new HashMap<>();

        replaceMap.put("{name}", BossAPI.getDropTableName(dropTable));
        replaceMap.put("{randomDrops}", StringUtils.get().formatString(""+dropTableElement.getRandomDrops()));
        replaceMap.put("{maxDrops}", NumberUtils.get().formatDouble(dropTableElement.getDropMaxDrops()));
        panelBuilder.addReplaceData(replaceMap);

        PanelBuilderCounter panelBuilderCounter = panelBuilder.getPanelBuilderCounter();
        Panel panel = panelBuilder.getPanel()
                .setParentPanelHandler(this.bossPanelManager.getDropTables());

        //handle buttons

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }
}
