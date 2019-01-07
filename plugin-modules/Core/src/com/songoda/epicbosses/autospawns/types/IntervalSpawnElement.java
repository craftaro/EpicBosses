package com.songoda.epicbosses.autospawns.types;

import com.google.gson.annotations.Expose;
import com.songoda.epicbosses.autospawns.AutoSpawn;
import com.songoda.epicbosses.autospawns.IAutoSpawnCustomSettingsHandler;
import com.songoda.epicbosses.autospawns.handlers.IntervalSpawnHandler;
import com.songoda.epicbosses.holder.autospawn.ActiveIntervalAutoSpawnHolder;
import com.songoda.epicbosses.managers.AutoSpawnManager;
import com.songoda.epicbosses.skills.interfaces.ICustomSettingAction;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
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

    private final IntervalSpawnHandler intervalSpawnHandler = new IntervalSpawnHandler();

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
    public List<ICustomSettingAction> getCustomSettingActions(AutoSpawn autoSpawn) {
        List<ICustomSettingAction> clickActions = new ArrayList<>();
        ItemStack clickStack = new ItemStack(Material.IRON_BLOCK);
        ClickAction lastBossKilledAction = this.intervalSpawnHandler.getSpawnAfterLastBossIsKilledAction(this);
        ClickAction locationAction = this.intervalSpawnHandler.getLocationAction(this);
        ClickAction placeholderAction = this.intervalSpawnHandler.getPlaceholderAction(this);
        ClickAction spawnRateAction = this.intervalSpawnHandler.getSpawnRateAction(this, autoSpawn);

        clickActions.add(AutoSpawnManager.createAutoSpawnAction("Spawn After Last Boss Is Killed", getSpawnAfterLastBossIsKilled()+"", this.intervalSpawnHandler.getSpawnAfterLastBossIsKilledExtraInformation(), clickStack.clone(), lastBossKilledAction));
        clickActions.add(AutoSpawnManager.createAutoSpawnAction("Location", getLocation(), this.intervalSpawnHandler.getLocationExtraInformation(), clickStack.clone(), locationAction));
        clickActions.add(AutoSpawnManager.createAutoSpawnAction("Placeholder", getPlaceholder(), this.intervalSpawnHandler.getPlaceholderExtraInformation(), clickStack.clone(), placeholderAction));
        clickActions.add(AutoSpawnManager.createAutoSpawnAction("Spawn Rate", getSpawnRate()+"", this.intervalSpawnHandler.getSpawnRateExtraInformation(), clickStack.clone(), spawnRateAction));

        return clickActions;
    }

    public boolean attemptSpawn(ActiveIntervalAutoSpawnHolder activeAutoSpawnHolder) {
        return this.intervalSpawnHandler.attemptSpawn(activeAutoSpawnHolder, this);
    }

    public Location getSpawnLocation() {
        return StringUtils.get().fromStringToLocation(this.location);
    }
}
