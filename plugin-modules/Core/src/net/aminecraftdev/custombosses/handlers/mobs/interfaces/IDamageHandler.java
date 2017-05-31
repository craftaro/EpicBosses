package net.aminecraftdev.custombosses.handlers.mobs.interfaces;

import org.bukkit.entity.Player;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 31-May-17
 */
public interface IDamageHandler {

    void onDamage(Player damager, int damage);

}
