package com.songoda.epicbosses.panel.autospawns;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.autospawns.AutoSpawn;
import com.songoda.epicbosses.autospawns.SpawnType;
import com.songoda.epicbosses.holder.ActiveAutoSpawnHolder;
import com.songoda.epicbosses.holder.autospawn.ActiveIntervalAutoSpawnHolder;
import com.songoda.epicbosses.managers.AutoSpawnManager;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.AutoSpawnFileManager;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.ObjectUtils;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import com.songoda.epicbosses.utils.panel.base.handlers.VariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 07-Jan-19
 */
public class MainAutoSpawnEditorPanel extends VariablePanelHandler<AutoSpawn> {

    private AutoSpawnFileManager autoSpawnFileManager;
    private AutoSpawnManager autoSpawnManager;

    public MainAutoSpawnEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.autoSpawnFileManager = plugin.getAutoSpawnFileManager();
        this.autoSpawnManager = plugin.getAutoSpawnManager();
    }

    @Override
    public void fillPanel(Panel panel, AutoSpawn autoSpawn) {

    }

    @Override
    public void openFor(Player player, AutoSpawn autoSpawn) {
        Map<String, String> replaceMap = new HashMap<>();
        String type = ObjectUtils.getValue(autoSpawn.getType(), "INTERVAL");
        String editing = ""+ObjectUtils.getValue(!autoSpawn.isLocked(), false);
        String entities = StringUtils.get().appendList(autoSpawn.getEntities());
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();

        replaceMap.put("{name}", BossAPI.getAutoSpawnName(autoSpawn));
        replaceMap.put("{type}", type);
        replaceMap.put("{editing}", editing);
        replaceMap.put("{entities}", entities);
        panelBuilder.addReplaceData(replaceMap);

        Panel panel = panelBuilder.getPanel();
        PanelBuilderCounter counter = panel.getPanelBuilderCounter();

        counter.getSlotsWith("Editing").forEach(slot -> panel.setOnClick(slot, getEditingAction(autoSpawn)));
        counter.getSlotsWith("SpecialSettings").forEach(slot -> {});
        counter.getSlotsWith("Type").forEach(slot -> {});
        counter.getSlotsWith("Entities").forEach(slot -> {});
        counter.getSlotsWith("CustomSettings").forEach(slot -> {});

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private ClickAction getEditingAction(AutoSpawn autoSpawn) {
        return event -> {
            Player player = (Player) event.getWhoClicked();

            if(autoSpawn.isCompleteEnoughToSpawn()) {
                autoSpawn.setEditing(!autoSpawn.isEditing());
                this.autoSpawnFileManager.save();
                player.closeInventory();

                Message.Boss_AutoSpawn_ToggleEditing.msg(player, BossAPI.getAutoSpawnName(autoSpawn), autoSpawn.isEditing());

                if(autoSpawn.isEditing()) {
                    this.autoSpawnManager.stopInterval(autoSpawn);
                } else {
                    ActiveAutoSpawnHolder autoSpawnHolder = this.autoSpawnManager.getActiveAutoSpawnHolder(autoSpawn);

                    if(autoSpawnHolder != null) {
                        if(autoSpawnHolder.getSpawnType() == SpawnType.INTERVAL) {
                            ActiveIntervalAutoSpawnHolder intervalAutoSpawnHolder = (ActiveIntervalAutoSpawnHolder) autoSpawnHolder;

                            intervalAutoSpawnHolder.restartInterval();
                        }
                    }
                }
            } else {
                Message.Boss_AutoSpawn_NotCompleteEnough.msg(player);
            }
        };
    }
}
