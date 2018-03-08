package net.aminecraftdev.custombosses.handlers;

import net.aminecraftdev.custombosses.entities.base.CustomEntity;
import org.bukkit.entity.LivingEntity;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 08-Mar-18
 */
public class EntityHandler {

    private CustomEntity customEntity;
    private LivingEntity livingEntity;

    public EntityHandler(LivingEntity livingEntity, CustomEntity customEntity) {
        this.customEntity = customEntity;
        this.livingEntity = livingEntity;
    }

    public CustomEntity getCustomEntity() {
        return this.customEntity;
    }

    public LivingEntity getLivingEntity() {
        return this.livingEntity;
    }
}
