package com.songoda.epicbosses.utils.panel;

import com.songoda.epicbosses.utils.panel.base.*;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import lombok.Getter;
import com.songoda.epicbosses.utils.ICloneable;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.itemstack.ItemStackConverter;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderSettings;
import org.bukkit.Bukkit;
import org.bukkit.Material;
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
 * @author AMinecraftDev
 * @version 2.0.0
 * @since 18-Jul-2018
 */
public class Panel implements Listener, ICloneable<Panel> {

    //--------------------------------------------------
    //
    // P A N E L   S T A T I C   F I E L D S
    //
    //--------------------------------------------------

    @Getter private static final ItemStackConverter ITEM_STACK_CONVERTER = new ItemStackConverter();
    @Getter private static final List<Panel> PANELS = new ArrayList<>();

    private static JavaPlugin PLUGIN;

    //--------------------------------------------------
    //
    // P A N E L   F I E L D S
    //
    //--------------------------------------------------

    private final Map<Integer, ClickAction> targettedSlotActions = new HashMap<>();
    private final List<Inventory> connectedInventories = new ArrayList<>();
    private final List<ClickAction> allSlotActions = new ArrayList<>();

    private final Map<UUID, Integer> currentPageContainer = new HashMap<>();
    private final List<UUID> openedUsers = new ArrayList<>();

    @Getter private boolean cancelClick = true, destroyWhenDone = true, cancelLowerClick = true;
    @Getter private PanelBuilderSettings panelBuilderSettings;
    @Getter private PanelBuilderCounter panelBuilderCounter;
    @Getter private Sound clickSound = null;
    @Getter private Inventory inventory;
    @Getter private int viewers = 0;

    private PageAction onPageChange = (player, currentPage, requestedPage) -> false;
    private PanelCloseAction panelClose = (p) -> {};

    //--------------------------------------------------
    //
    // P A N E L   C O N S T R U C T O R S
    //
    //--------------------------------------------------

    /**
     * Creates a Panel with the specified arguments
     *
     * @param title - Panel title
     * @param size - Panel size
     */
    public Panel(String title, int size) {
        Bukkit.getPluginManager().registerEvents(this, PLUGIN);

        if(size % 9 != 0 && size != 5) {
            throw new UnsupportedOperationException("Inventory size must be a multiple of 9 or 5");
        }

        this.inventory = size % 9 == 0 ? Bukkit.createInventory(null, size, StringUtils.get().translateColor(title)) : Bukkit.createInventory(null, InventoryType.HOPPER, StringUtils.get().translateColor(title));
        this.connectedInventories.add(this.inventory);
        PANELS.add(this);
    }

    /**
     * Creates a Panel with the specified arguments
     *
     * @param inventory - Panel inventory
     */
    public Panel(Inventory inventory) {
        this(inventory, null, null);
    }

    /**
     * Creates a Panel with the specified arguments
     *
     * @param inventory - Panel inventory
     */
    public Panel(Inventory inventory, PanelBuilderSettings panelBuilderSettings, PanelBuilderCounter panelBuilderCounter) {
        Bukkit.getPluginManager().registerEvents(this, PLUGIN);

        this.inventory = inventory;
        this.panelBuilderSettings = panelBuilderSettings;
        this.panelBuilderCounter = panelBuilderCounter;

        fillEmptySpace();

        this.connectedInventories.add(this.inventory);
        PANELS.add(this);
    }

    //--------------------------------------------------
    //
    // P A N E L   L I S T E N E R S
    //
    //--------------------------------------------------

    @EventHandler
    protected void onClick(InventoryClickEvent event) {
        if(event.getInventory() == null || event.getCursor() == null || getInventory() == null) return;
        if(!getInventory().equals(event.getInventory())) return;

        Player player = (Player) event.getWhoClicked();

        if(isCancelLowerClick() && isLowerClick(event.getRawSlot())) {
            event.setCancelled(true);
            return;
        }

        if(getClickSound() != null) player.playSound(player.getLocation(), getClickSound(), 3F, 1F);
        if(isCancelClick()) event.setCancelled(true);
        if(getInventory().equals(inventory)) executeAction(event.getSlot(), event);
    }

    @EventHandler
    protected void onClose(InventoryCloseEvent event) {
        if(event.getInventory() == null || getInventory() == null) return;
        if(!getInventory().equals(event.getInventory())) return;

        Player player = (Player) event.getPlayer();

        this.panelClose.onClose(player);
        this.openedUsers.remove(player.getUniqueId());
        this.viewers--;

        if(getViewers() <= 0 && isDestroyWhenDone()) destroy();
    }

    //--------------------------------------------------
    //
    // P A N E L   E X E C U T E   A C T I O N
    //
    //--------------------------------------------------

    private void executeAction(int slot, InventoryClickEvent e) {
        Player clicker = (Player) e.getWhoClicked();

        if(getPanelBuilderCounter().getPageData().containsKey(slot)) {
            int currentPage = this.currentPageContainer.getOrDefault(clicker.getUniqueId(), 0);

            if(getPanelBuilderCounter().getPageData().get(slot) > 0) {
                if(this.onPageChange.onPageAction(clicker, currentPage, currentPage+1)) {
                    this.currentPageContainer.put(clicker.getUniqueId(), currentPage+1);
                }
            } else {
                if(currentPage != 0) {
                    if (this.onPageChange.onPageAction(clicker, currentPage, currentPage-1)) {
                        this.currentPageContainer.put(clicker.getUniqueId(), currentPage - 1);
                    }
                }
            }
        }

        if(this.targettedSlotActions.containsKey(slot)) {
            this.targettedSlotActions.get(slot).onClick(e);
        }

        if(!this.allSlotActions.isEmpty()) {
            for(ClickAction clickAction : this.allSlotActions) {
                clickAction.onClick(e);
            }
        }
    }

    //--------------------------------------------------
    //
    // P A N E L   S E T   M E T H O D S
    //
    //--------------------------------------------------

    /**
     * Used to set an action for when a player clicks
     * the panel.
     *
     * @param slot - the slot for the action to happen
     * @param clickAction - the action to happen
     * @return an instance of the Panel.
     */
    public Panel setOnClick(int slot, ClickAction clickAction) {
        this.targettedSlotActions.put(slot, clickAction);
        return this;
    }

    /**
     * Used to set an action for when a player clicks
     * on any slot in the panel.
     *
     * @param clickAction - the action to happen
     * @return an instance of the Panel.
     */
    public Panel setOnClick(ClickAction clickAction) {
        this.allSlotActions.add(clickAction);
        return this;
    }

    /**
     * Used to add an item to the next open slot in
     * the panel.
     *
     * @param itemStack - the itemstack to add.
     * @return an instance of the Panel.
     */
    public Panel addItem(ItemStack itemStack) {
        this.inventory.addItem(itemStack);
        return this;
    }

    /**
     * Used to set an item to a specific slot in the
     * panel.
     *
     * @param slot - the slot for the item to be set to
     * @param item - the item to be set.
     * @return an instance of the Panel.
     */
    public Panel setItem(int slot, ItemStack item){
        this.inventory.setItem(slot, item);
        return this;
    }

    /**
     * Used to set an item to a specific slot in the
     * panel and for an action to also be set to that
     * specified slot.
     *
     * @param slot - the slot for the action and item to be set to
     * @param item - the item to be set
     * @param action - the action to be applied
     * @return an instance of the Panel.
     */
    public Panel setItem(int slot, ItemStack item, ClickAction action) {
        this.inventory.setItem(slot, item);

        return setOnClick(slot, action);
    }

    /**
     * Used to set the click sound for when a player
     * clicks the panel.
     *
     * @param clickSound - the sound to be played.
     * @return an instance of the Panel.
     */
    public Panel setClickSound(Sound clickSound) {
        this.clickSound = clickSound;
        return this;
    }

    /**
     * Used to open the panel for the specified player.
     *
     * @param player - the player to be opened for.
     * @return an instance of the Panel.
     */
    public Panel openFor(Player player) {
        player.openInventory(this.inventory);
        this.openedUsers.add(player.getUniqueId());
        viewers++;
        return this;
    }

    /**
     * Used to set an action for when a player closes
     * the panel.
     *
     * @param panelClose - the action to happen on close.
     * @return an instance of the Panel.
     */
    public Panel setOnClose(PanelCloseAction panelClose) {
        this.panelClose = panelClose;
        return this;
    }

    /**
     * Used to set an action for when a player changes
     * the panel page.
     *
     * @param onPageChange - the action to occur.
     * @return an instance of the Panel.
     */
    public Panel setOnPageChange(PageAction onPageChange) {
        this.onPageChange = onPageChange;
        return this;
    }

    /**
     * Used to set if clicks are cancelled in the panel.
     *
     * @param cancelClick - boolean if clicks are cancelled.
     * @return an instance of the Panel.
     */
    public Panel setCancelClick(boolean cancelClick) {
        this.cancelClick = cancelClick;
        return this;
    }

    /**
     * Used to specify if the panel is destroyed when the
     * last person closes it.
     *
     * @param destroyWhenDone - the boolean to set if the panel destroys on close.
     * @return an instance of the Panel.
     */
    public Panel setDestroyWhenDone(boolean destroyWhenDone) {
        this.destroyWhenDone = destroyWhenDone;
        return this;
    }

    /**
     * Used to set if the click is cancelled on the bottom
     * GUI.
     *
     * @param cancelClick - if the click is cancelled.
     * @return an instance of the Panel.
     */
    public Panel setCancelLowerClick(boolean cancelClick) {
        this.cancelLowerClick = cancelClick;
        return this;
    }

    public boolean isLowerClick(int rawSlot) {
        return rawSlot > inventory.getSize();
    }

    public void loadPage(int page, IPageHandler pageHandler) {
        int fillTo = getPanelBuilderSettings().getFillTo();
        int startIndex = page * fillTo;

        for(int i = startIndex; i < startIndex + fillTo; i++) {
            pageHandler.handleSlot(i, i-startIndex);
        }
    }

    /**
     * Used to set the parent panel for this Panel, which
     * will be used if the Back Button is set up for this
     * panel.
     *
     * @param parentPanel - the parent Panel
     * @return the current Panel
     */
    public Panel setParentPanel(Panel parentPanel) {
        if(!this.panelBuilderSettings.isBackButton()) return this;

        int slot = this.panelBuilderSettings.getBackButtonSlot() - 1;

        setOnClick(slot, event -> parentPanel.openFor((Player) event.getWhoClicked()));
        return this;
    }

    public Panel setParentPanel(PanelBuilder panelBuilder, boolean cancelClick, boolean destroyWhenDone, boolean cancelLowerClick) {
        if(!this.panelBuilderSettings.isBackButton()) return this;

        int slot = this.panelBuilderSettings.getBackButtonSlot() - 1;

        setOnClick(slot, event -> {
            Panel panel = panelBuilder.getPanel()
                    .setCancelClick(cancelClick)
                    .setDestroyWhenDone(destroyWhenDone)
                    .setCancelLowerClick(cancelLowerClick);

            panel.openFor((Player) event.getWhoClicked());
        });
        return this;
    }

    public Panel setParentPanelHandler(IPanelHandler panelHandler) {
        if(!this.panelBuilderSettings.isBackButton()) return this;

        int slot = this.panelBuilderSettings.getBackButtonSlot() - 1;

        setOnClick(slot, event -> panelHandler.openFor((Player) event.getWhoClicked()));
        return this;
    }

    public <T> Panel setParentPanelHandler(IVariablePanelHandler<T> variablePanelHandler, T variable) {
        System.out.println(this.panelBuilderSettings.isBackButton());

        if(!this.panelBuilderSettings.isBackButton()) return this;

        int slot = this.panelBuilderSettings.getBackButtonSlot() - 1;

        System.out.println(slot);

        setOnClick(slot, event -> variablePanelHandler.openFor((Player) event.getWhoClicked(), variable));
        return this;
    }

    public Panel setExitButton() {
        if(!this.panelBuilderSettings.isExitButton()) return this;

        int slot = this.panelBuilderSettings.getExitButtonSlot();

        setOnClick(slot, event -> event.getWhoClicked().closeInventory());
        return this;
    }


    //--------------------------------------------------
    //
    // O T H E R   P A N E L   M E T H O D S
    //
    //--------------------------------------------------

    /**
     * Used to destroy a panel, no matter how many people
     * are in it or what's happening in it.
     *
     * ** ONLY USE THIS IF YOU KNOW WHAT YOU'RE DOING **
     *
     */
    public void destroy() {
        this.currentPageContainer.clear();
        this.targettedSlotActions.clear();
        this.allSlotActions.clear();
        this.inventory = null;

        this.openedUsers.forEach(uuid -> {
            Player player = Bukkit.getPlayer(uuid);

            if(player == null) return;

            player.closeInventory();
        });

        this.openedUsers.clear();

        InventoryClickEvent.getHandlerList().unregister(this);
        InventoryCloseEvent.getHandlerList().unregister(this);
    }

    /**
     * Used to fill the empty spaces in the panel with the specified
     * EmptySpaceFiller item if it's set up in the config.
     */
    public void fillEmptySpace() {
        ItemStackHolder itemStackHolder = this.panelBuilderSettings.getEmptySpaceFillerItem();

        if(itemStackHolder == null) return;

        ItemStack itemStack = ITEM_STACK_CONVERTER.from(itemStackHolder);

        if(itemStack == null) return;

        for(int i = 0; i < getInventory().getSize(); i++) {
            ItemStack itemAtSlot = getInventory().getItem(i);

            if(getPanelBuilderCounter().isButtonAtSlot(i)) continue;

            if(itemAtSlot == null || itemAtSlot.getType() == Material.AIR) {
                getInventory().setItem(i, itemStack);
            }
        }
    }

    public Inventory cloneInventory() {
        Inventory thisInventory = getInventory();
        Inventory newInventory = Bukkit.createInventory(thisInventory.getHolder(), thisInventory.getSize(), thisInventory.getTitle());

        for(int i = 0; i < thisInventory.getSize(); i++) {
            ItemStack itemStack = thisInventory.getItem(i);

            if(itemStack == null) continue;

            newInventory.setItem(i, itemStack);
        }

        this.connectedInventories.add(newInventory);

        return newInventory;
    }

    @Override
    public Panel clone() {
        Panel panel = new Panel(this.inventory.getTitle(), this.inventory.getSize());

        panel.targettedSlotActions.putAll(this.targettedSlotActions);
        panel.allSlotActions.addAll(this.allSlotActions);
        panel.currentPageContainer.putAll(this.currentPageContainer);

        panel.cancelClick = this.cancelClick;
        panel.destroyWhenDone = this.destroyWhenDone;
        panel.cancelLowerClick = this.cancelLowerClick;
        panel.panelBuilderSettings = this.panelBuilderSettings;
        panel.panelBuilderCounter = this.panelBuilderCounter;
        panel.clickSound = this.clickSound;
        panel.onPageChange = this.onPageChange;
        panel.panelClose = this.panelClose;

        for(int i = 0; i < this.inventory.getSize(); i++) {
            ItemStack itemStack = this.inventory.getItem(i);

            if(itemStack != null) {
                panel.inventory.setItem(i, itemStack);
            }
        }

        return panel;
    }

    public int getMaxPage(List<?> list) {
        return (int) Math.ceil((double) list.size() / (double) getPanelBuilderSettings().getFillTo()) - 1;
    }

    public int getMaxPage(Map<?,?> map) {
        return (int) Math.ceil((double) map.size() / (double) getPanelBuilderSettings().getFillTo()) - 1;
    }

    //--------------------------------------------------
    //
    // P A N E L   S T A T I C   M E T H O D
    //
    //--------------------------------------------------

    public static void setPlugin(JavaPlugin javaPlugin) {
        PLUGIN = javaPlugin;
    }
}
