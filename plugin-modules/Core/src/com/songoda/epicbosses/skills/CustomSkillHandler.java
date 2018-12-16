package com.songoda.epicbosses.skills;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 18-Nov-18
 */
public abstract class CustomSkillHandler implements ICustomSkillHandler {

    @Override
    public String getSkillName() {
        return getClass().getSimpleName();
    }

}
