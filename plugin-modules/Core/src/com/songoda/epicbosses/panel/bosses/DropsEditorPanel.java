package com.songoda.epicbosses.panel.bosses;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.BossesFileManager;
import com.songoda.epicbosses.managers.files.DropTableFileManager;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.itemstack.ItemStackConverter;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.handlers.VariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 19-Nov-18
 */
public class DropsEditorPanel extends VariablePanelHandler<BossEntity> {

    private DropTableFileManager dropTableFileManager;
    private ItemStackConverter itemStackConverter;
    private BossesFileManager bossesFileManager;
    private CustomBosses plugin;

    public DropsEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.dropTableFileManager = plugin.getDropTableFileManager();
        this.bossesFileManager = plugin.getBossesFileManager();
        this.itemStackConverter = new ItemStackConverter();
        this.plugin = plugin;
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    @Override
    public void fillPanel(Panel panel, BossEntity bossEntity) {
        Map<String, DropTable> dropTableMap = this.dropTableFileManager.getDropTables();
        List<String> entryList = new ArrayList<>(dropTableMap.keySet());
        int maxPage = panel.getMaxPage(entryList);

        panel.setOnPageChange(((player, currentPage, requestedPage) -> {
            if(requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(panel, requestedPage, dropTableMap, entryList, bossEntity);
            return true;
        }));

        loadPage(panel, 0, dropTableMap, entryList, bossEntity);
    }

    @Override
    public void openFor(Player player, BossEntity bossEntity) {
        Map<String, String> replaceMap = new HashMap<>();

        replaceMap.put("{name}", BossAPI.getBossEntityName(bossEntity));
        replaceMap.put("{dropTable}", bossEntity.getDrops().getDropTable());

        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();

        panelBuilder.addReplaceData(replaceMap);

        Panel panel = panelBuilder.getPanel()
                .setDestroyWhenDone(true)
                .setCancelClick(true)
                .setCancelLowerClick(true);
        PanelBuilderCounter counter = panel.getPanelBuilderCounter();
        String dropTableName = bossEntity.getDrops().getDropTable();
        DropTable dropTable = this.dropTableFileManager.getDropTable(dropTableName);

        ServerUtils.get().runTaskAsync(() -> {
            fillPanel(panel, bossEntity);
            counter.getSlotsWith("Selected").forEach(slot -> panel.setOnClick(slot, event -> this.bossPanelManager.getMainDropTableEditMenu().openFor((Player) event.getWhoClicked(), dropTable)));
            counter.getSlotsWith("CreateDropTable").forEach(slot -> panel.setOnClick(slot, event -> {
                player.closeInventory();
                Message.Boss_New_CreateArgumentsDropTable.msg(event.getWhoClicked());
            }));
        });

        panel.openFor(player);
    }

    private void loadPage(Panel panel, int requestedPage, Map<String, DropTable> dropTableMap, List<String> entryList, BossEntity bossEntity) {
        String dropTableName = bossEntity.getDrops().getDropTable();

        panel.loadPage(requestedPage, (slot, realisticSlot) -> {
            if(slot >= dropTableMap.size()) {
                panel.setItem(realisticSlot, new ItemStack(Material.AIR), e -> {});
            } else {
                String name = entryList.get(slot);
                DropTable dropTable = dropTableMap.get(name);
                ItemStackHolder itemStackHolder;

                if(dropTableName.equalsIgnoreCase(name)) {
                    itemStackHolder = BossAPI.getStoredItemStack("DefaultSelectedDropTableItem");
                } else {
                    itemStackHolder = BossAPI.getStoredItemStack("DefaultDropTableMenuItem");
                }

                ItemStack itemStack = this.itemStackConverter.from(itemStackHolder);

                if(itemStack == null) {
                    itemStack = new ItemStack(Material.BARRIER);
                }

                Map<String, String> replaceMap = new HashMap<>();

                replaceMap.put("{name}", name);
                replaceMap.put("{type}", StringUtils.get().formatString(dropTable.getDropType()));

                ItemStackUtils.applyDisplayName(itemStack, this.plugin.getConfig().getString("Display.Boss.Drops.name"), replaceMap);
                ItemStackUtils.applyDisplayLore(itemStack, this.plugin.getConfig().getStringList("Display.Boss.Drops.lore"), replaceMap);

                panel.setItem(realisticSlot, itemStack, e -> {
                    if(!bossEntity.isEditing()) {
                        Message.Boss_Edit_CannotBeModified.msg(e.getWhoClicked());
                        return;
                    }

                    bossEntity.getDrops().setDropTable(name);
                    this.bossesFileManager.save();

                    openFor((Player) e.getWhoClicked(), bossEntity);
                });
            }
        });
    }

}
