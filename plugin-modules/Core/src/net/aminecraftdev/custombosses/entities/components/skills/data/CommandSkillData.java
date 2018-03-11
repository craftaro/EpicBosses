package net.aminecraftdev.custombosses.entities.components.skills.data;

import com.google.gson.annotations.Expose;
import net.aminecraftdev.custombosses.utils.base.BaseSkillData;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 11-Mar-18
 */
public class CommandSkillData extends BaseSkillData {

    @Expose private List<String> commands;

    public CommandSkillData(List<String> commands) {
        this.commands = commands;
    }

    public List<String> getCommands() {
        return commands;
    }

}
