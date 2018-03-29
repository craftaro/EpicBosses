package net.aminecraftdev.custombosses.entities.base;

import net.aminecraftdev.custombosses.utils.IEditor;
import net.aminecraftdev.custombosses.utils.identifier.IIdentifier;
import net.aminecraftdev.custombosses.utils.identifier.Identifier;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 25-Mar-18
 */
public abstract class CustomEntity implements IIdentifier, IEditor {

    private final boolean canDropHand, canDropEquipment;
    private final Map<Integer, ItemStack> equipment;
    private final List<PotionEffect> potionEffects;
    private final List<Object> customSkills;
    private final List<String> description;
    private final Identifier<String> identifier;
    private final ItemStack mainHand, offHand;
    private final EntityType entityType;
    private final String displayName;
    private final double health;

    public CustomEntity(Identifier<String> identifier, List<PotionEffect> potionEffects, Map<Integer, ItemStack> equipment, List<Object> customSkills, boolean canDropHand, boolean canDropEquipment, List<String> description, ItemStack mainHand, ItemStack offHand, EntityType entityType, String displayName, double health) {
        this.identifier = identifier;

        this.entityType = entityType;
        this.health = health;
        this.displayName = displayName;
        this.mainHand = mainHand;
        this.offHand = offHand;
        this.description = description;
        this.customSkills = customSkills;
        this.potionEffects = potionEffects;
        this.equipment = equipment;
        this.canDropHand = canDropHand;
        this.canDropEquipment = canDropEquipment;
    }

    @Override
    public Identifier<String> getIdentifier() {
        return this.identifier;
    }
}
