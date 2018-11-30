package com.songoda.epicbosses.utils.panel.base;

import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 30-Nov-18
 */
public interface IPanelListHandler<FilteredValue, PanelHandler extends IBasicPanelHandler> {

    Map<String, FilteredValue> getFilteredMap(Map<String, FilteredValue> originalMap);

    PanelHandler getParentHolder();

}
