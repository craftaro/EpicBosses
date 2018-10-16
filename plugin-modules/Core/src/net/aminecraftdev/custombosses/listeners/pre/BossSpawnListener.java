package net.aminecraftdev.custombosses.listeners.pre;

import net.aminecraftdev.custombosses.CustomBosses;
import net.aminecraftdev.custombosses.api.BossAPI;
import net.aminecraftdev.custombosses.entity.BossEntity;
import net.aminecraftdev.custombosses.events.BossSpawnEvent;
import net.aminecraftdev.custombosses.events.PreBossSpawnEvent;
import net.aminecraftdev.custombosses.holder.ActiveBossHolder;
import net.aminecraftdev.custombosses.managers.BossEntityManager;
import net.aminecraftdev.custombosses.managers.BossLocationManager;
import net.aminecraftdev.custombosses.utils.Message;
import net.aminecraftdev.custombosses.utils.ServerUtils;
import net.aminecraftdev.custombosses.utils.itemstack.ItemStackUtils;
import net.aminecraftdev.custombosses.utils.version.VersionHandler;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 15-Oct-18
 */
public class BossSpawnListener implements Listener {

    private BossLocationManager bossLocationManager;
    private BossEntityManager bossEntityManager;
    private VersionHandler versionHandler;

    public BossSpawnListener(CustomBosses customBosses) {
        this.versionHandler = customBosses.getVersionHandler();
        this.bossEntityManager = customBosses.getBossEntityManager();
        this.bossLocationManager = customBosses.getBossLocationManager();
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Block block = event.getClickedBlock();
        Location location = block.getLocation().clone();
        BlockFace blockFace = event.getBlockFace();
        Action action = event.getAction();

        if(!event.hasItem()) return;
        if(action != Action.RIGHT_CLICK_BLOCK) return;
        if(block.getType() == Material.AIR) return;

        Map<BossEntity, ItemStack> entitiesAndSpawnItems = this.bossEntityManager.getMapOfEntitiesAndSpawnItems();
        ItemStack itemStack = this.versionHandler.getItemInHand(player).clone();
        BossEntity bossEntity = null;

        for(Map.Entry<BossEntity, ItemStack> entry : entitiesAndSpawnItems.entrySet()) {
            if(ItemStackUtils.isItemStackSame(itemStack, entry.getValue())) {
                bossEntity = entry.getKey();
                break;
            }
        }

        if(bossEntity == null) return;

        if(bossEntity.isEditing()) {
            Message.Boss_Edit_CannotSpawn.msg(player);
            return;
        }

        if(blockFace == BlockFace.UP) {
            location.add(0,1,0);
        }

        if(!this.bossLocationManager.canSpawnBoss(player, location)) {
            Message.General_CannotSpawn.msg(player);
            event.setCancelled(true);
            return;
        }

        event.setCancelled(true);

        ActiveBossHolder activeBossHolder = BossAPI.spawnNewBoss(bossEntity, location);

        //TODO: Set TargetHandler to the boss

        PreBossSpawnEvent preBossSpawnEvent = new PreBossSpawnEvent(activeBossHolder, player, itemStack);

        ServerUtils.get().callEvent(preBossSpawnEvent);
    }

    @EventHandler
    public void onPreBossSpawnEvent(PreBossSpawnEvent event) {
        ActiveBossHolder activeBossHolder = event.getActiveBossHolder();
        ItemStack itemStack = event.getItemStackUsed().clone();
        Player player = event.getPlayer();

        itemStack.setAmount(1);
        player.getInventory().removeItem(itemStack);
        player.updateInventory();

        //TODO: Create AutoTarget for TargetHandler
        //TODO: Handle onSpawn commands, and messages

        BossSpawnEvent bossSpawnEvent = new BossSpawnEvent(activeBossHolder);
    }

}
