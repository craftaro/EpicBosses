package com.songoda.epicbosses.panel.droptables.rewards;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.ItemsFileManager;
import com.songoda.epicbosses.panel.droptables.rewards.interfaces.IDropTableNewRewardEditor;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.ServerUtils;
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
 * @since 02-Jan-19
 */
public abstract class DropTableNewRewardEditorPanel<SubVariable> extends SubVariablePanelHandler<DropTable, SubVariable> implements IDropTableNewRewardEditor<SubVariable> {

    private ItemsFileManager itemsFileManager;
    private EpicBosses plugin;

    public DropTableNewRewardEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, EpicBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.itemsFileManager = plugin.getItemStackManager();
        this.plugin = plugin;
    }

    @Override
    public void fillPanel(Panel panel, DropTable dropTable, SubVariable subVariable) {
        Map<String, ItemStackHolder> itemStacks = this.itemsFileManager.getItemStackHolders();
        List<String> currentKeys = getCurrentKeys(subVariable);
        List<String> filteredKeys = getFilteredKeys(itemStacks, currentKeys);
        int maxPage = panel.getMaxPage(filteredKeys);

        panel.setOnPageChange(((player, currentPage, requestedPage) -> {
            if(requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(panel, requestedPage, dropTable, subVariable, filteredKeys, itemStacks);
            return true;
        }));

        loadPage(panel, 0, dropTable, subVariable, filteredKeys, itemStacks);
    }

    @Override
    public void openFor(Player player, DropTable dropTable, SubVariable subVariable) {
        Panel panel = getPanelBuilder().getPanel()
                .setParentPanelHandler(getParentPanelHandler(), dropTable, subVariable);

        ServerUtils.get().runTaskAsync(() -> fillPanel(panel, dropTable, subVariable));
        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private void loadPage(Panel panel, int page, DropTable dropTable, SubVariable subVariable, List<String> filteredKeys, Map<String, ItemStackHolder> itemStacks) {
        panel.loadPage(page, (slot, realisticSlot) -> {
            if(slot >= filteredKeys.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e->{});
            } else {
                String name = filteredKeys.get(slot);
                ItemStackHolder itemStackHolder = itemStacks.get(name);
                ItemStack itemStack = this.itemsFileManager.getItemStackConverter().from(itemStackHolder);

                panel.setItem(realisticSlot, itemStack, event -> {
                    Map<String, Double> currentRewards = getRewards(subVariable);

                    currentRewards.put(name, 50.0);
                    saveDropTable(this.plugin.getDropTableFileManager(), dropTable, subVariable);

                    getRewardMainEditMenu().openFor((Player) event.getWhoClicked(), dropTable, subVariable, name);
                    Message.Boss_DropTable_AddedNewReward.msg(event.getWhoClicked(), BossAPI.getDropTableName(dropTable));
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
