package net.aminecraftdev.custombosses.mechanics;

import net.aminecraftdev.custombosses.entity.BossEntity;
import net.aminecraftdev.custombosses.holder.ActiveBossHolder;
import net.aminecraftdev.custombosses.utils.Debug;
import net.aminecraftdev.custombosses.utils.IMechanic;
import net.aminecraftdev.custombosses.utils.StringUtils;
import net.aminecraftdev.custombosses.utils.file.reader.SpigotYmlReader;
import net.aminecraftdev.custombosses.utils.version.VersionHandler;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 02-Jun-18
 */
public class SettingsMechanic implements IMechanic {

    private VersionHandler versionHandler;

    public SettingsMechanic() {
        this.versionHandler = new VersionHandler();
    }

    @Override
    public boolean applyMechanic(BossEntity bossEntity, ActiveBossHolder activeBossHolder) {
        if(activeBossHolder.getLivingEntity() == null) return false;

        LivingEntity livingEntity = activeBossHolder.getLivingEntity();
        EntityEquipment entityEquipment = livingEntity.getEquipment();

        livingEntity.setRemoveWhenFarAway(false);
        livingEntity.setCanPickupItems(false);
        entityEquipment.setHelmetDropChance(0.0F);
        entityEquipment.setChestplateDropChance(0.0F);
        entityEquipment.setLeggingsDropChance(0.0F);
        entityEquipment.setBootsDropChance(0.0F);

        if(this.versionHandler.canUseOffHand()) {
            entityEquipment.setItemInMainHandDropChance(0.0F);
            entityEquipment.setItemInOffHandDropChance(0.0F);
        } else {
            entityEquipment.setItemInHandDropChance(0.0F);
        }

        return true;
    }
}
