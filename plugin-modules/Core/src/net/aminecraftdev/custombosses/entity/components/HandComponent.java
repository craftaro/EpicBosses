package net.aminecraftdev.custombosses.entity.components;

import org.bukkit.inventory.ItemStack;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 24-Apr-18
 */
public class HandComponent {

    private final EditorComponent editorComponent;

    private ItemStack mainHand = null, offHand = null;
    private boolean dropHands = false;

    public HandComponent(EditorComponent editorComponent) {
        this.editorComponent = editorComponent;
    }

    public boolean setMainHand(ItemStack itemStack) {
        if(!this.editorComponent.isEditable()) return false;

        this.mainHand = itemStack;
        return true;
    }

    public ItemStack getMainHand() {
        return this.mainHand;
    }

    public boolean setOffHand(ItemStack itemStack) {
        if(!this.editorComponent.isEditable()) return false;

        this.offHand = itemStack;
        return true;
    }

    public ItemStack getOffHand() {
        return this.offHand;
    }

    public boolean setCanDropHands(boolean bool) {
        if(!this.editorComponent.isEditable()) return false;

        this.dropHands = bool;
        return true;
    }

    public boolean canDropHands() {
        return this.dropHands;
    }
}
