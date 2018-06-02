package net.aminecraftdev.custombosses.entity.elements;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 14-May-18
 */
public class CommandsElement {

    @Expose @Getter @Setter private String onSpawn, onDeath;

    public CommandsElement(String onSpawn, String onDeath) {
        this.onSpawn = onSpawn;
        this.onDeath = onDeath;
    }
}
