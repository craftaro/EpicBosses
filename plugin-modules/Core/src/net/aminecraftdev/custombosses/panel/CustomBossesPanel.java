package net.aminecraftdev.custombosses.panel;

import net.aminecraftdev.custombosses.CustomBosses;
import net.aminecraftdev.custombosses.api.BossAPI;
import net.aminecraftdev.custombosses.entity.BossEntity;
import net.aminecraftdev.custombosses.managers.BossPanelManager;
import net.aminecraftdev.custombosses.managers.files.BossItemFileManager;
import net.aminecraftdev.custombosses.managers.files.BossesFileManager;
import net.aminecraftdev.custombosses.utils.Debug;
import net.aminecraftdev.custombosses.utils.StringUtils;
import net.aminecraftdev.custombosses.utils.itemstack.holder.ItemStackHolder;
import net.aminecraftdev.custombosses.utils.panel.base.PanelHandler;
import net.aminecraftdev.custombosses.utils.panel.builder.PanelBuilder;
import org.bukkit.Material;
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

    private BossItemFileManager bossItemFileManager;
    private BossesFileManager bossesFileManager;

    public CustomBossesPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses customBosses) {
        super(bossPanelManager, panelBuilder);

        this.bossItemFileManager = customBosses.getItemStackManager();
        this.bossesFileManager = customBosses.getBossesFileManager();
        this.panel.setParentPanel(this.bossPanelManager.getMainMenu().getPanel());

        fillPanel();
    }

    @Override
    public void fillPanel() {
        Map<String, BossEntity> currentEntities = new HashMap<>(this.bossesFileManager.getBossEntities());
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
//                getPanel().setItem(i-startIndex, new ItemStack(Material.AIR), e -> {});
            } else {
                String name = entryList.get(i);
                BossEntity entity = bossEntityMap.get(name);
                ItemStackHolder itemStackHolder = BossAPI.getStoredItemStack(entity.getSpawnItem());

                if(itemStackHolder == null) {
                    Debug.FAILED_TO_LOAD_CUSTOM_ITEM.debug(entity.getSpawnItem(), name);
//                    getPanel().setItem(i-startIndex, new ItemStack(Material.AIR), e -> {});
                    continue;
                }

                ItemStack itemStack = this.bossItemFileManager.getItemStackConverter().from(itemStackHolder);

                if(itemStack == null) {
                    Debug.FAILED_TO_LOAD_CUSTOM_ITEM.debug(entity.getSpawnItem(), name);
//                    getPanel().setItem(i-startIndex, new ItemStack(Material.AIR), e -> {});
                    continue;
                }

                System.out.println("--- ITEM SET ---");
                System.out.println(itemStack);
                getPanel().setItem(i-startIndex, itemStack, e -> {});

                System.out.println(getPanel().getInventory().getItem(i-startIndex));
            }
        }
    }
}
