package net.aminecraftdev.custombosses.api;

import net.aminecraftdev.custombosses.CustomBosses;
import net.aminecraftdev.custombosses.entity.BossEntity;
import net.aminecraftdev.custombosses.entity.elements.*;
import net.aminecraftdev.custombosses.managers.files.BossItemFileManager;
import net.aminecraftdev.custombosses.utils.Debug;
import net.aminecraftdev.custombosses.utils.EntityFinder;
import net.aminecraftdev.custombosses.utils.itemstack.holder.ItemStackHolder;
import net.aminecraftdev.custombosses.utils.panel.Panel;
import net.aminecraftdev.custombosses.utils.potion.holder.PotionEffectHolder;

import java.util.ArrayList;
import java.util.Arrays;
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

        List<MainStatsElement> mainStatsElements = new ArrayList<>(Arrays.asList(new MainStatsElement()));
        EquipmentElement equipmentElement = new EquipmentElement();
        HandsElement handsElement = new HandsElement();
        List<PotionEffectHolder> potionEffectHolders = new ArrayList<>();
        SkillsElement skillsElement = new SkillsElement();
        DropsElement dropsElement = new DropsElement();
        MessagesElement messagesElement = new MessagesElement();
        CommandsElement commandsElement = new CommandsElement();

        BossEntity bossEntity = new BossEntity(true, null, mainStatsElements, equipmentElement, handsElement, potionEffectHolders, skillsElement, dropsElement, messagesElement, commandsElement);
        MainStatsElement mainStatsElement = mainStatsElements.get(0);

        mainStatsElement.setEntityType(entityFinder.getFancyName());
        mainStatsElement.setDisplayName(name);
        mainStatsElement.setHealth(50D);

        boolean result = PLUGIN.getBossEntityContainer().saveData(name, bossEntity);

        if (!result) {
            Debug.FAILED_TO_SAVE_THE_NEW_BOSS.debug(name, entityFinder.getFancyName());
            return null;
        }

        PLUGIN.getBossesFileManager().save();

        return bossEntity;
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
        BossItemFileManager bossItemFileManager = PLUGIN.getItemStackManager();

        return bossItemFileManager.getItemStackHolder(name);
    }

}
