package net.aminecraftdev.custombosses.innerapi.inventory;

import net.aminecraftdev.custombosses.innerapi.inventory.base.ClickAction;
import net.aminecraftdev.custombosses.innerapi.inventory.base.PageAction;
import net.aminecraftdev.custombosses.innerapi.inventory.base.PanelCloseAction;
import net.aminecraftdev.custombosses.innerapi.message.MessageUtils;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.*;

/**
 * @author Debugged
 * @version 1.0
 * @since 13-5-2017
 */
public class Panel {

    public static List<Panel> panels = new ArrayList<>();
    private static boolean registered = false;

    private Inventory inventory;
    public int viewers = 0;
    private Map<Integer, ClickAction> actions = new HashMap<>();
    private Map<Integer, Integer> pageData = new HashMap<>();
    private Map<UUID, Integer> currentPages = new HashMap<>();
    private Sound clickSound = null;
    private boolean cancelClick = true;
    private boolean destroyWhenDone = true;

    private PanelCloseAction panelClose = (p) -> {};
    private PageAction onPageChange = (player, currentPage, requestedPage) -> {return false;};

    /**
     * Creates a Panel with a title and a specific size
     *
     * @param title
     * @param size
     */
    public Panel(String title, int size, JavaPlugin javaPlugin) {
        if(!registered) {
            Bukkit.getPluginManager().registerEvents(getListeners(), javaPlugin);
            registered = true;
        }
        if(size % 9 != 0 && size != 5) {
            throw new UnsupportedOperationException("Inventory size must be a multiple of 9 or 5");
        }
        this.inventory = size % 9 == 0 ? Bukkit.createInventory(null, size, MessageUtils.translateString(title)) : Bukkit.createInventory(null, InventoryType.HOPPER, MessageUtils.translateString(title));
        panels.add(this);
    }

    public Panel(Inventory inventory, Map<Integer, Integer> pageData, JavaPlugin javaPlugin) {
        if(!registered) {
            Bukkit.getPluginManager().registerEvents(getListeners(), javaPlugin);
            registered = true;
        }
        this.inventory = inventory;
        this.pageData = pageData;

        panels.add(this);
    }

    public Panel(Inventory inventory, JavaPlugin javaPlugin) {
        this(inventory, new HashMap<>(), javaPlugin);
    }

    /**
     * Returns the listeners that are
     * needed for all Panel handlers to work properly
     *
     * @return Listener
     */
    private Listener getListeners() {
        return new Listener() {
            @EventHandler
            public void onInvClick(InventoryClickEvent e) {
                if (e.getInventory() != null && !Panel.panels.isEmpty()) {
                    if(e.getInventory() == null) return;
                    if(e.getCurrentItem() == null && e.getCursor() == null) return;
                    for(Panel panel : new ArrayList<>(Panel.panels)) {
                        if(panel != null && panel.getInventory() != null) {
                            if(!panel.getInventory().equals(e.getInventory())) continue;
                            if(panel.getClickSound() != null) {
                                ((Player)e.getWhoClicked()).playSound(e.getWhoClicked().getLocation(), panel.getClickSound(), 3f, 1f);
                            }
                            if (panel.willCancelClick()) {
                                e.setCancelled(true);
                            }
                            if (panel.getInventory().equals(e.getInventory())) {
                                panel.executeAction(e.getRawSlot(), e);
                            }
                        }
                    }
                }
            }

            @EventHandler
            public void onInvClose(InventoryCloseEvent e) {
                if (e.getInventory() != null && !Panel.panels.isEmpty()) {
                    for(Panel panel : new ArrayList<>(Panel.panels)) {
                        if(panel != null && panel.getInventory() != null) {
                            if (panel.getInventory().equals(e.getInventory())) {
                                panel.panelClose.onClose((Player) e.getPlayer());
                                panel.viewers--;
                                if (panel.getViewers() <= 0 && panel.willDestroyWhenDone()) {
                                    panel.destroy();
                                }
                            }
                        }
                    }
                }
            }
        };
    }

    /**
     * Triggers the ClickAction interfaces
     * for the specified slot
     *
     * @param slot int
     * @param e InventoryClickEvent
     */
    public void executeAction(int slot, InventoryClickEvent e) {
        Player clicker = (Player) e.getWhoClicked();
        if(pageData.containsKey(slot)) {
            int currentPage = currentPages.getOrDefault(clicker.getUniqueId(), 0);
            if(pageData.get(slot) > 0) {
                if(onPageChange.onPageAction(clicker, currentPage, currentPage+1)) {
                    currentPages.put(clicker.getUniqueId(), currentPage+1);
                }
            } else {
                if(currentPage != 0) {
                    if (onPageChange.onPageAction(clicker, currentPage, currentPage-1)) {
                        currentPages.put(clicker.getUniqueId(), currentPage - 1);
                    }
                }
            }
        }
        if(actions.containsKey(slot)) {
            actions.get(slot).onClick(e);
        }
    }

    /**
     * Sets the functional interface which will be called whenever a player closes the panel
     * <br>
     * <b>Builder method</b>
     *
     * @param panelClose {@link PanelCloseAction}
     * @return Panel
     */
    public Panel onClose(PanelCloseAction panelClose) {
        this.panelClose = panelClose;
        return this;
    }

    /**
     * Specify a ClickAction for the
     * given slot number
     * <br>
     * <b>Builder method</b>
     *
     * @param slot int
     * @param clickAction ClickAction
     */
    public Panel setOnClick(int slot, ClickAction clickAction) {
        actions.put(slot, clickAction);
        return this;
    }

    /**
     * Sets an item at the specified slot
     * <br>
     * <b>Builder method</b>
     *
     * @param slot int
     * @param item ItemStack
     * @return Panel
     */
    public Panel setItem(int slot, ItemStack item){
        inventory.setItem(slot, item);
        return this;
    }

    /**
     * Sets an item at the specified slot along with a functional interface which will<br>
     * be called whenever the item gets clicked
     * <br>
     * <b>Builder method</b>
     *
     * @param slot int
     * @param item ItemStack
     * @param action {@link ClickAction}
     * @return Panel
     */
    public Panel setItem(int slot, ItemStack item, ClickAction action) {
        inventory.setItem(slot, item);
        actions.put(slot, action);
        return this;
    }

    /**
     * Sets the sound that will be played whenever
     * an item gets clicked
     *
     * @param clickSound Sound
     * @return Panel
     */
    public Panel setClickSound(Sound clickSound) {
        this.clickSound = clickSound;
        return this;
    }

    /**
     * Opens the Panel for the specified Player
     * <br>
     * <b>Builder method</b>
     *
     * @param player Player
     * @return Panel
     */
    public Panel openFor(Player player) {
        player.openInventory(inventory);
        viewers++;
        return this;
    }

    public Panel setOnClose(PanelCloseAction panelClose) {
        this.panelClose = panelClose;
        return this;
    }

    public Panel setOnPageChange(PageAction onPageChange) {
        this.onPageChange = onPageChange;
        return this;
    }

    /**
     * Makes sure the panel will cancel or not cancel click when an item was clicked
     * <br>
     * <b>Builder method</b>
     *
     * @param cancelClick boolean
     * @return Panel
     */
    public Panel setCancelClick(boolean cancelClick) {
        this.cancelClick = cancelClick;
        return this;
    }

    /**
     * Makes sure the panel will destroy or not destroy when it has no viewers anymore
     * <br>
     * <b>Builder method</b>
     *
     * @param destroyWhenDone boolean
     * @return Panel
     */
    public Panel setDestroyWhenDone(boolean destroyWhenDone) {
        this.destroyWhenDone = destroyWhenDone;
        return this;
    }

    /**
     * Returns whether or not the panel will destroy when there are no viewers anymore
     *
     * @return boolean
     */
    public boolean willDestroyWhenDone() {
        return destroyWhenDone;
    }

    /**
     * Returns whether or not clicking an item in this panel will cancel the InventoryClickEvent
     *
     * @return boolean
     */
    public boolean willCancelClick() {
        return cancelClick;
    }

    /**
     * Returns the amount of players that are viewing this panel
     *
     * @return int
     */
    public int getViewers() {
        return viewers;
    }

    /**
     * Returns the sound that will be played
     * whenever a player clicks an item
     *
     * @return Sound
     */
    public Sound getClickSound() {
        return clickSound;
    }

    /**
     * Returns the bukkit Inventory object that corresponds to this panel
     *
     * @return Inventory
     */
    public Inventory getInventory() {
        return inventory;
    }

    /**
     * Destroys the panel in order to block further access
     */
    public void destroy() {
        this.actions.clear();
        this.pageData.clear();
        this.currentPages.clear();
        this.pageData = null;
        this.currentPages = null;
        this.actions = null;
        this.inventory = null;
        panels.remove(this);
    }

}
