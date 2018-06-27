package net.aminecraftdev.custombosses.entity;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import net.aminecraftdev.custombosses.entity.elements.*;
import net.aminecraftdev.custombosses.utils.potion.holder.PotionEffectHolder;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 14-May-18
 */
public class BossEntity {

    @Expose @Getter private final List<PotionEffectHolder> potions;
    @Expose @Getter private final MainStatsElement mainStats;
    @Expose @Getter private final EquipmentElement equipment;
    @Expose @Getter private final MessagesElement messages;
    @Expose @Getter private final CommandsElement commands;
    @Expose @Getter private final SkillsElement skills;
    @Expose @Getter private final HandsElement hands;
    @Expose @Getter private final DropsElement drops;

    @Expose @Getter @Setter private String spawnItem;
    @Expose @Getter @Setter private boolean editing;

    public BossEntity(boolean editing, MainStatsElement mainStats, String spawnItem, EquipmentElement equipment, HandsElement hands, List<PotionEffectHolder> potions,
                      SkillsElement skills, DropsElement drops, MessagesElement messages, CommandsElement commands) {
        this.editing = editing;
        this.mainStats = mainStats;
        this.spawnItem = spawnItem;
        this.equipment = equipment;
        this.hands = hands;
        this.potions = potions;
        this.skills = skills;
        this.drops = drops;
        this.messages = messages;
        this.commands = commands;
    }
}
