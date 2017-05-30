package net.aminecraftdev.custombosses.innerapi.inventory;

import net.aminecraftdev.custombosses.innerapi.factory.FactoryBuilder;
import net.aminecraftdev.custombosses.innerapi.itemstack.ItemStackUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by charl on 28-Apr-17.
 */
public class InventoryBuilder implements FactoryBuilder<Inventory>, IInventoryBuilder {

    private String name;
    private int slots;
    private ConfigurationSection items;
    private ConfigurationSection inventoryConfigSection;
    private Inventory inventory;
    private Set<Integer> _setOfItemSlots = new HashSet<>();
    private HashMap<String, String> _map = new HashMap<>();
    private Map<Integer, Integer> pageData = new HashMap<>();
    private JavaPlugin javaPlugin;

    public InventoryBuilder(ConfigurationSection inventory, JavaPlugin javaPlugin) {
        this.name = ChatColor.translateAlternateColorCodes('&', inventory.getString("name"));
        this.slots = inventory.getInt("slots");
        this.items = inventory.getConfigurationSection("Items");
        this.inventoryConfigSection = inventory;
        this.javaPlugin = javaPlugin;
    }

    @Override
    public Panel getInventory() {
        build();
        return new Panel(this.inventory, pageData, javaPlugin);
    }

    @Override
    public Inventory getClonedInventory() {
        Inventory inv = getInventory().getInventory();
        Inventory inventory = Bukkit.createInventory(inv.getHolder(), inv.getSize(), inv.getTitle());

        for(int i = 0; i < inv.getSize(); i++) {
            if(inv.getItem(i) == null || inv.getItem(i).getType() == Material.AIR) continue;

            inventory.setItem(i, inv.getItem(i));
        }

        return inventory;
    }

    @Override
    public void addReplacedMap(HashMap<String, String> map) {
        _map.putAll(map);
    }

    @Override
    public ConfigurationSection getConfigurationSection() {
        return inventoryConfigSection;
    }

    @Override
    public ConfigurationSection getItemConfigSection() {
        return items;
    }

    @Override
    public Set<Integer> getSetOfItemSlots() {
        return _setOfItemSlots;
    }

    @Override
    public Inventory build() {
        this.inventory = Bukkit.createInventory(null, this.slots, this.name);

        for(String s : this.items.getKeys(false)) {
            int slot = Integer.valueOf(s) - 1;
            ConfigurationSection section = this.items.getConfigurationSection(s);
            ItemStack stack = ItemStackUtils.createItemStack(section, 1, _map);
            if(section.contains("NextPage")) {
                boolean b = section.getBoolean("NextPage");
                if(b) {
                    pageData.put(slot, 1);
                }
            } else if(section.contains("PreviousPage")) {
                boolean b = section.getBoolean("PreviousPage");
                if(b) {
                    pageData.put(slot, -1);
                }
            }
            this.inventory.setItem(slot, stack);
            _setOfItemSlots.add(slot);
        }

        return this.inventory;
    }
}
