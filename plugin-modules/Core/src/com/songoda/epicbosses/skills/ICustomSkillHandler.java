package com.songoda.epicbosses.skills;

import com.songoda.epicbosses.skills.types.CustomSkillElement;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 16-Dec-18
 */
public interface ICustomSkillHandler extends ISkillHandler<CustomSkillElement> {

    String getSkillName();

}
