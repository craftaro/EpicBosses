package com.songoda.epicbosses.api;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.MinionEntity;
import com.songoda.epicbosses.entity.elements.*;
import com.songoda.epicbosses.events.PreBossSpawnEvent;
import com.songoda.epicbosses.events.PreBossSpawnItemEvent;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.managers.files.CommandsFileManager;
import com.songoda.epicbosses.managers.files.ItemsFileManager;
import com.songoda.epicbosses.managers.files.MessagesFileManager;
import com.songoda.epicbosses.skills.CustomSkillHandler;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.custom.Minions;
import com.songoda.epicbosses.skills.elements.CustomMinionSkillElement;
import com.songoda.epicbosses.skills.types.CustomSkillElement;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.EntityFinder;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import com.songoda.epicbosses.utils.panel.Panel;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 28-Jun-18
 */
public class BossAPI {

    private static CustomBosses PLUGIN;

    /**
     * Used to update the variable to the
     * plugin instance so the methods can
     * pull variables in the main class to use
     * in their method.
     *
     * This should only ever be used in house and
     * never to be used by an outside party.
     *
     * @param plugin - the plugin instance.
     */
    public BossAPI(CustomBosses plugin) {
        if(PLUGIN != null) {
            Debug.ATTEMPTED_TO_UPDATE_PLUGIN.debug();
            return;
        }

        PLUGIN = plugin;
        Panel.setPlugin(plugin);
    }

    /**
     * Used to register a Boss Entity into
     * the plugin after it has been created
     * using the BossAPI#createBaseBossEntity
     * method or by manually creating a BossEntity.
     *
     * @param name - Name for the boss section.
     * @param bossEntity - The boss section.
     * @return false if it failed, true if it saved successfully.
     */
    public static boolean registerBossEntity(String name, BossEntity bossEntity) {
        if(name == null || bossEntity == null) return false;

        PLUGIN.getBossEntityContainer().saveData(name, bossEntity);
        PLUGIN.getBossesFileManager().save();
        return true;
    }
    /**
     * Used to register a Minion Entity into
     * the plugin after it has been created
     * using the BossAPI#createBaseMinionEntity
     * method or by manually creating a MinionEntity.
     *
     * @param name - Name for the minion section.
     * @param minionEntity - The minion section.
     * @return false if it failed, true if it saved successfully.
     */
    public static boolean registerMinionEntity(String name, MinionEntity minionEntity) {
        if(name == null || minionEntity == null) return false;

        PLUGIN.getMinionEntityContainer().saveData(name, minionEntity);
        PLUGIN.getMinionsFileManager().save();
        return true;
    }

    /**
     * Used to register skills into the
     * system so that when a boss uses a
     * skill it will be recognised with
     * custom coding. All skills will be
     * instantly detected once registered
     * with this method.
     *
     * @param customSkill - The custom skill you are registering
     * @return boolean if the registration succeeded or failed
     */
    public static boolean registerCustomSkill(CustomSkillHandler customSkill) {
        return PLUGIN.getBossSkillManager().registerCustomSkill(customSkill);
    }

    /**
     * Used to unregister one of the
     * custom skills that are in the
     * system. You do not need to unregister
     * any external skills onDisable of the
     * server as it will have no impact on
     * the way it closes.
     *
     * @param customSkill - The custom skill you are trying to remove
     */
    public static void removeCustomSkill(CustomSkillHandler customSkill) {
        PLUGIN.getBossSkillManager().removeCustomSkill(customSkill);
    }

    /**
     * Used to get the Boss configuration section name
     * from a BossEntity instance.
     *
     * @param bossEntity - the boss Entity instance
     * @return name of the boss from the BossContainer or null if not found.
     */
    public static String getBossEntityName(BossEntity bossEntity) {
        for(Map.Entry<String, BossEntity> entry : PLUGIN.getBossEntityContainer().getData().entrySet()) {
            if(entry.getValue().equals(bossEntity)) {
                return entry.getKey();
            }
        }

        return null;
    }

    /**
     * Used to get the Minion configuration section name
     * from a MinionEntity instance.
     *
     * @param minionEntity - the Minion Entity instance
     * @return name of the minion from the MinionContainer or null if not found.
     */
    public static String getMinionEntityName(MinionEntity minionEntity) {
        for(Map.Entry<String, MinionEntity> entry : PLUGIN.getMinionEntityContainer().getData().entrySet()) {
            if(entry.getValue().equals(minionEntity)) {
                return entry.getKey();
            }
        }

        return null;
    }

    /**
     * Used to create a base BossEntity model which
     * can be used to fine tune and then once the main
     * elements are filled in editing can be disabled
     * and the boss can be spawned.
     *
     * @param name - boss name
     * @param entityTypeInput - entity type
     * @return null if something went wrong, or the BossEntity that was created.
     */
    public static BossEntity createBaseBossEntity(String name, String entityTypeInput) {
        String input = entityTypeInput.split(":")[0];
        EntityFinder entityFinder = EntityFinder.get(input);

        if(PLUGIN.getBossEntityContainer().exists(name)) {
            Debug.BOSS_NAME_EXISTS.debug(name);
            return null;
        }

        if (entityFinder == null) return null;

        List<EntityStatsElement> entityStatsElements = new ArrayList<>();
        MainStatsElement mainStatsElement = new MainStatsElement(1, entityFinder.getFancyName(), 50.0, name);
        EntityStatsElement entityStatsElement = new EntityStatsElement(mainStatsElement, new EquipmentElement("", "", "", ""), new HandsElement("", ""), new ArrayList<>());

        entityStatsElements.add(entityStatsElement);

        SkillsElement skillsElement = new SkillsElement(10.0, "", new ArrayList<>());
        DropsElement dropsElement = new DropsElement(true, true, "");
        MessagesElement messagesElement = new MessagesElement(new OnSpawnMessageElement("", -1), new OnDeathMessageElement("", "", -1, 5), new TauntElement(60, 100, new ArrayList<>()));
        CommandsElement commandsElement = new CommandsElement("", "");

        BossEntity bossEntity = new BossEntity(true, null, false, 100.0, entityStatsElements, skillsElement, dropsElement, messagesElement, commandsElement);
        boolean result = PLUGIN.getBossEntityContainer().saveData(name, bossEntity);

        if (!result) {
            Debug.FAILED_TO_SAVE_THE_NEW_BOSS.debug(name, entityFinder.getFancyName());
            return null;
        }

        PLUGIN.getBossesFileManager().saveBossEntity(name, bossEntity);
        PLUGIN.getBossesFileManager().save();

        return bossEntity;
    }

    /**
     * Used to create a base MinionEntity model which
     * can be used to fine tune and then once the main
     * elements are filled in editing can be disabled
     * and the minion can be spawned in skills.
     *
     * @param name - minion name
     * @param entityTypeInput - entity type
     * @return null if something went wrong, or the MinionEntity that was created.
     */
    public static MinionEntity createBaseMinionEntity(String name, String entityTypeInput) {
        String input = entityTypeInput.split(":")[0];
        EntityFinder entityFinder = EntityFinder.get(input);

        if(PLUGIN.getMinionEntityContainer().exists(name)) {
            Debug.MINION_NAME_EXISTS.debug(name);
            return null;
        }

        if (entityFinder == null) return null;

        List<EntityStatsElement> entityStatsElements = new ArrayList<>();
        MainStatsElement mainStatsElement = new MainStatsElement(1, entityFinder.getFancyName(), 50.0, name);
        EntityStatsElement entityStatsElement = new EntityStatsElement(mainStatsElement, new EquipmentElement("", "", "", ""), new HandsElement("", ""), new ArrayList<>());

        entityStatsElements.add(entityStatsElement);

        MinionEntity minionEntity = new MinionEntity(true,entityStatsElements);
        boolean result = PLUGIN.getMinionEntityContainer().saveData(name, minionEntity);

        if (!result) {
            Debug.FAILED_TO_SAVE_THE_NEW_MINION.debug(name, entityFinder.getFancyName());
            return null;
        }

        PLUGIN.getMinionsFileManager().save();

        return minionEntity;
    }

    /**
     * Used to spawn a new active boss for the
     * specified bossEntity.
     *
     * @param bossEntity - targetted BossEntity
     * @param location - Location to spawn the boss.
     * @param player - Player who spawned the boss.
     * @param itemStack - The itemstack used to spawn the boss.
     * @return ActiveBossHolder class with stored information
     */
    public static ActiveBossHolder spawnNewBoss(BossEntity bossEntity, Location location, Player player, ItemStack itemStack) {
//        if(bossEntity.isEditing()) {
//            Debug.ATTEMPTED_TO_SPAWN_WHILE_DISABLED.debug();
//            return null;
//        }

        String name = PLUGIN.getBossEntityContainer().getName(bossEntity);

        ActiveBossHolder activeBossHolder = PLUGIN.getBossEntityManager().createActiveBossHolder(bossEntity, location, name);

        if(activeBossHolder == null) {
            Debug.FAILED_TO_CREATE_ACTIVE_BOSS_HOLDER.debug();
            return null;
        }

        PreBossSpawnEvent preBossSpawnEvent;

        if(player != null && itemStack != null) {
            preBossSpawnEvent = new PreBossSpawnItemEvent(activeBossHolder, player, itemStack);
        } else {
            preBossSpawnEvent = new PreBossSpawnEvent(activeBossHolder);
        }

        System.out.println("SPAWNING EVENT " + preBossSpawnEvent);

        PLUGIN.getBossTargetManager().initializeTargetHandler(activeBossHolder);
        ServerUtils.get().callEvent(preBossSpawnEvent);

        return activeBossHolder;
    }

    /**
     * Used to spawn a new minion for the specified
     * bossEntity, under the activebossholder.
     *
     * @param activeBossHolder - targeted active boss
     * @param skill - the skill from the skills.json
     * @return boolean if the spawning of the minions succeeded or failed
     */
    public static boolean spawnNewMinion(ActiveBossHolder activeBossHolder, Skill skill) {
//        if(minionEntity.isEditing()) {
//            Debug.ATTEMPTED_TO_SPAWN_WHILE_DISABLED.debug();
//            return null;
//        }

        if(skill.getType().equalsIgnoreCase("CUSTOM")) {
            CustomSkillElement customSkillElement = PLUGIN.getBossSkillManager().getCustomSkillElement(skill);

            if(customSkillElement.getCustom().getType().equalsIgnoreCase("MINION")) {
                CustomMinionSkillElement customMinionSkillElement = customSkillElement.getCustom().getCustomMinionSkillData();

                PLUGIN.getBossEntityManager().spawnMinionsOnBossHolder(activeBossHolder, skill, customMinionSkillElement);
            }
        }


        return false;
    }

    /**
     * Used to obtain an item stack holder
     * of the specified item stack from the
     * ItemStack bank.
     *
     * @param name - name of the ItemStack
     * @return null if not found, instance if found
     */
    public static ItemStackHolder getStoredItemStack(String name) {
        ItemsFileManager bossItemFileManager = PLUGIN.getItemStackManager();

        return bossItemFileManager.getItemStackHolder(name);
    }

    /**
     * Used to obtain the list of strings that
     * a message is built in to.
     *
     * @param id - the id from the messages.json
     * @return null if not found, instance if found
     */
    public static List<String> getStoredMessages(String id) {
        MessagesFileManager bossMessagesFileManager = PLUGIN.getBossMessagesFileManager();

        return bossMessagesFileManager.getMessages(id);
    }

    /**
     * Used to obtain the list of strings that
     * a command is built of.
     *
     * @param id - the id from the commands.json
     * @return null if not found, instance if found
     */
    public static List<String> getStoredCommands(String id) {
        CommandsFileManager bossCommandFileManager = PLUGIN.getBossCommandFileManager();

        return bossCommandFileManager.getCommands(id);
    }

}
