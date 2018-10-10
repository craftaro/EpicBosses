package net.aminecraftdev.custombosses.panel;

import net.aminecraftdev.custombosses.managers.BossPanelManager;
import net.aminecraftdev.custombosses.utils.panel.base.PanelHandler;
import net.aminecraftdev.custombosses.utils.panel.builder.PanelBuilder;
import net.aminecraftdev.custombosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.entity.Player;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 10-Oct-18
 */
public class MainMenuPanel extends PanelHandler {

    public MainMenuPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder) {
        super(bossPanelManager, panelBuilder);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {
        PanelBuilderCounter panelBuilderCounter = panelBuilder.getPanelBuilderCounter();

        panelBuilderCounter
                .addSlotCounter("CustomBosses", event -> this.bossPanelManager.getBosses().openFor((Player) event.getWhoClicked()))
                .addSlotCounter("CustomItems", event -> this.bossPanelManager.getCustomItems().openFor((Player) event.getWhoClicked()))
                .addSlotCounter("AutoSpawns", event -> this.bossPanelManager.getAutoSpawns().openFor((Player) event.getWhoClicked()))
                .addSlotCounter("DropTables", event -> this.bossPanelManager.getDropTables().openFor((Player) event.getWhoClicked()))
                .addSlotCounter("CustomSkills", event -> this.bossPanelManager.getCustomSkills().openFor((Player) event.getWhoClicked()));

        this.panel = panelBuilder.getPanel()
                .setCancelClick(true)
                .setDestroyWhenDone(false)
                .setCancelLowerClick(true);
    }
}
