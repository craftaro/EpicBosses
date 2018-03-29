package net.aminecraftdev.custombosses.builders.entity;

import org.bukkit.potion.PotionEffect;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 25-Mar-18
 */
public interface IPotionBuilderComponent {

    void addPotionEffect(PotionEffect potionEffect);

    void removePotionEffect(PotionEffect potionEffect);

}
