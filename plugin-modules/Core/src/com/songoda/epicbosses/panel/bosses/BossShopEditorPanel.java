package com.songoda.epicbosses.panel.bosses;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.handlers.BossShopPriceHandler;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.BossesFileManager;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.ObjectUtils;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import com.songoda.epicbosses.utils.panel.base.handlers.VariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 19-Jan-19
 */
public class BossShopEditorPanel extends VariablePanelHandler<BossEntity> {

    private BossesFileManager bossesFileManager;

    public BossShopEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.bossesFileManager = plugin.getBossesFileManager();
    }

    @Override
    public void fillPanel(Panel panel, BossEntity bossEntity) {

    }

    @Override
    public void openFor(Player player, BossEntity bossEntity) {
        Map<String, String> replaceMap = new HashMap<>();
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();
        PanelBuilderCounter counter = panelBuilder.getPanelBuilderCounter();

        replaceMap.put("{name}", BossAPI.getBossEntityName(bossEntity));
        replaceMap.put("{buyable}", bossEntity.isBuyable()+"");
        replaceMap.put("{price}", NumberUtils.get().formatDouble(ObjectUtils.getValue(bossEntity.getPrice(), 0.0)));
        panelBuilder.addReplaceData(replaceMap);

        Panel panel = panelBuilder.getPanel()
                .setParentPanelHandler(this.bossPanelManager.getMainBossEditMenu(), bossEntity);

        counter.getSlotsWith("Buyable").forEach(slot -> panel.setOnClick(slot, getBuyableAction(bossEntity)));
        counter.getSlotsWith("Price").forEach(slot -> panel.setOnClick(slot, event -> {
            if(!bossEntity.isEditing()) {
                Message.Boss_Edit_CannotBeModified.msg(event.getWhoClicked());
                return;
            }

            Player whoClick = (Player) event.getWhoClicked();
            BossShopPriceHandler bossShopPriceHandler = new BossShopPriceHandler(whoClick, bossEntity, this.bossesFileManager, this);

            Message.Boss_Edit_Price.msg(event.getWhoClicked(), BossAPI.getBossEntityName(bossEntity));
            whoClick.closeInventory();
            bossShopPriceHandler.handle();
        }));

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    public ClickAction getBuyableAction(BossEntity bossEntity) {
        return event -> {
            if(!bossEntity.isEditing()) {
                Message.Boss_Edit_CannotBeModified.msg(event.getWhoClicked());
                return;
            }

            boolean current = bossEntity.isBuyable();

            bossEntity.setBuyable(!current);
            this.bossesFileManager.save();
            openFor((Player) event.getWhoClicked(), bossEntity);
        };
    }


}
