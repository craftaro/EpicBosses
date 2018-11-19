package com.songoda.epicbosses.droptable;

import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 25-Oct-18
 */
public class DropTable {

    @Expose @Getter @Setter private String dropType;
    @Expose @Getter @Setter private JsonObject rewards;

    public DropTable(String dropType, JsonObject rewards) {
        this.dropType = dropType;
        this.rewards = rewards;
    }

}
