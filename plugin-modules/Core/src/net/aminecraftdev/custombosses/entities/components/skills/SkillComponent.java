package net.aminecraftdev.custombosses.entities.components.skills;

import com.google.gson.annotations.Expose;
import net.aminecraftdev.custombosses.utils.base.BaseSkillData;
import net.aminecraftdev.custombosses.skills.enums.SkillTarget;
import net.aminecraftdev.custombosses.skills.enums.SkillType;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 11-Mar-18
 */
public class SkillComponent {

    @Expose private String name;
    @Expose private int radius;
    @Expose private SkillTarget skillTarget;
    @Expose private SkillType skillType;
    @Expose private String showName, customMessage;
    @Expose private BaseSkillData skillData;
    @Expose private SkillGroupComponent skillGroupComponent;

    public SkillComponent(String name, int radius, SkillTarget skillTarget, SkillType skillType, String showName, String customMessage, BaseSkillData skillData, SkillGroupComponent skillGroupComponent) {
        this.name = name;

        setRadius(radius);
        setSkillTarget(skillTarget);
        setSkillType(skillType, skillData);
        setShowName(showName);
        setCustomMessage(customMessage);

        this.skillGroupComponent = skillGroupComponent;
    }

    public String getName() {
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

    public void setSkillType(SkillType skillType, BaseSkillData baseSkillData) {
        this.skillType = skillType;
        this.skillData = baseSkillData;
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

    public BaseSkillData getSkillData() {
        return skillData;
    }

    public SkillGroupComponent getSkillGroupComponent() {
        return skillGroupComponent;
    }
}
