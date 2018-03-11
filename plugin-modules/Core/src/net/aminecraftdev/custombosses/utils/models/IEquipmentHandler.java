package net.aminecraftdev.custombosses.utils.models;

import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 08-Mar-18
 */
public interface IEquipmentHandler {

    void setMainHand(ItemStack itemStack);

    ItemStack getMainHand();

    void setOffHand(ItemStack itemStack);

    ItemStack getOffHand();

    void setArmor(int slot, ItemStack itemStack);

    Map<Integer, ItemStack> getArmor();

    void setCanDropEquipment(boolean bool);

    boolean canDropEquipment();

}
