package net.aminecraftdev.custombosses.listeners.pre;

import net.aminecraftdev.custombosses.CustomBosses;
import net.aminecraftdev.custombosses.utils.version.VersionHandler;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 15-Oct-18
 */
public class BossSpawnListener implements Listener {

    private VersionHandler versionHandler;

    public BossSpawnListener(CustomBosses customBosses) {
        this.versionHandler = customBosses.getVersionHandler();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        BlockFace blockFace = event.getBlockFace();
        Action action = event.getAction();

        if(!event.hasItem()) return;
        if(action != Action.RIGHT_CLICK_BLOCK) return;
        if(block.getType() == Material.AIR) return;

        ItemStack itemStack = this.versionHandler.getItemInHand(player);


    }

}
