package com.songoda.epicbosses.panel.droptables;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import com.songoda.epicbosses.utils.panel.base.handlers.VariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 23-Dec-18
 */
public class DropTableTypeEditorPanel extends VariablePanelHandler<DropTable> {

    private CustomBosses plugin;

    public DropTableTypeEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.plugin = plugin;
    }

    @Override
    public void fillPanel(Panel panel, DropTable dropTable) {

    }

    @Override
    public void openFor(Player player, DropTable dropTable) {
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();
        Map<String, String> replaceMap = new HashMap<>();

        replaceMap.put("{name}", BossAPI.getDropTableName(dropTable));
        panelBuilder.addReplaceData(replaceMap);

        PanelBuilderCounter panelBuilderCounter = panelBuilder.getPanelBuilderCounter();
        Panel panel = panelBuilder.getPanel()
                .setParentPanelHandler(this.bossPanelManager.getMainDropTableEditMenu(), dropTable);

        panelBuilderCounter.getSlotsWith("Spray").forEach(slot -> panel.setOnClick(slot, getAction(dropTable, "Spray")));
        panelBuilderCounter.getSlotsWith("Drop").forEach(slot -> panel.setOnClick(slot, getAction(dropTable, "Drop")));
        panelBuilderCounter.getSlotsWith("Give").forEach(slot -> panel.setOnClick(slot, getAction(dropTable, "Give")));

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private ClickAction getAction(DropTable dropTable, String button) {
        return event -> {
            dropTable.setDropType(button.toUpperCase());
            save(dropTable, event);
        };
    }

    private void save(DropTable dropTable, InventoryClickEvent event) {
        this.plugin.getDropTableFileManager().save();
        this.bossPanelManager.getMainDropTableEditMenu().openFor((Player) event.getWhoClicked(), dropTable);
    }
}
