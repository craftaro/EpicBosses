package com.songoda.epicbosses.handlers;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.managers.files.SkillsFileManager;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.utils.IHandler;
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
 * @since 02-Dec-18
 */
public class SkillDisplayNameHandler implements IHandler {

    private final IVariablePanelHandler<Skill> panelHandler;
    private final SkillsFileManager skillsFileManager;
    private final Player player;
    private final Skill skill;

    private boolean handled = false;
    private Listener listener;

    public SkillDisplayNameHandler(Player player, Skill skill, SkillsFileManager skillsFileManager, IVariablePanelHandler<Skill> panelHandler) {
        this.skill = skill;
        this.player = player;
        this.skillsFileManager = skillsFileManager;
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

                getSkill().setDisplayName(input);
                getSkillsFileManager().save();
                event.setCancelled(true);
                setHandled(true);

                Bukkit.getScheduler().scheduleSyncDelayedTask(EpicBosses.getInstance(), SkillDisplayNameHandler.this::finish);
            }
        };
    }

    private void finish() {
        AsyncPlayerChatEvent.getHandlerList().unregister(this.listener);
        getPanelHandler().openFor(getPlayer(), getSkill());
    }

    public IVariablePanelHandler<Skill> getPanelHandler() {
        return this.panelHandler;
    }

    public SkillsFileManager getSkillsFileManager() {
        return this.skillsFileManager;
    }

    public Player getPlayer() {
        return this.player;
    }

    public Skill getSkill() {
        return this.skill;
    }

    public boolean isHandled() {
        return this.handled;
    }

    public void setHandled(boolean handled) {
        this.handled = handled;
    }
}