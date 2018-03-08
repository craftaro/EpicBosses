package net.aminecraftdev.custombosses.handlers.models;

import org.bukkit.potion.PotionEffect;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 08-Mar-18
 */
public interface IPotionHandler {

    void addPotionEffect(PotionEffect potionEffect);

    List<PotionEffect> getPotionEffects();

}
