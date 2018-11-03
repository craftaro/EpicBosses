package com.songoda.epicbosses.listeners.pre;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.container.BossEntityContainer;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.events.BossSpawnEvent;
import com.songoda.epicbosses.events.PreBossSpawnEvent;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.managers.BossEntityManager;
import com.songoda.epicbosses.managers.BossLocationManager;
import com.songoda.epicbosses.managers.BossTargetManager;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.Message;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.StringUtils;
import com.songoda.epicbosses.utils.itemstack.ItemStackUtils;
import com.songoda.epicbosses.utils.version.VersionHandler;
import org.bukkit.Bukkit;
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

import java.util.List;
import java.util.Map;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 15-Oct-18
 */
public class BossSpawnListener implements Listener {

    private BossLocationManager bossLocationManager;
    private BossTargetManager bossTargetManager;
    private BossEntityManager bossEntityManager;
    private VersionHandler versionHandler;

    public BossSpawnListener(CustomBosses customBosses) {
        this.versionHandler = customBosses.getVersionHandler();
        this.bossEntityManager = customBosses.getBossEntityManager();
        this.bossTargetManager = customBosses.getBossTargetManager();
        this.bossLocationManager = customBosses.getBossLocationManager();
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

//        if(bossEntity.isEditing()) {
//            Message.Boss_Edit_CannotSpawn.msg(player);
//            event.setCancelled(true);
//            return;
//        }

        Location location = block.getLocation().clone();

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

        if(activeBossHolder == null) {
            Debug.FAILED_TO_CREATE_ACTIVE_BOSS_HOLDER.debug();
            event.setCancelled(true);
            return;
        }

        PreBossSpawnEvent preBossSpawnEvent = new PreBossSpawnEvent(activeBossHolder, player, itemStack);

        this.bossTargetManager.initializeTargetHandler(activeBossHolder);
        ServerUtils.get().callEvent(preBossSpawnEvent);
    }

    @EventHandler
    public void onPreBossSpawnEvent(PreBossSpawnEvent event) {
        ActiveBossHolder activeBossHolder = event.getActiveBossHolder();
        BossEntity bossEntity = activeBossHolder.getBossEntity();
        Location location = activeBossHolder.getLocation();
        ItemStack itemStack = event.getItemStackUsed().clone();
        Player player = event.getPlayer();

        itemStack.setAmount(1);
        player.getInventory().removeItem(itemStack);
        player.updateInventory();

        List<String> commands = this.bossEntityManager.getOnSpawnCommands(bossEntity);
        List<String> messages = this.bossEntityManager.getOnSpawnMessage(bossEntity);
        int messagesRadius = this.bossEntityManager.getOnSpawnMessageRadius(bossEntity);

        if(commands != null) {
            commands.forEach(command -> Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command));
        }
        if(messages != null) {
            if(activeBossHolder.getName() != null) messages.replaceAll(s -> s.replace("{boss}", activeBossHolder.getName()));
            messages.replaceAll(s -> s.replace("{location}", StringUtils.get().translateLocation(location)));
            messages.replaceAll(s -> s.replace('&', '§'));

            if(messagesRadius == -1) {
                messages.forEach(Bukkit::broadcastMessage);
            } else {
                Bukkit.getOnlinePlayers().forEach(onlinePlayer -> {
                    if(onlinePlayer.getWorld().getName().equals(location.getWorld().getName())) {
                        if(onlinePlayer.getLocation().distanceSquared(location) <= messagesRadius) {
                            messages.forEach(onlinePlayer::sendMessage);
                        }
                    }
                });
            }
        }

        activeBossHolder.getTargetHandler().runTargetCycle();
        //TODO: Handle Taunts

        BossSpawnEvent bossSpawnEvent = new BossSpawnEvent(activeBossHolder);

        ServerUtils.get().callEvent(bossSpawnEvent);
    }

}
