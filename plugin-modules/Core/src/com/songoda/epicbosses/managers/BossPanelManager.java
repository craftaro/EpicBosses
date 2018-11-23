package com.songoda.epicbosses.managers;

import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.entity.elements.EquipmentElement;
import com.songoda.epicbosses.entity.elements.HandsElement;
import com.songoda.epicbosses.panel.bosses.*;
import com.songoda.epicbosses.panel.bosses.list.BossListEquipmentEditorPanel;
import com.songoda.epicbosses.panel.bosses.equipment.BootsEditorPanel;
import com.songoda.epicbosses.panel.bosses.equipment.ChestplateEditorPanel;
import com.songoda.epicbosses.panel.bosses.equipment.HelmetEditorPanel;
import com.songoda.epicbosses.panel.bosses.equipment.LeggingsEditorPanel;
import com.songoda.epicbosses.utils.panel.base.ISubVariablePanelHandler;
import com.songoda.epicbosses.utils.panel.base.IVariablePanelHandler;
import lombok.Getter;
import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.panel.*;
import com.songoda.epicbosses.utils.ILoadable;
import com.songoda.epicbosses.utils.IReloadable;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.panel.base.IPanelHandler;
import com.songoda.epicbosses.utils.panel.builder.PanelBuilder;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 10-Oct-18
 */
public class BossPanelManager implements ILoadable, IReloadable {

    @Getter private IPanelHandler mainMenu, customItems, bosses, autoSpawns, dropTables, customSkills, shopPanel;
    @Getter private IPanelHandler addItemsMenu;

    @Getter private ISubVariablePanelHandler<BossEntity, EntityStatsElement> equipmentEditMenu, helmetEditorMenu, chestplateEditorMenu, leggingsEditorMenu, bootsEditorMenu;
    @Getter private IVariablePanelHandler<BossEntity> mainBossEditMenu, dropsEditMenu, targetingEditMenu;
    @Getter private BossListEditorPanel equipmentListEditMenu;

    private final CustomBosses customBosses;

    public BossPanelManager(CustomBosses customBosses) {
        this.customBosses = customBosses;
    }

    @Override
    public void load() {
        loadMainMenu();
        loadShopMenu();

        loadAutoSpawnsMenu();
        loadCustomBossesMenu();
        loadCustomItemsMenu();
        loadCustomSkillsMenu();
        loadDropTableMenu();

        loadAddItemsMenu();
        loadMainEditMenu();
        loadDropsEditMenu();
        loadEditorListMenus();
        loadTargetingEditMenu();

        loadEquipmentEditMenu();
        loadEquipmentEditMenus();
    }

    @Override
    public void reload() {
        reloadMainMenu();
        reloadShopMenu();

        reloadAutoSpawnsMenu();
        reloadCustomBosses();
        reloadCustomItems();
        reloadCustomSkills();
        reloadDropTable();

        reloadAddItemsMenu();
        reloadMainEditMenu();
        reloadDropsEditMenu();
        reloadEditorListMenus();
        reloadTargetingEditMenu();

        reloadEquipmentEditMenu();
        reloadEquipmentEditMenus();
    }

    public int isItemStackUsed(String name) {
        Collection<BossEntity> values = this.customBosses.getBossEntityContainer().getData().values();
        int timesUsed = 0;

        for(BossEntity bossEntity : values) {
            if(bossEntity.getSpawnItem().equalsIgnoreCase(name)) timesUsed += 1;

            List<EntityStatsElement> entityStatsElements = bossEntity.getEntityStats();

            for(EntityStatsElement entityStatsElement : entityStatsElements) {
                EquipmentElement equipmentElement = entityStatsElement.getEquipment();
                HandsElement handsElement = entityStatsElement.getHands();

                if(handsElement.getMainHand().equalsIgnoreCase(name)) timesUsed += 1;
                if(handsElement.getOffHand().equalsIgnoreCase(name)) timesUsed += 1;
                if(equipmentElement.getHelmet().equalsIgnoreCase(name)) timesUsed += 1;
                if(equipmentElement.getChestplate().equalsIgnoreCase(name)) timesUsed += 1;
                if(equipmentElement.getLeggings().equalsIgnoreCase(name)) timesUsed += 1;
                if(equipmentElement.getBoots().equalsIgnoreCase(name)) timesUsed += 1;
            }
        }

        return timesUsed;
    }

    //---------------------------------------------
    //
    //  E Q U I P M E N T   E D I T   P A N E L S
    //
    //---------------------------------------------

    private void loadEquipmentEditMenus() {
        FileConfiguration editor = this.customBosses.getEditor();

        this.helmetEditorMenu = new HelmetEditorPanel(this, editor.getConfigurationSection(HELMET_EDITOR_PATH), this.customBosses);
        this.chestplateEditorMenu = new ChestplateEditorPanel(this, editor.getConfigurationSection(CHESTPLATE_EDITOR_PATH), this.customBosses);
        this.leggingsEditorMenu = new LeggingsEditorPanel(this, editor.getConfigurationSection(LEGGINGS_EDITOR_PATH), this.customBosses);
        this.bootsEditorMenu = new BootsEditorPanel(this, editor.getConfigurationSection(BOOTS_EDITOR_PATH), this.customBosses);
    }

    private void reloadEquipmentEditMenus() {
        FileConfiguration editor = this.customBosses.getEditor();

        this.helmetEditorMenu.initializePanel(new PanelBuilder(editor.getConfigurationSection(HELMET_EDITOR_PATH)));
        this.chestplateEditorMenu.initializePanel(new PanelBuilder(editor.getConfigurationSection(CHESTPLATE_EDITOR_PATH)));
        this.leggingsEditorMenu.initializePanel(new PanelBuilder(editor.getConfigurationSection(LEGGINGS_EDITOR_PATH)));
        this.bootsEditorMenu.initializePanel(new PanelBuilder(editor.getConfigurationSection(BOOTS_EDITOR_PATH)));
    }

    //---------------------------------------------
    //
    //  L I S T   E D I T   P A N E LS
    //
    //---------------------------------------------

    private static final String HELMET_EDITOR_PATH = "HelmetEditorPanel", CHESTPLATE_EDITOR_PATH = "ChestplateEditorPanel", LEGGINGS_EDITOR_PATH = "LeggingsEditorPanel",
            BOOTS_EDITOR_PATH = "BootsEditorPanel";


    private void loadEditorListMenus() {
        PanelBuilder panelBuilder = new PanelBuilder(this.customBosses.getEditor().getConfigurationSection("BossListEditorPanel"));

        this.equipmentListEditMenu = new BossListEquipmentEditorPanel(this, panelBuilder, this.customBosses);
    }

    private void reloadEditorListMenus() {
        PanelBuilder panelBuilder = new PanelBuilder(this.customBosses.getEditor().getConfigurationSection("BossListEditorPanel"));

        this.equipmentListEditMenu.initializePanel(panelBuilder);
    }

    //---------------------------------------------
    //
    //  E Q U I P M E N T   E D I T   P A N E L
    //
    //---------------------------------------------

    private void loadEquipmentEditMenu() {
        PanelBuilder panelBuilder = new PanelBuilder(this.customBosses.getEditor().getConfigurationSection("EquipmentEditorPanel"));

        this.equipmentEditMenu = new EquipmentEditorPanel(this, panelBuilder);
    }

    private void reloadEquipmentEditMenu() {
        PanelBuilder panelBuilder = new PanelBuilder(this.customBosses.getEditor().getConfigurationSection("EquipmentEditorPanel"));

        this.equipmentEditMenu.initializePanel(panelBuilder);
    }

    //---------------------------------------------
    //
    //  D R O P S   E D I T   P A N E L
    //
    //---------------------------------------------

    private void loadDropsEditMenu() {
        PanelBuilder panelBuilder = new PanelBuilder(this.customBosses.getEditor().getConfigurationSection("DropsEditorPanel"));

        this.dropsEditMenu = new DropsEditorPanel(this, panelBuilder, this.customBosses);
    }

    private void reloadDropsEditMenu() {
        PanelBuilder panelBuilder = new PanelBuilder(this.customBosses.getEditor().getConfigurationSection("DropsEditorPanel"));

        this.dropsEditMenu.initializePanel(panelBuilder);
    }

    //---------------------------------------------
    //
    //  T A R G E T I N G   E D I T   P A N E L
    //
    //---------------------------------------------

    private void loadTargetingEditMenu() {
        PanelBuilder panelBuilder = new PanelBuilder(this.customBosses.getEditor().getConfigurationSection("TargetingPanel"));

        this.targetingEditMenu = new TargetingEditorPanel(this, panelBuilder, this.customBosses.getBossesFileManager());
    }

    private void reloadTargetingEditMenu() {
        PanelBuilder panelBuilder = new PanelBuilder(this.customBosses.getEditor().getConfigurationSection("TargetingPanel"));

        this.targetingEditMenu.initializePanel(panelBuilder);
    }

    //---------------------------------------------
    //
    //  M A I N   E D I T   P A N E L
    //
    //---------------------------------------------

    private void loadMainEditMenu() {
        PanelBuilder panelBuilder = new PanelBuilder(this.customBosses.getEditor().getConfigurationSection("MainEditorPanel"));

        this.mainBossEditMenu = new MainBossEditPanel(this, panelBuilder, this.customBosses);
    }

    private void reloadMainEditMenu() {
        PanelBuilder panelBuilder = new PanelBuilder(this.customBosses.getEditor().getConfigurationSection("MainEditorPanel"));

        this.mainBossEditMenu.initializePanel(panelBuilder);
    }

    //---------------------------------------------
    //
    //  A D D   I T E M S   P A N E L
    //
    //---------------------------------------------

    private void loadAddItemsMenu() {
        PanelBuilder panelBuilder = new PanelBuilder(this.customBosses.getEditor().getConfigurationSection("AddItemsMenu"));

        this.addItemsMenu = new AddItemsPanel(this, panelBuilder, this.customBosses);
    }

    private void reloadAddItemsMenu() {
        PanelBuilder panelBuilder = new PanelBuilder(this.customBosses.getEditor().getConfigurationSection("AddItemsMenu"));

        this.addItemsMenu.initializePanel(panelBuilder);
    }

    //---------------------------------------------
    //
    //  S H O P   P A N E L
    //
    //---------------------------------------------

    private void loadShopMenu() {
        this.shopPanel = new ShopPanel(this, getListMenu("Shop"), this.customBosses);
    }

    private void reloadShopMenu() {
        this.shopPanel.initializePanel(getListMenu("Shop"));
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
        this.autoSpawns = new AutoSpawnsPanel(this, getListMenu("AutoSpawns"));
    }

    private void reloadAutoSpawnsMenu() {
        this.autoSpawns.initializePanel(getListMenu("AutoSpawns"));
    }

    //---------------------------------------------
    //
    //  C U S T O M   B O S S E S   P A N E L
    //
    //---------------------------------------------

    private void loadCustomBossesMenu() {
        this.bosses = new CustomBossesPanel(this, getListMenu("Bosses"), this.customBosses);
    }

    private void reloadCustomBosses() {
        this.bosses.initializePanel(getListMenu("Bosses"));
    }

    //---------------------------------------------
    //
    //  C U S T O M   S K I L L S   P A N E L
    //
    //---------------------------------------------

    private void loadCustomSkillsMenu() {
        this.customSkills = new CustomSkillsPanel(this, getListMenu("Skills"), this.customBosses);
    }

    private void reloadCustomSkills() {
        this.customSkills.initializePanel(getListMenu("Skills"));
    }

    //---------------------------------------------
    //
    //  D R O P   T A B L E   P A N E L
    //
    //---------------------------------------------

    private void loadDropTableMenu() {
        this.dropTables = new DropTablePanel(this, getListMenu("DropTable"), this.customBosses);
    }

    private void reloadDropTable() {
        this.dropTables.initializePanel(getListMenu("DropTable"));
    }

    //---------------------------------------------
    //
    //  C U S T O M   I T E M S   P A N E L
    //
    //---------------------------------------------

    private void loadCustomItemsMenu() {
        this.customItems = new CustomItemsPanel(this, getListMenu("Items"), this.customBosses);
    }

    private void reloadCustomItems() {
        this.customItems.initializePanel(getListMenu("Items"));
    }

    //---------------------------------------------
    //
    //  G E N E R A L   L I S T   P A N E L
    //
    //---------------------------------------------

    private PanelBuilder getListMenu(String path) {
        Map<String, String> replaceMap = new HashMap<>();

        replaceMap.put("{panelName}", StringUtils.get().translateColor(this.customBosses.getConfig().getString(getPath(path))));

        return new PanelBuilder(this.customBosses.getEditor().getConfigurationSection("ListPanel"), replaceMap);
    }

    private String getPath(String key) {
        return "Display." + key + ".menuName";
    }

}
