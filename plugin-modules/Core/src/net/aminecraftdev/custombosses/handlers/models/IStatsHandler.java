package net.aminecraftdev.custombosses.handlers.models;

import org.bukkit.entity.EntityType;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 08-Mar-18
 */
public interface IStatsHandler {

    void setEntityType(EntityType entityType);

    EntityType getEntityType();

    void setMaxHealth(double health);

    double getMaxHealth();

    void setDisplayName(String displayName);

    String getDisplayName();

    void setDescription(List<String> description);

    List<String> getDescription();

}
