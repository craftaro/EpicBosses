package com.songoda.epicbosses.handlers;

import com.songoda.epicbosses.autospawns.AutoSpawn;
import com.songoda.epicbosses.managers.files.AutoSpawnFileManager;
import com.songoda.epicbosses.utils.IHandler;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.panel.base.IVariablePanelHandler;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 07-Jan-19
 */
public class AutoSpawnLocationHandler implements IHandler {

    @Getter private final IVariablePanelHandler<AutoSpawn> panelHandler;

    @Getter private AutoSpawnFileManager autoSpawnFileManager;
    @Getter private final AutoSpawn autoSpawn;
    @Getter private final Player player;

    @Getter @Setter private boolean handled = false;
    private Listener listener;

    public AutoSpawnLocationHandler(Player player, AutoSpawn autoSpawn, AutoSpawnFileManager autoSpawnFileManager, IVariablePanelHandler<AutoSpawn> panelHandler) {
        this.player = player;
        this.autoSpawn = autoSpawn;
        this.panelHandler = panelHandler;
        this.autoSpawnFileManager = autoSpawnFileManager;

        this.listener = getListener();
    }

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

                }

                Location location = StringUtils.get().fromStringToLocation(input);

                if(location == null) {
                    Message.Boss_AutoSpawn_InvalidLocation.msg(getPlayer(), input);
                    return;
                }

                autoSpawn.getIntervalSpawnData().setLocation(input);
                getAutoSpawnFileManager().save();
                event.setCancelled(true);
                setHandled(true);

                finish();
            }
        };
    }

    private void finish() {
        AsyncPlayerChatEvent.getHandlerList().unregister(this.listener);
        getPanelHandler().openFor(getPlayer(), getAutoSpawn());
    }
}
