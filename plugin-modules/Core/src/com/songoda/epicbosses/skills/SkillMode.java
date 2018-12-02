package com.songoda.epicbosses.skills;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Dec-18
 */
public enum SkillMode {

    BLANK(0),
    ALL(1),
    RANDOM(2),
    ONE(3),
    BOSS(4);

    private int rank;

    SkillMode(int rank) {
        this.rank = rank;
    }

    public SkillMode getNext() {
        return get(this.rank+1);
    }

    public static SkillMode getCurrent(String input) {
        if(input == null || input.isEmpty()) return BLANK;

        for(SkillMode skillMode : values()) {
            if(skillMode.name().equalsIgnoreCase(input)) return skillMode;
        }

        return BLANK;
    }

    private static SkillMode get(int rank) {
        for(SkillMode skillMode : values()) {
            if(skillMode.rank == rank) {
                return skillMode;
            }
        }

        return ALL;
    }

}
