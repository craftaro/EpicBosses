package com.songoda.epicbosses.skills;

import com.songoda.epicbosses.skills.types.CustomSkillElement;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 18-Nov-18
 */
public abstract class CustomSkillHandler implements ISkillHandler<CustomSkillElement> {

    @Override
    public String getSkillName() {
        return getClass().getSimpleName();
    }

}
