package com.songoda.epicbosses.autospawns;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Jan-19
 */
public enum SpawnTypes {

    BLANK(0),
    INTERVAL(1);

    private int rank;

    SpawnTypes(int rank) {
        this.rank = rank;
    }

    public SpawnTypes getNext() {
        return get(this.rank+1);
    }

    public static SpawnTypes getCurrent(String input) {
        if(input == null || input.isEmpty()) return BLANK;

        for(SpawnTypes spawnTypes : values()) {
            if(spawnTypes.name().equalsIgnoreCase(input)) return spawnTypes;
        }

        return BLANK;
    }

    public static List<SpawnTypes> getSpawnTypes() {
        List<SpawnTypes> list = new ArrayList<>();

        for(SpawnTypes spawnTypes : values()) {
            if(spawnTypes.rank > 0) list.add(spawnTypes);
        }

        return list;
    }

    private static SpawnTypes get(int rank) {
        for(SpawnTypes spawnTypes : values()) {
            if(spawnTypes.rank == rank) {
                return spawnTypes;
            }
        }

        return INTERVAL;
    }
}
