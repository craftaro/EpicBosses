package com.songoda.epicbosses.panel;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.ItemsFileManager;
import com.songoda.epicbosses.utils.Message;
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
    private ItemsFileManager itemsFileManager;

    public AddItemsPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.itemsFileManager = plugin.getItemStackManager();
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {
        PanelBuilderCounter panelBuilderCounter = panelBuilder.getPanelBuilderCounter();

        panelBuilderCounter
                .addSlotCounter("Cancel", getCancelAction())
                .addSlotCounter("Accept", getAcceptAction())
                .addSlotCounter("SelectedSlot", getSelectedSlotAction(panelBuilderCounter));
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

        panel.openFor(player);
    }

    private ClickAction getSelectedSlotAction(PanelBuilderCounter panelBuilderCounter) {
        return event -> {
            Player player = (Player) event.getWhoClicked();
            UUID uuid = player.getUniqueId();

            if(this.storedItemStacks.containsKey(uuid)) {
                player.getInventory().addItem(this.storedItemStacks.get(uuid));
                this.storedItemStacks.remove(uuid);

                panelBuilderCounter.getSlotsWith("SelectedSlot").forEach(slot -> event.getInventory().setItem(slot, new ItemStack(Material.AIR)));
            }
        };
    }

    private ClickAction getAcceptAction() {
        return event -> {
            Player player = (Player) event.getWhoClicked();
            UUID uuid = player.getUniqueId();

            if(this.storedItemStacks.containsKey(uuid)) {
                this.itemsFileManager.addItemStack(UUID.randomUUID().toString(), this.storedItemStacks.get(uuid));
                Message.Boss_Items_Added.msg(player);
                this.storedItemStacks.remove(uuid);
            }

            this.bossPanelManager.getCustomItems().openFor(player);
        };
    }

    private ClickAction getCancelAction() {
        return event -> {
            Player player = (Player) event.getWhoClicked();
            UUID uuid = player.getUniqueId();

            if(this.storedItemStacks.containsKey(uuid)) {
                player.getInventory().addItem(this.storedItemStacks.get(uuid));
                this.storedItemStacks.remove(uuid);
            }

            this.bossPanelManager.getCustomItems().openFor(player);
        };
    }
}
