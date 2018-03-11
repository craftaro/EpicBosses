package net.aminecraftdev.custombosses.entities.components.skills;

import com.google.gson.annotations.Expose;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 11-Mar-18
 */
public class SkillGroupComponent {

    @Expose private boolean isGroup;
    @Expose private List<String> connectedSkills;

    public SkillGroupComponent(boolean isGroup, List<String> connectedSkills) {
        this.isGroup = isGroup;
        this.connectedSkills = connectedSkills;
    }

    public boolean isGroup() {
        return this.isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public List<String> getConnectedSkills() {
        return connectedSkills;
    }

    public void setConnectedSkills(List<String> connectedSkills) {
        this.connectedSkills = connectedSkills;
    }
}
