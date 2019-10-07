package com.songoda.epicbosses.panel.bosses;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.BossesFileManager;
import com.songoda.epicbosses.managers.files.ItemsFileManager;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.handlers.VariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 22-Nov-18
 */
public class TargetingEditorPanel extends VariablePanelHandler<BossEntity> {

    private BossesFileManager bossesFileManager;
    private ItemsFileManager itemsFileManager;

    public TargetingEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, EpicBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.bossesFileManager = plugin.getBossesFileManager();
        this.itemsFileManager = plugin.getItemStackManager();
    }

    @Override
    public void fillPanel(Panel panel, BossEntity bossEntity) {

    }

    @Override
    public void openFor(Player player, BossEntity bossEntity) {
        Map<String, String> replaceMap = new HashMap<>();
        String current = bossEntity.getTargetingValue();
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();
        PanelBuilderCounter counter = panelBuilder.getPanelBuilderCounter();

        replaceMap.put("{name}", BossAPI.getBossEntityName(bossEntity));
        replaceMap.put("{selected}", current);
        panelBuilder.addReplaceData(replaceMap);
        counter.addSpecialCounter("TargetingSystem");

        Panel panel = panelBuilder.getPanel()
                .setParentPanelHandler(this.bossPanelManager.getMainBossEditMenu(), bossEntity);

        counter.getSpecialSlotsWith("TargetingSystem").forEach((slot, returnValue) -> {
            ItemStack currentStack = panel.getInventory().getItem(slot);
            ItemStack newItemStack = getItemStack(current, (String) returnValue, currentStack);

            panel.setItem(slot, newItemStack , event -> {
                if(!bossEntity.isEditing()) {
                    Message.Boss_Edit_CannotBeModified.msg(event.getWhoClicked());
                    return;
                }

                bossEntity.setTargeting((String) returnValue);
                this.bossesFileManager.save();

                openFor(player, bossEntity);

            });
        });

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private ItemStack getItemStack(String current, String thisType, ItemStack currentItemStack) {
        ItemStack itemStack = this.itemsFileManager.getItemStackConverter().from(this.itemsFileManager.getItemStackHolder("DefaultSelectedTargetingItem"));
        ItemStack cloneStack = currentItemStack.clone();

        if(thisType.equalsIgnoreCase(current)) {
            cloneStack.setType(itemStack.getType());
        }

        return cloneStack;
    }
}
