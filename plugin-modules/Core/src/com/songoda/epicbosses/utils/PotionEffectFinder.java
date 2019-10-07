package com.songoda.epicbosses.utils;

import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 21-Oct-18
 */
public enum PotionEffectFinder {

    Absorption("Absorption", PotionEffectType.ABSORPTION),
    Blindness("Blind", PotionEffectType.BLINDNESS, "blindness", "cantsee"),
    ConduitPower("ConduitPower", PotionEffectType.getByName("CONDUIT_POWER"), "conduit", "conduit_power"),
    Confusion("Confusion", PotionEffectType.CONFUSION, "nausea"),
    Resistance("Resistance", PotionEffectType.DAMAGE_RESISTANCE, "damage_resistance", "res", "damageresistance"),
    DolphinsGrace("DolphinsGrace", PotionEffectType.getByName("DOLPHINS_GRACE"), "grace", "dolphins_grace"),
    Haste("Haste", PotionEffectType.FAST_DIGGING, "fast_digging", "haste"),
    Fire_Resistance("FireResistance", PotionEffectType.FIRE_RESISTANCE, "fire_resistance", "fire_resist", "fire_res", "fireresist", "fireres"),
    Glowing("Glowing", PotionEffectType.getByName("GLOWING")),
    Harm("Harm", PotionEffectType.HARM, "damage"),
    Heal("Heal", PotionEffectType.HEAL),
    HealthBoost("HealthBoost", PotionEffectType.HEALTH_BOOST, "healthboost", "health_boost"),
    Hunger("Hunger", PotionEffectType.HUNGER, "starve", "starving"),
    Strength("Strength", PotionEffectType.INCREASE_DAMAGE, "increase_damage", "increasedamage"),
    Invisibility("Invisibility", PotionEffectType.INVISIBILITY),
    Jump("Jump", PotionEffectType.JUMP),
    Levitation("Levitation", PotionEffectType.getByName("LEVITATION")),
    Luck("Luck", PotionEffectType.getByName("LUCK")),
    NightVision("NightVision", PotionEffectType.NIGHT_VISION, "seeinthedarkness", "nv", "night_vision"),
    Poison("Poison", PotionEffectType.POISON, "witched"),
    Regen("regen", PotionEffectType.REGENERATION, "regeneration"),
    Saturation("Saturation", PotionEffectType.SATURATION, "saturated"),
    Slow("Slow", PotionEffectType.SLOW, "tank"),
    SlowFalling("SlowFalling", PotionEffectType.getByName("SLOW_FALLING"), "slow_falling"),
    MiningFatigue("MiningFatigue", PotionEffectType.SLOW_DIGGING, "slow_digging"),
    Speed("Speed", PotionEffectType.SPEED, "fast", "fastboots"),
    Unluck("Unlucky", PotionEffectType.getByName("UNLUCK"), "unluck", "notlucky"),
    WaterBreathing("WaterBreathing", PotionEffectType.WATER_BREATHING, "breathunderwater", "water_breathing", "fish"),
    Weakness("Weakness", PotionEffectType.WEAKNESS),
    Wither("Wither", PotionEffectType.WITHER, "blackhearts");

    private List<String> names = new ArrayList<>();
    private PotionEffectType potionEffectType;
    private String fancyName;

    PotionEffectFinder(String fancyName, PotionEffectType potionEffectType, String... names) {
        this.fancyName = fancyName;
        this.potionEffectType = potionEffectType;

        this.names.addAll(Arrays.asList(names));
        this.names.add(fancyName);
    }

    public static PotionEffectFinder getByName(String name) {
        for(PotionEffectFinder potionEffectFinder : values()) {
            List<String> names = potionEffectFinder.getNames();

            for(String s : names) {
                if(s.equalsIgnoreCase(name)) return potionEffectFinder;
            }
        }

        return null;
    }

    public static PotionEffectFinder getByEffect(PotionEffectType potionEffectType) {
        for(PotionEffectFinder potionEffectFinder : values()) {
            PotionEffectType effectType = potionEffectFinder.getPotionEffectType();

            if(effectType == null) continue;
            if(potionEffectType.equals(effectType)) return potionEffectFinder;
        }

        return null;
    }

    public List<String> getNames() {
        return this.names;
    }

    public PotionEffectType getPotionEffectType() {
        return this.potionEffectType;
    }

    public String getFancyName() {
        return this.fancyName;
    }
}
