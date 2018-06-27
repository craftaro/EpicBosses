package net.aminecraftdev.custombosses.mechanics;

import net.aminecraftdev.custombosses.entity.BossEntity;
import net.aminecraftdev.custombosses.holder.ActiveBossHolder;
import net.aminecraftdev.custombosses.utils.IMechanic;
import net.aminecraftdev.custombosses.utils.potion.PotionEffectConverter;
import net.aminecraftdev.custombosses.utils.potion.holder.PotionEffectHolder;
import org.bukkit.entity.LivingEntity;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Jun-18
 */
public class PotionMechanic implements IMechanic {

    private PotionEffectConverter potionEffectConverter;

    public PotionMechanic() {
        this.potionEffectConverter = new PotionEffectConverter();
    }

    @Override
    public boolean applyMechanic(BossEntity bossEntity, ActiveBossHolder activeBossHolder) {
        if(activeBossHolder.getLivingEntity() == null) return false;

        LivingEntity livingEntity = activeBossHolder.getLivingEntity();
        List<PotionEffectHolder> potionElements = bossEntity.getPotions();

        if(potionElements != null && !potionElements.isEmpty()) {
            potionElements.forEach(potionElement -> livingEntity.addPotionEffect(this.potionEffectConverter.from(potionElement)));
        }

        return true;
    }
}
