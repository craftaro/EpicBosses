package com.songoda.epicbosses.autospawns;

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
//
//    public SkillMode getNext() {
//        return get(this.rank+1);
//    }
//
//    public static SpawnTypes getCurrent(String input) {
//
//    }
}
