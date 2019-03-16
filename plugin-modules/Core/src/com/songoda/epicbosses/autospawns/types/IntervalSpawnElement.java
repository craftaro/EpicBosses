package com.songoda.epicbosses.autospawns.types;

import com.google.gson.annotations.Expose;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.autospawns.AutoSpawn;
import com.songoda.epicbosses.autospawns.IAutoSpawnCustomSettingsHandler;
import com.songoda.epicbosses.autospawns.handlers.IntervalSpawnHandler;
import com.songoda.epicbosses.autospawns.settings.AutoSpawnSettings;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.holder.autospawn.ActiveIntervalAutoSpawnHolder;
import com.songoda.epicbosses.listeners.IBossDeathHandler;
import com.songoda.epicbosses.managers.AutoSpawnManager;
import com.songoda.epicbosses.skills.interfaces.ICustomSettingAction;
import com.songoda.epicbosses.utils.*;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import com.songoda.epicbosses.utils.panel.base.handlers.VariablePanelHandler;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Jan-19
 */
public class IntervalSpawnElement implements IAutoSpawnCustomSettingsHandler {

    @Expose @Getter @Setter private Boolean spawnAfterLastBossIsKilled;
    @Expose @Getter @Setter private String location, placeholder;
    @Expose @Getter @Setter private Integer spawnRate;

    public IntervalSpawnElement(String location, String placeholder, Integer spawnRate, boolean spawnAfterLastBossIsKilled) {
        this.location = location;
        this.placeholder = placeholder;
        this.spawnRate = spawnRate;
        this.spawnAfterLastBossIsKilled = spawnAfterLastBossIsKilled;
    }

    @Override
    public List<ICustomSettingAction> getCustomSettingActions(AutoSpawn autoSpawn, VariablePanelHandler<AutoSpawn> variablePanelHandler) {
        List<ICustomSettingAction> clickActions = new ArrayList<>();
        ItemStack clickStack = new ItemStack(Material.IRON_BLOCK);
        IntervalSpawnHandler intervalSpawnHandler = new IntervalSpawnHandler();

        ClickAction lastBossKilledAction = intervalSpawnHandler.getSpawnAfterLastBossIsKilledAction(this, autoSpawn, variablePanelHandler);
        ClickAction locationAction = intervalSpawnHandler.getLocationAction(this, autoSpawn, variablePanelHandler);
        ClickAction placeholderAction = intervalSpawnHandler.getPlaceholderAction(this, autoSpawn, variablePanelHandler);
        ClickAction spawnRateAction = intervalSpawnHandler.getSpawnRateAction(this, autoSpawn, variablePanelHandler);

        clickActions.add(AutoSpawnManager.createAutoSpawnAction("Spawn After Last Boss Is Killed", getSpawnAfterLastBossIsKilled()+"", intervalSpawnHandler.getSpawnAfterLastBossIsKilledExtraInformation(), clickStack.clone(), lastBossKilledAction));
        clickActions.add(AutoSpawnManager.createAutoSpawnAction("Location", getLocation(), intervalSpawnHandler.getLocationExtraInformation(), clickStack.clone(), locationAction));
        clickActions.add(AutoSpawnManager.createAutoSpawnAction("Placeholder", getPlaceholder(), intervalSpawnHandler.getPlaceholderExtraInformation(), clickStack.clone(), placeholderAction));
        clickActions.add(AutoSpawnManager.createAutoSpawnAction("Spawn Rate", getSpawnRate()+"", intervalSpawnHandler.getSpawnRateExtraInformation(), clickStack.clone(), spawnRateAction));

        return clickActions;
    }

    public boolean attemptSpawn(ActiveIntervalAutoSpawnHolder activeAutoSpawnHolder) {
        IBossDeathHandler bossDeathHandler = activeAutoSpawnHolder.getPostDeathHandler();
        AutoSpawn autoSpawn = activeAutoSpawnHolder.getAutoSpawn();
        AutoSpawnSettings autoSpawnSettings = autoSpawn.getAutoSpawnSettings();
        boolean customSpawnMessage = ObjectUtils.getValue(autoSpawnSettings.getOverrideDefaultSpawnMessage(), false);
        String spawnMessage = autoSpawnSettings.getSpawnMessage();
        int amountToSpawn = ObjectUtils.getValue(autoSpawnSettings.getAmountPerSpawn(), 1);
        boolean shuffleList = ObjectUtils.getValue(autoSpawnSettings.getShuffleEntitiesList(), false);
        List<String> bosses = autoSpawn.getEntities();
        Location location = getSpawnLocation();

        if(bosses == null || bosses.isEmpty()) {
            ServerUtils.get().logDebug("BOSSES IS EMPTY!");
            return false;
        }

        if(shuffleList) Collections.shuffle(bosses);

        Queue<String> queue = new LinkedList<>(bosses);

        for(int i = 1; i <= amountToSpawn; i++) {
            if(queue.isEmpty()) queue = new LinkedList<>(bosses);

            BossEntity bossEntity = BossAPI.getBossEntity(queue.poll());
            ActiveBossHolder activeBossHolder = BossAPI.spawnNewBoss(bossEntity, location, null, null, customSpawnMessage);

            if(activeBossHolder == null) continue;

            activeBossHolder.getPostBossDeathHandlers().add(bossDeathHandler);
            activeAutoSpawnHolder.getActiveBossHolders().add(activeBossHolder);
        }

        if(customSpawnMessage && spawnMessage != null) {
            String x = NumberUtils.get().formatDouble(location.getBlockX());
            String y = NumberUtils.get().formatDouble(location.getBlockY());
            String z = NumberUtils.get().formatDouble(location.getBlockZ());
            String world = StringUtils.get().formatString(location.getWorld().getName());

            List<String> spawnMessages = BossAPI.getStoredMessages(spawnMessage);

            if(spawnMessages != null) {
                spawnMessages.replaceAll(s -> s.replace("{x}", x).replace("{y}", y).replace("{z}", z).replace("{world}", world));

                MessageUtils.get().sendMessage(location, -1, spawnMessages);
            }
        }

        return true;
    }

    public Location getSpawnLocation() {
        return StringUtils.get().fromStringToLocation(this.location);
    }
}
