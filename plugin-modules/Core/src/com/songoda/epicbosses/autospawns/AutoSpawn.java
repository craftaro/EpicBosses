package com.songoda.epicbosses.autospawns;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.songoda.epicbosses.autospawns.settings.AutoSpawnSettings;
import com.songoda.epicbosses.autospawns.types.IntervalSpawnElement;
import com.songoda.epicbosses.utils.BossesGson;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Jan-19
 */
public class AutoSpawn {

    @Expose
    private boolean editing;
    @Expose
    private String type;
    @Expose
    private List<String> entities;
    @Expose
    private AutoSpawnSettings autoSpawnSettings;
    @Expose
    private JsonObject customData;

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

    public boolean isCompleteEnoughToSpawn() {
        if(this.type == null) return false;

        List<String> entities = getEntities();

        if(entities == null || entities.isEmpty()) return false;

        return true;
    }

    public boolean isEditing() {
        return this.editing;
    }

    public String getType() {
        return this.type;
    }

    public List<String> getEntities() {
        return this.entities;
    }

    public AutoSpawnSettings getAutoSpawnSettings() {
        return this.autoSpawnSettings;
    }

    public JsonObject getCustomData() {
        return this.customData;
    }

    public void setEditing(boolean editing) {
        this.editing = editing;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setEntities(List<String> entities) {
        this.entities = entities;
    }

    public void setAutoSpawnSettings(AutoSpawnSettings autoSpawnSettings) {
        this.autoSpawnSettings = autoSpawnSettings;
    }

    public void setCustomData(JsonObject customData) {
        this.customData = customData;
    }
}
