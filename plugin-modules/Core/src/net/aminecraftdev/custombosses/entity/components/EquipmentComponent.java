package net.aminecraftdev.custombosses.entity.components;

import org.bukkit.inventory.ItemStack;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 24-Apr-18
 */
public class EquipmentComponent {

    private final EditorComponent editorComponent;

    private ItemStack helmet = null, chestplate = null, leggings = null, boots = null;
    private boolean dropEquipment = false;

    public EquipmentComponent(EditorComponent editorComponent) {
        this.editorComponent = editorComponent;
    }

    public boolean setHelmet(ItemStack itemStack) {
        if(!this.editorComponent.isEditable()) return false;

        this.helmet = itemStack;
        return true;
    }

    public boolean hasHelmet() {
        return this.helmet != null;
    }

    public ItemStack getHelmet() {
        return this.helmet;
    }

    public boolean setChestplate(ItemStack itemStack) {
        if(!this.editorComponent.isEditable()) return false;

        this.chestplate = itemStack;
        return true;
    }

    public boolean hasChestplate() {
        return this.chestplate != null;
    }

    public ItemStack getChestplate() {
        return this.chestplate;
    }

    public boolean setLeggings(ItemStack itemStack) {
        if(!this.editorComponent.isEditable()) return false;

        this.leggings = itemStack;
        return true;
    }

    public boolean hasLeggings() {
        return this.leggings != null;
    }

    public ItemStack getLeggings() {
        return this.leggings;
    }

    public boolean setBoots(ItemStack itemStack) {
        if(!this.editorComponent.isEditable()) return false;

        this.boots = itemStack;
        return true;
    }

    public boolean hasBoots() {
        return this.boots != null;
    }

    public ItemStack getBoots() {
        return this.boots;
    }

    public boolean setCanDropEquipment(boolean bool) {
        if(!this.editorComponent.isEditable()) return false;

        this.dropEquipment = bool;
        return true;
    }

    public boolean canDropEquipment() {
        return this.dropEquipment;
    }
}
