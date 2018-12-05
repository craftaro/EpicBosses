package com.songoda.epicbosses.panel.handlers;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.CommandsFileManager;
import com.songoda.epicbosses.managers.files.MessagesFileManager;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.itemstack.ItemStackConverter;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.IVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.base.handlers.VariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 05-Dec-18
 */
public abstract class ListCommandListEditor<T> extends VariablePanelHandler<T> {

    private CommandsFileManager commandsFileManager;
    private ItemStackConverter itemStackConverter;
    private CustomBosses plugin;

    public ListCommandListEditor(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.plugin = plugin;
        this.itemStackConverter = new ItemStackConverter();
        this.commandsFileManager = plugin.getBossCommandFileManager();
    }

    public abstract List<String> getCurrent(T object);

    public abstract void updateMessage(T object, String modifiedValue);

    public abstract IVariablePanelHandler<T> getParentHolder();

    public abstract String getName(T object);

    @Override
    public void fillPanel(Panel panel, T object) {
        Map<String, List<String>> currentTexts = this.commandsFileManager.getCommandsMap();
        List<String> entryList = new ArrayList<>(currentTexts.keySet());
        int maxPage = panel.getMaxPage(entryList);

        panel.setOnPageChange(((player, currentPage, requestedPage) -> {
            if(requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(panel, requestedPage, currentTexts, entryList, object);
            return true;
        }));

        loadPage(panel, 0, currentTexts, entryList, object);
    }

    @Override
    public void openFor(Player player, T object) {
        Map<String, String> replaceMap = new HashMap<>();
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();

        replaceMap.put("{name}", getName(object));
        panelBuilder.addReplaceData(replaceMap);

        Panel panel = panelBuilder.getPanel()
                .setDestroyWhenDone(true)
                .setCancelClick(true)
                .setCancelLowerClick(true)
                .setParentPanelHandler(getParentHolder(), object);

        fillPanel(panel, object);

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private void loadPage(Panel panel, int page, Map<String, List<String>> currentMessages, List<String> entryList, T object) {
        List<String> current = getCurrent(object);

        panel.loadPage(page, (slot, realisticSlot) -> {
            if(slot >= entryList.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e -> {});
            } else {
                String name = entryList.get(slot);
                List<String> messages = currentMessages.get(name);
                ItemStackHolder itemStackHolder = BossAPI.getStoredItemStack("DefaultTextMenuItem");
                ItemStack itemStack = this.itemStackConverter.from(itemStackHolder);

                Map<String, String> replaceMap = new HashMap<>();

                replaceMap.put("{name}", name);

                if(current.contains(name)) {
                    ItemStackUtils.applyDisplayName(itemStack, this.plugin.getConfig().getString("Display.Boss.Commands.selectedName"), replaceMap);
                } else {
                    ItemStackUtils.applyDisplayName(itemStack, this.plugin.getConfig().getString("Display.Boss.Command.name"), replaceMap);
                }

                ItemMeta itemMeta = itemStack.getItemMeta();
                List<String> presetLore = this.plugin.getConfig().getStringList("Display.Boss.Commands.lore");
                List<String> newLore = new ArrayList<>();

                for(String s : presetLore) {
                    if(s.contains("{commands}")) {
                        for(String message : messages) {
                            newLore.add(StringUtils.get().translateColor("&7" + message));
                        }
                    } else {
                        newLore.add(StringUtils.get().translateColor(s));
                    }
                }

                itemMeta.setLore(newLore);
                itemStack.setItemMeta(itemMeta);

                panel.setItem(realisticSlot, itemStack, e -> {
                    updateMessage(object, name);
                    this.plugin.getBossesFileManager().save();
                    loadPage(panel, page, currentMessages, entryList, object);
                });
            }
        });
    }
}