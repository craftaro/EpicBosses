package com.songoda.epicbosses.entity;

import com.google.gson.annotations.Expose;
import com.songoda.epicbosses.entity.elements.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 14-May-18
 */
public class BossEntity {

    @Expose
    private final List<EntityStatsElement> entityStats;
    @Expose
    private final MessagesElement messages;
    @Expose
    private final CommandsElement commands;
    @Expose
    private final SkillsElement skills;
    @Expose
    private final DropsElement drops;
    @Expose
    private final HealthBarElement healthBar;
    @Expose
    private String spawnItem, targeting;
    @Expose
    private boolean editing, buyable;
    @Expose
    private Double price;

    public BossEntity(boolean editing, String spawnItem, String targeting, boolean buyable, Double price, List<EntityStatsElement> entityStats, SkillsElement skills, DropsElement drops, MessagesElement messages, CommandsElement commands, HealthBarElement healthBar) {
        this.editing = editing;
        this.entityStats = entityStats;
        this.targeting = targeting;
        this.spawnItem = spawnItem;
        this.skills = skills;
        this.drops = drops;
        this.healthBar = healthBar;
        this.messages = messages;
        this.commands = commands;
        this.buyable = buyable;
        this.price = price;
    }

    public String getEditingValue() {
        return this.editing ? "Enabled" : "Disabled";
    }

    public String getTargetingValue() {
        if (getTargeting() == null || getTargeting().isEmpty() || getTargeting().equalsIgnoreCase("")) {
            return "N/A";
        } else {
            return getTargeting();
        }
    }

    public List<String> getIncompleteSectionsToEnable() {
        List<String> incompleteList = new ArrayList<>();

        if (this.entityStats == null) incompleteList.add("EntityStats");
        else {
            EntityStatsElement entityStatsElement = this.entityStats.get(0);

            if (entityStatsElement == null) incompleteList.add("EntityStatsElement");
            else {
                MainStatsElement mainStatsElement = entityStatsElement.getMainStats();

                if (mainStatsElement == null) incompleteList.add("MainStatsElement");
                else {
                    Integer position = mainStatsElement.getPosition();
                    String entityType = mainStatsElement.getEntityType();
                    Double health = mainStatsElement.getHealth();

                    if (position == null) incompleteList.add("Entity Position");
                    if (health == null) incompleteList.add("Entity Health");
                    if (entityType == null || entityType.isEmpty()) incompleteList.add("Entity Type");
                    if (getSpawnItem() == null || getSpawnItem().isEmpty()) incompleteList.add("Spawn Item");
                }
            }
        }

        return incompleteList;
    }

    public boolean isCompleteEnoughToSpawn() {
        if (this.entityStats == null) return false;

        EntityStatsElement entityStatsElement = this.entityStats.get(0);

        if (entityStatsElement == null) return false;

        MainStatsElement mainStatsElement = entityStatsElement.getMainStats();

        if (mainStatsElement == null) return false;

        Integer position = mainStatsElement.getPosition();
        String entityType = mainStatsElement.getEntityType();
        Double health = mainStatsElement.getHealth();

        return (position != null && entityType != null && !entityType.isEmpty() && health != null && getSpawnItem() != null && !getSpawnItem().isEmpty());
    }

    public boolean canBeBought() {
        return !isEditing() && isBuyable() && (getPrice() != null) && isCompleteEnoughToSpawn();
    }

    public String getSpawnItem() {
        return this.spawnItem;
    }

    public void setSpawnItem(String spawnItem) {
        this.spawnItem = spawnItem;
    }

    public String getTargeting() {
        return this.targeting;
    }

    public void setTargeting(String targeting) {
        this.targeting = targeting;
    }

    public boolean isEditing() {
        return this.editing;
    }

    public void setEditing(boolean editing) {
        this.editing = editing;
    }

    public boolean isBuyable() {
        return this.buyable;
    }

    public void setBuyable(boolean buyable) {
        this.buyable = buyable;
    }

    public Double getPrice() {
        return this.price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public List<EntityStatsElement> getEntityStats() {
        return this.entityStats;
    }

    public MessagesElement getMessages() {
        return this.messages;
    }

    public CommandsElement getCommands() {
        return this.commands;
    }

    public SkillsElement getSkills() {
        return this.skills;
    }

    public DropsElement getDrops() {
        return this.drops;
    }

    public HealthBarElement getHealthBar() {
        return healthBar;
    }
}
