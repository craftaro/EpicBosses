package net.aminecraftdev.custombosses.utils.parser.skills;

import net.aminecraftdev.custombosses.entities.components.skills.SkillComponent;
import net.aminecraftdev.custombosses.entities.components.skills.data.CommandSkillData;
import net.aminecraftdev.custombosses.skills.types.CommandSkill;
import net.aminecraftdev.custombosses.utils.base.BaseParser;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 12-Mar-18
 */
public class CommandSkillParser extends BaseParser<SkillComponent, CommandSkill> {

    public CommandSkillParser(SkillComponent skillComponent) {
        super(skillComponent);
    }

    @Override
    public CommandSkill parse() {
        if(this.input.getSkillData() instanceof CommandSkillData) {
            CommandSkillData commandSkillData = (CommandSkillData) this.input.getSkillData();

            return new CommandSkill(this.input, commandSkillData);
        }

        return null;
    }
}
