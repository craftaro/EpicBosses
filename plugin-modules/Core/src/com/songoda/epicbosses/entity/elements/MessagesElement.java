package com.songoda.epicbosses.entity.elements;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 14-May-18
 */
public class MessagesElement {

    @Expose @Getter @Setter private OnSpawnMessageElement onSpawn;
    @Expose @Getter @Setter private OnDeathMessageElement onDeath;
    @Expose @Getter @Setter private TauntElement taunts;

}
