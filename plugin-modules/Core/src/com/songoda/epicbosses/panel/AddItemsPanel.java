package com.songoda.epicbosses.panel;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.ItemsFileManager;
import com.songoda.epicbosses.panel.additems.interfaces.IParentPanelHandler;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import com.songoda.epicbosses.utils.panel.base.PanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 18-Nov-18
 */
public class AddItemsPanel extends PanelHandler {

    private Map<UUID, ItemStack> storedItemStacks = new HashMap<>();
    private IParentPanelHandler parentPanelHandler;
    private ItemsFileManager itemsFileManager;

    public AddItemsPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin, IParentPanelHandler parentPanelHandler) {
        super(bossPanelManager, panelBuilder);

        this.itemsFileManager = plugin.getItemStackManager();
        this.parentPanelHandler = parentPanelHandler;
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    @Override
    public void fillPanel(Panel panel) {

    }

    @Override
    public void openFor(Player player) {
        Panel panel = getPanelBuilder().getPanel()
                .setDestroyWhenDone(true)
                .setCancelClick(true)
                .setCancelLowerClick(false);

        PanelBuilderCounter counter = panel.getPanelBuilderCounter();

        ServerUtils.get().runTaskAsync(() -> {
            counter.getSlotsWith("Accept").forEach(slot -> panel.setOnClick(slot, getAcceptAction(panel)));
            counter.getSlotsWith("Cancel").forEach(slot -> panel.setOnClick(slot, getCancelAction(panel)));
            counter.getSlotsWith("SelectedSlot").forEach(slot -> panel.setOnClick(slot, getSelectedSlotAction(counter, panel)));

            panel.setOnClick(event -> {
                Player playerWhoClicked = (Player) event.getWhoClicked();
                UUID uuid = playerWhoClicked.getUniqueId();
                int rawSlot = event.getRawSlot();
                int slot = event.getSlot();

                if(panel.isLowerClick(rawSlot)) {
                    if(this.storedItemStacks.containsKey(uuid)) {
                        Message.Boss_Items_AlreadySet.msg(playerWhoClicked);
                        return;
                    }

                    ItemStack itemStack = event.getClickedInventory().getItem(slot);

                    this.storedItemStacks.put(uuid, itemStack.clone());
                    panel.getPanelBuilderCounter().getSlotsWith("SelectedSlot").forEach(s -> event.getInventory().setItem(s, itemStack.clone()));
                    event.getClickedInventory().setItem(slot, new ItemStack(Material.AIR));
                }
            });
        });

        panel.openFor(player);
    }

    private void openParentPanel(Player player) {
        this.parentPanelHandler.openParentPanel(player);
    }

    private ClickAction getSelectedSlotAction(PanelBuilderCounter panelBuilderCounter, Panel panel) {
        return event -> {
            int rawSlot = event.getRawSlot();

            if(panel.isLowerClick(rawSlot)) return;

            Player player = (Player) event.getWhoClicked();
            UUID uuid = player.getUniqueId();

            if(this.storedItemStacks.containsKey(uuid)) {
                player.getInventory().addItem(this.storedItemStacks.get(uuid));
                this.storedItemStacks.remove(uuid);

                panelBuilderCounter.getSlotsWith("SelectedSlot").forEach(slot -> event.getInventory().setItem(slot, new ItemStack(Material.AIR)));
            }
        };
    }

    private ClickAction getAcceptAction(Panel panel) {
        return event -> {
            int rawSlot = event.getRawSlot();

            if(panel.isLowerClick(rawSlot)) return;

            Player player = (Player) event.getWhoClicked();
            UUID uuid = player.getUniqueId();

            if(this.storedItemStacks.containsKey(uuid)) {
                this.itemsFileManager.addItemStack(UUID.randomUUID().toString(), this.storedItemStacks.get(uuid));
                Message.Boss_Items_Added.msg(player);
                this.storedItemStacks.remove(uuid);
            }

            openParentPanel(player);
        };
    }

    private ClickAction getCancelAction(Panel panel) {
        return event -> {
            int rawSlot = event.getRawSlot();

            if(panel.isLowerClick(rawSlot)) return;

            Player player = (Player) event.getWhoClicked();
            UUID uuid = player.getUniqueId();

            if(this.storedItemStacks.containsKey(uuid)) {
                player.getInventory().addItem(this.storedItemStacks.get(uuid));
                this.storedItemStacks.remove(uuid);
            }

            openParentPanel(player);
        };
    }
}
