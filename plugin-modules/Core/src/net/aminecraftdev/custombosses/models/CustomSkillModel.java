package net.aminecraftdev.custombosses.models;

import net.aminecraftdev.custombosses.handlers.IIdentifier;
import net.aminecraftdev.custombosses.skills.types.SkillTarget;
import net.aminecraftdev.custombosses.skills.types.SkillType;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 08-Mar-18
 */
public class CustomSkillModel implements IIdentifier {

    private SkillTarget skillTarget;
    private SkillType skillType;
    private String identifier;

    public CustomSkillModel(String identifier, SkillTarget skillTarget, SkillType skillType) {
        this.identifier = identifier;

        setSkillTarget(skillTarget);
        setSkillType(skillType);
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    public SkillType getSkillType() {
        return this.skillType;
    }

    public void setSkillType(SkillType skillType) {
        this.skillType = skillType;
    }

    public SkillTarget getSkillTarget() {
        return this.skillTarget;
    }

    public void setSkillTarget(SkillTarget skillTarget) {
        this.skillTarget = skillTarget;
    }
}
