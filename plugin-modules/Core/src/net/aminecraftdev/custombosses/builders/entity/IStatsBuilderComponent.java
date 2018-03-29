package net.aminecraftdev.custombosses.builders.entity;

import org.bukkit.entity.EntityType;

import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 25-Mar-18
 */
public interface IStatsBuilderComponent {

    void setEntityType(EntityType entityType);

    void setMaxHealth(double health);

    void setDisplayName(String displayName);

    void setDescription(List<String> description);

}
