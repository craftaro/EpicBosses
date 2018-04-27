package net.aminecraftdev.custombosses.entity;

import net.aminecraftdev.custombosses.entity.components.*;
import net.aminecraftdev.custombosses.entity.interfaces.IBaseEntity;
import net.aminecraftdev.custombosses.utils.Debug;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.LivingEntity;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 25-Apr-18
 */
public class BaseEntity implements IBaseEntity {

    private final SpawnItemComponent spawnItemComponent;
    private final EquipmentComponent equipmentComponent;
    private final EditorComponent editorComponent;
    private final PotionComponent potionComponent;
    private final SkillComponent skillComponent;
    private final StatsComponent statsComponent;
    private final HandComponent handComponent;
    private final String identifier;

    public BaseEntity(String identifier, SpawnItemComponent spawnItemComponent, EquipmentComponent equipmentComponent, PotionComponent potionComponent,
                      SkillComponent skillComponent, StatsComponent statsComponent, HandComponent handComponent, EditorComponent editorComponent) {
        this.spawnItemComponent = spawnItemComponent;
        this.equipmentComponent = equipmentComponent;
        this.editorComponent = editorComponent;
        this.potionComponent = potionComponent;
        this.skillComponent = skillComponent;
        this.statsComponent = statsComponent;
        this.handComponent = handComponent;
        this.identifier = identifier;
    }


    @Override
    public EditorComponent getEditorComponent() {
        return editorComponent;
    }

    @Override
    public EquipmentComponent getEquipmentComponent() {
        return equipmentComponent;
    }

    @Override
    public HandComponent getHandComponent() {
        return handComponent;
    }

    @Override
    public PotionComponent getPotionComponent() {
        return potionComponent;
    }

    @Override
    public SkillComponent getSkillComponent() {
        return skillComponent;
    }

    @Override
    public SpawnItemComponent getSpawnItemComponent() {
        return spawnItemComponent;
    }

    @Override
    public StatsComponent getStatsComponent() {
        return statsComponent;
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public LivingEntity spawn(Location location) {
        if(!getEditorComponent().isEditable()) {
            Debug.ATTEMPTED_TO_SPAWN_WHILE_DISABLED.debug(getIdentifier());
            return null;
        }

        //
        //---------------------------
        //
        // BASIC STATS HANDLING
        //
        //---------------------------
        //

        if(!getStatsComponent().hasEntityType()) {
            Debug.NULL_ENTITY_TYPE.debug(getIdentifier());
            return null;
        }

        LivingEntity livingEntity = (LivingEntity) location.getWorld().spawn(location, getStatsComponent().getEntityType().getEntityClass());

        if(getStatsComponent().hasDisplayName()) {
            //TODO: Handle setting invisible armor stand names if set in config
            livingEntity.setCustomName(getStatsComponent().getDisplayName());
            livingEntity.setCustomNameVisible(true);
        }

        if(getStatsComponent().hasMaxHealth()) {
            livingEntity.setHealth(getStatsComponent().getMaxHealth());
        }

        livingEntity.setRemoveWhenFarAway(false);
        livingEntity.setCanPickupItems(false);

        //
        //---------------------------
        //
        // EQUIPMENT HANDLING
        //
        //---------------------------
        //

        boolean canDrop = getEquipmentComponent().canDropEquipment();

        if(getEquipmentComponent().hasHelmet()) {
            if(!canDrop) livingEntity.getEquipment().setHelmetDropChance(0F);

            livingEntity.getEquipment().setHelmet(getEquipmentComponent().getHelmet());
        }

        if(getEquipmentComponent().hasChestplate()) {
            if(!canDrop) livingEntity.getEquipment().setChestplateDropChance(0F);

            livingEntity.getEquipment().setChestplate(getEquipmentComponent().getChestplate());
        }

        if(getEquipmentComponent().hasLeggings()) {
            if(!canDrop) livingEntity.getEquipment().setLeggingsDropChance(0F);

            livingEntity.getEquipment().setLeggings(getEquipmentComponent().getLeggings());
        }

        if(getEquipmentComponent().hasBoots()) {
            if(!canDrop) livingEntity.getEquipment().setBootsDropChance(0F);

            livingEntity.getEquipment().setBoots(getEquipmentComponent().getBoots());
        }

        //
        //---------------------------
        //
        // HAND HANDLING
        //
        //---------------------------
        //

        //TODO: Can handle offhand ticket

        return null;
    }

    @Override
    public void killAll() {

    }

    @Override
    public void killAll(World world) {

    }
}
