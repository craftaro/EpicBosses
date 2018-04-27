package net.aminecraftdev.custombosses.entity.components;

import org.bukkit.potion.PotionEffect;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 24-Apr-18
 */
public class PotionComponent {

    private final EditorComponent editorComponent;

    private List<PotionEffect> potionEffects = new ArrayList<>();

    public PotionComponent(EditorComponent editorComponent) {
        this.editorComponent = editorComponent;
    }

    public boolean addPotionEffect(PotionEffect potionEffect) {
        if(!this.editorComponent.isEditable()) return false;

        this.potionEffects.add(potionEffect);
        return true;
    }

    public List<PotionEffect> getPotionEffects() {
        return this.potionEffects;
    }

    public boolean removePotionEffect(PotionEffect potionEffect) {
        if(!this.editorComponent.isEditable()) return false;

        this.potionEffects.remove(potionEffect);
        return true;
    }
}
