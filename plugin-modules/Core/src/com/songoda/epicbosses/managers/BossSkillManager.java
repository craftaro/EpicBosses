package com.songoda.epicbosses.managers;

import com.songoda.epicbosses.skills.Skill;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 05-Nov-18
 */
public class BossSkillManager {

    private static final Set<Skill> SKILLS = new HashSet<>();

    public boolean registerSkill(Skill skill) {
        if(SKILLS.contains(skill)) return false;

        SKILLS.add(skill);
        return true;
    }

    public void removeSkill(Skill skill) {
        if(!SKILLS.contains(skill)) return;

        SKILLS.remove(skill);
    }

}
