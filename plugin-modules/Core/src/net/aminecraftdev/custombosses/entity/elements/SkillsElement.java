package net.aminecraftdev.custombosses.entity.elements;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 14-May-18
 */
public class SkillsElement {

    @Expose @Getter @Setter private Double overallChance;
    @Expose @Getter @Setter private String masterMessage;
    @Expose @Getter @Setter private List<String> skills;

}
