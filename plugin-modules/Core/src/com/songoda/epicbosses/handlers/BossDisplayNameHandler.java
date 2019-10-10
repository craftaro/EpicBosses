package com.songoda.epicbosses.handlers;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.managers.files.BossesFileManager;
import com.songoda.epicbosses.utils.IHandler;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.panel.base.ISubVariablePanelHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Nov-18
 */
public class BossDisplayNameHandler implements IHandler {

    private final ISubVariablePanelHandler<BossEntity, EntityStatsElement> panelHandler;

    private final EntityStatsElement entityStatsElement;
    private final BossesFileManager bossesFileManager;
    private final BossEntity bossEntity;
    private final Player player;

    private boolean handled = false;
    private Listener listener;

    public BossDisplayNameHandler(Player player, BossEntity bossEntity, EntityStatsElement entityStatsElement, BossesFileManager bossesFileManager, ISubVariablePanelHandler<BossEntity, EntityStatsElement> panelHandler) {
        this.entityStatsElement = entityStatsElement;
        this.bossEntity = bossEntity;
        this.player = player;
        this.bossesFileManager = bossesFileManager;
        this.panelHandler = panelHandler;

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

                if (!uuid.equals(getPlayer().getUniqueId())) return;
                if (isHandled()) return;

                String input = event.getMessage();

                if (input.equalsIgnoreCase("-")) {
                    input = null;
                }

                getEntityStatsElement().getMainStats().setDisplayName(input);
                getBossesFileManager().save();
                event.setCancelled(true);
                setHandled(true);

                Bukkit.getScheduler().scheduleSyncDelayedTask(EpicBosses.getInstance(), BossDisplayNameHandler.this::finish);
            }
        };
    }

    private void finish() {
        AsyncPlayerChatEvent.getHandlerList().unregister(this.listener);
        getPanelHandler().openFor(getPlayer(), getBossEntity(), getEntityStatsElement());
    }

    public ISubVariablePanelHandler<BossEntity, EntityStatsElement> getPanelHandler() {
        return this.panelHandler;
    }

    public EntityStatsElement getEntityStatsElement() {
        return this.entityStatsElement;
    }

    public BossesFileManager getBossesFileManager() {
        return this.bossesFileManager;
    }

    public BossEntity getBossEntity() {
        return this.bossEntity;
    }

    public Player getPlayer() {
        return this.player;
    }

    public boolean isHandled() {
        return this.handled;
    }

    public void setHandled(boolean handled) {
        this.handled = handled;
    }
}
