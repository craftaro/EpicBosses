package net.aminecraftdev.custombosses.mechanics;

import net.aminecraftdev.custombosses.entity.BossEntity;
import net.aminecraftdev.custombosses.entity.elements.HandsElement;
import net.aminecraftdev.custombosses.holder.ActiveBossHolder;
import net.aminecraftdev.custombosses.managers.BossItemFileManager;
import net.aminecraftdev.custombosses.utils.IMechanic;
import net.aminecraftdev.custombosses.utils.version.VersionHandler;
import net.aminecraftdev.custombosses.utils.itemstack.holder.ItemStackHolder;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Jun-18
 */
public class WeaponMechanic implements IMechanic {

    private BossItemFileManager itemStackManager;
    private VersionHandler versionHandler;

    public WeaponMechanic(BossItemFileManager itemStackManager) {
        this.itemStackManager = itemStackManager;
        this.versionHandler = new VersionHandler();
    }

    @Override
    public boolean applyMechanic(BossEntity bossEntity, ActiveBossHolder activeBossHolder) {
        if(activeBossHolder.getLivingEntity() == null) return false;

        LivingEntity livingEntity = activeBossHolder.getLivingEntity();
        EntityEquipment entityEquipment = livingEntity.getEquipment();
        HandsElement handsElement = bossEntity.getHands();
        String mainHand = handsElement.getMainHand();
        String offHand = handsElement.getOffHand();

        if(mainHand != null) {
            ItemStackHolder itemStackHolder = this.itemStackManager.getItemStackHolder(mainHand);

            if(itemStackHolder != null) {
                ItemStack itemStack = this.itemStackManager.getItemStackConverter().from(itemStackHolder);

                if(this.versionHandler.canUseOffHand()) {
                    entityEquipment.setItemInMainHand(itemStack);
                } else {
                    entityEquipment.setItemInHand(itemStack);
                }
            }
        }

        if(offHand != null && this.versionHandler.canUseOffHand()) {
            ItemStackHolder itemStackHolder = this.itemStackManager.getItemStackHolder(offHand);

            if(itemStackHolder != null) {
                ItemStack itemStack = this.itemStackManager.getItemStackConverter().from(itemStackHolder);

                entityEquipment.setItemInOffHand(itemStack);
            }
        }

        return false;
    }
}
