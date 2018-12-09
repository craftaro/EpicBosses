package com.songoda.epicbosses.panel.bosses.text;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.elements.TauntElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.BossesFileManager;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import com.songoda.epicbosses.utils.panel.base.handlers.VariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 01-Dec-18
 */
public class TauntTextEditorPanel extends VariablePanelHandler<BossEntity> {

    private BossesFileManager bossesFileManager;

    public TauntTextEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.bossesFileManager = plugin.getBossesFileManager();
    }

    @Override
    public void fillPanel(Panel panel, BossEntity bossEntity) {

    }

    @Override
    public void openFor(Player player, BossEntity bossEntity) {
        Map<String, String> replaceMap = new HashMap<>();
        TauntElement tauntElement = bossEntity.getMessages().getTaunts();
        Integer radius = tauntElement.getRadius();
        Integer delay = tauntElement.getDelay();
        List<String> taunts = tauntElement.getTaunts();
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();

        if(radius == null) radius = 100;
        if(delay == null) delay = 60;
        if(taunts == null || taunts.isEmpty()) taunts = Arrays.asList("N/A");

        replaceMap.put("{name}", BossAPI.getBossEntityName(bossEntity));
        replaceMap.put("{radius}", NumberUtils.get().formatDouble(radius));
        replaceMap.put("{delay}", NumberUtils.get().formatDouble(delay));
        replaceMap.put("{taunts}", StringUtils.get().appendList(taunts));
        panelBuilder.addReplaceData(replaceMap);

        Panel panel = panelBuilder.getPanel()
                .setDestroyWhenDone(true)
                .setCancelClick(true)
                .setCancelLowerClick(true)
                .setParentPanelHandler(this.bossPanelManager.getMainTextEditMenu(), bossEntity);
        PanelBuilderCounter counter = panel.getPanelBuilderCounter();

        counter.getSlotsWith("Radius").forEach(slot -> panel.setOnClick(slot, getRadiusAction(bossEntity)));
        counter.getSlotsWith("Delay").forEach(slot -> panel.setOnClick(slot, getDelayAction(bossEntity)));
        counter.getSlotsWith("Taunts").forEach(slot -> panel.setOnClick(slot, event -> this.bossPanelManager.getOnTauntTextEditMenu().openFor((Player) event.getWhoClicked(), bossEntity)));

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private ClickAction getRadiusAction(BossEntity bossEntity) {
        return event -> {
            ClickType clickType = event.getClick();
            int radiusToModifyBy = 0;

            if(clickType == ClickType.LEFT) {
                radiusToModifyBy = 1;
            } else if(clickType == ClickType.SHIFT_LEFT) {
                radiusToModifyBy = 10;
            } else if(clickType == ClickType.RIGHT) {
                radiusToModifyBy = -1;
            } else if(clickType == ClickType.SHIFT_RIGHT) {
                radiusToModifyBy = -10;
            }

            String modifyValue = radiusToModifyBy > 0? "increased" : "decreased";
            Integer currentRadius = bossEntity.getMessages().getTaunts().getRadius();

            if(currentRadius == null) currentRadius = 0;

            int newRadius = currentRadius + radiusToModifyBy;

            if(newRadius < -1) {
                newRadius = -1;
            }

            bossEntity.getMessages().getTaunts().setRadius(newRadius);
            this.bossesFileManager.save();
            Message.Boss_Messages_SetTauntRadius.msg(event.getWhoClicked(), modifyValue, NumberUtils.get().formatDouble(newRadius));
            openFor((Player) event.getWhoClicked(), bossEntity);
        };
    }

    private ClickAction getDelayAction(BossEntity bossEntity) {
        return event -> {
            ClickType clickType = event.getClick();
            int delayToModifyBy = 0;

            if(clickType == ClickType.LEFT) {
                delayToModifyBy = 1;
            } else if(clickType == ClickType.SHIFT_LEFT) {
                delayToModifyBy = 10;
            } else if(clickType == ClickType.RIGHT) {
                delayToModifyBy = -1;
            } else if(clickType == ClickType.SHIFT_RIGHT) {
                delayToModifyBy = -10;
            }

            String modifyValue = delayToModifyBy > 0? "increased" : "decreased";
            Integer currentDelay = bossEntity.getMessages().getTaunts().getDelay();

            if(currentDelay == null) currentDelay = 0;

            int newDelay = currentDelay + delayToModifyBy;

            if(newDelay < -1) {
                newDelay = -1;
            }

            bossEntity.getMessages().getTaunts().setDelay(newDelay);
            this.bossesFileManager.save();
            Message.Boss_Messages_SetTauntDelay.msg(event.getWhoClicked(), modifyValue, NumberUtils.get().formatDouble(newDelay));
            openFor((Player) event.getWhoClicked(), bossEntity);
        };
    }
}
