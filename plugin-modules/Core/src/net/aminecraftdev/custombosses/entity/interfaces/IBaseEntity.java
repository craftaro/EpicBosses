package net.aminecraftdev.custombosses.entity.interfaces;

import net.aminecraftdev.custombosses.entity.components.*;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 25-Apr-18
 */
public interface IBaseEntity {

    //BASE ENTITY COMPONENTS
    EditorComponent getEditorComponent();

    EquipmentComponent getEquipmentComponent();

    HandComponent getHandComponent();

    PotionComponent getPotionComponent();

    SkillComponent getSkillComponent();

    SpawnItemComponent getSpawnItemComponent();

    StatsComponent getStatsComponent();

    //BASE ENTITY FUNCTIONS
    String getIdentifier();

    LivingEntity spawn(Location location);

    void killAll();

    void killAll(World world);

}
