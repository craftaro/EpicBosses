package com.songoda.epicbosses.handlers.droptable;

import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.handlers.IGetDropTableListItem;
import com.songoda.epicbosses.utils.Debug;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Oct-18
 */
public class GetDropTableCommand implements IGetDropTableListItem<List<String>> {

    @Override
    public List<String> getListItem(String id) {
        List<String> commands = BossAPI.getStoredCommands(id);

        if (commands == null) {
            Debug.FAILED_TO_LOAD_COMMANDS.debug(id);
        }

        return commands;
    }
}
