package net.aminecraftdev.custombosses.utils;

import net.aminecraftdev.custombosses.utils.itemstack.ItemStackUtils;
import org.bukkit.inventory.ItemStack;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 28-Apr-18
 */
public class ItemStackUtilsTest {

    private ItemStackUtils itemStackUtils;

    @Before
    public void init() {
        this.itemStackUtils = new ItemStackUtils();
    }

    @Test
    public void getItemStackId() {
        System.out.println("--------------------------------------------------------------");
        System.out.println("Testing 'getItemStackId'...");

        ItemStack itemStack = this.itemStackUtils.getItemStackByString("diamondhelmet");

        System.out.println("type > " + itemStack.getType());
        System.out.println("durability > " + itemStack.getDurability());
        System.out.println("--------------------------------------------------------------");
    }

}
