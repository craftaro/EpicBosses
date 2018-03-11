package net.aminecraftdev.custombosses.entities.base;

import net.aminecraftdev.custombosses.handlers.EntityHandler;
import net.aminecraftdev.custombosses.utils.IIdentifier;
import net.aminecraftdev.custombosses.models.CustomEntityModel;
import net.aminecraftdev.custombosses.skills.CustomSkill;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 08-Mar-18
 */
public class CustomEntity implements IIdentifier {

    private List<CustomSkill> customSkills = new ArrayList<>();
    private CustomEntityModel customEntityModel;
    private String identifier;

    public CustomEntity(String identifier, CustomEntityModel customEntityModel) {
        this.identifier = identifier;
        this.customEntityModel = customEntityModel;
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    public List<CustomSkill> getCustomSkills() {
        return this.customSkills;
    }

    public EntityHandler spawn(Location location) {
        LivingEntity livingEntity = (LivingEntity) location.getWorld().spawn(location, this.customEntityModel.getEntityType().getEntityClass());
        EntityEquipment entityEquipment = livingEntity.getEquipment();

        /* SETTING HEALTH */
        livingEntity.setMaxHealth(this.customEntityModel.getMaxHealth());
        livingEntity.setHealth(this.customEntityModel.getMaxHealth());

        /* SETTING DISPLAY NAME */
        if(this.customEntityModel.getDisplayName() != null) {
            livingEntity.setCustomName(this.customEntityModel.getDisplayName());
            livingEntity.setCustomNameVisible(true);
        }

        /* SETTING MAIN HAND */
        if(this.customEntityModel.getMainHand() != null) {
            entityEquipment.setItemInMainHand(this.customEntityModel.getMainHand());

            if(cannotDrop()) entityEquipment.setItemInMainHandDropChance(0);
        }

        /* SETTING OFF HAND */
        if(this.customEntityModel.getOffHand() != null) {
            entityEquipment.setItemInOffHand(this.customEntityModel.getOffHand());

            if(cannotDrop()) entityEquipment.setItemInOffHandDropChance(0);
        }

        /* SETTING ARMOUR */
        if(!this.customEntityModel.getArmor().isEmpty()) {
            Map<Integer, ItemStack> equipment = this.customEntityModel.getArmor();

            if(equipment.getOrDefault(0, null) != null) {
                entityEquipment.setHelmet(equipment.get(0));

                if(cannotDrop()) entityEquipment.setHelmetDropChance(0);
            }

            if(equipment.getOrDefault(1, null) != null) {
                entityEquipment.setChestplate(equipment.get(1));

                if(cannotDrop()) entityEquipment.setChestplateDropChance(0);
            }

            if(equipment.getOrDefault(2, null) != null) {
                entityEquipment.setLeggings(equipment.get(2));

                if(cannotDrop()) entityEquipment.setLeggingsDropChance(0);
            }

            if(equipment.getOrDefault(3, null) != null) {
                entityEquipment.setBoots(equipment.get(3));

                if(cannotDrop()) entityEquipment.setBootsDropChance(0);
            }
        }

        /*  SETTINGS POTIONS */
        if(!this.customEntityModel.getPotionEffects().isEmpty()) {
            livingEntity.addPotionEffects(this.customEntityModel.getPotionEffects());
        }

        return new EntityHandler(livingEntity, this);
    }

    private boolean cannotDrop() {
        return !this.customEntityModel.canDropEquipment();
    }
}
