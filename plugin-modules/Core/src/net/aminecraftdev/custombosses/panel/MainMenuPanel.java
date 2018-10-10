package net.aminecraftdev.custombosses.panel;

import net.aminecraftdev.custombosses.managers.BossPanelManager;
import net.aminecraftdev.custombosses.utils.Message;
import net.aminecraftdev.custombosses.utils.panel.base.ClickAction;
import net.aminecraftdev.custombosses.utils.panel.base.PanelHandler;
import net.aminecraftdev.custombosses.utils.panel.builder.PanelBuilder;
import net.aminecraftdev.custombosses.utils.panel.builder.PanelBuilderCounter;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 10-Oct-18
 */
public class MainMenuPanel extends PanelHandler {

    public MainMenuPanel(BossPanelManager bossPanelManager, PanelBuilder panelBuilder) {
        super(bossPanelManager, panelBuilder);
    }

    @Override
    public void initializePanel(PanelBuilder panelBuilder) {
        PanelBuilderCounter panelBuilderCounter = panelBuilder.getPanelBuilderCounter();

        panelBuilderCounter
                .addSlotCounter("CustomBosses", getBossesAction())
                .addSlotCounter("CustomItems", getCustomItemsAction())
                .addSlotCounter("AutoSpawns", getAutoSpawnsAction())
                .addSlotCounter("DropTables", getDropTablesAction())
                .addSlotCounter("CustomSkills", getCustomSkillsAction());

        this.panel = panelBuilder.getPanel()
                .setCancelClick(true)
                .setDestroyWhenDone(false)
                .setCancelLowerClick(true);
    }

    private ClickAction getBossesAction() {
        return event -> {
            Player player = (Player) event.getWhoClicked();

            if(event.getClick() == ClickType.LEFT || event.getClick() == ClickType.SHIFT_LEFT) {
                this.bossPanelManager.getBosses().openFor(player);
            } else if(event.getClick() == ClickType.RIGHT || event.getClick() == ClickType.SHIFT_RIGHT) {
                Message.Boss_Create_InvalidArgs.msg(player);
                player.closeInventory();
            }
        };
    }

    private ClickAction getCustomItemsAction() {
        return event -> {
            Player player = (Player) event.getWhoClicked();

            if(event.getClick() == ClickType.LEFT || event.getClick() == ClickType.SHIFT_LEFT) {
                this.bossPanelManager.getCustomItems().openFor(player);
            } else if(event.getClick() == ClickType.RIGHT || event.getClick() == ClickType.SHIFT_RIGHT) {
                //TODO: Set up create item inventory
                player.closeInventory();
            }
        };
    }

    private ClickAction getAutoSpawnsAction() {
        return event -> {
            Player player = (Player) event.getWhoClicked();

            if(event.getClick() == ClickType.LEFT || event.getClick() == ClickType.SHIFT_LEFT) {
                this.bossPanelManager.getAutoSpawns().openFor(player);
            } else if(event.getClick() == ClickType.RIGHT || event.getClick() == ClickType.SHIFT_RIGHT) {
                //TODO: Set up create auto spawn command
                player.closeInventory();
            }
        };
    }

    private ClickAction getDropTablesAction() {
        return event -> {
            Player player = (Player) event.getWhoClicked();

            if(event.getClick() == ClickType.LEFT || event.getClick() == ClickType.SHIFT_LEFT) {
                this.bossPanelManager.getDropTables().openFor(player);
            } else if(event.getClick() == ClickType.RIGHT || event.getClick() == ClickType.SHIFT_RIGHT) {
                //TODO: Set up create drop table command
                player.closeInventory();
            }
        };
    }

    private ClickAction getCustomSkillsAction() {
        return event -> {
            Player player = (Player) event.getWhoClicked();

            if(event.getClick() == ClickType.LEFT || event.getClick() == ClickType.SHIFT_LEFT) {
                this.bossPanelManager.getCustomSkills().openFor(player);
            } else if(event.getClick() == ClickType.RIGHT || event.getClick() == ClickType.SHIFT_RIGHT) {
                //TODO: Set up create skills command
                player.closeInventory();
            }
        };
    }
}
