package com.songoda.epicbosses.handlers;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.autospawns.AutoSpawn;
import com.songoda.epicbosses.autospawns.types.IntervalSpawnElement;
import com.songoda.epicbosses.managers.files.AutoSpawnFileManager;
import com.songoda.epicbosses.utils.IHandler;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.panel.base.IVariablePanelHandler;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 08-Jan-19
 */
public abstract class AutoSpawnVariableHandler implements IHandler {

    @Getter private final IVariablePanelHandler<AutoSpawn> panelHandler;

    @Getter private final IntervalSpawnElement intervalSpawnElement;
    @Getter private final AutoSpawnFileManager autoSpawnFileManager;
    @Getter private final AutoSpawn autoSpawn;
    @Getter private final Player player;

    @Getter @Setter private boolean handled = false;
    private Listener listener;

    public AutoSpawnVariableHandler(Player player, AutoSpawn autoSpawn, IntervalSpawnElement intervalSpawnElement, AutoSpawnFileManager autoSpawnFileManager, IVariablePanelHandler<AutoSpawn> panelHandler) {
        this.player = player;
        this.autoSpawn = autoSpawn;
        this.panelHandler = panelHandler;
        this.autoSpawnFileManager = autoSpawnFileManager;
        this.intervalSpawnElement = intervalSpawnElement;

        this.listener = getListener();
    }

    protected abstract boolean confirmValue(String input, IntervalSpawnElement intervalSpawnElement);

    @Override
    public void handle() {
        ServerUtils.get().registerListener(this.listener);
    }

    private Listener getListener() {
        return new Listener() {
            @EventHandler
            public void onChat(AsyncPlayerChatEvent event) {
                Player player = event.getPlayer();
                UUID uuid = player.getUniqueId();

                if(!uuid.equals(getPlayer().getUniqueId())) return;
                if(isHandled()) return;

                String input = event.getMessage();

                if(input.equalsIgnoreCase("-")) {
                    input = null;
                }

                if(input == null) {
                    Bukkit.getScheduler().scheduleSyncDelayedTask(CustomBosses.get(), AutoSpawnVariableHandler.this::finish);
                    return;
                }

                if(!confirmValue(input, getIntervalSpawnElement())) return;

                getAutoSpawn().setCustomData(BossAPI.convertObjectToJsonObject(getIntervalSpawnElement()));
                getAutoSpawnFileManager().save();
                event.setCancelled(true);
                setHandled(true);

                Bukkit.getScheduler().scheduleSyncDelayedTask(CustomBosses.get(), AutoSpawnVariableHandler.this::finish);
            }
        };
    }

    private void finish() {
        AsyncPlayerChatEvent.getHandlerList().unregister(this.listener);
        getPanelHandler().openFor(getPlayer(), getAutoSpawn());
    }
}
