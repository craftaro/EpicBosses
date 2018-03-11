package net.aminecraftdev.custombosses.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.aminecraftdev.custombosses.utils.base.BaseSkillData;
import net.aminecraftdev.custombosses.skills.enums.SkillTarget;
import net.aminecraftdev.custombosses.skills.enums.SkillType;
import net.aminecraftdev.custombosses.utils.adapters.PotionEffectTypeAdapter;
import net.aminecraftdev.custombosses.utils.adapters.SkillDataAdapter;
import net.aminecraftdev.custombosses.utils.adapters.SkillTargetAdapter;
import net.aminecraftdev.custombosses.utils.adapters.SkillTypeAdapter;
import org.bukkit.potion.PotionEffectType;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 11-Mar-18
 */
public class BossesGson {

    private static Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .excludeFieldsWithoutExposeAnnotation()
            .registerTypeAdapter(PotionEffectType.class, new PotionEffectTypeAdapter())
            .registerTypeAdapter(BaseSkillData.class, new SkillDataAdapter())
            .registerTypeAdapter(SkillTarget.class, new SkillTargetAdapter())
            .registerTypeAdapter(SkillType.class, new SkillTypeAdapter())
            .create();

    public static Gson get() {
        return gson;
    }

}
