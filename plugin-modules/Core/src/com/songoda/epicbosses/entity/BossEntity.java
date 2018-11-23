package com.songoda.epicbosses.entity;

import com.google.gson.annotations.Expose;
import com.songoda.epicbosses.utils.StringUtils;
import lombok.Getter;
import lombok.Setter;
import com.songoda.epicbosses.entity.elements.*;
import com.songoda.epicbosses.utils.potion.holder.PotionEffectHolder;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 14-May-18
 */
public class BossEntity {

    @Expose @Getter @Setter private String spawnItem, targeting;
    @Expose @Getter @Setter private boolean editing, buyable;
    @Expose @Getter @Setter private Double price;

    @Expose @Getter private final List<EntityStatsElement> entityStats;
    @Expose @Getter private final MessagesElement messages;
    @Expose @Getter private final CommandsElement commands;
    @Expose @Getter private final SkillsElement skills;
    @Expose @Getter private final DropsElement drops;

    public BossEntity(boolean editing, String spawnItem, boolean buyable, Double price, List<EntityStatsElement> entityStats, SkillsElement skills, DropsElement drops, MessagesElement messages, CommandsElement commands) {
        this.editing = editing;
        this.entityStats = entityStats;
        this.spawnItem = spawnItem;
        this.skills = skills;
        this.drops = drops;
        this.messages = messages;
        this.commands = commands;
        this.buyable = buyable;
        this.price = price;
    }

    public String getEditingValue() {
        return this.editing? "Enabled" : "Disabled";
    }

    public String getTargetingValue() {
        if(getTargeting() == null || getTargeting().isEmpty() || getTargeting().equalsIgnoreCase("")) {
            return "N/A";
        } else {
            return getTargeting();
        }
    }

    public boolean isCompleteEnoughToSpawn() {
        if(this.entityStats == null) return false;

        EntityStatsElement entityStatsElement = this.entityStats.get(0);

        if(entityStatsElement == null) return false;

        MainStatsElement mainStatsElement = entityStatsElement.getMainStats();

        if(mainStatsElement == null) return false;

        Integer position = mainStatsElement.getPosition();
        String entityType = mainStatsElement.getEntityType();
        Double health = mainStatsElement.getHealth();

        return (position != null && entityType != null && !entityType.isEmpty() && health != null && getSpawnItem() != null && !getSpawnItem().isEmpty());
    }

    public boolean canBeBought() {
        return !isEditing() && isBuyable() && (getPrice() != null) && isCompleteEnoughToSpawn();
    }
}
