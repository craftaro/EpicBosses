package com.songoda.epicbosses.entity.elements;

import com.google.gson.annotations.Expose;
import com.songoda.epicbosses.utils.potion.holder.PotionEffectHolder;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 21-Oct-18
 */
public class EntityStatsElement {

    @Expose
    private MainStatsElement mainStats;
    @Expose
    private EquipmentElement equipment;
    @Expose
    private HandsElement hands;
    @Expose
    private List<PotionEffectHolder> potions;

    public EntityStatsElement(MainStatsElement mainStatsElement, EquipmentElement equipmentElement, HandsElement handsElement, List<PotionEffectHolder> potionEffectHolders) {
        this.mainStats = mainStatsElement;
        this.equipment = equipmentElement;
        this.hands = handsElement;
        this.potions = potionEffectHolders;
    }

    public MainStatsElement getMainStats() {
        return this.mainStats;
    }

    public void setMainStats(MainStatsElement mainStats) {
        this.mainStats = mainStats;
    }

    public EquipmentElement getEquipment() {
        return this.equipment;
    }

    public void setEquipment(EquipmentElement equipment) {
        this.equipment = equipment;
    }

    public HandsElement getHands() {
        return this.hands;
    }

    public void setHands(HandsElement hands) {
        this.hands = hands;
    }

    public List<PotionEffectHolder> getPotions() {
        return this.potions;
    }

    public void setPotions(List<PotionEffectHolder> potions) {
        this.potions = potions;
    }
}
