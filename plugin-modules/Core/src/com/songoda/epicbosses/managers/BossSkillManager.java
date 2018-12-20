package com.songoda.epicbosses.managers;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.events.BossSkillEvent;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.skills.CustomSkillHandler;
import com.songoda.epicbosses.skills.interfaces.ICustomSkillAction;
import com.songoda.epicbosses.skills.interfaces.ISkillHandler;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.custom.*;
import com.songoda.epicbosses.skills.types.CommandSkillElement;
import com.songoda.epicbosses.skills.types.CustomSkillElement;
import com.songoda.epicbosses.skills.types.GroupSkillElement;
import com.songoda.epicbosses.skills.types.PotionSkillElement;
import com.songoda.epicbosses.utils.*;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 05-Nov-18
 */
public class BossSkillManager implements ILoadable {

    private static final Set<CustomSkillHandler> SKILLS = new HashSet<>();

    private CustomBosses plugin;

    public BossSkillManager(CustomBosses plugin) {
        this.plugin = plugin;
    }

    @Override
    public void load() {
        registerCustomSkill(new Cage(this.plugin));
        registerCustomSkill(new Disarm());
        registerCustomSkill(new Fireball());
        registerCustomSkill(new Grapple());
        registerCustomSkill(new Insidious());
        registerCustomSkill(new Knockback());
        registerCustomSkill(new Launch());
        registerCustomSkill(new Lightning());
        registerCustomSkill(new Minions(this.plugin));
        registerCustomSkill(new Warp());
    }

    public void handleSkill(List<String> masterMessage, Skill skill, List<LivingEntity> targetedEntities, ActiveBossHolder activeBossHolder, boolean message, boolean subSkill) {
        if(skill == null) {
            Debug.SKILL_NOT_FOUND.debug();
            return;
        }

        ISkillHandler<?> skillHandler;
        String bossDisplayName = activeBossHolder.getName();
        String skillDisplayName = skill.getDisplayName();

        if(skill.getType().equalsIgnoreCase("POTION")) {
            PotionSkillElement potionSkillElement = getPotionSkillElement(skill);

            potionSkillElement.castSkill(skill, potionSkillElement, activeBossHolder, targetedEntities);
            skillHandler = potionSkillElement;
        } else if(skill.getType().equalsIgnoreCase("COMMAND")) {
            CommandSkillElement commandSkillElement = getCommandSkillElement(skill);

            commandSkillElement.castSkill(skill, commandSkillElement, activeBossHolder, targetedEntities);
            skillHandler = commandSkillElement;
        } else if(skill.getType().equalsIgnoreCase("GROUP")) {
            if(subSkill) return;

            GroupSkillElement groupSkillElement = getGroupSkillElement(skill);

            groupSkillElement.castSkill(skill, groupSkillElement, activeBossHolder, targetedEntities);
            skillHandler = groupSkillElement;
        } else if(skill.getType().equalsIgnoreCase("CUSTOM")) {
            CustomSkillElement customSkillElement = getCustomSkillElement(skill);

            skillHandler = handleCustomSkillCasting(skill, customSkillElement, activeBossHolder, targetedEntities);
        } else {
            return;
        }

        if(message && masterMessage != null) {
            masterMessage.replaceAll(s -> s.replace("{boss}", bossDisplayName).replace("{skill}", skillDisplayName));
            targetedEntities.forEach(livingEntity -> MessageUtils.get().sendMessage(livingEntity, masterMessage));
        }

        BossSkillEvent bossSkillEvent = new BossSkillEvent(activeBossHolder, skillHandler, skill);
        ServerUtils.get().callEvent(bossSkillEvent);
    }

    public PotionSkillElement getPotionSkillElement(Skill skill) {
        if(skill.getType().equalsIgnoreCase("POTION")) {
            return BossesGson.get().fromJson(skill.getCustomData(), PotionSkillElement.class);
        }

        return null;
    }

    public CommandSkillElement getCommandSkillElement(Skill skill) {
        if(skill.getType().equalsIgnoreCase("COMMAND")) {
            return BossesGson.get().fromJson(skill.getCustomData(), CommandSkillElement.class);
        }

        return null;
    }

    public GroupSkillElement getGroupSkillElement(Skill skill) {
        if(skill.getType().equalsIgnoreCase("GROUP")) {
            return BossesGson.get().fromJson(skill.getCustomData(), GroupSkillElement.class);
        }

        return null;
    }

    public CustomSkillElement getCustomSkillElement(Skill skill) {
        if(skill.getType().equalsIgnoreCase("CUSTOM")) {
            return BossesGson.get().fromJson(skill.getCustomData(), CustomSkillElement.class);
        }

        return null;
    }

    public List<CustomSkillHandler> getSkills() {
        return new ArrayList<>(SKILLS);
    }

    public boolean registerCustomSkill(CustomSkillHandler customSkillHandler) {
        if(SKILLS.contains(customSkillHandler)) return false;
        if(customSkillHandler == null) return false;

        SKILLS.add(customSkillHandler);
        return true;
    }

    public void removeCustomSkill(CustomSkillHandler customSkillHandler) {
        if(!SKILLS.contains(customSkillHandler)) return;
        if(customSkillHandler == null) return;

        SKILLS.remove(customSkillHandler);
    }

    public List<LivingEntity> getTargetedEntities(ActiveBossHolder activeBossHolder, Skill skill, Location center, LivingEntity damager) {
        double radiusSqr = skill.getRadius() * skill.getRadius();
        List<LivingEntity> targetedList = new ArrayList<>();
        String mode = skill.getMode();

        if(mode.equalsIgnoreCase("ONE")) {
            return Arrays.asList(damager);
        } else if(mode.equalsIgnoreCase("BOSS")) {
            targetedList.addAll(activeBossHolder.getLivingEntityMap().values());
        } else {
            for(Player player : Bukkit.getOnlinePlayers()) {
                if(!player.getWorld().equals(center.getWorld())) continue;
                if(center.distanceSquared(player.getLocation()) > radiusSqr) continue;

                if(mode.equalsIgnoreCase("ALL")) {
                    targetedList.add(player);
                } else if(mode.equalsIgnoreCase("RANDOM")) {
                    if(RandomUtils.get().preformRandomAction()) {
                        targetedList.add(player);
                    }
                }
            }
        }

        return targetedList;
    }

    private CustomSkillHandler handleCustomSkillCasting(Skill skill, CustomSkillElement customSkillElement, ActiveBossHolder activeBossHolder, List<LivingEntity> nearbyEntities) {
        String type = customSkillElement.getCustom().getType();
        CustomSkillHandler customSkillHandler = getCustomSkillHandler(type);

        if(customSkillHandler == null) {
            Debug.FAILED_TO_OBTAIN_THE_SKILL_HANDLER.debug(type);
            return null;
        }

        customSkillHandler.castSkill(skill, customSkillElement, activeBossHolder, nearbyEntities);
        return customSkillHandler;
    }

    private CustomSkillHandler getCustomSkillHandler(String name) {
        for(CustomSkillHandler customSkillHandler : new HashSet<>(SKILLS)) {
            String skillName = customSkillHandler.getSkillName();

            if(skillName.equalsIgnoreCase(name)) return customSkillHandler;
        }

        return null;
    }

    public static ICustomSkillAction createCustomSkillAction(String name, ItemStack displayStack, ClickAction clickAction) {
        return new CustomSkillActionCreator(name, displayStack, clickAction);
    }

    private static class CustomSkillActionCreator implements ICustomSkillAction {

        private final ClickAction clickAction;
        private final String name;
        private final ItemStack itemStack;

        public CustomSkillActionCreator(String name, ItemStack itemStack, ClickAction clickAction) {
            this.name = name;
            this.itemStack = itemStack;
            this.clickAction = clickAction;
        }

        @Override
        public ClickAction getAction() {
            return null;
        }

        @Override
        public String getSettingName() {
            return null;
        }

        @Override
        public ItemStack getDisplayItemStack() {
            return null;
        }
    }
}
