package com.songoda.epicbosses.droptable;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import com.songoda.epicbosses.droptable.elements.RewardsTableElement;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 25-Oct-18
 */
public class DropTable {

    @Expose @Getter @Setter private String dropType;
    @Expose @Getter @Setter private RewardsTableElement rewards;

}
