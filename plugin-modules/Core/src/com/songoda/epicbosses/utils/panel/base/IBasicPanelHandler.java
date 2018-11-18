package com.songoda.epicbosses.utils.panel.base;

import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 18-Nov-18
 */
public interface IBasicPanelHandler {

    PanelBuilder getPanelBuilder();

    Panel getPanel();

    void initializePanel(PanelBuilder panelBuilder);

}
