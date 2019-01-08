package com.songoda.epicbosses.handlers.variables;

import com.songoda.epicbosses.autospawns.AutoSpawn;
import com.songoda.epicbosses.autospawns.types.IntervalSpawnElement;
import com.songoda.epicbosses.handlers.AutoSpawnVariableHandler;
import com.songoda.epicbosses.managers.files.AutoSpawnFileManager;
import com.songoda.epicbosses.utils.panel.base.IVariablePanelHandler;
import org.bukkit.entity.Player;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 08-Jan-19
 */
public class AutoSpawnPlaceholderVariableHandler extends AutoSpawnVariableHandler {

    public AutoSpawnPlaceholderVariableHandler(Player player, AutoSpawn autoSpawn, IntervalSpawnElement intervalSpawnElement, AutoSpawnFileManager autoSpawnFileManager, IVariablePanelHandler<AutoSpawn> panelHandler) {
        super(player, autoSpawn, intervalSpawnElement, autoSpawnFileManager, panelHandler);
    }

    @Override
    protected boolean confirmValue(String input, IntervalSpawnElement intervalSpawnElement) {
        intervalSpawnElement.setPlaceholder(input);
        return true;
    }
}
