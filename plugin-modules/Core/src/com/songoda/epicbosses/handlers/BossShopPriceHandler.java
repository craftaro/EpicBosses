package com.songoda.epicbosses.handlers;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.managers.files.BossesFileManager;
import com.songoda.epicbosses.utils.IHandler;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.NumberUtils;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.panel.base.IVariablePanelHandler;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.UUID;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 20-Jan-19
 */
public class BossShopPriceHandler implements IHandler {

    private final IVariablePanelHandler<BossEntity> panelHandler;

    private final BossesFileManager bossesFileManager;
    private final BossEntity bossEntity;
    private final Player player;

    private boolean handled = false;
    private Listener listener;

    public BossShopPriceHandler(Player player, BossEntity bossEntity, BossesFileManager bossesFileManager, IVariablePanelHandler<BossEntity> panelHandler) {
        this.player = player;
        this.bossEntity = bossEntity;
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

                event.setCancelled(true);

                if (NumberUtils.get().isDouble(input)) {
                    double amount = NumberUtils.get().getDouble(input);
                    getBossEntity().setPrice(amount);
                    getBossesFileManager().save();
                    setHandled(true);
                    Message.Boss_Edit_PriceSet.msg(getPlayer(), BossAPI.getBossEntityName(getBossEntity()), NumberUtils.get().formatDouble(amount));

                    Bukkit.getScheduler().scheduleSyncDelayedTask(EpicBosses.getInstance(), BossShopPriceHandler.this::finish);
                } else {
                    Message.Boss_Edit_Price.msg(getPlayer(), BossAPI.getBossEntityName(getBossEntity()));
                }
            }
        };
    }

    private void finish() {
        AsyncPlayerChatEvent.getHandlerList().unregister(this.listener);
        getPanelHandler().openFor(getPlayer(), getBossEntity());
    }

    public IVariablePanelHandler<BossEntity> getPanelHandler() {
        return this.panelHandler;
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
