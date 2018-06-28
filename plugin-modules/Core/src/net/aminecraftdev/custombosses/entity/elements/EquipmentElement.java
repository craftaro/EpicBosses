package net.aminecraftdev.custombosses.entity.elements;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 14-May-18
 */
public class EquipmentElement {

    @Expose @Getter @Setter private String helmet, chestplate, leggings, boots;
    
}
