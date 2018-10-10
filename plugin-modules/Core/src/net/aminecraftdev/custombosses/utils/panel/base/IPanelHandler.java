package net.aminecraftdev.custombosses.utils.panel.base;

import net.aminecraftdev.custombosses.utils.panel.Panel;
import net.aminecraftdev.custombosses.utils.panel.builder.PanelBuilder;
import org.bukkit.entity.Player;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 10-Oct-18
 */
public interface IPanelHandler {

    Panel getPanel();

    void initializePanel(PanelBuilder panelBuilder);

    void fillPanel();

    void openFor(Player player);

}
