package net.aminecraftdev.custombosses.innerapi.serialization;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;

/**
 * @author Debugged
 * @version 1.0
 * @since 18-5-2017
 */
public class Serialize {

    public static String serialize(Location location) {
        return location.getWorld().getName() + ";" +
                location.getX() + ";" +
                location.getY() + ";" +
                location.getZ() + ";" +
                location.getYaw() + ";" +
                location.getPitch() + ";";
    }

    public static Location deserializeLocation(String locationString) {
        String[] split = locationString.split(";");

        try {
            World world = Bukkit.getWorld(split[0]);
            double x = Double.parseDouble(split[1]);
            double y = Double.parseDouble(split[2]);
            double z = Double.parseDouble(split[3]);
            float yaw = Float.parseFloat(split[4]);
            float pitch = Float.parseFloat(split[5]);

            return new Location(world, x, y, z, yaw, pitch);
        } catch (Exception ex) {
            return null;
        }
    }

}
