package net.aminecraftdev.custombosses.api;

import net.aminecraftdev.custombosses.entity.BossEntity;
import net.aminecraftdev.custombosses.entity.elements.*;
import net.aminecraftdev.custombosses.utils.EntityTypeUtil;
import net.aminecraftdev.custombosses.utils.potion.holder.PotionEffectHolder;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 28-Jun-18
 */
public class BossAPI {

    public static BossEntity createBoss(String name, String entityTypeInput) {
        MainStatsElement mainStatsElement = new MainStatsElement();
        EquipmentElement equipmentElement = new EquipmentElement();
        HandsElement handsElement = new HandsElement();
        List<PotionEffectHolder> potionEffectHolders = new ArrayList<>();
        SkillsElement skillsElement = new SkillsElement();
        DropsElement dropsElement = new DropsElement();
        MessagesElement messagesElement = new MessagesElement();
        CommandsElement commandsElement = new CommandsElement();

        //TODO: Set the entityType to said entityTypeInput

        BossEntity bossEntity = new BossEntity(true, null, mainStatsElement, equipmentElement, handsElement, potionEffectHolders, skillsElement, dropsElement, messagesElement, commandsElement);


        return bossEntity;
    }

}
