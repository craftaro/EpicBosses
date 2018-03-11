package net.aminecraftdev.custombosses.entities.components.entity;

import com.google.gson.annotations.Expose;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 11-Mar-18
 */
public class HandComponent {

    @Expose private String mainHand, offHand;

    public HandComponent(String mainHand, String offHand) {
        this.mainHand = mainHand;
        this.offHand = offHand;
    }

    public String getMainHand() {
        return mainHand;
    }

    public void setMainHand(String mainHand) {
        this.mainHand = mainHand;
    }

    public String getOffHand() {
        return offHand;
    }

    public void setOffHand(String offHand) {
        this.offHand = offHand;
    }

}
