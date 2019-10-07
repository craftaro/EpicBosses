package com.songoda.epicbosses.autospawns.handlers;

import com.google.gson.JsonObject;
import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.autospawns.AutoSpawn;
import com.songoda.epicbosses.autospawns.types.IntervalSpawnElement;
import com.songoda.epicbosses.handlers.AutoSpawnVariableHandler;
import com.songoda.epicbosses.handlers.variables.AutoSpawnLocationVariableHandler;
import com.songoda.epicbosses.handlers.variables.AutoSpawnPlaceholderVariableHandler;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.ObjectUtils;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import com.songoda.epicbosses.utils.panel.base.handlers.VariablePanelHandler;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 07-Jan-19
 */
public class IntervalSpawnHandler {

    public ClickAction getSpawnAfterLastBossIsKilledAction(IntervalSpawnElement intervalSpawnElement, AutoSpawn autoSpawn, VariablePanelHandler<AutoSpawn> panelHandler) {
        return event -> {
            Player player = (Player) event.getWhoClicked();

            intervalSpawnElement.setSpawnAfterLastBossIsKilled(!ObjectUtils.getValue(intervalSpawnElement.getSpawnAfterLastBossIsKilled(), false));
            autoSpawn.setCustomData(BossAPI.convertObjectToJsonObject(intervalSpawnElement));
            EpicBosses.getInstance().getAutoSpawnFileManager().save();

            panelHandler.openFor(player, autoSpawn);
        };
    }

    public List<String> getSpawnAfterLastBossIsKilledExtraInformation() {
        return Arrays.asList("&7Click here to toggle the timer", "&7being enabled/reset after the last boss is killed.", "&7This will make it so only one active", "&7boss from this section can be spawned", "&7at a time.");
    }

    public ClickAction getLocationAction(IntervalSpawnElement intervalSpawnElement, AutoSpawn autoSpawn, VariablePanelHandler<AutoSpawn> variablePanelHandler) {
        return event -> {
            Player player = (Player) event.getWhoClicked();
            AutoSpawnVariableHandler autoSpawnVariableHandler = new AutoSpawnLocationVariableHandler(player, autoSpawn, intervalSpawnElement, EpicBosses.getInstance().getAutoSpawnFileManager(), variablePanelHandler);

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
            AutoSpawnVariableHandler autoSpawnVariableHandler = new AutoSpawnPlaceholderVariableHandler(player, autoSpawn, intervalSpawnElement, EpicBosses.getInstance().getAutoSpawnFileManager(), variablePanelHandler);

            Message.Boss_AutoSpawn_SetPlaceholder.msg(player);
            autoSpawnVariableHandler.handle();
            player.closeInventory();
        };
    }

    public List<String> getPlaceholderExtraInformation() {
        return Arrays.asList("&7Click here to modify the placeholder", "&7that is used to display the timer for this", "&7auto spawn section in a hologram or", "&7through PlaceholderAPI.");
    }

    public ClickAction getSpawnRateAction(IntervalSpawnElement intervalSpawnElement, AutoSpawn autoSpawn, VariablePanelHandler<AutoSpawn> panelHandler) {
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
            EpicBosses.getInstance().getAutoSpawnFileManager().save();
            Message.Boss_AutoSpawn_SpawnRate.msg(event.getWhoClicked(), modifyValue, NumberUtils.get().formatDouble(newAmount));
            panelHandler.openFor((Player) event.getWhoClicked(), autoSpawn);
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
