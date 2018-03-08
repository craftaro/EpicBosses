package net.aminecraftdev.custombosses.handlers.models;

import net.aminecraftdev.custombosses.models.CustomSkillModel;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 08-Mar-18
 */
public interface ISkillHandler {

    void addSkill(CustomSkillModel skillModel);

    List<CustomSkillModel> getSkills();

}
