package com.songoda.epicbosses.skills;

import java.util.ArrayList;
import java.util.List;

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

    public static SkillMode getCurrent(String input) {
        if (input == null || input.isEmpty()) return BLANK;

        for (SkillMode skillMode : values()) {
            if (skillMode.name().equalsIgnoreCase(input)) return skillMode;
        }

        return BLANK;
    }

    public static List<SkillMode> getSkillModes() {
        List<SkillMode> list = new ArrayList<>();

        for (SkillMode skillMode : values()) {
            if (skillMode.rank > 0) list.add(skillMode);
        }

        return list;
    }

    private static SkillMode get(int rank) {
        for (SkillMode skillMode : values()) {
            if (skillMode.rank == rank) {
                return skillMode;
            }
        }

        return ALL;
    }

    public SkillMode getNext() {
        return get(this.rank + 1);
    }

}
