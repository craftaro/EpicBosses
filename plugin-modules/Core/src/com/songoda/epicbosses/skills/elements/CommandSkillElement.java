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
public class CommandSkillElement {

    @Expose @Getter @Setter private Double chance;
    @Expose @Getter @Setter private List<String> commands;

}
