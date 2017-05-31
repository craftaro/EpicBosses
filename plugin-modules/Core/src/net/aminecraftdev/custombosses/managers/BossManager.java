package net.aminecraftdev.custombosses.managers;

import net.aminecraftdev.custombosses.handlers.builders.EntityHandler;
import net.aminecraftdev.custombosses.handlers.builders.EquipmentHandler;
import net.aminecraftdev.custombosses.handlers.mobs.BossEntity;
import net.aminecraftdev.custombosses.handlers.mobs.MinionEntity;
import net.aminecraftdev.custombosses.innerapi.PotionUtils;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.potion.PotionEffect;

import java.util.HashSet;
import java.util.Set;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 31-May-17
 */
public class BossManager {

    private static final Set<BossEntity> BOSSES = new HashSet<>();
    private static final Set<MinionEntity> MINIONS = new HashSet<>();
    private EntityHandler entityHandler;
    private EquipmentHandler equipmentHandler;

    public BossManager() {
        this.entityHandler = new EntityHandler();
        this.equipmentHandler = new EquipmentHandler();
    }




    public EntityHandler getEntityHandler() {
        return entityHandler;
    }

    public EquipmentHandler getEquipmentHandler() {
        return equipmentHandler;
    }

    public void applyPotionEffects(LivingEntity livingEntity, ConfigurationSection configurationSection) {
        for(String s : configurationSection.getKeys(false)) {
            PotionEffect potionEffect = PotionUtils.getPotionEffect(configurationSection.getConfigurationSection(s));

            if(potionEffect == null) continue;

            livingEntity.addPotionEffect(potionEffect);
        }
    }



}
