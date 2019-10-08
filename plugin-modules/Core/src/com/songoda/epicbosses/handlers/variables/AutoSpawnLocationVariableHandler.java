package com.songoda.epicbosses.handlers.variables;

import com.songoda.epicbosses.autospawns.AutoSpawn;
import com.songoda.epicbosses.autospawns.types.IntervalSpawnElement;
import com.songoda.epicbosses.handlers.AutoSpawnVariableHandler;
import com.songoda.epicbosses.managers.files.AutoSpawnFileManager;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.panel.base.IVariablePanelHandler;
import org.bukkit.Location;
import org.bukkit.entity.Player;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 07-Jan-19
 */
public class AutoSpawnLocationVariableHandler extends AutoSpawnVariableHandler {

    public AutoSpawnLocationVariableHandler(Player player, AutoSpawn autoSpawn, IntervalSpawnElement intervalSpawnElement, AutoSpawnFileManager autoSpawnFileManager, IVariablePanelHandler<AutoSpawn> panelHandler) {
        super(player, autoSpawn, intervalSpawnElement, autoSpawnFileManager, panelHandler);
    }

    @Override
    protected boolean confirmValue(String input, IntervalSpawnElement intervalSpawnElement) {
        Location location = StringUtils.get().fromStringToLocation(input);

        if (location == null) {
            Message.Boss_AutoSpawn_InvalidLocation.msg(getPlayer(), input);
            return false;
        }

        intervalSpawnElement.setLocation(StringUtils.get().translateLocation(location));
        return true;
    }

}
