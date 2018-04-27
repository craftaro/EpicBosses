package net.aminecraftdev.custombosses.entity.components;

import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 25-Apr-18
 */
public class StatsComponent {

    private final EditorComponent editorComponent;

    private List<String> description = new ArrayList<>();
    private EntityType entityType = null;
    private String displayName = null;
    private double maxHealth = 0.0;

    public StatsComponent(EditorComponent editorComponent) {
        this.editorComponent = editorComponent;
    }

    public boolean setEntityType(EntityType entityType) {
        if(!this.editorComponent.isEditable()) return false;

        this.entityType = entityType;
        return true;
    }

    public boolean hasEntityType() {
        return this.entityType != null;
    }

    public EntityType getEntityType() {
        return this.entityType;
    }

    public boolean setMaxHealth(double health) {
        if(!this.editorComponent.isEditable()) return false;

        this.maxHealth = health;
        return true;
    }

    public boolean hasMaxHealth() {
        return this.maxHealth != 0;
    }

    public double getMaxHealth() {
        return this.maxHealth;
    }

    public boolean setDisplayName(String displayName) {
        if(!this.editorComponent.isEditable()) return false;

        this.displayName = displayName;
        return true;
    }

    public boolean hasDisplayName() {
        return this.displayName != null;
    }

    public String getDisplayName() {
        return this.displayName;
    }

    public boolean setDescription(List<String> description) {
        if(!this.editorComponent.isEditable()) return false;

        this.description = description;
        return true;
    }

    public boolean hasDescription() {
        return !this.description.isEmpty();
    }

    public List<String> getDescription() {
        return this.description;
    }
}
