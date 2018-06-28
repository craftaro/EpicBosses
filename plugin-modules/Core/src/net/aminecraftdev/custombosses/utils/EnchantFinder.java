package net.aminecraftdev.custombosses.utils;

import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 14-Nov-17
 */
public enum EnchantFinder {

    protection("Protection", Enchantment.PROTECTION_ENVIRONMENTAL, "protection", "protection_environmental"),
    fire_protection("Fire Protection", Enchantment.PROTECTION_FIRE, "fire protection", "fireprot", "fireprotection", "protection_fire"),
    feather_falling("Feather Falling", Enchantment.PROTECTION_FALL, "feather falling", "featherfalling", "feather_falling", "protectionfall", "protection_fall"),
    blast_protection("Blast Protection", Enchantment.PROTECTION_EXPLOSIONS, "blast protection", "blastprotect", "blastprotection", "blast_protection", "protection_explosions"),
    projectile_protection("Projectile Protection", Enchantment.PROTECTION_PROJECTILE, "projectile protection", "projprotect", "projectileprotect", "projectileprotection", "projectile_protection", "protection_projectile"),
    respiration("Respiration", Enchantment.OXYGEN, "oxygen", "respiration"),
    aqua_affinity("Aqua Affinity", Enchantment.WATER_WORKER, "water_worker", "water worker", "aquaaffinity", "aqua_affinity", "aqua affinity"),
    thorns("Thorns", Enchantment.THORNS, "thorns"),
    depth_strider("Depth Strider", Enchantment.DEPTH_STRIDER, "depth_strider", "depth strider"),
    frost_walker("Frost Walker", Enchantment.getById(9), "frost_walker", "frost walker"),
    binding_curse("Curse of Binding", Enchantment.getById(10), "binding_curse", "binding curse", "curse"),
    sharpness("Sharpness", Enchantment.DAMAGE_ALL, "sharpness", "damage", "damage all", "damage_all"),
    smite("Smite", Enchantment.DAMAGE_UNDEAD, "smite", "damage_undead", "damage undead"),
    bane_of_arthropods("Bane of Arthropods", Enchantment.DAMAGE_ARTHROPODS, "damage_arthropods", "arthropods", "bane_of_arthropods", "bane of arthropods"),
    knockback("Knockback", Enchantment.KNOCKBACK, "knockback"),
    fire_aspect("Fire Aspect", Enchantment.FIRE_ASPECT, "fire aspect", "fire_aspect", "fireaspect"),
    looting("Looting", Enchantment.LOOT_BONUS_MOBS, "looting", "loot bonus mobs", "loot_bonus_mobs"),
    sweeping_edge("Sweeping Edge", Enchantment.getById(22), "sweeping edge", "sweeping_edge"),
    efficiency("Efficiency", Enchantment.DIG_SPEED, "efficiency", "dig speed", "dig_speed", "digspeed"),
    silk_touch("Silk Touch", Enchantment.SILK_TOUCH, "silk touch", "silk_touch", "silktouch", "silk"),
    unbreaking("Unbreaking", Enchantment.DURABILITY, "durability", "unbreaking"),
    fortune("Fortune", Enchantment.LOOT_BONUS_BLOCKS, "fortune", "loot_bonus_blocks", "loot bonus blocks"),
    power("Power", Enchantment.ARROW_DAMAGE, "power", "arrow_damage", "arrow damage"),
    punch("Punch", Enchantment.ARROW_KNOCKBACK, "punch", "arrow_knockback", "arrow knockback"),
    flame("Flame", Enchantment.ARROW_FIRE, "flame", "arrow_fire", "arrow fire"),
    infinite("Infinite", Enchantment.ARROW_INFINITE, "infinite", "arrow_infinite", "arrow infinite"),
    luck("Luck", Enchantment.LUCK, "luck"),
    lure("Lure", Enchantment.LURE, "lure"),
    mending("Mending", Enchantment.getById(70), "mending"),
    curse_of_vanishing("Curse of Vanishing", Enchantment.getById(71), "vanishing", "vanishing curse", "vanishing_curse", "curseofvanishing", "vanishingcurse", "curse of vanishing", "curse_of_vanishing");

    private Enchantment enchantment;
    private String fancyName;
    private List<String> names = new ArrayList<>();

    EnchantFinder(String fancyName, Enchantment enchantment, String... names) {
        this.fancyName = fancyName;
        this.enchantment = enchantment;

        this.names.addAll(Arrays.asList(names));
        this.names.add(fancyName);
    }

    public Enchantment getEnchantment() {
        return enchantment;
    }

    public List<String> getNames() {
        return names;
    }

    public String getFancyName() {
        return fancyName;
    }

    public static EnchantFinder getByName(String name) {
        for(EnchantFinder enchantFinder : values()) {
            List<String> names = enchantFinder.getNames();

            for(String s : names) {
                if(s.equalsIgnoreCase(name)) return enchantFinder;
            }
        }

        return null;
    }

    public static EnchantFinder getByEnchant(Enchantment enchantment) {
        for(EnchantFinder enchantFinder : values()) {
            Enchantment enchantFinderEnchant = enchantFinder.getEnchantment();

            if(enchantFinderEnchant.equals(enchantment)) return enchantFinder;
        }

        return null;
    }
}
