package net.aminecraftdev.custombosses.droptable.elements;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import net.aminecraftdev.custombosses.droptable.elements.RewardsTableElement;

import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 25-Oct-18
 */
public class DropTableElement extends RewardsTableElement {

    @Expose @Getter @Setter private Map<String, Double> dropRewards;
    @Expose @Getter @Setter private Integer dropMaxDrops;

}
