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
import com.songoda.epicbosses.skills.types.CommandSkill;
import com.songoda.epicbosses.skills.types.CustomSkill;
import com.songoda.epicbosses.skills.types.PotionSkill;
import com.songoda.epicbosses.utils.Debug;
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
import org.bukkit.potion.Potion;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 17-Nov-18
 */
public class BossSkillListener implements Listener {

    private BossEntityManager bossEntityManager;
    private SkillsFileManager skillsFileManager;

    public BossSkillListener(CustomBosses plugin) {
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

        List<LivingEntity> targettedEntities = getTargetedEntities(activeBossHolder, skill, activeBossHolder.getLivingEntity().getLocation(), damagingEntity);

        if(skill.getType().equalsIgnoreCase("POTION")) {
            PotionSkill potionSkill = (PotionSkill) skill;

            potionSkill.castSkill(activeBossHolder, targettedEntities);
        } else if(skill.getType().equalsIgnoreCase("COMMAND")) {
            CommandSkill commandSkill = (CommandSkill) skill;

            commandSkill.castSkill(activeBossHolder, targettedEntities);
        } else if(skill.getType().equalsIgnoreCase("CUSTOM")) {
            CustomSkill customSkill = (CustomSkill) skill;


        }
    }

    private List<LivingEntity> getTargetedEntities(ActiveBossHolder activeBossHolder, Skill skill, Location center, LivingEntity damager) {
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

}
