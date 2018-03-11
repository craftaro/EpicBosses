package net.aminecraftdev.custombosses.entities.components.skills.data;

import com.google.gson.annotations.Expose;
import net.aminecraftdev.custombosses.utils.base.BaseSkillData;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 11-Mar-18
 */
public class CustomSkillData extends BaseSkillData {

    @Expose private Object customData;

    public CustomSkillData(Object object) {
        this.customData = object;
    }

    public Object getCustomData() {
        return customData;
    }
}
