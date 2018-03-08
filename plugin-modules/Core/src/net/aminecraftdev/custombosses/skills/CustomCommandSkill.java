package net.aminecraftdev.custombosses.skills;

import net.aminecraftdev.custombosses.handlers.ValidationHandler;
import net.aminecraftdev.custombosses.models.CustomSkillModel;
import net.aminecraftdev.custombosses.skills.base.CustomSkill;
import org.bukkit.Bukkit;
import org.bukkit.entity.LivingEntity;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 08-Mar-18
 */
public class CustomCommandSkill extends CustomSkill {

    private List<String> commands = new ArrayList<>();

    public CustomCommandSkill(String identifier, CustomSkillModel customSkillModel, List<String> commands) {
        super(identifier, customSkillModel);

        if(ValidationHandler.isNull(commands)) return;

        this.commands.addAll(commands);
    }

    @Override
    public void executeSkill(LivingEntity livingEntity) {
        for(String command : this.commands) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command.replace("%player%", livingEntity.getName()));
        }
    }
}
