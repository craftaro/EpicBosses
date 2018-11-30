package com.songoda.epicbosses.panel.bosses.text;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.MessagesFileManager;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.itemstack.ItemStackConverter;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import com.songoda.epicbosses.utils.panel.Panel;
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
 * @since 29-Nov-18
 */
public abstract class DeathMessageListEditor extends VariablePanelHandler<BossEntity> {

    private MessagesFileManager messagesFileManager;
    private ItemStackConverter itemStackConverter;
    private CustomBosses plugin;

    public DeathMessageListEditor(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.plugin = plugin;
        this.itemStackConverter = new ItemStackConverter();
        this.messagesFileManager = plugin.getBossMessagesFileManager();
    }

    public abstract String getCurrent(BossEntity bossEntity);

    public abstract void updateMessage(BossEntity bossEntity, String newPath);

    @Override
    public void fillPanel(Panel panel, BossEntity bossEntity) {
        Map<String, List<String>> currentTexts = this.messagesFileManager.getMessagesMap();
        List<String> entryList = new ArrayList<>(currentTexts.keySet());
        int maxPage = panel.getMaxPage(entryList);

        panel.setOnPageChange(((player, currentPage, requestedPage) -> {
            if(requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(panel, requestedPage, currentTexts, entryList, bossEntity);
            return true;
        }));

        loadPage(panel, 0, currentTexts, entryList, bossEntity);
    }

    @Override
    public void openFor(Player player, BossEntity bossEntity) {
        Map<String, String> replaceMap = new HashMap<>();
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();

        replaceMap.put("{name}", BossAPI.getBossEntityName(bossEntity));
        panelBuilder.addReplaceData(replaceMap);

        Panel panel = panelBuilder.getPanel()
                .setDestroyWhenDone(true)
                .setCancelClick(true)
                .setCancelLowerClick(true)
                .setParentPanelHandler(this.bossPanelManager.getOnDeathSubTextEditMenu(), bossEntity);

        fillPanel(panel, bossEntity);

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private void loadPage(Panel panel, int page, Map<String, List<String>> currentMessages, List<String> entryList, BossEntity bossEntity) {
        String current = getCurrent(bossEntity);

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

                if(current.equalsIgnoreCase(name)) {
                    ItemStackUtils.applyDisplayName(itemStack, this.plugin.getConfig().getString("Display.Boss.Text.selectedName"), replaceMap);
                } else {
                    ItemStackUtils.applyDisplayName(itemStack, this.plugin.getConfig().getString("Display.Boss.Text.name"), replaceMap);
                }

                ItemMeta itemMeta = itemStack.getItemMeta();
                List<String> presetLore = this.plugin.getConfig().getStringList("Display.Boss.Text.lore");
                List<String> newLore = new ArrayList<>();

                for(String s : presetLore) {
                    if(s.contains("{message}")) {
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
                    updateMessage(bossEntity, name);
                    this.plugin.getBossesFileManager().save();
                    loadPage(panel, page, currentMessages, entryList, bossEntity);
                });
            }
        });
    }
}
