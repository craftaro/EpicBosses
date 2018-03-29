package net.aminecraftdev.custombosses.entities;

import net.aminecraftdev.custombosses.entities.base.CustomEntity;
import net.aminecraftdev.custombosses.utils.identifier.Identifier;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 30-Mar-18
 */
public class CustomBoss extends CustomEntity {

    private ItemStack spawnItemStack;
    private boolean isEditing;

    public CustomBoss(Identifier<String> identifier, List<PotionEffect> potionEffects, Map<Integer, ItemStack> equipment, List<Object> customSkills, boolean canDropHand, boolean canDropEquipment, List<String> description, ItemStack mainHand, ItemStack offHand, EntityType entityType, String displayName, double health, ItemStack spawnItem) {
        super(identifier, potionEffects, equipment, customSkills, canDropHand, canDropEquipment, description, mainHand, offHand, entityType, displayName, health);

        this.isEditing = true;
    }

    @Override
    public void toggleEditing(boolean bool) {
        this.isEditing = bool;
    }

    @Override
    public void openEditor(Player player) {

    }
}
