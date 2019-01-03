package com.songoda.epicbosses.autospawns;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Jan-19
 */
public enum SpawnType {

    BLANK(0),
    INTERVAL(1);

    private int rank;

    SpawnType(int rank) {
        this.rank = rank;
    }

    public SpawnType getNext() {
        return get(this.rank+1);
    }

    public static SpawnType getCurrent(String input) {
        if(input == null || input.isEmpty()) return BLANK;

        for(SpawnType spawnTypes : values()) {
            if(spawnTypes.name().equalsIgnoreCase(input)) return spawnTypes;
        }

        return BLANK;
    }

    public static List<SpawnType> getSpawnTypes() {
        List<SpawnType> list = new ArrayList<>();

        for(SpawnType spawnTypes : values()) {
            if(spawnTypes.rank > 0) list.add(spawnTypes);
        }

        return list;
    }

    private static SpawnType get(int rank) {
        for(SpawnType spawnTypes : values()) {
            if(spawnTypes.rank == rank) {
                return spawnTypes;
            }
        }

        return INTERVAL;
    }
}
