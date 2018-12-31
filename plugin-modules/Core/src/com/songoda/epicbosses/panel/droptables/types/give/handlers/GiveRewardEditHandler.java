package com.songoda.epicbosses.panel.droptables.types.give.handlers;

import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.droptable.elements.GiveTableElement;
import lombok.Getter;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 31-Dec-18
 */
public class GiveRewardEditHandler {

    @Getter private final String damagePosition, dropSection;
    @Getter private final GiveTableElement giveTableElement;
    @Getter private final DropTable dropTable;

    public GiveRewardEditHandler(String damagePosition, String dropSection, DropTable dropTable, GiveTableElement giveTableElement) {
        this.damagePosition = damagePosition;
        this.dropSection = dropSection;
        this.dropTable = dropTable;
        this.giveTableElement = giveTableElement;
    }

}
