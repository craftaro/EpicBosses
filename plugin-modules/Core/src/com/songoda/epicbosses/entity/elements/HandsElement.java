package com.songoda.epicbosses.entity.elements;

import com.google.gson.annotations.Expose;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 14-May-18
 */
public class HandsElement {

    @Expose
    private String mainHand, offHand;

    public HandsElement(String mainHand, String offHand) {
        this.mainHand = mainHand;
        this.offHand = offHand;
    }

    public String getMainHand() {
        return this.mainHand;
    }

    public void setMainHand(String mainHand) {
        this.mainHand = mainHand;
    }

    public String getOffHand() {
        return this.offHand;
    }

    public void setOffHand(String offHand) {
        this.offHand = offHand;
    }
}
