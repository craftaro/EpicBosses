package net.aminecraftdev.custombosses.builders.base;

import net.aminecraftdev.custombosses.builders.entity.*;
import net.aminecraftdev.custombosses.utils.IBuilder;
import net.aminecraftdev.custombosses.utils.identifier.IIdentifier;
import net.aminecraftdev.custombosses.utils.identifier.Identifier;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 25-Mar-18
 */
public abstract class CustomEntityBuilder<T> implements IIdentifier, IBuilder<T>, IStatsBuilderComponent, IEquipmentBuilderComponent, IHandBuilderComponent, IPotionBuilderComponent, ISkillBuilderComponent {

    protected final List<PotionEffect> potionEffects = new ArrayList<>();
    protected final Map<Integer, ItemStack> equipment = new HashMap<>();
    protected final List<Object> customSkills = new ArrayList<>();
    protected final Identifier<String> identifier;

    protected boolean canDropHand = true, canDropEquipment = true;
    protected List<String> description = new ArrayList<>();
    protected ItemStack mainHand = null, offHand = null;
    protected EntityType entityType = null;
    protected String displayName = null;
    protected double health = 0;

    public CustomEntityBuilder(String identifier) {
        this.identifier = new Identifier<>(identifier);
    }

    @Override
    public Identifier<String> getIdentifier() {
        return this.identifier;
    }

    @Override
    public void setArmor(int slot, ItemStack itemStack) {
        if(slot > 4 || slot < 1) return;

        this.equipment.put(slot, itemStack);
    }

    @Override
    public void setCanDropEquipment(boolean bool) {
        this.canDropEquipment = bool;
    }

    @Override
    public void setMainHand(ItemStack itemStack) {
        this.mainHand = itemStack;
    }

    @Override
    public void setOffHand(ItemStack itemStack) {
        this.offHand = itemStack;
    }

    @Override
    public void setCanDropHands(boolean bool) {
        this.canDropHand = bool;
    }

    @Override
    public void addPotionEffect(PotionEffect potionEffect) {
        this.potionEffects.add(potionEffect);
    }

    @Override
    public void removePotionEffect(PotionEffect potionEffect) {
        this.potionEffects.remove(potionEffect);
    }

    @Override
    public void addSkill(Object object) {
        this.customSkills.add(object);
    }

    @Override
    public void removeSkill(Object object) {
        this.customSkills.remove(object);
    }

    @Override
    public void setEntityType(EntityType entityType) {
        this.entityType = entityType;
    }

    @Override
    public void setMaxHealth(double health) {
        this.health = health;
    }

    @Override
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    @Override
    public void setDescription(List<String> description) {
        this.description = description;
    }
}
