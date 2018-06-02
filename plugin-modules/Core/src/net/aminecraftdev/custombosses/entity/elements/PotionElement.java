package net.aminecraftdev.custombosses.entity.elements;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 14-May-18
 */
public class PotionElement {

    @Expose @Getter @Setter private String type;
    @Expose @Getter @Setter private int leve;
    @Expose @Getter @Setter private long duration;

}
