package com.songoda.epicbosses.skills.elements;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 05-Nov-18
 */
public class SubCommandSkillElement {

    @Expose @Getter private final String name;

    @Expose @Getter @Setter private Double chance;
    @Expose @Getter @Setter private List<String> commands;

    public SubCommandSkillElement(String name, Double chance, List<String> commands) {
        this.name = name;
        this.chance = chance;
        this.commands = commands;
    }

}
