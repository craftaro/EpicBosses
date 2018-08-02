package net.aminecraftdev.custombosses.utils.panel.builder;

import lombok.Getter;
import net.aminecraftdev.custombosses.utils.panel.base.ClickAction;
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

    @Getter private final Map<String, Map<Integer, Object>> specialValuesCounter = new HashMap<>();
    @Getter private final Map<String, Set<Integer>> slotsWithCounter = new HashMap<>();
    @Getter private final Map<String, ClickAction> clickActions = new HashMap<>();
    @Getter private final Map<Integer, String> buttonCounters = new HashMap<>();
    @Getter private final Map<String, ItemStack> itemStacks = new HashMap<>();
    @Getter private final Map<Integer, Integer> pageData = new HashMap<>();

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

}
