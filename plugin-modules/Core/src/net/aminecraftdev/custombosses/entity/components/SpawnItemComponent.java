package net.aminecraftdev.custombosses.entity.components;

import org.bukkit.inventory.ItemStack;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 24-Apr-18
 */
public class SpawnItemComponent {

    private final EditorComponent editorComponent;

    private ItemStack spawnItem = null;

    public SpawnItemComponent(EditorComponent editorComponent) {
        this.editorComponent = editorComponent;
    }

    public boolean setSpawnItem(ItemStack itemStack) {
        if(!this.editorComponent.isEditable()) return false;

        this.spawnItem = itemStack;
        return true;
    }

    public ItemStack getSpawnItem() {
        return this.spawnItem;
    }
}
