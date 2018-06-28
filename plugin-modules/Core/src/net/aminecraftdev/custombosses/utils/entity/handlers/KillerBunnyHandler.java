package net.aminecraftdev.custombosses.utils.entity.handlers;

import net.aminecraftdev.custombosses.utils.Versions;
import net.aminecraftdev.custombosses.utils.entity.ICustomEntityHandler;
import net.aminecraftdev.custombosses.utils.version.VersionHandler;
import org.bukkit.Location;
import org.bukkit.entity.*;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 01-Jun-18
 */
public class KillerBunnyHandler implements ICustomEntityHandler {

    private VersionHandler versionHandler = new VersionHandler();

    @Override
    public LivingEntity getBaseEntity(String entityType, Location spawnLocation) {
        if(this.versionHandler.getVersion().isLessThanOrEqualTo(Versions.v1_7_R4)) {
            throw new NullPointerException("This feature is only implemented in version 1.8 and above of Minecraft.");
        }

        Rabbit rabbit = (Rabbit) spawnLocation.getWorld().spawnEntity(spawnLocation, EntityType.RABBIT);
        rabbit.setRabbitType(Rabbit.Type.THE_KILLER_BUNNY);

        return rabbit;
    }
}
