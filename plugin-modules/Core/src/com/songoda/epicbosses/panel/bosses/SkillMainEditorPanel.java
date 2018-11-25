package com.songoda.epicbosses.panel.bosses;

import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import com.songoda.epicbosses.utils.panel.base.handlers.VariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 25-Nov-18
 */
public class SkillMainEditorPanel extends VariablePanelHandler<BossEntity> {

    public SkillMainEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder) {
        super(bossPanelManager, panelBuilder);
    }

    @Override
    public void fillPanel(Panel panel, BossEntity bossEntity) {

    }

    @Override
    public void openFor(Player player, BossEntity bossEntity) {
        Map<String, String> replaceMap = new HashMap<>();

        replaceMap.put("{name}", BossAPI.getBossEntityName(bossEntity));

        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();

        panelBuilder.addReplaceData(replaceMap);

        Panel panel = panelBuilder.getPanel()
                .setDestroyWhenDone(true)
                .setCancelClick(true)
                .setCancelLowerClick(true);
        PanelBuilderCounter counter = panel.getPanelBuilderCounter();

        fillPanel(panel, bossEntity);
        counter.getSlotsWith("OverallChance").forEach(slot -> panel.setOnClick(slot, getOverallChanceAction(bossEntity)));
        counter.getSlotsWith("SkillList").forEach(slot -> panel.setOnClick(slot, getSkillListAction()));
        counter.getSlotsWith("Message").forEach(slot -> panel.setOnClick(slot, getMessageAction()));

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {
        PanelBuilderCounter panelBuilderCounter = panelBuilder.getPanelBuilderCounter();

        panelBuilderCounter
                .addSlotCounter("OverallChance")
                .addSlotCounter("SkillList")
                .addSlotCounter("Message");
    }

    private ClickAction getOverallChanceAction(BossEntity bossEntity) {
        return event -> {
            ClickType clickType = event.getClick();
            double chanceToModifyBy = 0.0;

            if(clickType == ClickType.LEFT) {
                chanceToModifyBy = 1.0;
            } else if(clickType == ClickType.SHIFT_LEFT) {
                chanceToModifyBy = 0.1;
            } else if(clickType == ClickType.RIGHT) {
                chanceToModifyBy = -1.0;
            } else if(clickType == ClickType.SHIFT_RIGHT) {
                chanceToModifyBy = -0.1;
            }

            String modifyValue = chanceToModifyBy > 0.0? "increased" : "decreased";
            Double currentChance = bossEntity.getSkills().getOverallChance();

            if(currentChance == null) currentChance = 0.0;

            double newChance = currentChance + chanceToModifyBy;

            if(newChance < 0.0) {
                newChance = 0.0;
            }

            bossEntity.getSkills().setOverallChance(newChance);
            Message.Boss_Skill_SetChance.msg(event.getWhoClicked(), modifyValue, NumberUtils.get().formatDouble(newChance));
            openFor((Player) event.getWhoClicked(), bossEntity);
        };
    }

    private ClickAction getSkillListAction() {
        return event -> {};
    }

    private ClickAction getMessageAction() {
        return event -> {};
    }
}
