package com.songoda.epicbosses.managers;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.commands.boss.*;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.ILoadable;
import com.songoda.epicbosses.utils.command.SubCommandService;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Oct-18
 */
public class BossCommandManager implements ILoadable {

    private SubCommandService<?> commandService;
    private boolean hasBeenLoaded = false;
    private CustomBosses customBosses;

    public BossCommandManager(SubCommandService<?> commandService, CustomBosses customBosses) {
        this.commandService = commandService;
        this.customBosses = customBosses;
    }

    @Override
    public void load() {
        if(this.hasBeenLoaded) {
            Debug.FAILED_TO_LOAD_BOSSCOMMANDMANAGER.debug();
            return;
        }

        this.commandService.registerSubCommand(new BossCreateCmd(this.customBosses.getBossEntityContainer()));
        this.commandService.registerSubCommand(new BossDebugCmd(this.customBosses.getDebugManager()));
        this.commandService.registerSubCommand(new BossDropTableCmd(this.customBosses.getBossPanelManager()));
        this.commandService.registerSubCommand(new BossEditCmd(this.customBosses.getBossPanelManager(), this.customBosses.getBossEntityContainer()));
        this.commandService.registerSubCommand(new BossGiveEggCmd(this.customBosses.getBossesFileManager(), this.customBosses.getBossEntityManager()));
        this.commandService.registerSubCommand(new BossHelpCmd());
        this.commandService.registerSubCommand(new BossInfoCmd(this.customBosses.getBossesFileManager(), this.customBosses.getBossEntityManager()));
        this.commandService.registerSubCommand(new BossItemsCmd(this.customBosses.getBossPanelManager()));
        this.commandService.registerSubCommand(new BossKillAllCmd(this.customBosses.getBossEntityManager()));
        this.commandService.registerSubCommand(new BossListCmd(this.customBosses.getBossPanelManager()));
        this.commandService.registerSubCommand(new BossMenuCmd(this.customBosses.getBossPanelManager()));
        this.commandService.registerSubCommand(new BossNearbyCmd(this.customBosses));
        this.commandService.registerSubCommand(new BossNewCmd(this.customBosses));
        this.commandService.registerSubCommand(new BossReloadCmd(this.customBosses, this.customBosses.getBossEntityManager()));
        this.commandService.registerSubCommand(new BossShopCmd(this.customBosses));
        this.commandService.registerSubCommand(new BossSkillsCmd(this.customBosses.getBossPanelManager()));
        this.commandService.registerSubCommand(new BossSpawnCmd(this.customBosses.getBossesFileManager()));
        this.commandService.registerSubCommand(new BossTimeCmd());

        this.hasBeenLoaded = true;
    }
}
