package com.songoda.epicbosses.listeners.during;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.events.BossSkillEvent;
import com.songoda.epicbosses.events.PreBossSkillEvent;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.managers.BossEntityManager;
import com.songoda.epicbosses.managers.BossSkillManager;
import com.songoda.epicbosses.managers.files.SkillsFileManager;
import com.songoda.epicbosses.skills.ISkillHandler;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.types.CommandSkillElement;
import com.songoda.epicbosses.skills.types.CustomSkillElement;
import com.songoda.epicbosses.skills.types.GroupSkillElement;
import com.songoda.epicbosses.skills.types.PotionSkillElement;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.MessageUtils;
import com.songoda.epicbosses.utils.RandomUtils;
import com.songoda.epicbosses.utils.ServerUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import java.util.*;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 17-Nov-18
 */
public class BossSkillListener implements Listener {

    private BossEntityManager bossEntityManager;
    private SkillsFileManager skillsFileManager;
    private BossSkillManager bossSkillManager;

    public BossSkillListener(CustomBosses plugin) {
        this.bossSkillManager = plugin.getBossSkillManager();
        this.bossEntityManager = plugin.getBossEntityManager();
        this.skillsFileManager = plugin.getSkillsFileManager();
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onBossDamage(EntityDamageByEntityEvent event) {
        Entity entityBeingDamaged = event.getEntity();
        Entity entityDamaging = event.getDamager();

        if(!(entityBeingDamaged instanceof LivingEntity)) return;
        if(!(entityDamaging instanceof LivingEntity)) return;

        LivingEntity livingEntity = (LivingEntity) entityBeingDamaged;
        ActiveBossHolder activeBossHolder = this.bossEntityManager.getActiveBossHolder(livingEntity);

        if(activeBossHolder == null) return;

        BossEntity bossEntity = activeBossHolder.getBossEntity();

        if(bossEntity.getSkills() == null || bossEntity.getSkills().getOverallChance() == null) return;

        if(RandomUtils.get().canPreformAction(bossEntity.getSkills().getOverallChance())) {
            PreBossSkillEvent preBossSkillEvent = new PreBossSkillEvent(activeBossHolder, livingEntity, (LivingEntity) entityDamaging);

            ServerUtils.get().callEvent(preBossSkillEvent);
        }
    }

    @EventHandler
    public void onPreSkill(PreBossSkillEvent event) {
        ActiveBossHolder activeBossHolder = event.getActiveBossHolder();
        BossEntity bossEntity = event.getActiveBossHolder().getBossEntity();
        LivingEntity damagingEntity = event.getDamagingEntity();
        List<String> skills = bossEntity.getSkills().getSkills();
        List<String> masterMessage = BossAPI.getStoredMessages(bossEntity.getSkills().getMasterMessage());

        if(skills.isEmpty()) {
            Debug.SKILL_EMPTY_SKILLS.debug(BossAPI.getBossEntityName(bossEntity));
            return;
        }

        Collections.shuffle(skills);

        String skillInput = skills.get(0);
        Skill skill = this.skillsFileManager.getSkill(skillInput);

        if(skill == null) {
            Debug.SKILL_NOT_FOUND.debug();
            return;
        }

        List<LivingEntity> targettedEntities = this.bossSkillManager.getTargetedEntities(activeBossHolder, skill, activeBossHolder.getLivingEntity().getLocation(), damagingEntity);
        String bossDisplayName = activeBossHolder.getName();
        String skillDisplayName = skill.getDisplayName();
        ISkillHandler<?> skillHandler;

        if(skill.getType().equalsIgnoreCase("POTION")) {
            PotionSkillElement potionSkillElement = this.bossSkillManager.getPotionSkillElement(skill);

            potionSkillElement.castSkill(skill, potionSkillElement, activeBossHolder, targettedEntities);
            skillHandler = potionSkillElement;
        } else if(skill.getType().equalsIgnoreCase("COMMAND")) {
            CommandSkillElement commandSkillElement = this.bossSkillManager.getCommandSkillElement(skill);

            commandSkillElement.castSkill(skill, commandSkillElement, activeBossHolder, targettedEntities);
            skillHandler = commandSkillElement;
        } else if(skill.getType().equalsIgnoreCase("GROUP")) {
            GroupSkillElement groupSkillElement = this.bossSkillManager.getGroupSkillElement(skill);

            groupSkillElement.castSkill(skill, groupSkillElement, activeBossHolder, targettedEntities);
            skillHandler = groupSkillElement;
        } else if(skill.getType().equalsIgnoreCase("CUSTOM")) {
            CustomSkillElement customSkillElement = this.bossSkillManager.getCustomSkillElement(skill);

            skillHandler = this.bossSkillManager.handleCustomSkillCasting(skill, customSkillElement, activeBossHolder, targettedEntities);
        } else {
            return;
        }

        masterMessage.replaceAll(s -> s.replace("{boss}", bossDisplayName).replace("{skill}", skillDisplayName));
        targettedEntities.forEach(livingEntity -> MessageUtils.get().sendMessage(livingEntity, masterMessage));

        BossSkillEvent bossSkillEvent = new BossSkillEvent(activeBossHolder, skillHandler, skill);
        ServerUtils.get().callEvent(bossSkillEvent);
    }

}
