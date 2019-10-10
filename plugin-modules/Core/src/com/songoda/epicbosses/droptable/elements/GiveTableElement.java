package com.songoda.epicbosses.droptable.elements;

import com.google.gson.annotations.Expose;

import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 25-Oct-18
 */
public class GiveTableElement {

    @Expose
    private Map<String, Map<String, GiveTableSubElement>> giveRewards;

    public GiveTableElement(Map<String, Map<String, GiveTableSubElement>> giveRewards) {
        this.giveRewards = giveRewards;
    }

    public Map<String, Map<String, GiveTableSubElement>> getGiveRewards() {
        return this.giveRewards;
    }

    public void setGiveRewards(Map<String, Map<String, GiveTableSubElement>> giveRewards) {
        this.giveRewards = giveRewards;
    }
}
