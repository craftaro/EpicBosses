package net.aminecraftdev.custombosses.utils.parser.skills;

import net.aminecraftdev.custombosses.entities.components.skills.SkillComponent;
import net.aminecraftdev.custombosses.entities.components.skills.data.PotionSkillData;
import net.aminecraftdev.custombosses.skills.types.PotionSkill;
import net.aminecraftdev.custombosses.utils.base.BaseParser;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 12-Mar-18
 */
public class PotionSkillParser extends BaseParser<SkillComponent, PotionSkill> {

    public PotionSkillParser(SkillComponent skillComponent) {
        super(skillComponent);
    }

    @Override
    public PotionSkill parse() {
        if(this.input.getSkillData() instanceof PotionSkillData) {
            PotionSkillData potionSkillData = (PotionSkillData) this.input.getSkillData();

            return new PotionSkill(this.input, potionSkillData);
        }

        return null;
    }
}
