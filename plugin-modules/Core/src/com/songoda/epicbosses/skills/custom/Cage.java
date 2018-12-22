package com.songoda.epicbosses.skills.custom;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.api.BossAPI;
import com.songoda.epicbosses.holder.ActiveBossHolder;
import com.songoda.epicbosses.managers.BossPanelManager;
import com.songoda.epicbosses.managers.BossSkillManager;
import com.songoda.epicbosses.panel.skills.custom.custom.MaterialTypeEditorPanel;
import com.songoda.epicbosses.skills.CustomSkillHandler;
import com.songoda.epicbosses.skills.interfaces.ICustomSkillAction;
import com.songoda.epicbosses.skills.Skill;
import com.songoda.epicbosses.skills.custom.cage.CageLocationData;
import com.songoda.epicbosses.skills.custom.cage.CagePlayerData;
import com.songoda.epicbosses.skills.elements.CustomCageSkillElement;
import com.songoda.epicbosses.skills.types.CustomSkillElement;
import com.songoda.epicbosses.utils.Debug;
import com.songoda.epicbosses.utils.ServerUtils;
import com.songoda.epicbosses.utils.itemstack.converters.MaterialConverter;
import com.songoda.epicbosses.utils.panel.base.ClickAction;
import com.songoda.epicbosses.utils.panel.base.IVariablePanelHandler;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockState;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.*;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 06-Nov-18
 */
public class Cage extends CustomSkillHandler {

    private static final MaterialConverter MATERIAL_CONVERTER = new MaterialConverter();

    @Getter private static final Map<Location, CageLocationData> cageLocationDataMap = new HashMap<>();
    @Getter private static final List<UUID> playersInCage = new ArrayList<>();

    private final MaterialTypeEditorPanel flatTypeEditor, wallTypeEditor, insideTypeEditor;
    private BossPanelManager bossPanelManager;
    private CustomBosses plugin;

    public Cage(CustomBosses plugin) {
        this.plugin = plugin;
        this.bossPanelManager = plugin.getBossPanelManager();

        this.flatTypeEditor = getFlatTypeEditor();
        this.wallTypeEditor = getWallTypeEditor();
        this.insideTypeEditor = getInsideTypeEditor();
    }

    @Override
    public boolean doesUseMultiplier() {
        return false;
    }

    @Override
    public Map<String, Object> getOtherSkillData() {
        Map<String, Object> map = new HashMap<>();

        map.put("flatType", "IRON_BLOCK");
        map.put("wallType", "IRON_BARS");
        map.put("insideType", "AIR");

        return map;
    }

    @Override
    public List<ICustomSkillAction> getOtherSkillDataActions(Skill skill, CustomSkillElement customSkillElement) {
        List<ICustomSkillAction> clickActions = new ArrayList<>();
        ItemStack clickStack = new ItemStack(Material.STONE_PRESSURE_PLATE);
        ClickAction flatAction = (event -> this.flatTypeEditor.openFor((Player) event.getWhoClicked(), skill, customSkillElement));
        ClickAction wallAction = (event -> this.wallTypeEditor.openFor((Player) event.getWhoClicked(), skill, customSkillElement));
        ClickAction insideAction = (event -> this.insideTypeEditor.openFor((Player) event.getWhoClicked(), skill, customSkillElement));

        clickActions.add(BossSkillManager.createCustomSkillAction("Flat Type Editor", clickStack.clone(), flatAction));
        clickActions.add(BossSkillManager.createCustomSkillAction("Wall Type Editor", clickStack.clone(), wallAction));
        clickActions.add(BossSkillManager.createCustomSkillAction("Inside Type Editor", clickStack.clone(), insideAction));

        return clickActions;
    }

    @Override
    public void castSkill(Skill skill, CustomSkillElement customSkillElement, ActiveBossHolder activeBossHolder, List<LivingEntity> nearbyEntities) {
        nearbyEntities.forEach(livingEntity -> {
            UUID uuid = livingEntity.getUniqueId();

            if(getPlayersInCage().contains(uuid)) return;

            getPlayersInCage().add(uuid);

            Location teleportLocation = getTeleportLocation(livingEntity);
            CagePlayerData cagePlayerData = new CagePlayerData(uuid);

            cagePlayerData.setBlockStateMaps(teleportLocation);
            livingEntity.teleport(teleportLocation);

            ServerUtils.get().runLater(1L, () -> setCageBlocks(cagePlayerData, customSkillElement.getCustom().getCustomCageSkillData(), skill));
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

    private void setCageBlocks(CagePlayerData cagePlayerData, CustomCageSkillElement cage, Skill skill) {
        Map<String, Queue<BlockState>> queueMap = cagePlayerData.getMapOfCages();

        setBlocks(queueMap.get("W"), cage.getWallType(), skill);
        setBlocks(queueMap.get("F"), cage.getFlatType(), skill);
        setBlocks(queueMap.get("I"), cage.getInsideType(), skill);
    }

    private void setBlocks(Queue<BlockState> queue, String materialType, Skill skill) {
        Material material = MATERIAL_CONVERTER.from(materialType);

        if(material == null) {
            Debug.SKILL_CAGE_INVALID_MATERIAL.debug(materialType, skill.getDisplayName());
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

    private MaterialTypeEditorPanel getFlatTypeEditor() {
        return new MaterialTypeEditorPanel(this.bossPanelManager, this.bossPanelManager.getListMenu("Skills.Material"), this.plugin) {
            @Override
            public void saveSetting(Skill skill, CustomSkillElement customSkillElement, String newValue) {
                CustomCageSkillElement customCageSkillElement = customSkillElement.getCustom().getCustomCageSkillData();

                customCageSkillElement.setFlatType(newValue);
                customSkillElement.getCustom().setOtherSkillData(BossAPI.convertObjectToJsonObject(customCageSkillElement));
                skill.setCustomData(BossAPI.convertObjectToJsonObject(customSkillElement));
                Cage.this.plugin.getSkillsFileManager().save();
            }

            @Override
            public String getCurrentSetting(CustomSkillElement customSkillElement) {
                return customSkillElement.getCustom().getCustomCageSkillData().getFlatType();
            }

            @Override
            public IVariablePanelHandler<Skill> getParentHolder(Skill skill) {
                return this.bossPanelManager.getCustomSkillEditorPanel();
            }
        };
    }

    private MaterialTypeEditorPanel getWallTypeEditor() {
        return new MaterialTypeEditorPanel(this.bossPanelManager, this.bossPanelManager.getListMenu("Skills.Material"), this.plugin) {
            @Override
            public void saveSetting(Skill skill, CustomSkillElement customSkillElement, String newValue) {
                CustomCageSkillElement customCageSkillElement = customSkillElement.getCustom().getCustomCageSkillData();

                customCageSkillElement.setWallType(newValue);
                customSkillElement.getCustom().setOtherSkillData(BossAPI.convertObjectToJsonObject(customCageSkillElement));
                skill.setCustomData(BossAPI.convertObjectToJsonObject(customSkillElement));
                Cage.this.plugin.getSkillsFileManager().save();
            }

            @Override
            public String getCurrentSetting(CustomSkillElement customSkillElement) {
                return customSkillElement.getCustom().getCustomCageSkillData().getWallType();
            }

            @Override
            public IVariablePanelHandler<Skill> getParentHolder(Skill skill) {
                return this.bossPanelManager.getCustomSkillEditorPanel();
            }
        };
    }

    private MaterialTypeEditorPanel getInsideTypeEditor() {
        return new MaterialTypeEditorPanel(this.bossPanelManager, this.bossPanelManager.getListMenu("Skills.Material"), this.plugin) {
            @Override
            public void saveSetting(Skill skill, CustomSkillElement customSkillElement, String newValue) {
                CustomCageSkillElement customCageSkillElement = customSkillElement.getCustom().getCustomCageSkillData();

                customCageSkillElement.setInsideType(newValue);
                customSkillElement.getCustom().setOtherSkillData(BossAPI.convertObjectToJsonObject(customCageSkillElement));
                skill.setCustomData(BossAPI.convertObjectToJsonObject(customSkillElement));
                Cage.this.plugin.getSkillsFileManager().save();
            }

            @Override
            public String getCurrentSetting(CustomSkillElement customSkillElement) {
                return customSkillElement.getCustom().getCustomCageSkillData().getInsideType();
            }

            @Override
            public IVariablePanelHandler<Skill> getParentHolder(Skill skill) {
                return this.bossPanelManager.getCustomSkillEditorPanel();
            }
        };
    }

}
