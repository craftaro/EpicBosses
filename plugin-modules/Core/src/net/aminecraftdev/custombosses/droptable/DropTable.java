package net.aminecraftdev.custombosses.droptable;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import net.aminecraftdev.custombosses.droptable.elements.RewardsTableElement;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 25-Oct-18
 */
public class DropTable {

    @Expose @Getter @Setter private String dropType;
    @Expose @Getter @Setter private RewardsTableElement rewards;

}
