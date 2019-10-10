package com.songoda.epicbosses.panel.droptables.types.give.handlers;

import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.droptable.elements.GiveTableElement;
import com.songoda.epicbosses.droptable.elements.GiveTableSubElement;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 31-Dec-18
 */
public class GiveRewardEditHandler {

    private final GiveTableSubElement giveTableSubElement;
    private final String damagePosition, dropSection;
    private final GiveTableElement giveTableElement;
    private final DropTable dropTable;

    public GiveRewardEditHandler(String damagePosition, String dropSection, DropTable dropTable, GiveTableElement giveTableElement, GiveTableSubElement giveTableSubElement) {
        this.damagePosition = damagePosition;
        this.dropSection = dropSection;
        this.dropTable = dropTable;
        this.giveTableElement = giveTableElement;
        this.giveTableSubElement = giveTableSubElement;
    }

    public GiveTableSubElement getGiveTableSubElement() {
        return this.giveTableSubElement;
    }

    public String getDamagePosition() {
        return this.damagePosition;
    }

    public String getDropSection() {
        return this.dropSection;
    }

    public GiveTableElement getGiveTableElement() {
        return this.giveTableElement;
    }

    public DropTable getDropTable() {
        return this.dropTable;
    }
}
