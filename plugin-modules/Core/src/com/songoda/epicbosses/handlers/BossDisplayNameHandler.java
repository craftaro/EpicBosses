package com.songoda.epicbosses.handlers;

import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.managers.files.BossesFileManager;
import com.songoda.epicbosses.utils.IHandler;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.panel.base.ISubVariablePanelHandler;
import lombok.Getter;
import lombok.Setter;
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

    @Getter private final ISubVariablePanelHandler<BossEntity, EntityStatsElement> panelHandler;

    @Getter private final EntityStatsElement entityStatsElement;
    @Getter private final BossesFileManager bossesFileManager;
    @Getter private final BossEntity bossEntity;
    @Getter private final Player player;

    @Getter @Setter private boolean handled = false;
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

                if(!uuid.equals(getPlayer().getUniqueId())) return;
                if(isHandled()) return;

                String input = event.getMessage();

                if(input.equalsIgnoreCase("-")) {
                    input = null;
                }

                getEntityStatsElement().getMainStats().setDisplayName(input);
                getBossesFileManager().save();
                setHandled(true);

                finish();
            }
        };
    }

    private void finish() {
        AsyncPlayerChatEvent.getHandlerList().unregister(this.listener);
        getPanelHandler().openFor(getPlayer(), getBossEntity(), getEntityStatsElement());
    }
}
