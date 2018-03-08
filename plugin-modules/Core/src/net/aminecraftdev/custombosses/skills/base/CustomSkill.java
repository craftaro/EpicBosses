package net.aminecraftdev.custombosses.skills.base;

import net.aminecraftdev.custombosses.handlers.IIdentifier;
import net.aminecraftdev.custombosses.models.CustomSkillModel;
import org.bukkit.entity.LivingEntity;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 08-Mar-18
 */
public abstract class CustomSkill implements IIdentifier {

    private CustomSkillModel customSkillModel;
    private String identifier;

    public CustomSkill(String identifier, CustomSkillModel customSkillModel) {
        this.identifier = identifier;
        this.customSkillModel = customSkillModel;
    }

    @Override
    public String getIdentifier() {
        return this.identifier;
    }

    public CustomSkillModel getCustomSkillModel() {
        return this.customSkillModel;
    }

    public abstract void executeSkill(LivingEntity livingEntity);
}
