package net.aminecraftdev.custombosses.models;

import net.aminecraftdev.custombosses.handlers.IIdentifier;
import net.aminecraftdev.custombosses.handlers.ValidationHandler;
import net.aminecraftdev.custombosses.handlers.models.IEquipmentHandler;
import net.aminecraftdev.custombosses.handlers.models.IPotionHandler;
import net.aminecraftdev.custombosses.handlers.models.ISkillHandler;
import net.aminecraftdev.custombosses.handlers.models.IStatsHandler;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.*;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 06-Sep-17
 *
 * TODO: Make it load messages via a messages.json file
 * TODO: Make it load all custom items via. a items.json file
 */
public class CustomEntityModel implements IIdentifier, IStatsHandler, IEquipmentHandler, ISkillHandler, IPotionHandler {

    private List<PotionEffect> defaultPotionEffects = new ArrayList<>();
    private Map<Integer, ItemStack> equipment = new HashMap<>();
    private List<CustomSkillModel> skillModels = new ArrayList<>();
    private List<String> description = new ArrayList<>();

    private String identifier, displayName;
    private ItemStack mainHand, offHand;
    private boolean canDropEquipment;
    private EntityType entityType;
    private double health;

    public CustomEntityModel(EntityType entityType, double health, String identifier) {
        this(entityType, new HashMap<>(), null, new ArrayList<>(), null, null, identifier, health, null, false);
    }

    public CustomEntityModel(EntityType entityType, Map<Integer, ItemStack> equipment, List<String> description, List<PotionEffect> defaultPotionEffects, ItemStack itemStackInMainHand, ItemStack itemStackInOffHand, String identifier, double health, String name, boolean canDropEquipment) {
        this.identifier = identifier;

        try {
            setEntityType(entityType);
            setDescription(description);
            setMainHand(itemStackInMainHand);
            setOffHand(itemStackInOffHand);
            setMaxHealth(health);
            setDisplayName(name);
            setCanDropEquipment(canDropEquipment);

            defaultPotionEffects.forEach(this::addPotionEffect);
            equipment.forEach(this::setArmor);
        } catch (IllegalArgumentException ex) {}
    }

    @Override
    public void setMainHand(ItemStack itemStack) {
        if(ValidationHandler.isNull(itemStack)) return;

        this.mainHand = itemStack;
    }

    @Override
    public ItemStack getMainHand() {
        return this.mainHand;
    }

    @Override
    public void setOffHand(ItemStack itemStack) {
        if(ValidationHandler.isNull(itemStack)) return;

        this.offHand = itemStack;
    }

    @Override
    public ItemStack getOffHand() {
        return this.offHand;
    }

    @Override
    public void setArmor(int slot, ItemStack itemStack) {
        if(ValidationHandler.isNull(itemStack)) return;

        this.equipment.put(slot, itemStack);
    }

    @Override
    public Map<Integer, ItemStack> getArmor() {
        return this.equipment;
    }

    @Override
    public void setCanDropEquipment(boolean bool) {
        this.canDropEquipment = bool;
    }

    @Override
    public boolean canDropEquipment() {
        return this.canDropEquipment;
    }

    @Override
    public void addPotionEffect(PotionEffect potionEffect) {
        if(ValidationHandler.isNull(potionEffect)) return;

        this.defaultPotionEffects.add(potionEffect);
    }

    @Override
    public List<PotionEffect> getPotionEffects() {
        return this.defaultPotionEffects;
    }

    @Override
    public void addSkill(CustomSkillModel skillModel) {
        if(ValidationHandler.isNull(skillModel)) return;

        this.skillModels.add(skillModel);
    }

    @Override
    public List<CustomSkillModel> getSkills() {
        return this.skillModels;
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    @Override
    public void setEntityType(EntityType entityType) {
        if(ValidationHandler.isNull(entityType)) return;

        this.entityType = entityType;
    }

    @Override
    public EntityType getEntityType() {
        return this.entityType;
    }

    @Override
    public void setMaxHealth(double health) {
        this.health = health;
    }

    @Override
    public double getMaxHealth() {
        return this.health;
    }

    @Override
    public void setDisplayName(String displayName) {
        if(ValidationHandler.isNull(displayName)) return;

        this.displayName = displayName;
    }

    @Override
    public String getDisplayName() {
        return this.displayName;
    }

    @Override
    public void setDescription(List<String> description) {
        if(ValidationHandler.isNull(description)) return;

        this.description = description;
    }

    @Override
    public List<String> getDescription() {
        return this.description;
    }
}
