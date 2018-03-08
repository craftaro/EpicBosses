package net.aminecraftdev.custombosses.entities;

import net.aminecraftdev.custombosses.entities.base.CustomEntity;
import net.aminecraftdev.custombosses.handlers.EntityHandler;
import net.aminecraftdev.custombosses.models.CustomEntityModel;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 06-Sep-17
 */
public class BossEntity extends CustomEntity {

    private List<Location> possibleSpawns = new ArrayList<>();
    private List<EntityHandler> minions = new ArrayList<>();
    private boolean randomWorldSpawns = false;

    public BossEntity(String identifier, CustomEntityModel customEntityModel) {
        super(identifier, customEntityModel);
    }


}
