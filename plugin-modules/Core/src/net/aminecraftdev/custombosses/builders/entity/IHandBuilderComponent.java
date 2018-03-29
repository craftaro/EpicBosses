package net.aminecraftdev.custombosses.builders.entity;

import org.bukkit.inventory.ItemStack;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 25-Mar-18
 */
public interface IHandBuilderComponent {

    void setMainHand(ItemStack itemStack);

    void setOffHand(ItemStack itemStack);

    void setCanDropHands(boolean bool);

}
