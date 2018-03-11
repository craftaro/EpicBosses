package net.aminecraftdev.custombosses.skills.types;

import net.aminecraftdev.custombosses.entities.components.skills.SkillComponent;
import net.aminecraftdev.custombosses.skills.CustomSkill;
import net.aminecraftdev.custombosses.entities.components.skills.data.CommandSkillData;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 12-Mar-18
 */
public class CommandSkill extends CustomSkill {

    private List<String> commands;

    public CommandSkill(SkillComponent skillComponent, CommandSkillData commandSkillData) {
        super(skillComponent);

        this.commands = commandSkillData.getCommands();
    }

    public List<String> getCommands() {
        return commands;
    }

    public void setCommands(List<String> commands) {
        this.commands = commands;
    }

    @Override
    public void execute(Player player) {
        for(String s : getCommands()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), s.replace("%player%", player.getName()));
        }
    }
}
