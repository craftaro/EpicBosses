package net.aminecraftdev.custombosses.innerapi.inventory;

import net.aminecraftdev.custombosses.innerapi.factory.CloneableFactory;
import net.aminecraftdev.custombosses.innerapi.factory.Factory;
import org.bukkit.Bukkit;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by charl on 07-May-17.
 */
public class InventoryFactory extends Factory<Panel> implements CloneableFactory<InventoryFactory> {

    private Map<Integer, ItemStack> mapOfItems = new HashMap<>();
    private JavaPlugin javaPlugin;
    private InventoryHolder inventoryHolder;
    private String name;
    private int slot;

    public InventoryFactory(String name, int slot, JavaPlugin javaPlugin) {
        setName(name);
        setSlots(slot);
        setHolder(null);

        this.javaPlugin = javaPlugin;
    }

    public InventoryFactory setSlots(int amount) {
        this.slot = amount;
        return this;
    }

    public InventoryFactory setName(String name) {
        this.name = name;
        return this;
    }

    public InventoryFactory setHolder(InventoryHolder inventoryHolder) {
        this.inventoryHolder = inventoryHolder;
        return this;
    }

    public InventoryFactory addItem(ItemStack... itemStacks) {
        for(ItemStack itemStack : itemStacks) {
            for(int i = 0; i < this.slot; i++) {
                if(mapOfItems.containsKey(i)) continue;

                mapOfItems.put(i, itemStack);
            }
        }

        return this;
    }

    public InventoryFactory setItem(ItemStack itemStack, int slot) {
        mapOfItems.put(slot, itemStack);
        return this;
    }

    @Override
    public Panel build() {
        Inventory inventory = Bukkit.createInventory(this.inventoryHolder, this.slot, this.name);

        for(int i : this.mapOfItems.keySet()) {
            inventory.setItem(i, this.mapOfItems.get(i));
        }

        return new Panel(inventory, javaPlugin);
    }

    @Override
    public InventoryFactory clone() {
        InventoryFactory clone = new InventoryFactory(this.name, this.slot, javaPlugin);
        clone.inventoryHolder = this.inventoryHolder;
        clone.mapOfItems.putAll(this.mapOfItems);

        return clone;
    }
}
