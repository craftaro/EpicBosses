package com.songoda.epicbosses.panel.droptables.types.give;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.droptable.DropTable;
import com.songoda.epicbosses.droptable.elements.GiveTableSubElement;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.panel.droptables.types.give.handlers.GiveRewardEditHandler;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.ObjectUtils;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.handlers.SubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 31-Dec-18
 */
public class GiveRewardMainEditPanel extends SubVariablePanelHandler<DropTable, GiveRewardEditHandler> {

    public GiveRewardMainEditPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);
    }

    @Override
    public void openFor(Player player, DropTable dropTable, GiveRewardEditHandler giveRewardEditHandler) {
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();
        Map<String, String> replaceMap = new HashMap<>();
        GiveTableSubElement giveTableSubElement = giveRewardEditHandler.getGiveTableSubElement();
        Integer itemDrops = giveTableSubElement.getItems().size();
        Integer commandDrops = giveTableSubElement.getCommands().size();
        Double requiredPercentage = ObjectUtils.getValue(giveTableSubElement.getRequiredPercentage(), 0.0);
        Integer maxDrops = ObjectUtils.getValue(giveTableSubElement.getMaxDrops(), 3);
        Integer maxCommands = ObjectUtils.getValue(giveTableSubElement.getMaxCommands(), 3);
        Boolean randomDrops = ObjectUtils.getValue(giveTableSubElement.getRandomDrops(), false);
        Boolean randomCommands = ObjectUtils.getValue(giveTableSubElement.getRandomCommands(), false);


        replaceMap.put("{name}", BossAPI.getDropTableName(dropTable));
        replaceMap.put("{position}", giveRewardEditHandler.getDamagePosition());
        replaceMap.put("{randomDrops}", ""+randomDrops);
        replaceMap.put("{maxDrops}", NumberUtils.get().formatDouble(maxDrops));
        replaceMap.put("{drops}", NumberUtils.get().formatDouble(itemDrops));
        replaceMap.put("{requiredPercentage}", NumberUtils.get().formatDouble(requiredPercentage));
        replaceMap.put("{commands}", NumberUtils.get().formatDouble(commandDrops));
        replaceMap.put("{maxCommands}", NumberUtils.get().formatDouble(maxCommands));
        replaceMap.put("{randomCommands}", ""+randomCommands);
        panelBuilder.addReplaceData(replaceMap);

        PanelBuilderCounter panelBuilderCounter = panelBuilder.getPanelBuilderCounter();
        Panel panel = panelBuilder.getPanel()
                .setParentPanelHandler(this.bossPanelManager.getGiveRewardRewardsListMenu(), dropTable, giveRewardEditHandler.getGiveTableElement(), giveRewardEditHandler.getDamagePosition());

        //handle buttons

        panel.openFor(player);
    }

    @Override
    public void fillPanel(Panel panel, DropTable dropTable, GiveRewardEditHandler giveRewardEditHandler) {

    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }
}
