package com.songoda.epicbosses.utils.itemstack.converters;

import com.songoda.epicbosses.utils.EnchantFinder;
import com.songoda.epicbosses.utils.IConverter;
import org.bukkit.enchantments.Enchantment;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 28-Apr-18
 */
public class EnchantConverter implements IConverter<List<String>, Map<Enchantment, Integer>> {

    @Override
    public List<String> to(Map<Enchantment, Integer> enchantmentIntegerMap) {
        List<String> enchants = new ArrayList<>();

        for(Map.Entry<Enchantment, Integer> entry : enchantmentIntegerMap.entrySet()) {
            int level = entry.getValue();
            Enchantment enchantment = entry.getKey();
            EnchantFinder enchantFinder = EnchantFinder.getByEnchant(enchantment);

            if(enchantFinder == null) continue;

            enchants.add(enchantFinder.getFancyName() + ":" + level);
        }

        return enchants;
    }

    @Override
    public Map<Enchantment, Integer> from(List<String> strings) {
        Map<Enchantment, Integer> enchantments = new HashMap<>();

        for(String s : strings) {
            String[] split = s.split(":");
            String fancyName = split[0];
            Integer level = Integer.parseInt(split[1]);
            EnchantFinder enchantFinder = EnchantFinder.getByName(fancyName);

            if(enchantFinder == null) continue;

            enchantments.put(enchantFinder.getEnchantment(), level);
        }

        return enchantments;
    }
}
