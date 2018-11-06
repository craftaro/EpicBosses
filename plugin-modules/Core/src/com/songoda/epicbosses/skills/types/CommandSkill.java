package com.songoda.epicbosses.skills.types;

import com.google.gson.annotations.Expose;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.elements.CommandSkillElement;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.RandomUtils;
import com.songoda.epicbosses.utils.ServerUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.LivingEntity;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 05-Nov-18
 */
public class CommandSkill extends Skill {

    @Expose @Getter @Setter private List<CommandSkillElement> commands;

    @Override
    public void castSkill(ActiveBossHolder activeBossHolder, List<LivingEntity> nearbyEntities) {
        List<CommandSkillElement> commandSkillElements = getCommands();
        ServerUtils serverUtils = ServerUtils.get();

        if(commandSkillElements.isEmpty()) {
            Debug.SKILL_COMMANDS_ARE_EMPTY.debug(getDisplayName());
            return;
        }

        nearbyEntities.forEach(livingEntity ->
            commandSkillElements.forEach(commandSkillElement -> {
                Double chance = commandSkillElement.getChance();
                List<String> commands = commandSkillElement.getCommands();

                if(commands == null || commands.isEmpty()) return;
                if(chance == null) chance = 100.0;
                if(!RandomUtils.get().canPreformAction(chance)) return;

                commands.replaceAll(s -> s.replace("%player%", livingEntity.getName()));
                commands.forEach(serverUtils::sendConsoleCommand);
            })
        );
    }
}
