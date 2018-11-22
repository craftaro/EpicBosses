package com.songoda.epicbosses.panel.bosses;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.entity.elements.EquipmentElement;
import com.songoda.epicbosses.entity.elements.HandsElement;
import com.songoda.epicbosses.entity.elements.MainStatsElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.BossesFileManager;
import com.songoda.epicbosses.utils.itemstack.ItemStackConverter;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import com.songoda.epicbosses.utils.panel.base.VariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 22-Nov-18
 */
public abstract class BossListEditorPanel extends VariablePanelHandler<BossEntity> {

    protected final BossesFileManager bossesFileManager;

    private ItemStackConverter itemStackConverter;
    private CustomBosses plugin;
    private String type;

    public BossListEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin, String type) {
        super(bossPanelManager, panelBuilder);

        this.itemStackConverter = new ItemStackConverter();
        this.bossesFileManager = plugin.getBossesFileManager();
        this.plugin = plugin;
        this.type = type;
    }

    public abstract ClickAction getAction(BossEntity bossEntity, EntityStatsElement entityStatsElement);

    @Override
    public void fillPanel(Panel panel, BossEntity bossEntity) {
        List<EntityStatsElement> entityStatsElements = new ArrayList<>(bossEntity.getEntityStats());
        int slot = 0;

        for (EntityStatsElement entityStatsElement : entityStatsElements) {
            ItemStackHolder itemStackHolder = BossAPI.getStoredItemStack("DefaultBossListEditorMenuItem");
            ItemStack itemStack = this.itemStackConverter.from(itemStackHolder);

            Map<String, String> replaceMap = new HashMap<>();

            replaceMap.put("{position}", "" + entityStatsElement.getMainStats().getPosition());
            replaceMap.put("{targetType}", this.type);

            ItemStackUtils.applyDisplayName(itemStack, this.plugin.getConfig().getString("Display.BossListEditor.name"), replaceMap);
            ItemStackUtils.applyDisplayLore(itemStack, this.plugin.getConfig().getStringList("Display.BossListEditor.lore"), replaceMap);

            panel.setItem(slot, itemStack, getAction(bossEntity, entityStatsElement));
            slot++;
        }
    }

    @Override
    public void openFor(Player player, BossEntity bossEntity) {
        Map<String, String> replaceMap = new HashMap<>();

        replaceMap.put("{name}", BossAPI.getBossEntityName(bossEntity));

        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();

        panelBuilder.addReplaceData(replaceMap);

        int nextNumber = bossEntity.getEntityStats().size() + 1;
        Panel panel = panelBuilder.getPanel()
                .setDestroyWhenDone(true)
                .setCancelClick(true)
                .setCancelLowerClick(true)
                .setParentPanelHandler(this.bossPanelManager.getMainBossEditMenu(), bossEntity);

        fillPanel(panel, bossEntity);

        panel.getPanelBuilderCounter().getSlotsWith("CreateEntity").forEach(slot -> panel.setOnClick(slot, event -> {
            MainStatsElement mainStatsElement = new MainStatsElement(nextNumber, "", 50.0, "");
            EquipmentElement equipmentElement = new EquipmentElement("", "", "", "");
            HandsElement handsElement = new HandsElement("", "");

            EntityStatsElement entityStatsElement = new EntityStatsElement(mainStatsElement, equipmentElement, handsElement, new ArrayList<>());

            bossEntity.getEntityStats().add(entityStatsElement);
            this.bossesFileManager.save();

            openFor((Player) event.getWhoClicked(), bossEntity);
        }));

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {
        PanelBuilderCounter panelBuilderCounter = panelBuilder.getPanelBuilderCounter();

        panelBuilderCounter.addSlotCounter("CreateEntity");
    }
}