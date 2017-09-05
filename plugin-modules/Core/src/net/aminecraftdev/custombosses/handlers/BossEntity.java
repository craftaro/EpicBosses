package net.aminecraftdev.custombosses.handlers;

import net.aminecraftdev.custombosses.models.CustomEntityModel;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.List;
import java.util.Map;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 06-Sep-17
 */
public class BossEntity extends CustomEntityModel {

    public BossEntity(EntityType entityType, double health, String identifier) {
        super(entityType, health, identifier);
    }

    public BossEntity(EntityType entityType, Map<Integer, ItemStack> equipment, List<String> description, List<PotionEffect> defaultPotionEffects, ItemStack itemStackInMainHand, ItemStack itemStackInOffHand, String identifier, double health, String name) {
        super(entityType, equipment, description, defaultPotionEffects, itemStackInMainHand, itemStackInOffHand, identifier, health, name);
    }

}
