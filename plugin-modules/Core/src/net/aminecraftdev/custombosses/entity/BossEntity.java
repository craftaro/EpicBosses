package net.aminecraftdev.custombosses.entity;

import com.google.gson.annotations.Expose;
import lombok.Getter;
import lombok.Setter;
import net.aminecraftdev.custombosses.entity.elements.*;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 14-May-18
 */
public class BossEntity {

    @Expose private final List<PotionElement> potions;
    @Expose private final MainStatsElement mainStats;
    @Expose private final EquipmentElement equipment;
    @Expose private final MessagesElement messages;
    @Expose private final CommandsElement commands;
    @Expose private final SkillsElement skills;
    @Expose private final HandsElement hands;
    @Expose private final DropsElement drops;

    @Expose @Getter @Setter private String spawnItem;
    @Expose @Getter @Setter private boolean editing;

    public BossEntity(boolean editing, MainStatsElement mainStats, String spawnItem, EquipmentElement equipment, HandsElement hands, List<PotionElement> potions,
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

    public MainStatsElement getMainStats() {
        return mainStats;
    }

    public EquipmentElement getEquipment() {
        return equipment;
    }

    public HandsElement getHands() {
        return hands;
    }

    public List<PotionElement> getPotions() {
        return potions;
    }

    public SkillsElement getSkills() {
        return skills;
    }

    public DropsElement getDrops() {
        return drops;
    }

    public MessagesElement getMessages() {
        return messages;
    }

    public CommandsElement getCommands() {
        return commands;
    }
}
