package net.aminecraftdev.custombosses.api;

import net.aminecraftdev.custombosses.CustomBosses;
import net.aminecraftdev.custombosses.entity.BossEntity;
import net.aminecraftdev.custombosses.entity.elements.*;
import net.aminecraftdev.custombosses.utils.Debug;
import net.aminecraftdev.custombosses.utils.EntityFinder;
import net.aminecraftdev.custombosses.utils.potion.holder.PotionEffectHolder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 28-Jun-18
 */
public class BossAPI {

    private static CustomBosses PLUGIN;

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

        boolean result = PLUGIN.getBossesFileManager().saveNewBossEntity(name, bossEntity);

        if (!result) {
            Debug.BOSS_NAME_EXISTS.debug(name);
            return null;
        }

        return bossEntity;
    }

    /**
     * Used to update the variable to the
     * plugin instance so the methods can
     * pull variables in the main class to use
     * in their method.
     *
     * This should only ever be used in house and
     * never to be used by an outside party.
     *
     * @param customBosses - the plugin instance.
     */
    public static void setPlugin(CustomBosses customBosses) {
        if(PLUGIN != null) {
            Debug.ATTEMPTED_TO_UPDATE_PLUGIN.debug();
            return;
        }

        PLUGIN = customBosses;
    }

}
