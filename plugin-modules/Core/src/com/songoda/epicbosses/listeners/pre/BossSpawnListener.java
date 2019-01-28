package com.songoda.epicbosses.listeners.pre;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.container.BossEntityContainer;
import com.songoda.epicbosses.entity.BossEntity;
import com.songoda.epicbosses.events.BossSpawnEvent;
import com.songoda.epicbosses.events.PreBossSpawnEvent;
import com.songoda.epicbosses.events.PreBossSpawnItemEvent;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.managers.BossEntityManager;
import com.songoda.epicbosses.managers.BossLocationManager;
import com.songoda.epicbosses.managers.BossTargetManager;
import com.songoda.epicbosses.managers.BossTauntManager;
import com.songoda.epicbosses.utils.*;
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
    private BossTauntManager bossTauntManager;
    private VersionHandler versionHandler;

    public BossSpawnListener(CustomBosses customBosses) {
        this.versionHandler = customBosses.getVersionHandler();
        this.bossTauntManager = customBosses.getBossTauntManager();
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

        if(bossEntity.isEditing()) {
            Message.Boss_Edit_CannotSpawn.msg(player);
            event.setCancelled(true);
            return;
        }

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

        ActiveBossHolder activeBossHolder = BossAPI.spawnNewBoss(bossEntity, location, player, itemStack, false);

        if(activeBossHolder == null) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onPreBossSpawnItem(PreBossSpawnItemEvent event) {
        handleEvent(event);
    }

    @EventHandler
    public void onPreBossSpawnEvent(PreBossSpawnEvent event) {
        handleEvent(event);
    }

    private void handleEvent(PreBossSpawnEvent event) {
        ActiveBossHolder activeBossHolder = event.getActiveBossHolder();
        BossEntity bossEntity = activeBossHolder.getBossEntity();
        Location location = activeBossHolder.getLocation();

        if(event instanceof PreBossSpawnItemEvent) {
            PreBossSpawnItemEvent preBossSpawnItemEvent = (PreBossSpawnItemEvent) event;
            ItemStack itemStack = preBossSpawnItemEvent.getItemStackUsed().clone();
            Player player = preBossSpawnItemEvent.getPlayer();

            itemStack.setAmount(1);
            player.getInventory().removeItem(itemStack);
            player.updateInventory();
        }

        List<String> commands = this.bossEntityManager.getOnSpawnCommands(bossEntity);
        List<String> messages = this.bossEntityManager.getOnSpawnMessage(bossEntity);
        int messageRadius = this.bossEntityManager.getOnSpawnMessageRadius(bossEntity);
        ServerUtils serverUtils = ServerUtils.get();

        if(commands != null) {
            commands.forEach(serverUtils::sendConsoleCommand);
        }
        if(messages != null) {
            if(activeBossHolder.getName() != null) messages.replaceAll(s -> s.replace("{boss}", activeBossHolder.getName()));
            messages.replaceAll(s -> s.replace("{location}", StringUtils.get().translateLocation(location)));

            MessageUtils.get().sendMessage(location, NumberUtils.get().getSquared(messageRadius), messages);
        }

        activeBossHolder.getTargetHandler().runTargetCycle();
        this.bossTauntManager.handleTauntSystem(activeBossHolder);

        BossSpawnEvent bossSpawnEvent = new BossSpawnEvent(activeBossHolder);

        ServerUtils.get().callEvent(bossSpawnEvent);
    }

}
