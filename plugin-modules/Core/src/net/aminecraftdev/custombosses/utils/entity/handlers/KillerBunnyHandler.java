package net.aminecraftdev.custombosses.utils.entity.handlers;

import net.aminecraftdev.custombosses.utils.ReflectionUtil;
import net.aminecraftdev.custombosses.utils.entity.ICustomEntityHandler;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Rabbit;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 01-Jun-18
 */
public class KillerBunnyHandler implements ICustomEntityHandler {

    private String version = ReflectionUtil.get().getVersion();

    @Override
    public LivingEntity getBaseEntity(String entityType, Location spawnLocation) {
        if(this.version.startsWith("v1_7_") || this.version.startsWith("v1_6_")) {
            throw new NullPointerException("This feature is only implemented in version 1.8 and above of Minecraft.");
        }

        Rabbit rabbit = (Rabbit) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.RABBIT);
        rabbit.setRabbitType(Rabbit.Type.THE_KILLER_BUNNY);

        return rabbit;
    }
}
