package com.songoda.epicbosses.autospawns;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.songoda.epicbosses.autospawns.settings.AutoSpawnSettings;
import com.songoda.epicbosses.autospawns.types.IntervalSpawnElement;
import com.songoda.epicbosses.utils.BossesGson;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Jan-19
 */
public class AutoSpawn {

    @Expose @Getter @Setter private Boolean editing;
    @Expose @Getter @Setter private String type;
    @Expose @Getter @Setter private List<String> entities;
    @Expose @Getter @Setter private AutoSpawnSettings autoSpawnSettings;
    @Expose @Getter @Setter private JsonObject customData;

    public AutoSpawn(boolean editing, List<String> entities, AutoSpawnSettings autoSpawnSettings) {
        this.editing = editing;
        this.entities = entities;
        this.autoSpawnSettings = autoSpawnSettings;
    }

    public IntervalSpawnElement getIntervalSpawnData() {
        if(getType().equalsIgnoreCase("INTERVAL")) {
            return BossesGson.get().fromJson(this.customData, IntervalSpawnElement.class);
        }

        return null;
    }

    public boolean isLocked() {
        return this.editing != null && this.editing;
    }


}
