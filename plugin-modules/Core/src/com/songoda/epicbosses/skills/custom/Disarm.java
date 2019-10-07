package com.songoda.epicbosses.skills.custom;

import com.songoda.epicbosses.EpicBosses;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.skills.CustomSkillHandler;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.interfaces.ICustomSettingAction;
import com.songoda.epicbosses.skills.interfaces.IOtherSkillDataElement;
import com.songoda.epicbosses.skills.types.CustomSkillElement;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.RandomUtils;
import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 11-Nov-18
 */
public class Disarm extends CustomSkillHandler {

    @Override
    public boolean doesUseMultiplier() {
        return false;
    }

    @Override
    public IOtherSkillDataElement getOtherSkillData() {
        return null;
    }

    @Override
    public List<ICustomSettingAction> getOtherSkillDataActions(Skill skill, CustomSkillElement customSkillElement) {
        return null;
    }

    @Override
    public void castSkill(Skill skill, CustomSkillElement customSkillElement, ActiveBossHolder activeBossHolder, List<LivingEntity> nearbyEntities) {
        nearbyEntities.forEach(livingEntity -> {
            EntityEquipment entityEquipment = livingEntity.getEquipment();
            int itemSlot = RandomUtils.get().getRandomNumber(5);
            ItemStack replacementItemStack = new ItemStack(Material.AIR);
            ItemStack itemStack;

            switch (itemSlot) {
                case 0:
                    if (livingEntity instanceof HumanEntity) {
                        HumanEntity humanEntity = (HumanEntity) livingEntity;

                        itemStack = EpicBosses.getInstance().getVersionHandler().getItemInHand(humanEntity);
                        EpicBosses.getInstance().getVersionHandler().setItemInHand(humanEntity, replacementItemStack);
                        break;
                    }
                case 1:
                    itemStack = entityEquipment.getHelmet();
                    entityEquipment.setHelmet(replacementItemStack);
                    break;
                case 2:
                    itemStack = entityEquipment.getChestplate();
                    entityEquipment.setChestplate(replacementItemStack);
                    break;
                case 3:
                    itemStack = entityEquipment.getLeggings();
                    entityEquipment.setLeggings(replacementItemStack);
                    break;
                default:
                case 4:
                    itemStack = entityEquipment.getBoots();
                    entityEquipment.setBoots(replacementItemStack);
                    break;
            }

            if (itemStack == null || itemStack.getType() == Material.AIR) return;

            livingEntity.getWorld().dropItemNaturally(livingEntity.getLocation(), itemStack);
            Message.General_Disarmed.msg(livingEntity);
        });
    }
}
