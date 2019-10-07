package com.songoda.epicbosses.managers;

import com.songoda.epicbosses.EpicBosses;
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
    private EpicBosses epicBosses;

    public BossCommandManager(SubCommandService<?> commandService, EpicBosses epicBosses) {
        this.commandService = commandService;
        this.epicBosses = epicBosses;
    }

    @Override
    public void load() {
        if (this.hasBeenLoaded) {
            Debug.FAILED_TO_LOAD_BOSSCOMMANDMANAGER.debug();
            return;
        }

        this.commandService.registerSubCommand(new BossCreateCmd(this.epicBosses.getBossEntityContainer()));
        this.commandService.registerSubCommand(new BossDebugCmd(this.epicBosses.getDebugManager()));
        this.commandService.registerSubCommand(new BossDropTableCmd(this.epicBosses.getBossPanelManager()));
        this.commandService.registerSubCommand(new BossEditCmd(this.epicBosses.getBossPanelManager(), this.epicBosses.getBossEntityContainer()));
        this.commandService.registerSubCommand(new BossGiveEggCmd(this.epicBosses.getBossesFileManager(), this.epicBosses.getBossEntityManager()));
        this.commandService.registerSubCommand(new BossHelpCmd());
        this.commandService.registerSubCommand(new BossInfoCmd(this.epicBosses.getBossesFileManager(), this.epicBosses.getBossEntityManager()));
        this.commandService.registerSubCommand(new BossItemsCmd(this.epicBosses.getBossPanelManager()));
        this.commandService.registerSubCommand(new BossKillAllCmd(this.epicBosses.getBossEntityManager()));
        this.commandService.registerSubCommand(new BossListCmd(this.epicBosses.getBossPanelManager()));
        this.commandService.registerSubCommand(new BossMenuCmd(this.epicBosses.getBossPanelManager()));
        this.commandService.registerSubCommand(new BossNearbyCmd(this.epicBosses));
        this.commandService.registerSubCommand(new BossNewCmd(this.epicBosses));
        this.commandService.registerSubCommand(new BossReloadCmd(this.epicBosses, this.epicBosses.getBossEntityManager()));
        this.commandService.registerSubCommand(new BossShopCmd(this.epicBosses));
        this.commandService.registerSubCommand(new BossSkillsCmd(this.epicBosses.getBossPanelManager()));
        this.commandService.registerSubCommand(new BossSpawnCmd(this.epicBosses.getBossesFileManager()));
        this.commandService.registerSubCommand(new BossTimeCmd(this.epicBosses));

        this.hasBeenLoaded = true;
    }
}
