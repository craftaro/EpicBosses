package net.aminecraftdev.custombosses.mechanics;

import net.aminecraftdev.custombosses.entity.BossEntity;
import net.aminecraftdev.custombosses.entity.elements.EquipmentElement;
import net.aminecraftdev.custombosses.holder.ActiveBossHolder;
import net.aminecraftdev.custombosses.managers.files.BossItemFileManager;
import net.aminecraftdev.custombosses.utils.IMechanic;
import net.aminecraftdev.custombosses.utils.itemstack.holder.ItemStackHolder;
import net.aminecraftdev.custombosses.utils.mechanics.IOptionalMechanic;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 03-Jun-18
 */
public class EquipmentMechanic implements IOptionalMechanic {

    private BossItemFileManager itemStackManager;

    public EquipmentMechanic(BossItemFileManager itemStackManager) {
        this.itemStackManager = itemStackManager;
    }

    @Override
    public boolean applyMechanic(BossEntity bossEntity, ActiveBossHolder activeBossHolder) {
        if(activeBossHolder.getLivingEntityMap().getOrDefault(0, null) == null) return false;

        LivingEntity livingEntity = activeBossHolder.getLivingEntityMap().getOrDefault(0, null);
        EquipmentElement equipmentElement = bossEntity.getEquipment();
        EntityEquipment entityEquipment = livingEntity.getEquipment();
        String helmet = equipmentElement.getHelmet();
        String chestplate = equipmentElement.getChestplate();
        String leggings = equipmentElement.getLeggings();
        String boots = equipmentElement.getBoots();

        if(helmet != null) {
            ItemStackHolder itemStackHolder = this.itemStackManager.getItemStackHolder(helmet);

            if(itemStackHolder != null) {
                ItemStack itemStack = this.itemStackManager.getItemStackConverter().from(itemStackHolder);

                entityEquipment.setHelmet(itemStack);
            }
        }

        if(chestplate != null) {
            ItemStackHolder itemStackHolder = this.itemStackManager.getItemStackHolder(chestplate);

            if(itemStackHolder != null) {
                ItemStack itemStack = this.itemStackManager.getItemStackConverter().from(itemStackHolder);

                entityEquipment.setChestplate(itemStack);
            }
        }

        if(leggings != null) {
            ItemStackHolder itemStackHolder = this.itemStackManager.getItemStackHolder(leggings);

            if(itemStackHolder != null) {
                ItemStack itemStack = this.itemStackManager.getItemStackConverter().from(itemStackHolder);

                entityEquipment.setLeggings(itemStack);
            }
        }

        if(boots != null) {
            ItemStackHolder itemStackHolder = this.itemStackManager.getItemStackHolder(boots);

            if(itemStackHolder != null) {
                ItemStack itemStack = this.itemStackManager.getItemStackConverter().from(itemStackHolder);

                entityEquipment.setBoots(itemStack);
            }
        }

        return true;
    }
}
