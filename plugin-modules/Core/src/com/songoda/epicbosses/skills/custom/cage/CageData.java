package com.songoda.epicbosses.skills.custom.cage;

import lombok.Getter;
import org.bukkit.block.BlockState;

import java.util.HashMap;
import java.util.Map;
import java.util.Queue;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 10-Nov-18
 */
public class CageData {

    @Getter private Map<String, Queue<BlockState>> mapOfCages = new HashMap<>(), mapOfRestoreCages = new HashMap<>();



}
