package com.songoda.epicbosses.droptable;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.songoda.epicbosses.droptable.elements.DropTableElement;
import com.songoda.epicbosses.droptable.elements.GiveTableElement;
import com.songoda.epicbosses.droptable.elements.SprayTableElement;
import com.songoda.epicbosses.utils.BossesGson;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 25-Oct-18
 */
public class DropTable {

    @Expose
    private String dropType;
    @Expose
    private JsonObject rewards;

    public DropTable(String dropType, JsonObject rewards) {
        this.dropType = dropType;
        this.rewards = rewards;
    }

    public GiveTableElement getGiveTableData() {
        if(getDropType().equalsIgnoreCase("GIVE")) {
            return BossesGson.get().fromJson(this.rewards, GiveTableElement.class);
        }

        return null;
    }

    public SprayTableElement getSprayTableData() {
        if(getDropType().equalsIgnoreCase("SPRAY")) {
            return BossesGson.get().fromJson(this.rewards, SprayTableElement.class);
        }

        return null;
    }

    public DropTableElement getDropTableData() {
        if(getDropType().equalsIgnoreCase("DROP")) {
            return BossesGson.get().fromJson(this.rewards, DropTableElement.class);
        }

        return null;
    }

    public String getDropType() {
        return this.dropType;
    }

    public JsonObject getRewards() {
        return this.rewards;
    }

    public void setDropType(String dropType) {
        this.dropType = dropType;
    }

    public void setRewards(JsonObject rewards) {
        this.rewards = rewards;
    }
}
