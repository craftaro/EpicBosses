package com.songoda.epicbosses.skills.custom;

import com.google.gson.annotations.Expose;
import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.skills.ISkillHandler;
import com.songoda.epicbosses.skills.custom.cage.CageLocationData;
import com.songoda.epicbosses.skills.custom.cage.CagePlayerData;
import com.songoda.epicbosses.skills.elements.CustomCageSkillElement;
import com.songoda.epicbosses.skills.types.CustomSkill;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.itemstack.converters.MaterialConverter;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.LivingEntity;

import java.util.*;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 06-Nov-18
 */
public class Cage extends CustomSkill implements ISkillHandler {

    private static final MaterialConverter MATERIAL_CONVERTER = new MaterialConverter();

    @Getter private static final Map<Location, CageLocationData> cageLocationDataMap = new HashMap<>();
    @Getter private static final List<UUID> playersInCage = new ArrayList<>();

    @Expose @Getter @Setter private CustomCageSkillElement cage;

    public Cage(String mode, String type, Double radius, String displayName, String customMessage) {
        super(mode, type, radius, displayName, customMessage);
    }

    @Override
    public void castSkill(ActiveBossHolder activeBossHolder, List<LivingEntity> nearbyEntities) {
        nearbyEntities.forEach(livingEntity -> {
            UUID uuid = livingEntity.getUniqueId();

            if(getPlayersInCage().contains(uuid)) return;

            getPlayersInCage().add(uuid);

            Location teleportLocation = getTeleportLocation(livingEntity);
            CagePlayerData cagePlayerData = new CagePlayerData(uuid);

            cagePlayerData.setBlockStateMaps(teleportLocation);
            livingEntity.teleport(teleportLocation);

            ServerUtils.get().runLater(1L, () -> setCageBlocks(cagePlayerData));
            ServerUtils.get().runLater(100L, () -> {
                restoreCageBlocks(cagePlayerData);
                getPlayersInCage().remove(uuid);
            });
        });
    }

    private void restoreCageBlocks(CagePlayerData cagePlayerData) {
        Map<String, Queue<BlockState>> queueMap = cagePlayerData.getMapOfRestoreCages();

        restoreBlocks(queueMap.get("W"));
        restoreBlocks(queueMap.get("F"));
        restoreBlocks(queueMap.get("I"));
    }

    private void restoreBlocks(Queue<BlockState> queue) {
        queue.forEach(blockState -> {
            if(blockState == null) return;

            Location location = blockState.getLocation();
            CageLocationData cageLocationData = getCageLocationDataMap().getOrDefault(location, new CageLocationData(location, 1));
            int amountOfCages = cageLocationData.getAmountOfCages();

            if(amountOfCages == 1) {
                BlockState oldState = cageLocationData.getOldBlockState();

                if(oldState != null) {
                    location.getBlock().setType(oldState.getType());
                    location.getBlock().setBlockData(oldState.getBlockData());
                }

                getCageLocationDataMap().remove(location);
            } else {
                cageLocationData.setAmountOfCages(amountOfCages-1);
                getCageLocationDataMap().put(location, cageLocationData);
            }
        });
    }

    private void setCageBlocks(CagePlayerData cagePlayerData) {
        Map<String, Queue<BlockState>> queueMap = cagePlayerData.getMapOfCages();

        setBlocks(queueMap.get("W"), getCage().getWallType());
        setBlocks(queueMap.get("F"), getCage().getFlatType());
        setBlocks(queueMap.get("I"), getCage().getInsideType());
    }

    private void setBlocks(Queue<BlockState> queue, String materialType) {
        Material material = MATERIAL_CONVERTER.from(materialType);

        if(material == null) {
            Debug.SKILL_CAGE_INVALID_MATERIAL.debug(materialType, getDisplayName());
            return;
        }

        queue.forEach(blockState -> {
            if(blockState == null) return;

            Location location = blockState.getLocation();
            CageLocationData cageLocationData = getCageLocationDataMap().getOrDefault(location, new CageLocationData(location, 0));
            int currentAmount = cageLocationData.getAmountOfCages();

            if(currentAmount == 0 || cageLocationData.getOldBlockState() == null) cageLocationData.setOldBlockState(blockState);

            blockState.getBlock().setType(material);
            cageLocationData.setAmountOfCages(currentAmount+1);
            getCageLocationDataMap().put(location, cageLocationData);
        });
    }

    private Location getTeleportLocation(LivingEntity livingEntity) {
        Location currentLocation = livingEntity.getLocation();

        return currentLocation.clone().add(0.5, 0, 0.5);
    }
}
