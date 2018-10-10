package net.aminecraftdev.custombosses.managers;

import net.aminecraftdev.custombosses.CustomBosses;
import net.aminecraftdev.custombosses.commands.boss.*;
import net.aminecraftdev.custombosses.utils.Debug;
import net.aminecraftdev.custombosses.utils.ILoadable;
import net.aminecraftdev.custombosses.utils.command.SubCommandService;

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
        this.commandService.registerSubCommand(new BossEditCmd(this.customBosses.getBossPanelManager()));
        this.commandService.registerSubCommand(new BossHelpCmd());
        this.commandService.registerSubCommand(new BossInfoCmd());
        this.commandService.registerSubCommand(new BossItemsCmd(this.customBosses.getBossPanelManager()));
        this.commandService.registerSubCommand(new BossKillAllCmd());
        this.commandService.registerSubCommand(new BossListCmd());
        this.commandService.registerSubCommand(new BossMenuCmd(this.customBosses.getBossPanelManager()));
        this.commandService.registerSubCommand(new BossNearbyCmd());
        this.commandService.registerSubCommand(new BossReloadCmd(this.customBosses));
        this.commandService.registerSubCommand(new BossShopCmd());
        this.commandService.registerSubCommand(new BossSkillsCmd(this.customBosses.getBossPanelManager()));
        this.commandService.registerSubCommand(new BossSpawnCmd());
        this.commandService.registerSubCommand(new BossTimeCmd());

        this.hasBeenLoaded = true;
    }
}
