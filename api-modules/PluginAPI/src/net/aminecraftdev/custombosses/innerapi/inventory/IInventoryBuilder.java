package net.aminecraftdev.custombosses.innerapi.inventory;

import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;

import java.util.HashMap;
import java.util.Set;

/**
 * Created by charl on 28-Apr-17.
 */
public interface IInventoryBuilder {

    Panel getInventory();

    Inventory getClonedInventory();

    void addReplacedMap(HashMap<String, String> map);

    ConfigurationSection getConfigurationSection();

    ConfigurationSection getItemConfigSection();

    Set<Integer> getSetOfItemSlots();

}
