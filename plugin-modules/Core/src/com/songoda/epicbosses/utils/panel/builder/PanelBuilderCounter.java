package com.songoda.epicbosses.utils.panel.builder;

import com.songoda.epicbosses.utils.panel.base.ClickAction;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 18-Jul-18
 */
public class PanelBuilderCounter {

    private final Map<String, Map<Integer, Object>> specialValuesCounter = new HashMap<>();
    private final Map<String, Set<Integer>> slotsWithCounter = new HashMap<>();
    private final Map<String, ClickAction> clickActions = new HashMap<>();
    private final Map<Integer, String> buttonCounters = new HashMap<>();
    private final Map<String, ItemStack> itemStacks = new HashMap<>();
    private final Map<Integer, Integer> pageData = new HashMap<>();

    public boolean isButtonAtSlot(int slot) {
        for (Set<Integer> integers : this.slotsWithCounter.values()) {
            if (integers.contains(slot)) return true;
        }

        for (Map<Integer, Object> map : this.specialValuesCounter.values()) {
            if (map.containsKey(slot)) return true;
        }

        return false;
    }

    public PanelBuilderCounter addSpecialCounter(String identifier) {
        this.specialValuesCounter.put(identifier, new HashMap<>());

        return this;
    }

    public PanelBuilderCounter addSpecialCounter(String identifier, ClickAction clickAction) {
        this.specialValuesCounter.put(identifier, new HashMap<>());
        this.clickActions.put(identifier, clickAction);

        return this;
    }

    public PanelBuilderCounter addSlotCounter(String identifier) {
        this.slotsWithCounter.put(identifier, new HashSet<>());

        return this;
    }

    public PanelBuilderCounter addSlotCounter(String identifier, ClickAction clickAction) {
        this.slotsWithCounter.put(identifier, new HashSet<>());
        this.clickActions.put(identifier, clickAction);

        return this;
    }

    public PanelBuilderCounter addSlotCounter(String identifier, ItemStack itemStack) {
        this.slotsWithCounter.put(identifier, new HashSet<>());
        this.itemStacks.put(identifier, itemStack);

        return this;
    }

    public PanelBuilderCounter addSlotCounter(String identifier, ItemStack itemStack, ClickAction clickAction) {
        this.slotsWithCounter.put(identifier, new HashSet<>());
        this.itemStacks.put(identifier, itemStack);
        this.clickActions.put(identifier, clickAction);

        return this;
    }

    public PanelBuilderCounter addPageData(int slot, int pageMath) {
        this.pageData.put(slot, pageMath);
        return this;
    }

    public Set<Integer> getSlotsWith(String identifier) {
        return this.slotsWithCounter.getOrDefault(identifier, new HashSet<>());
    }

    public Map<Integer, Object> getSpecialSlotsWith(String identifier) {
        return this.specialValuesCounter.getOrDefault(identifier, new HashMap<>());
    }

    public PanelBuilderCounter cloneCounter() {
        PanelBuilderCounter clone = new PanelBuilderCounter();

        clone.specialValuesCounter.putAll(this.specialValuesCounter);
        clone.slotsWithCounter.putAll(this.slotsWithCounter);
        clone.clickActions.putAll(this.clickActions);
        clone.buttonCounters.putAll(this.buttonCounters);
        clone.itemStacks.putAll(this.itemStacks);
        clone.pageData.putAll(this.pageData);

        return clone;
    }

    public Map<String, Map<Integer, Object>> getSpecialValuesCounter() {
        return this.specialValuesCounter;
    }

    public Map<String, Set<Integer>> getSlotsWithCounter() {
        return this.slotsWithCounter;
    }

    public Map<String, ClickAction> getClickActions() {
        return this.clickActions;
    }

    public Map<Integer, String> getButtonCounters() {
        return this.buttonCounters;
    }

    public Map<String, ItemStack> getItemStacks() {
        return this.itemStacks;
    }

    public Map<Integer, Integer> getPageData() {
        return this.pageData;
    }
}
