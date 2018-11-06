package com.songoda.epicbosses.api;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.entity.elements.*;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.managers.files.CommandsFileManager;
import com.songoda.epicbosses.managers.files.ItemsFileManager;
import com.songoda.epicbosses.managers.files.MessagesFileManager;
import com.songoda.epicbosses.skills.types.CustomSkill;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.EntityFinder;
import com.songoda.epicbosses.utils.itemstack.holder.ItemStackHolder;
import com.songoda.epicbosses.utils.panel.Panel;
import org.bukkit.Location;

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
    public static boolean registerCustomSkill(CustomSkill customSkill) {
        return PLUGIN.getBossSkillManager().registerSkill(customSkill);
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
    public static void removeCustomSkill(CustomSkill customSkill) {
        PLUGIN.getBossSkillManager().removeSkill(customSkill);
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
        EntityStatsElement entityStatsElement = new EntityStatsElement();
        MainStatsElement mainStatsElement = new MainStatsElement();

        mainStatsElement.setHealth(50D);
        mainStatsElement.setDisplayName(name);
        mainStatsElement.setEntityType(entityFinder.getFancyName());

        entityStatsElement.setMainStats(mainStatsElement);
        entityStatsElement.setEquipment(new EquipmentElement());
        entityStatsElement.setHands(new HandsElement());
        entityStatsElement.setPotions(new ArrayList<>());

        entityStatsElements.add(entityStatsElement);

        SkillsElement skillsElement = new SkillsElement();
        DropsElement dropsElement = new DropsElement();
        MessagesElement messagesElement = new MessagesElement();
        CommandsElement commandsElement = new CommandsElement();

        BossEntity bossEntity = new BossEntity(true, null, entityStatsElements, skillsElement, dropsElement, messagesElement, commandsElement);
        boolean result = PLUGIN.getBossEntityContainer().saveData(name, bossEntity);

        if (!result) {
            Debug.FAILED_TO_SAVE_THE_NEW_BOSS.debug(name, entityFinder.getFancyName());
            return null;
        }

        PLUGIN.getBossesFileManager().save();

        return bossEntity;
    }

    /**
     * Used to spawn a new active boss for the
     * specified bossEntity.
     *
     * @param bossEntity - targetted BossEntity
     * @param location - Location to spawn the boss.
     * @return ActiveBossHolder class with stored information
     */
    public static ActiveBossHolder spawnNewBoss(BossEntity bossEntity, Location location) {
//        if(bossEntity.isEditing()) {
//            Debug.ATTEMPTED_TO_SPAWN_WHILE_DISABLED.debug();
//            return null;
//        }

        String name = PLUGIN.getBossEntityContainer().getName(bossEntity);

        return PLUGIN.getBossEntityManager().createActiveBossHolder(bossEntity, location, name);
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
