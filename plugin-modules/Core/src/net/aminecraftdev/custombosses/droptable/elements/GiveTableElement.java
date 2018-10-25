package net.aminecraftdev.custombosses.droptable.elements;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import net.aminecraftdev.custombosses.droptable.elements.GiveTableSubElement;
import net.aminecraftdev.custombosses.droptable.elements.RewardsTableElement;

import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 25-Oct-18
 */
public class GiveTableElement extends RewardsTableElement {

    @Expose @Getter @Setter private Map<String, GiveTableSubElement> giveRewards;

}
