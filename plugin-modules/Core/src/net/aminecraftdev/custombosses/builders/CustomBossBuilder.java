package net.aminecraftdev.custombosses.builders;

import net.aminecraftdev.custombosses.builders.base.CustomEntityBuilder;
import net.aminecraftdev.custombosses.builders.entity.ISpawnItemBuilderComponent;
import net.aminecraftdev.custombosses.entities.CustomBoss;
import org.bukkit.inventory.ItemStack;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 30-Mar-18
 */
public class CustomBossBuilder extends CustomEntityBuilder<CustomBoss> implements ISpawnItemBuilderComponent {

    private ItemStack spawnItem = null;

    public CustomBossBuilder(String identifier) {
        super(identifier);
    }

    @Override
    public void setSpawnItem(ItemStack itemStack) {
        this.spawnItem = itemStack;
    }

    @Override
    public CustomBoss build() {
        if(this.health <= 0) return null;

        return new CustomBoss(this.identifier, this.potionEffects, this.equipment, this.customSkills, this.canDropHand, this.canDropEquipment, this.description, this.mainHand, this.offHand, this.entityType, this.displayName, this.health, this.spawnItem);
    }

}
