package com.songoda.epicbosses.entity;

import com.google.gson.annotations.Expose;
import com.songoda.epicbosses.entity.elements.*;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 12-Nov-18
 */
public class MinionEntity {

    @Expose @Getter private final List<EntityStatsElement> entityStats;

    @Expose @Getter @Setter private String targeting;
    @Expose @Getter @Setter private boolean editing;

    public MinionEntity(boolean editing, List<EntityStatsElement> entityStats) {
        this.editing = editing;
        this.entityStats = entityStats;
    }
}
