package com.songoda.epicbosses.panel.autospawns;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.autospawns.AutoSpawn;
import com.songoda.epicbosses.autospawns.settings.AutoSpawnSettings;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.files.AutoSpawnFileManager;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.ObjectUtils;
import com.songoda.epicbosses.utils.panel.Panel;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import com.songoda.epicbosses.utils.panel.base.handlers.VariablePanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 07-Jan-19
 */
public class AutoSpawnSpecialSettingsEditorPanel extends VariablePanelHandler<AutoSpawn> {

    private AutoSpawnFileManager autoSpawnFileManager;
    private CustomBosses plugin;

    public AutoSpawnSpecialSettingsEditorPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder, CustomBosses plugin) {
        super(bossPanelManager, panelBuilder);

        this.autoSpawnFileManager = plugin.getAutoSpawnFileManager();
        this.plugin = plugin;
    }

    @Override
    public void fillPanel(Panel panel, AutoSpawn autoSpawn) {

    }

    @Override
    public void openFor(Player player, AutoSpawn autoSpawn) {
        Map<String, String> replaceMap = new HashMap<>();
        PanelBuilder panelBuilder = getPanelBuilder().cloneBuilder();
        AutoSpawnSettings autoSpawnSettings = autoSpawn.getAutoSpawnSettings();
        String shuffleEntities = ObjectUtils.getValue(autoSpawnSettings.getShuffleEntitiesList(), false)+"";
        String maxAliveAtOnce = NumberUtils.get().formatDouble(ObjectUtils.getValue(autoSpawnSettings.getMaxAliveAtOnce(), 1));
        String amountPerSpawn = NumberUtils.get().formatDouble(ObjectUtils.getValue(autoSpawnSettings.getAmountPerSpawn(), 1));
        String chunkIsntLoaded = ObjectUtils.getValue(autoSpawnSettings.getSpawnWhenChunkIsntLoaded(), false)+"";
        String overrideSpawnMessage = ObjectUtils.getValue(autoSpawnSettings.getOverrideDefaultSpawnMessage(), false)+"";
        String spawnMessage = ObjectUtils.getValue(autoSpawnSettings.getSpawnMessage(), "");

        replaceMap.put("{name}", BossAPI.getAutoSpawnName(autoSpawn));
        replaceMap.put("{shuffleEntities}", shuffleEntities);
        replaceMap.put("{maxAliveEntities}", maxAliveAtOnce);
        replaceMap.put("{amountPerSpawn}", amountPerSpawn);
        replaceMap.put("{chunkIsntLoaded}", chunkIsntLoaded);
        replaceMap.put("{overrideDefaultMessage}", overrideSpawnMessage);
        replaceMap.put("{spawnMessage}", spawnMessage);
        panelBuilder.addReplaceData(replaceMap);

        Panel panel = panelBuilder.getPanel()
                .setParentPanelHandler(this.bossPanelManager.getMainAutoSpawnEditPanel(), autoSpawn);
        PanelBuilderCounter counter = panel.getPanelBuilderCounter();

        counter.getSlotsWith("ShuffleEntities").forEach(slot -> panel.setOnClick(slot, getShuffleEntitiesButton(autoSpawn)));
        counter.getSlotsWith("MaxAliveEntities").forEach(slot -> panel.setOnClick(slot, getMaxAliveEntitiesButton(autoSpawn)));
        counter.getSlotsWith("AmountPerSpawn").forEach(slot -> panel.setOnClick(slot, getAmountPerSpawnButton(autoSpawn)));
        counter.getSlotsWith("ChunkIsntLoaded").forEach(slot -> panel.setOnClick(slot, getChunkIsntLoadedButton(autoSpawn)));
        counter.getSlotsWith("OverrideSpawnMessage").forEach(slot -> panel.setOnClick(slot, getOverrideSpawnMessageButton(autoSpawn)));
        counter.getSlotsWith("SpawnMessage").forEach(slot -> panel.setOnClick(slot, event -> this.bossPanelManager.getAutoSpawnMessageEditorPanel().openFor((Player) event.getWhoClicked(), autoSpawn)));

        panel.openFor(player);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {

    }

    private ClickAction getShuffleEntitiesButton(AutoSpawn autoSpawn) {
        return event -> {
            if(isBlocked(autoSpawn, event)) return;

            AutoSpawnSettings settings = autoSpawn.getAutoSpawnSettings();

            settings.setShuffleEntitiesList(!ObjectUtils.getValue(settings.getShuffleEntitiesList(), false));
            save(autoSpawn, event);
        };
    }

    private ClickAction getMaxAliveEntitiesButton(AutoSpawn autoSpawn) {
        return event -> {
            if(isBlocked(autoSpawn, event)) return;

            AutoSpawnSettings settings = autoSpawn.getAutoSpawnSettings();
            ClickType clickType = event.getClick();
            int amountToModifyBy;

            if(clickType.name().contains("RIGHT")) {
                amountToModifyBy = -1;
            } else {
                amountToModifyBy = +1;
            }

            int currentAmount = ObjectUtils.getValue(settings.getMaxAliveAtOnce(), 1);
            int newAmount = currentAmount + amountToModifyBy;

            if(newAmount <= 1) newAmount = 1;

            settings.setMaxAliveAtOnce(newAmount);
            save(autoSpawn, event);
        };
    }

    private ClickAction getAmountPerSpawnButton(AutoSpawn autoSpawn) {
        return event -> {
            if(isBlocked(autoSpawn, event)) return;

            AutoSpawnSettings settings = autoSpawn.getAutoSpawnSettings();
            ClickType clickType = event.getClick();
            int amountToModifyBy;

            if(clickType.name().contains("RIGHT")) {
                amountToModifyBy = -1;
            } else {
                amountToModifyBy = +1;
            }

            int currentAmount = ObjectUtils.getValue(settings.getAmountPerSpawn(), 1);
            int newAmount = currentAmount + amountToModifyBy;

            if(newAmount <= 1) newAmount = 1;

            settings.setAmountPerSpawn(newAmount);
            save(autoSpawn, event);
        };
    }

    private ClickAction getChunkIsntLoadedButton(AutoSpawn autoSpawn) {
        return event -> {
            if(isBlocked(autoSpawn, event)) return;

            AutoSpawnSettings settings = autoSpawn.getAutoSpawnSettings();

            settings.setSpawnWhenChunkIsntLoaded(!ObjectUtils.getValue(settings.getSpawnWhenChunkIsntLoaded(), false));
            save(autoSpawn, event);
        };
    }

    private ClickAction getOverrideSpawnMessageButton(AutoSpawn autoSpawn) {
        return event -> {
            if(isBlocked(autoSpawn, event)) return;

            AutoSpawnSettings settings = autoSpawn.getAutoSpawnSettings();

            settings.setOverrideDefaultSpawnMessage(!ObjectUtils.getValue(settings.getOverrideDefaultSpawnMessage(), false));
            save(autoSpawn, event);
        };
    }

    private boolean isBlocked(AutoSpawn autoSpawn, InventoryClickEvent event) {
        if(!autoSpawn.isEditing()) {
            Message.Boss_AutoSpawn_MustToggleEditing.msg(event.getWhoClicked());
            return true;
        }

        return false;
    }

    private void save(AutoSpawn autoSpawn, InventoryClickEvent event) {
        this.autoSpawnFileManager.save();
        openFor((Player) event.getWhoClicked(), autoSpawn);
    }
}
