package com.songoda.epicbosses.entity;

import com.google.gson.annotations.Expose;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 12-Nov-18
 */
public class MinionEntity {

    @Expose
    private final List<EntityStatsElement> entityStats;

    @Expose
    private String targeting;
    @Expose
    private boolean editing;

    public MinionEntity(boolean editing, List<EntityStatsElement> entityStats) {
        this.editing = editing;
        this.entityStats = entityStats;
    }

    public List<EntityStatsElement> getEntityStats() {
        return this.entityStats;
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
}
