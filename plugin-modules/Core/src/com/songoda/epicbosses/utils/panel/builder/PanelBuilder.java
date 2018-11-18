package com.songoda.epicbosses.utils.panel.builder;

import lombok.Getter;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author AMinecraftDev
 * @version 2.0.0
 * @since 18-Jul-2018
 */
public class PanelBuilder {

    private final Map<String, String> replaceMap = new HashMap<>();
    private final Set<Integer> defaultSlots = new HashSet<>();
    private final ConfigurationSection configurationSection;
    private final PanelBuilderSettings panelBuilderSettings;

    @Getter private PanelBuilderCounter panelBuilderCounter;

    private Inventory inventory;
    private int size = 0;

    public PanelBuilder(ConfigurationSection configurationSection) {
        this(configurationSection, null);
    }

    public PanelBuilder(ConfigurationSection configurationSection, Map<String, String> replaceMap) {
        this.panelBuilderSettings = new PanelBuilderSettings(configurationSection);
        this.panelBuilderCounter = new PanelBuilderCounter();
        this.configurationSection = configurationSection;

        if(replaceMap != null) this.replaceMap.putAll(replaceMap);
    }

    public PanelBuilder setSize(int size) {
        this.size = size;
        return this;
    }

    public PanelBuilder addReplaceData(Map<String, String> replaceMap) {
        if(replaceMap != null) this.replaceMap.putAll(replaceMap);
        return this;
    }

    public boolean isDefaultSlot(int slot) {
        return defaultSlots.contains(slot);
    }

    public PanelBuilder cloneBuilder() {
        PanelBuilder panelBuilder = new PanelBuilder(this.configurationSection, this.replaceMap);

        panelBuilder.inventory = this.inventory;
        panelBuilder.size = this.size;
        panelBuilder.defaultSlots.addAll(this.defaultSlots);
        panelBuilder.panelBuilderCounter = getPanelBuilderCounter().cloneCounter();

        return panelBuilder;
    }

    public Panel getPanel() {
        build();

        Panel panel = new Panel(this.inventory, this.panelBuilderSettings, this.panelBuilderCounter);

        Map<String, ItemStack> itemStackMap = this.panelBuilderCounter.getItemStacks();
        Map<String, ClickAction> clickActionMap = this.panelBuilderCounter.getClickActions();

        this.panelBuilderCounter.getSlotsWithCounter().forEach((identifier, slotsWith) -> {
            if(itemStackMap.containsKey(identifier)) {
                slotsWith.forEach(slot -> panel.setItem(slot, itemStackMap.get(identifier)));
            }
            if(clickActionMap.containsKey(identifier)) {
                slotsWith.forEach(slot -> panel.setOnClick(slot, clickActionMap.get(identifier)));
            }
        });

        return panel;
    }

    private void build() {
        String name = configurationSection.contains("name")? StringUtils.get().translateColor(configurationSection.getString("name")) : "?!? naming convention error ?!?";
        int slots = this.size != 0? this.size : configurationSection.contains("slots")? configurationSection.getInt("slots") : 9;
        ConfigurationSection itemSection = configurationSection.contains("Items")? configurationSection.getConfigurationSection("Items") : null;

        name = replace(name);
        this.inventory = Bukkit.createInventory(null, slots, name);

        if(itemSection != null) {
            Map<String, Set<Integer>> slotsWith = this.panelBuilderCounter.getSlotsWithCounter();
            Map<String, Map<Integer, Object>> specialSlotsWith = this.panelBuilderCounter.getSpecialValuesCounter();

            for(String s : itemSection.getKeys(false)) {
                int slot = NumberUtils.get().isInt(s)? Integer.valueOf(s) - 1 : 0;
                ConfigurationSection innerSection = itemSection.getConfigurationSection(s);

                if(innerSection.contains("NextPage") && innerSection.getBoolean("NextPage")) this.panelBuilderCounter.addPageData(slot, 1);
                if(innerSection.contains("PreviousPage") && innerSection.getBoolean("PreviousPage")) this.panelBuilderCounter.addPageData(slot, -1);

                if(innerSection.contains("Button") && slotsWith.containsKey(innerSection.getString("Button"))) {
                    String identifier = innerSection.getString("Button");
                    Set<Integer> current = slotsWith.get(identifier);

                    current.add(slot);
                    this.panelBuilderCounter.getSlotsWithCounter().put(identifier, current);
                }

                for(String identifier : specialSlotsWith.keySet()) {
                    if(innerSection.contains(identifier)) {
                        Map<Integer, Object> current = specialSlotsWith.get(identifier);

                        current.put(slot, innerSection.get(identifier));
                        this.panelBuilderCounter.getSpecialValuesCounter().put(identifier, current);
                    }
                }

                if(slot > inventory.getSize() - 1) continue;

                this.defaultSlots.add(slot);

                if(innerSection.contains("Item")) innerSection = innerSection.getConfigurationSection("Item");
                if(!innerSection.contains("type")) continue;

                this.inventory.setItem(slot, ItemStackUtils.createItemStack(innerSection, 1, replaceMap));
            }
        }
    }

    private String replace(String input) {
        for(Map.Entry<String, String> entry : replaceMap.entrySet()) {
            if(input.contains(entry.getKey())) {
                input = input.replace(entry.getKey(), entry.getValue());
            }
        }

        return input;
    }
}
