package net.aminecraftdev.custombosses.entities.components.entity;

import com.google.gson.annotations.Expose;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 11-Mar-18
 */
public class ArmorComponent {

    @Expose private String helmet, chestplate, leggings, boots;

    public ArmorComponent(String helmet, String chestplate, String leggings, String boots, String mainHand, String offHand) {
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
    }

    public String getHelmet() {
        return helmet;
    }

    public void setHelmet(String helmet) {
        this.helmet = helmet;
    }

    public String getChestplate() {
        return chestplate;
    }

    public void setChestplate(String chestplate) {
        this.chestplate = chestplate;
    }

    public String getLeggings() {
        return leggings;
    }

    public void setLeggings(String leggings) {
        this.leggings = leggings;
    }

    public String getBoots() {
        return boots;
    }

    public void setBoots(String boots) {
        this.boots = boots;
    }
}
