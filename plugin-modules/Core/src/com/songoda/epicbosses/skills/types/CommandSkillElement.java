package com.songoda.epicbosses.skills.types;

import com.google.gson.annotations.Expose;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.elements.SubCommandSkillElement;
import com.songoda.epicbosses.skills.interfaces.ISkillHandler;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.RandomUtils;
import com.songoda.epicbosses.utils.ServerUtils;
import org.bukkit.entity.LivingEntity;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 05-Nov-18
 */
public class CommandSkillElement implements ISkillHandler<CommandSkillElement> {

    @Expose
    private List<SubCommandSkillElement> commands;

    public CommandSkillElement(List<SubCommandSkillElement> commandSkillElements) {
        this.commands = commandSkillElements;
    }

    @Override
    public void castSkill(Skill skill, CommandSkillElement commandSkillElement, ActiveBossHolder activeBossHolder, List<LivingEntity> nearbyEntities) {
        List<SubCommandSkillElement> commandSkillElements = getCommands();
        ServerUtils serverUtils = ServerUtils.get();

        if(commandSkillElements.isEmpty()) {
            Debug.SKILL_COMMANDS_ARE_EMPTY.debug();
            return;
        }

        nearbyEntities.forEach(livingEntity ->
                commandSkillElements.forEach(commandSkillEle -> {
                    Double chance = commandSkillEle.getChance();
                    List<String> commands = commandSkillEle.getCommands();

                    if(commands == null || commands.isEmpty()) return;
                    if(chance == null) chance = 100.0;
                    if(!RandomUtils.get().canPreformAction(chance)) return;

                    commands.replaceAll(s -> s.replace("%player%", livingEntity.getName()));
                    commands.forEach(serverUtils::sendConsoleCommand);
                })
        );
    }

    public List<SubCommandSkillElement> getCommands() {
        return this.commands;
    }

    public void setCommands(List<SubCommandSkillElement> commands) {
        this.commands = commands;
    }
}
