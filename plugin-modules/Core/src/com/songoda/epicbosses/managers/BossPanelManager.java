package com.songoda.epicbosses.managers;

import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.elements.EntityStatsElement;
import com.songoda.epicbosses.entity.elements.EquipmentElement;
import com.songoda.epicbosses.entity.elements.HandsElement;
import com.songoda.epicbosses.panel.bosses.*;
import com.songoda.epicbosses.panel.bosses.commands.OnDeathCommandEditor;
import com.songoda.epicbosses.panel.bosses.commands.OnSpawnCommandEditor;
import com.songoda.epicbosses.panel.bosses.list.BossListEquipmentEditorPanel;
import com.songoda.epicbosses.panel.bosses.equipment.BootsEditorPanel;
import com.songoda.epicbosses.panel.bosses.equipment.ChestplateEditorPanel;
import com.songoda.epicbosses.panel.bosses.equipment.HelmetEditorPanel;
import com.songoda.epicbosses.panel.bosses.equipment.LeggingsEditorPanel;
import com.songoda.epicbosses.panel.bosses.list.BossListStatisticEditorPanel;
import com.songoda.epicbosses.panel.bosses.list.BossListWeaponEditorPanel;
import com.songoda.epicbosses.panel.bosses.text.*;
import com.songoda.epicbosses.panel.bosses.weapons.MainHandEditorPanel;
import com.songoda.epicbosses.panel.bosses.weapons.OffHandEditorPanel;
import com.songoda.epicbosses.panel.handlers.*;
import com.songoda.epicbosses.panel.skills.MainSkillEditorPanel;
import com.songoda.epicbosses.panel.skills.SkillTypeEditorPanel;
import com.songoda.epicbosses.panel.skills.custom.PotionSkillEditorPanel;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.types.PotionSkillElement;
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
import org.bukkit.Material;
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

    private static final String HELMET_EDITOR_PATH = "HelmetEditorPanel", CHESTPLATE_EDITOR_PATH = "ChestplateEditorPanel", LEGGINGS_EDITOR_PATH = "LeggingsEditorPanel",
            BOOTS_EDITOR_PATH = "BootsEditorPanel", MAIN_HAND_EDITOR_PATH = "MainHandEditorPanel", OFF_HAND_EDITOR_PATH = "OffHandEditorPanel";

    @Getter private IPanelHandler mainMenu, customItems, bosses, autoSpawns, dropTables, customSkills, shopPanel;
    @Getter private IPanelHandler addItemsMenu;

    @Getter private ISubVariablePanelHandler<BossEntity, EntityStatsElement> equipmentEditMenu, helmetEditorMenu, chestplateEditorMenu, leggingsEditorMenu, bootsEditorMenu;
    @Getter private ISubVariablePanelHandler<BossEntity, EntityStatsElement> weaponEditMenu, offHandEditorMenu, mainHandEditorMenu;
    @Getter private ISubVariablePanelHandler<BossEntity, EntityStatsElement> statisticMainEditMenu, entityTypeEditMenu;
    @Getter private IVariablePanelHandler<BossEntity> mainBossEditMenu, dropsEditMenu, targetingEditMenu, skillsBossEditMenu, skillListBossEditMenu, commandsMainEditMenu, onSpawnCommandEditMenu,
            onDeathCommandEditMenu, mainDropsEditMenu, mainTextEditMenu, mainTauntEditMenu, onSpawnTextEditMenu, onSpawnSubTextEditMenu, onDeathTextEditMenu, onDeathSubTextEditMenu, onDeathPositionTextEditMenu,
            onTauntTextEditMenu;
    @Getter private BossListEditorPanel equipmentListEditMenu, weaponListEditMenu, statisticListEditMenu;

    @Getter private IVariablePanelHandler<Skill> mainSkillEditMenu, customMessageEditMenu, skillTypeEditMenu, potionSkillEditorPanel;
    @Getter private ISubVariablePanelHandler<Skill, PotionSkillElement> createPotionEffectMenu;

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
        loadSkillsEditMenu();
        loadStatEditMenu();
        loadCommandEditMenus();
        loadTextEditMenus();
        loadEquipmentEditMenu();
        loadWeaponEditMenu();
        loadEquipmentEditMenus();

        loadSkillEditMenus();
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
        reloadSkillsEditMenu();
        reloadStatEditMenu();
        reloadCommandEditMenus();
        reloadTextEditMenus();
        reloadEquipmentEditMenu();
        reloadWeaponEditMenu();
        reloadEquipmentEditMenus();

        reloadSkillEditMenus();
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
    //  S K I L L   E D I T   P A N E L S
    //
    //---------------------------------------------

    private void loadSkillEditMenus() {
        FileConfiguration editor = this.customBosses.getEditor();
        PanelBuilder panelBuilder = new PanelBuilder(editor.getConfigurationSection("SkillEditorPanel"));
        PanelBuilder panelBuilder1 = new PanelBuilder(editor.getConfigurationSection("SkillTypeEditorPanel"));
        PanelBuilder panelBuilder2 = new PanelBuilder(editor.getConfigurationSection("PotionSkillEditorPanel"));

        this.mainSkillEditMenu = new MainSkillEditorPanel(this, panelBuilder, this.customBosses);
        this.customMessageEditMenu = new SingleMessageListEditor<Skill>(this, getListMenu("Skills.MainEdit"), this.customBosses) {

            @Override
            public String getCurrent(Skill object) {
                return object.getCustomMessage();
            }

            @Override
            public void updateMessage(Skill object, String newPath) {
                object.setCustomMessage(newPath);
                BossPanelManager.this.customBosses.getSkillsFileManager().save();
            }

            @Override
            public IVariablePanelHandler<Skill> getParentHolder() {
                return getMainSkillEditMenu();
            }

            @Override
            public String getName(Skill object) {
                return BossAPI.getSkillName(object);
            }
        };
        this.skillTypeEditMenu = new SkillTypeEditorPanel(this, panelBuilder1, this.customBosses);
        this.potionSkillEditorPanel = new PotionSkillEditorPanel(this, panelBuilder2, this.customBosses);
    }

    private void reloadSkillEditMenus() {
        FileConfiguration editor = this.customBosses.getEditor();
        PanelBuilder panelBuilder = new PanelBuilder(editor.getConfigurationSection("SkillEditorPanel"));
        PanelBuilder panelBuilder1 = new PanelBuilder(editor.getConfigurationSection("SkillTypeEditorPanel"));
        PanelBuilder panelBuilder2 = new PanelBuilder(editor.getConfigurationSection("PotionSkillEditorPanel"));

        this.mainSkillEditMenu.initializePanel(panelBuilder);
        this.customMessageEditMenu.initializePanel(getListMenu("Skills.MainEdit"));
        this.skillTypeEditMenu.initializePanel(panelBuilder1);
        this.potionSkillEditorPanel.initializePanel(panelBuilder2);
    }

    //---------------------------------------------
    //
    //  T E X T   E D I T   P A N E L S
    //
    //---------------------------------------------

    private void loadTextEditMenus() {
        FileConfiguration editor = this.customBosses.getEditor();
        PanelBuilder panelBuilder = new PanelBuilder(editor.getConfigurationSection("TextEditorMainPanel"));
        PanelBuilder panelBuilder1 = new PanelBuilder(editor.getConfigurationSection("SpawnTextEditorPanel"));
        PanelBuilder panelBuilder2 = new PanelBuilder(editor.getConfigurationSection("DeathTextEditorPanel"));
        PanelBuilder panelBuilder3 = new PanelBuilder(editor.getConfigurationSection("TauntEditorPanel"));

        this.mainTextEditMenu = new TextMainEditorPanel(this, panelBuilder);
        this.onSpawnSubTextEditMenu = new SpawnTextEditorPanel(this, panelBuilder1, this.customBosses);
        this.onDeathSubTextEditMenu = new DeathTextEditorPanel(this, panelBuilder2, this.customBosses);
        this.mainTauntEditMenu = new TauntTextEditorPanel(this, panelBuilder3, this.customBosses);
        this.onSpawnTextEditMenu = new SingleMessageListEditor<BossEntity>(this, getListMenu("Boss.Text"), this.customBosses) {
            @Override
            public String getCurrent(BossEntity bossEntity) {
                return bossEntity.getMessages().getOnSpawn().getMessage();
            }

            @Override
            public void updateMessage(BossEntity bossEntity, String newPath) {
                bossEntity.getMessages().getOnSpawn().setMessage(newPath);
            }

            @Override
            public IVariablePanelHandler<BossEntity> getParentHolder() {
                return getOnSpawnSubTextEditMenu();
            }

            @Override
            public String getName(BossEntity object) {
                return BossAPI.getBossEntityName(object);
            }
        };
        this.onDeathTextEditMenu = new SingleMessageListEditor<BossEntity>(this, getListMenu("Boss.Text"), this.customBosses) {
            @Override
            public String getCurrent(BossEntity bossEntity) {
                return bossEntity.getMessages().getOnDeath().getMessage();
            }

            @Override
            public void updateMessage(BossEntity bossEntity, String newPath) {
                bossEntity.getMessages().getOnDeath().setMessage(newPath);
            }

            @Override
            public IVariablePanelHandler<BossEntity> getParentHolder() {
                return getOnDeathSubTextEditMenu();
            }

            @Override
            public String getName(BossEntity object) {
                return BossAPI.getBossEntityName(object);
            }
        };
        this.onDeathPositionTextEditMenu = new SingleMessageListEditor<BossEntity>(this, getListMenu("Boss.Text"), this.customBosses) {
            @Override
            public String getCurrent(BossEntity bossEntity) {
                return bossEntity.getMessages().getOnDeath().getPositionMessage();
            }

            @Override
            public void updateMessage(BossEntity bossEntity, String newPath) {
                bossEntity.getMessages().getOnDeath().setPositionMessage(newPath);
            }

            @Override
            public IVariablePanelHandler<BossEntity> getParentHolder() {
                return getOnDeathSubTextEditMenu();
            }

            @Override
            public String getName(BossEntity object) {
                return BossAPI.getBossEntityName(object);
            }
        };
        this.onTauntTextEditMenu = new ListMessageListEditor(this, getListMenu("Boss.Text"), this.customBosses) {
            @Override
            public List<String> getCurrent(BossEntity bossEntity) {
                return bossEntity.getMessages().getTaunts().getTaunts();
            }

            @Override
            public void updateMessage(BossEntity bossEntity, String modifiedValue) {
                List<String> current = getCurrent(bossEntity);

                if(current.contains(modifiedValue)) {
                    current.remove(modifiedValue);
                } else {
                    current.add(modifiedValue);
                }

                bossEntity.getMessages().getTaunts().setTaunts(current);
            }

            @Override
            public IVariablePanelHandler<BossEntity> getParentHolder() {
                return getMainTauntEditMenu();
            }
        };
    }

    private void reloadTextEditMenus() {
        FileConfiguration editor = this.customBosses.getEditor();
        PanelBuilder panelBuilder = new PanelBuilder(editor.getConfigurationSection("TextEditorMainPanel"));
        PanelBuilder panelBuilder1 = new PanelBuilder(editor.getConfigurationSection("SpawnTextEditorPanel"));
        PanelBuilder panelBuilder2 = new PanelBuilder(editor.getConfigurationSection("DeathTextEditorPanel"));
        PanelBuilder panelBuilder3 = new PanelBuilder(editor.getConfigurationSection("TauntEditorPanel"));

        this.mainTextEditMenu.initializePanel(panelBuilder);
        this.onSpawnSubTextEditMenu.initializePanel(panelBuilder1);
        this.onDeathSubTextEditMenu.initializePanel(panelBuilder2);
        this.mainTauntEditMenu.initializePanel(panelBuilder3);
        this.onSpawnTextEditMenu.initializePanel(getListMenu("Boss.Text"));
        this.onDeathTextEditMenu.initializePanel(getListMenu("Boss.Text"));
        this.onDeathPositionTextEditMenu.initializePanel(getListMenu("Boss.Text"));
        this.onTauntTextEditMenu.initializePanel(getListMenu("Boss.Text"));
    }

    //---------------------------------------------
    //
    //  C O M M A N D S   E D I T   P A N E L S
    //
    //---------------------------------------------

    private void loadCommandEditMenus() {
        PanelBuilder panelBuilder = new PanelBuilder(this.customBosses.getEditor().getConfigurationSection("CommandsEditorPanel"));

        this.commandsMainEditMenu = new CommandsMainEditorPanel(this, panelBuilder);
        this.onSpawnCommandEditMenu = new OnSpawnCommandEditor(this, getListMenu("Boss.Commands"), this.customBosses);
        this.onDeathCommandEditMenu = new OnDeathCommandEditor(this, getListMenu("Boss.Commands"), this.customBosses);
    }

    private void reloadCommandEditMenus() {
        PanelBuilder panelBuilder = new PanelBuilder(this.customBosses.getEditor().getConfigurationSection("CommandsEditorPanel"));

        this.commandsMainEditMenu.initializePanel(panelBuilder);
        this.onSpawnCommandEditMenu.initializePanel(getListMenu("Boss.Commands"));
        this.onDeathCommandEditMenu.initializePanel(getListMenu("Boss.Commands"));
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

        this.mainHandEditorMenu = new MainHandEditorPanel(this, editor.getConfigurationSection(MAIN_HAND_EDITOR_PATH), this.customBosses);
        this.offHandEditorMenu = new OffHandEditorPanel(this, editor.getConfigurationSection(OFF_HAND_EDITOR_PATH), this.customBosses);
    }

    private void reloadEquipmentEditMenus() {
        FileConfiguration editor = this.customBosses.getEditor();

        this.helmetEditorMenu.initializePanel(new PanelBuilder(editor.getConfigurationSection(HELMET_EDITOR_PATH)));
        this.chestplateEditorMenu.initializePanel(new PanelBuilder(editor.getConfigurationSection(CHESTPLATE_EDITOR_PATH)));
        this.leggingsEditorMenu.initializePanel(new PanelBuilder(editor.getConfigurationSection(LEGGINGS_EDITOR_PATH)));
        this.bootsEditorMenu.initializePanel(new PanelBuilder(editor.getConfigurationSection(BOOTS_EDITOR_PATH)));

        this.mainHandEditorMenu.initializePanel(new PanelBuilder(editor.getConfigurationSection(MAIN_HAND_EDITOR_PATH)));
        this.offHandEditorMenu.initializePanel(new PanelBuilder(editor.getConfigurationSection(OFF_HAND_EDITOR_PATH)));
    }

    //---------------------------------------------
    //
    //  L I S T   E D I T   P A N E LS
    //
    //---------------------------------------------

    private void loadEditorListMenus() {
        PanelBuilder panelBuilder = new PanelBuilder(this.customBosses.getEditor().getConfigurationSection("BossListEditorPanel"));

        this.equipmentListEditMenu = new BossListEquipmentEditorPanel(this, panelBuilder.cloneBuilder(), this.customBosses);
        this.weaponListEditMenu = new BossListWeaponEditorPanel(this, panelBuilder.cloneBuilder(), this.customBosses);
        this.statisticListEditMenu = new BossListStatisticEditorPanel(this, panelBuilder.cloneBuilder(), this.customBosses);
    }

    private void reloadEditorListMenus() {
        PanelBuilder panelBuilder = new PanelBuilder(this.customBosses.getEditor().getConfigurationSection("BossListEditorPanel"));

        this.equipmentListEditMenu.initializePanel(panelBuilder.cloneBuilder());
        this.weaponListEditMenu.initializePanel(panelBuilder.cloneBuilder());
        this.statisticListEditMenu.initializePanel(panelBuilder.cloneBuilder());
    }

    //---------------------------------------------
    //
    //  S K I L L S   E D I T   P A N E L
    //
    //---------------------------------------------

    private void loadStatEditMenu() {
        PanelBuilder panelBuilder = new PanelBuilder(this.customBosses.getEditor().getConfigurationSection("StatisticsMainEditorPanel"));

        this.statisticMainEditMenu = new StatisticMainEditorPanel(this, panelBuilder, this.customBosses);
        this.entityTypeEditMenu = new EntityTypeEditorPanel(this, getListMenu("Boss.EntityType"), this.customBosses);
    }

    private void reloadStatEditMenu() {
        PanelBuilder panelBuilder = new PanelBuilder(this.customBosses.getEditor().getConfigurationSection("StatisticsMainEditorPanel"));

        this.statisticMainEditMenu.initializePanel(panelBuilder);
        this.entityTypeEditMenu.initializePanel(getListMenu("Boss.EntityType"));
    }

    //---------------------------------------------
    //
    //  S K I L L S   E D I T   P A N E L
    //
    //---------------------------------------------

    private void loadSkillsEditMenu() {
        PanelBuilder panelBuilder = new PanelBuilder(this.customBosses.getEditor().getConfigurationSection("SkillMainEditorPanel"));

        this.skillsBossEditMenu = new SkillMainEditorPanel(this, panelBuilder, this.customBosses);
        this.skillListBossEditMenu = new SkillListEditorPanel(this, getListMenu("Boss.Skills"), this.customBosses);
    }

    private void reloadSkillsEditMenu() {
        PanelBuilder panelBuilder = new PanelBuilder(this.customBosses.getEditor().getConfigurationSection("SkillMainEditorPanel"));

        this.skillsBossEditMenu.initializePanel(panelBuilder);
        this.skillListBossEditMenu.initializePanel(getListMenu("Boss.Skills"));
    }

    //---------------------------------------------
    //
    //  E Q U I P M E N T   E D I T   P A N E L
    //
    //---------------------------------------------

    private void loadWeaponEditMenu() {
        PanelBuilder panelBuilder = new PanelBuilder(this.customBosses.getEditor().getConfigurationSection("WeaponEditorPanel"));

        this.weaponEditMenu = new WeaponsEditorPanel(this, panelBuilder);
    }

    private void reloadWeaponEditMenu() {
        PanelBuilder panelBuilder = new PanelBuilder(this.customBosses.getEditor().getConfigurationSection("WeaponEditorPanel"));

        this.weaponEditMenu.initializePanel(panelBuilder);
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
        PanelBuilder panelBuilder1 = new PanelBuilder(this.customBosses.getEditor().getConfigurationSection("DropsMainEditorPanel"));

        this.mainDropsEditMenu = new DropsMainEditorPanel(this, panelBuilder1, this.customBosses);
        this.dropsEditMenu = new DropsEditorPanel(this, panelBuilder, this.customBosses);
    }

    private void reloadDropsEditMenu() {
        PanelBuilder panelBuilder = new PanelBuilder(this.customBosses.getEditor().getConfigurationSection("DropsEditorPanel"));
        PanelBuilder panelBuilder1 = new PanelBuilder(this.customBosses.getEditor().getConfigurationSection("DropsMainEditorPanel"));

        this.mainDropsEditMenu.initializePanel(panelBuilder1);
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
        this.customSkills = new CustomSkillsPanel(this, getListMenu("Skills.Main"), this.customBosses);
    }

    private void reloadCustomSkills() {
        this.customSkills.initializePanel(getListMenu("Skills.Main"));
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
        String finalPath = getPath(path);
        String value = this.customBosses.getConfig().getString(finalPath);

        replaceMap.put("{panelName}", StringUtils.get().translateColor(value));

        return new PanelBuilder(this.customBosses.getEditor().getConfigurationSection("ListPanel"), replaceMap);
    }

    private String getPath(String key) {
        return "Display." + key + ".menuName";
    }

}
