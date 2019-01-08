package com.songoda.epicbosses.autospawns.handlers;

import com.google.gson.JsonObject;
import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.autospawns.AutoSpawn;
import com.songoda.epicbosses.autospawns.settings.AutoSpawnSettings;
import com.songoda.epicbosses.autospawns.types.IntervalSpawnElement;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.handlers.AutoSpawnVariableHandler;
import com.songoda.epicbosses.handlers.variables.AutoSpawnLocationVariableHandler;
import com.songoda.epicbosses.handlers.variables.AutoSpawnPlaceholderVariableHandler;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.holder.autospawn.ActiveIntervalAutoSpawnHolder;
import com.songoda.epicbosses.listeners.IBossDeathHandler;
import com.songoda.epicbosses.utils.*;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import com.songoda.epicbosses.utils.panel.base.handlers.VariablePanelHandler;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.*;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 07-Jan-19
 */
public class IntervalSpawnHandler {

    public boolean attemptSpawn(ActiveIntervalAutoSpawnHolder activeAutoSpawnHolder, IntervalSpawnElement intervalSpawnElement) {
        IBossDeathHandler bossDeathHandler = activeAutoSpawnHolder.getPostDeathHandler();
        AutoSpawn autoSpawn = activeAutoSpawnHolder.getAutoSpawn();
        AutoSpawnSettings autoSpawnSettings = autoSpawn.getAutoSpawnSettings();
        boolean customSpawnMessage = ObjectUtils.getValue(autoSpawnSettings.getOverrideDefaultSpawnMessage(), false);
        String spawnMessage = autoSpawnSettings.getSpawnMessage();
        int amountToSpawn = ObjectUtils.getValue(autoSpawnSettings.getAmountPerSpawn(), 1);
        boolean shuffleList = ObjectUtils.getValue(autoSpawnSettings.getShuffleEntitiesList(), false);
        List<String> bosses = autoSpawn.getEntities();
        Location location = intervalSpawnElement.getSpawnLocation();

        if(bosses == null || bosses.isEmpty()) return false;

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

    public ClickAction getSpawnAfterLastBossIsKilledAction(IntervalSpawnElement intervalSpawnElement) {
        return event -> {
            
        };
    }

    public List<String> getSpawnAfterLastBossIsKilledExtraInformation() {
        return Arrays.asList("&7Click here to toggle the timer", "&7being enabled/reset after the last boss is killed.", "&7This will make it so only one active", "&7boss from this section can be spawned", "&7at a time.");
    }

    public ClickAction getLocationAction(IntervalSpawnElement intervalSpawnElement, AutoSpawn autoSpawn, VariablePanelHandler<AutoSpawn> variablePanelHandler) {
        return event -> {
            Player player = (Player) event.getWhoClicked();
            AutoSpawnVariableHandler autoSpawnVariableHandler = new AutoSpawnLocationVariableHandler(player, autoSpawn, intervalSpawnElement, CustomBosses.get().getAutoSpawnFileManager(), variablePanelHandler);

            Message.Boss_AutoSpawn_SetLocation.msg(player);
            autoSpawnVariableHandler.handle();
            player.closeInventory();
        };
    }

    public List<String> getLocationExtraInformation() {
        return Arrays.asList("&7Click here to update the location", "&7of this interval spawn section.", "&7", "&7This will ask you to put something in chat", "&7in the specific format of the new", "&7location.");
    }

    public ClickAction getPlaceholderAction(IntervalSpawnElement intervalSpawnElement, AutoSpawn autoSpawn, VariablePanelHandler<AutoSpawn> variablePanelHandler) {
        return event -> {
            Player player = (Player) event.getWhoClicked();
            AutoSpawnVariableHandler autoSpawnVariableHandler = new AutoSpawnPlaceholderVariableHandler(player, autoSpawn, intervalSpawnElement, CustomBosses.get().getAutoSpawnFileManager(), variablePanelHandler);

            Message.Boss_AutoSpawn_SetLocation.msg(player);
            autoSpawnVariableHandler.handle();
            player.closeInventory();
        };
    }

    public List<String> getPlaceholderExtraInformation() {
        return Arrays.asList("&7Click here to modify the placeholder", "&7that is used to display the timer for this", "&7auto spawn section in a hologram or", "&7through PlaceholderAPI.");
    }

    public ClickAction getSpawnRateAction(IntervalSpawnElement intervalSpawnElement, AutoSpawn autoSpawn) {
        return event -> {
            ClickType clickType = event.getClick();
            int amountToModifyBy;

            if(clickType == ClickType.SHIFT_LEFT) {
                amountToModifyBy = 10;
            } else if(clickType == ClickType.RIGHT) {
                amountToModifyBy = -1;
            } else if(clickType == ClickType.SHIFT_RIGHT) {
                amountToModifyBy = -10;
            } else {
                amountToModifyBy = 1;
            }

            int currentAmount = ObjectUtils.getValue(intervalSpawnElement.getSpawnRate(), 30);
            String modifyValue;
            int newAmount;

            if(amountToModifyBy > 0) {
                modifyValue = "increased";
                newAmount = currentAmount + amountToModifyBy;
            } else {
                modifyValue = "decreased";
                newAmount = currentAmount + amountToModifyBy;
            }

            if(newAmount <= 0) {
                newAmount = 0;
            }

            intervalSpawnElement.setSpawnRate(newAmount);

            JsonObject jsonObject = BossAPI.convertObjectToJsonObject(intervalSpawnElement);

            autoSpawn.setCustomData(jsonObject);
            CustomBosses.get().getAutoSpawnFileManager().save();
            Message.Boss_AutoSpawn_SpawnRate.msg(event.getWhoClicked(), modifyValue, NumberUtils.get().formatDouble(newAmount));
        };
    }

    public List<String> getSpawnRateExtraInformation() {
        List<String> extraInformation = new ArrayList<>();

        extraInformation.add("&7Click here to modify the spawn rate of");
        extraInformation.add("&7this interval section. The rate is calculated");
        extraInformation.add("&7by a minute.");
        extraInformation.add("&7");
        extraInformation.add("&31 Hour is &f60");
        extraInformation.add("&35 Hours is &f300");
        extraInformation.add("&312 Hours is &f720");
        extraInformation.add("&7");
        extraInformation.add("&bLeft Click &8» &f+1");
        extraInformation.add("&bShift Left-Click &8» &f+10");
        extraInformation.add("&7");
        extraInformation.add("&bRight Click &8» &f-1");
        extraInformation.add("&bShift Right-Click &8» &f-10");

        return extraInformation;
    }

}
