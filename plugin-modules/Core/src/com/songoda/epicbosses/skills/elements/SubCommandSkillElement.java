package com.songoda.epicbosses.skills.elements;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 05-Nov-18
 */
public class SubCommandSkillElement {

    @Expose
    private final String name;

    @Expose
    private Double chance;
    @Expose
    private List<String> commands;

    public SubCommandSkillElement(String name, Double chance, List<String> commands) {
        this.name = name;
        this.chance = chance;
        this.commands = commands;
    }

    public String getName() {
        return this.name;
    }

    public Double getChance() {
        return this.chance;
    }

    public void setChance(Double chance) {
        this.chance = chance;
    }

    public List<String> getCommands() {
        return this.commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }
}
