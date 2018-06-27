package net.aminecraftdev.custombosses.utils.version;

import net.aminecraftdev.custombosses.utils.Versions;
import org.bukkit.Bukkit;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 27-Jun-18
 */
public class VersionHandler {

    private Versions version;

    public VersionHandler() {
        String v = Bukkit.getServer().getClass().getPackage().getName();

        v = v.substring(v.lastIndexOf(".") + 1);

        this.version = Versions.getVersion(v);
    }

    public boolean canUseOffHand() {
        return this.version.isHigherThan(Versions.v1_9_R1);
    }

}
