package net.aminecraftdev.custombosses.panel;

import net.aminecraftdev.custombosses.CustomBosses;
import net.aminecraftdev.custombosses.api.BossAPI;
import net.aminecraftdev.custombosses.entity.BossEntity;
import net.aminecraftdev.custombosses.managers.BossPanelManager;
import net.aminecraftdev.custombosses.utils.Debug;
import net.aminecraftdev.custombosses.utils.itemstack.ItemStackUtils;
import net.aminecraftdev.custombosses.utils.itemstack.holder.ItemStackHolder;
import net.aminecraftdev.custombosses.utils.panel.base.PanelHandler;
import net.aminecraftdev.custombosses.utils.panel.builder.PanelBuilder;
import org.bukkit.Material;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 10-Oct-18
 */
public class CustomBossesPanel extends PanelHandler {

    private CustomBosses customBosses;

    public CustomBossesPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses customBosses) {
        super(bossPanelManager, panelBuilder);

        this.customBosses = customBosses;
        this.panel.setParentPanel(this.bossPanelManager.getMainMenu().getPanel());

        fillPanel();
    }

    @Override
    public void fillPanel() {
        Map<String, BossEntity> currentEntities = new HashMap<>(this.customBosses.getBossesFileManager().getBossEntities());
        List<String> entryList = new ArrayList<>(currentEntities.keySet());
        int maxPage = this.panel.getMaxPage(entryList);

        this.panel.setOnPageChange(((player, currentPage, requestedPage) -> {
            if(requestedPage < 0 || requestedPage > maxPage) return false;

            loadPage(requestedPage, currentEntities, entryList);
            return true;
        }));

        loadPage(0, currentEntities, entryList);
    }

    private void loadPage(int page, Map<String, BossEntity> bossEntityMap, List<String> entryList) {
        int fillTo = getPanel().getPanelBuilderSettings().getFillTo();
        int startIndex = page * fillTo;

        for(int i = startIndex; i < startIndex + fillTo; i++) {
            if(i >= bossEntityMap.size()) {
                getPanel().setItem(i-startIndex, new ItemStack(Material.AIR), e -> {});
            } else {
                String name = entryList.get(i);
                BossEntity entity = bossEntityMap.get(name);
                ItemStackHolder itemStackHolder = BossAPI.getStoredItemStack(entity.getSpawnItem());

                if(itemStackHolder == null) {
                    Debug.FAILED_TO_LOAD_CUSTOM_ITEM.debug(entity.getSpawnItem(), name);
                    getPanel().setItem(i-startIndex, new ItemStack(Material.AIR), e -> {});
                    continue;
                }

                ItemStack itemStack = this.customBosses.getItemStackManager().getItemStackConverter().from(itemStackHolder);

                if(itemStack == null) {
                    Debug.FAILED_TO_LOAD_CUSTOM_ITEM.debug(entity.getSpawnItem(), name);
                    getPanel().setItem(i-startIndex, new ItemStack(Material.AIR), e -> {});
                    continue;
                }

                Map<String, String> replaceMap = new HashMap<>();
                ItemStack clone = itemStack.clone();

                replaceMap.put("{name}", name);
                replaceMap.put("{enabled}", ""+!entity.isEditing());

                ItemStackUtils.applyDisplayName(clone, this.customBosses.getConfig().getString("Display.Bosses.name"), replaceMap);
                ItemStackUtils.applyDisplayLore(clone, this.customBosses.getConfig().getStringList("Display.Bosses.lore"), replaceMap);

                getPanel().setItem(i-startIndex, clone, e -> {
                    if(e.getClick() == ClickType.LEFT || e.getClick() == ClickType.SHIFT_LEFT) {
                        //TODO
                    } else if(e.getClick() == ClickType.RIGHT || e.getClick() == ClickType.SHIFT_RIGHT) {
                        e.getWhoClicked().getInventory().addItem(itemStack.clone());
                    }
                });
            }
        }
    }
}
