package net.aminecraftdev.custombosses.skills;

import net.aminecraftdev.custombosses.entities.components.skills.SkillComponent;
import net.aminecraftdev.custombosses.utils.IIdentifier;
import net.aminecraftdev.custombosses.skills.enums.SkillTarget;
import net.aminecraftdev.custombosses.skills.enums.SkillType;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 11-Mar-18
 */
public abstract class CustomSkill implements IIdentifier {

    private String name, showName, customMessage;
    private List<String> linkedSkills;
    private SkillTarget skillTarget;
    private SkillType skillType;
    private boolean isGroup;
    private int radius;

    public CustomSkill(SkillComponent skillComponent) {
        this.name = skillComponent.getName();
        this.radius = skillComponent.getRadius();
        this.skillTarget = skillComponent.getSkillTarget();
        this.skillType = skillComponent.getSkillType();
        this.showName = skillComponent.getShowName();
        this.customMessage = skillComponent.getCustomMessage();
        this.linkedSkills = skillComponent.getSkillGroupComponent().getConnectedSkills();
        this.isGroup = skillComponent.getSkillGroupComponent().isGroup();
    }

    @Override
    public String getIdentifier() {
        return name;
    }

    public SkillTarget getSkillTarget() {
        return skillTarget;
    }

    public void setSkillTarget(SkillTarget skillTarget) {
        this.skillTarget = skillTarget;
    }

    public SkillType getSkillType() {
        return skillType;
    }

    public void setSkillType(SkillType skillType) {
        this.skillType = skillType;
    }

    public int getRadius() {
        return radius;
    }

    public void setRadius(int radius) {
        this.radius = radius;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public String getCustomMessage() {
        return customMessage;
    }

    public void setCustomMessage(String customMessage) {
        this.customMessage = customMessage;
    }

    public boolean isGroup() {
        return isGroup;
    }

    public void setGroup(boolean group) {
        isGroup = group;
    }

    public List<String> getLinkedSkills() {
        return linkedSkills;
    }

    public void setLinkedSkills(List<String> linkedSkills) {
        this.linkedSkills = linkedSkills;
    }

    public abstract void execute(Player player);
}
