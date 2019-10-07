package com.songoda.epicbosses.entity.elements;

import com.google.gson.annotations.Expose;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 14-May-18
 */
public class EquipmentElement {

    @Expose
    private String helmet, chestplate, leggings, boots;

    public EquipmentElement(String helmet, String chestplate, String leggings, String boots) {
        this.helmet = helmet;
        this.chestplate = chestplate;
        this.leggings = leggings;
        this.boots = boots;
    }

    public String getHelmet() {
        return this.helmet;
    }

    public void setHelmet(String helmet) {
        this.helmet = helmet;
    }

    public String getChestplate() {
        return this.chestplate;
    }

    public void setChestplate(String chestplate) {
        this.chestplate = chestplate;
    }

    public String getLeggings() {
        return this.leggings;
    }

    public void setLeggings(String leggings) {
        this.leggings = leggings;
    }

    public String getBoots() {
        return this.boots;
    }

    public void setBoots(String boots) {
        this.boots = boots;
    }
}
