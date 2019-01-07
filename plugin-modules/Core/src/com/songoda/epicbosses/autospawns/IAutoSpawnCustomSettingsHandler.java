package com.songoda.epicbosses.autospawns;

import com.songoda.epicbosses.skills.interfaces.ICustomSettingAction;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 07-Jan-19
 */
public interface IAutoSpawnCustomSettingsHandler {

    List<ICustomSettingAction> getCustomSettingActions();

}
