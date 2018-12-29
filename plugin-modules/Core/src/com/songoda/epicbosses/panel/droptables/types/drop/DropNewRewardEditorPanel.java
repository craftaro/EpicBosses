package com.songoda.epicbosses.panel.droptables.types.drop;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.droptable.elements.DropTableElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.ItemsFileManager;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.handlers.SubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 29-Dec-18
 */
public class DropNewRewardEditorPanel extends SubVariablePanelHandler<DropTable, DropTableElement> {

    private ItemsFileManager itemsFileManager;
    private CustomBosses plugin;

    public DropNewRewardEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.itemsFileManager = plugin.getItemStackManager();
        this.plugin = plugin;
    }

    @Override
    public void fillPanel(Panel panel, DropTable dropTable, DropTableElement dropTableElement) {
        Map<String, ItemStackHolder> itemStacks = this.itemsFileManager.getItemStackHolders();
        List<String> currentKeys = new ArrayList<>(dropTableElement.getDropRewards().keySet());
        List<String> filteredKeys = getFilteredKeys(itemStacks, currentKeys);
        int maxPage = panel.getMaxPage(filteredKeys);

        panel.setOnPageChange(((player, currentPage, requestedPage) -> {
            if(requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(panel, requestedPage, dropTable, dropTableElement, filteredKeys, itemStacks);
            return true;
        }));

        loadPage(panel, 0, dropTable, dropTableElement, filteredKeys, itemStacks);
    }

    @Override
    public void openFor(Player player, DropTable dropTable, DropTableElement dropTableElement) {
        Panel panel = getPanelBuilder().getPanel()
                .setParentPanelHandler(this.bossPanelManager.getDropRewardListEditMenu(), dropTable, dropTableElement);

        fillPanel(panel, dropTable, dropTableElement);
        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private void loadPage(Panel panel, int page, DropTable dropTable, DropTableElement dropTableElement, List<String> filteredKeys, Map<String, ItemStackHolder> itemStacks) {
        panel.loadPage(page, (slot, realisticSlot) -> {
            if(slot >= filteredKeys.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e->{});
            } else {
                String name = filteredKeys.get(slot);
                ItemStackHolder itemStackHolder = itemStacks.get(name);
                ItemStack itemStack = this.itemsFileManager.getItemStackConverter().from(itemStackHolder);

                panel.setItem(realisticSlot, itemStack, event -> {
                    Map<String, Double> currentRewards = dropTableElement.getDropRewards();

                    currentRewards.put(name, 50.0);
                    dropTable.setRewards(BossAPI.convertObjectToJsonObject(dropTableElement));
                    this.plugin.getDropTableFileManager().save();

                    this.bossPanelManager.getDropRewardMainEditMenu().openFor((Player) event.getWhoClicked(), dropTable, dropTableElement, name);
                    Message.Boss_DropTable_DropAddedNewReward.msg(event.getWhoClicked(), BossAPI.getDropTableName(dropTable));
                });
            }
        });
    }

    private List<String> getFilteredKeys(Map<String, ItemStackHolder> itemStacks, List<String> currentKeys) {
        List<String> filteredList = new ArrayList<>();

        itemStacks.keySet().forEach(string -> {
            if(currentKeys.contains(string)) return;

            filteredList.add(string);
        });

        return filteredList;
    }
}
