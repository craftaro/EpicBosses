package com.songoda.epicbosses.skills;

import com.songoda.epicbosses.skills.types.CustomSkillElement;
import com.songoda.epicbosses.utils.panel.base.ClickAction;

import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 16-Dec-18
 */
public interface ICustomSkillHandler extends ISkillHandler<CustomSkillElement> {

    boolean doesUseMultiplier();

    Map<String, Class<?>> getOtherSkillData();

    Map<Integer, ClickAction> getOtherSkillDataActions(Skill skill, CustomSkillElement customSkillElement);

    String getSkillName();

}
