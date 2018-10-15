package net.aminecraftdev.custombosses.utils.panel.base;

import lombok.Getter;
import net.aminecraftdev.custombosses.managers.BossPanelManager;
import net.aminecraftdev.custombosses.utils.panel.Panel;
import net.aminecraftdev.custombosses.utils.panel.builder.PanelBuilder;
import org.bukkit.entity.Player;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 10-Oct-18
 */
public class PanelHandler implements IPanelHandler {

    protected final BossPanelManager bossPanelManager;

    @Getter protected Panel panel;

    public PanelHandler(BossPanelManager bossPanelManager, PanelBuilder panelBuilder) {
        this.bossPanelManager = bossPanelManager;

        initializePanel(panelBuilder);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {
        this.panel = panelBuilder.getPanel()
                .setDestroyWhenDone(false)
                .setCancelClick(true)
                .setCancelLowerClick(true);
    }

    @Override
    public void fillPanel() {

    }

    @Override
    public void openFor(Player player) {
        System.out.println(this.panel);
        this.panel.openFor(player);
    }
}
