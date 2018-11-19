package com.songoda.epicbosses.skills.elements;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 13-Nov-18
 */
public class CustomMinionSkillElement {

    @Expose @Getter @Setter private String minionToSpawn;
    @Expose @Getter @Setter private Integer amount;

    public CustomMinionSkillElement(Integer amount, String minionToSpawn) {
        this.amount = amount;
        this.minionToSpawn = minionToSpawn;
    }

}
