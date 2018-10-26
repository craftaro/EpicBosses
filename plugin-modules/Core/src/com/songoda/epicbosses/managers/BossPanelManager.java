package com.songoda.epicbosses.managers;

import lombok.Getter;
import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.panel.*;
import com.songoda.epicbosses.utils.ILoadable;
import com.songoda.epicbosses.utils.IReloadable;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.panel.base.IPanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 10-Oct-18
 */
public class BossPanelManager implements ILoadable, IReloadable {

    @Getter private IPanelHandler mainMenu, customItems, bosses, autoSpawns, dropTables, customSkills;

    private final String customItemsTitle = "&6&lCustomBosses &e&lItems", autoSpawnsTitle = "&6&lCustomBosses &e&lAutoSpawns",
                         customBossesTitle = "&6&lCustomBosses &e&lBosses", customSkillsTitle = "&6&lCustomBosses &e&lSkills",
                         dropTableTitle = "&6&lCustomBosses &e&lDropTable";
    private final CustomBosses customBosses;

    public BossPanelManager(CustomBosses customBosses) {
        this.customBosses = customBosses;
    }

    @Override
    public void load() {
        loadMainMenu();

        loadAutoSpawnsMenu();
        loadCustomBossesMenu();
        loadCustomItemsMenu();
        loadCustomSkillsMenu();
        loadDropTableMenu();

    }

    @Override
    public void reload() {
        reloadMainMenu();

        reloadAutoSpawnsMenu();
        reloadCustomBosses();
        reloadCustomItems();
        reloadCustomSkills();
        reloadDropTable();

    }

    //---------------------------------------------
    //
    //  M A I N   M E N U   P A N E L
    //
    //---------------------------------------------

    private void loadMainMenu() {
        PanelBuilder panelBuilder = new PanelBuilder(this.customBosses.getEditor().getConfigurationSection("MainMenu"));

        this.mainMenu = new MainMenuPanel(this, panelBuilder);
    }

    private void reloadMainMenu() {
        PanelBuilder panelBuilder = new PanelBuilder(this.customBosses.getEditor().getConfigurationSection("MainMenu"));

        this.mainMenu.initializePanel(panelBuilder);
    }

    //---------------------------------------------
    //
    //  A U T O   S P A W N S   P A N E L
    //
    //---------------------------------------------

    private void loadAutoSpawnsMenu() {
        this.autoSpawns = new AutoSpawnsPanel(this, getListMenu(this.autoSpawnsTitle));
    }

    private void reloadAutoSpawnsMenu() {
        this.autoSpawns.initializePanel(getListMenu(this.autoSpawnsTitle));
    }

    //---------------------------------------------
    //
    //  C U S T O M   B O S S E S   P A N E L
    //
    //---------------------------------------------

    private void loadCustomBossesMenu() {
        this.bosses = new CustomBossesPanel(this, getListMenu(this.customBossesTitle), this.customBosses);
    }

    private void reloadCustomBosses() {
        this.bosses.initializePanel(getListMenu(this.customBossesTitle));
    }

    //---------------------------------------------
    //
    //  C U S T O M   S K I L L S   P A N E L
    //
    //---------------------------------------------

    private void loadCustomSkillsMenu() {
        this.customSkills = new CustomSkillsPanel(this, getListMenu(this.customSkillsTitle));
    }

    private void reloadCustomSkills() {
        this.customSkills.initializePanel(getListMenu(this.customSkillsTitle));
    }

    //---------------------------------------------
    //
    //  D R O P   T A B L E   P A N E L
    //
    //---------------------------------------------

    private void loadDropTableMenu() {
        this.dropTables = new DropTablePanel(this, getListMenu(this.dropTableTitle));
    }

    private void reloadDropTable() {
        this.dropTables.initializePanel(getListMenu(this.dropTableTitle));
    }

    //---------------------------------------------
    //
    //  C U S T O M   I T E M S   P A N E L
    //
    //---------------------------------------------

    private void loadCustomItemsMenu() {
        this.customItems = new CustomItemsPanel(this, getListMenu(this.customItemsTitle));
    }

    private void reloadCustomItems() {
        this.customItems.initializePanel(getListMenu(this.customItemsTitle));
    }

    //---------------------------------------------
    //
    //  G E N E R A L   L I S T   P A N E L
    //
    //---------------------------------------------

    private PanelBuilder getListMenu(String name) {
        Map<String, String> replaceMap = new HashMap<>();

        replaceMap.put("{panelName}", StringUtils.get().translateColor(name));

        return new PanelBuilder(this.customBosses.getEditor().getConfigurationSection("ListPanel"), replaceMap);
    }


}
