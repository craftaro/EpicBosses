package com.songoda.epicbosses.autospawns;

import com.songoda.epicbosses.skills.interfaces.ICustomSettingAction;
import com.songoda.epicbosses.utils.panel.base.handlers.VariablePanelHandler;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 07-Jan-19
 */
public interface IAutoSpawnCustomSettingsHandler {

    List<ICustomSettingAction> getCustomSettingActions(AutoSpawn autoSpawn, VariablePanelHandler<AutoSpawn> variablePanelHandler);

}
