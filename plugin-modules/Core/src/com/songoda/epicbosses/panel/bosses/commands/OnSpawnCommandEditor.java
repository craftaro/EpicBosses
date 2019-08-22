package com.songoda.epicbosses.panel.bosses.commands;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.CommandsFileManager;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.ServerUtils;
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
 * @since 28-Nov-18
 */
public class OnSpawnCommandEditor extends VariablePanelHandler<BossEntity> {

    private CommandsFileManager commandsFileManager;
    private ItemStackConverter itemStackConverter;
    private CustomBosses plugin;

    public OnSpawnCommandEditor(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.plugin = plugin;
        this.itemStackConverter = new ItemStackConverter();
        this.commandsFileManager = plugin.getBossCommandFileManager();
    }

    @Override
    public void fillPanel(Panel panel, BossEntity bossEntity) {
        Map<String, List<String>> currentCommands = this.commandsFileManager.getCommandsMap();
        List<String> entryList = new ArrayList<>(currentCommands.keySet());
        int maxPage = panel.getMaxPage(entryList);

        panel.setOnPageChange(((player, currentPage, requestedPage) -> {
            if(requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(panel, requestedPage, currentCommands, entryList, bossEntity);
            return true;
        }));

        loadPage(panel, 0, currentCommands, entryList, bossEntity);
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
                .setParentPanelHandler(this.bossPanelManager.getCommandsMainEditMenu(), bossEntity);

        ServerUtils.get().runTaskAsync(() -> fillPanel(panel, bossEntity));

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private void loadPage(Panel panel, int page, Map<String, List<String>> currentCommands, List<String> entryList, BossEntity bossEntity) {
        panel.loadPage(page, (slot, realisticSlot) -> {
            if(slot >= entryList.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e -> {});
            } else {
                String name = entryList.get(slot);
                List<String> commands = currentCommands.get(name);
                ItemStackHolder itemStackHolder = BossAPI.getStoredItemStack("DefaultTextMenuItem");
                ItemStack itemStack = this.itemStackConverter.from(itemStackHolder);

                Map<String, String> replaceMap = new HashMap<>();

                replaceMap.put("{name}", name);

                if(bossEntity.getCommands().getOnSpawn() != null && bossEntity.getCommands().getOnSpawn().equalsIgnoreCase(name)) {
                    ItemStackUtils.applyDisplayName(itemStack, this.plugin.getConfig().getString("Display.Boss.Commands.selectedName"), replaceMap);
                } else {
                    ItemStackUtils.applyDisplayName(itemStack, this.plugin.getConfig().getString("Display.Boss.Commands.name"), replaceMap);
                }

                ItemMeta itemMeta = itemStack.getItemMeta();
                List<String> presetLore = this.plugin.getConfig().getStringList("Display.Boss.Commands.lore");
                List<String> newLore = new ArrayList<>();

                for(String s : presetLore) {
                    if(s.contains("{commands}")) {
                        for(String command : commands) {
                            newLore.add(StringUtils.get().translateColor("&7" + command));
                        }
                    } else {
                        newLore.add(StringUtils.get().translateColor(s));
                    }
                }

                itemMeta.setLore(newLore);
                itemStack.setItemMeta(itemMeta);

                panel.setItem(realisticSlot, itemStack, e -> {
                    if(!bossEntity.isEditing()) {
                        Message.Boss_Edit_CannotBeModified.msg(e.getWhoClicked());
                        return;
                    }

                    bossEntity.getCommands().setOnSpawn(name);
                    this.plugin.getBossesFileManager().save();
                    loadPage(panel, page, currentCommands, entryList, bossEntity);
                });
            }
        });
    }
}
