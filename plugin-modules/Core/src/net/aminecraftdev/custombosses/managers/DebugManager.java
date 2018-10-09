package net.aminecraftdev.custombosses.managers;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 09-Oct-18
 */
public class DebugManager {

    private static Set<UUID> toggledPlayers = new HashSet<>();

    public void togglePlayerOn(UUID uuid) {
        toggledPlayers.add(uuid);
    }

    public void togglePlayerOff(UUID uuid) {
        toggledPlayers.remove(uuid);
    }

    public boolean isToggled(UUID uuid) {
        return toggledPlayers.contains(uuid);
    }

    public Set<UUID> getToggledPlayers() {
        return new HashSet<>(toggledPlayers);
    }
}
