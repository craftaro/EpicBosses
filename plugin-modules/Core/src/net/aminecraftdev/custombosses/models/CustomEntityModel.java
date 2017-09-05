package net.aminecraftdev.custombosses.models;

import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.EntityEquipment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import java.util.*;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 06-Sep-17
 */
public class CustomEntityModel {

    private List<PotionEffect> defaultPotionEffects = new ArrayList<>();
    private Map<UUID, Location> spawnedLocations = new HashMap<>();
    private Map<Integer, ItemStack> equipment = new HashMap<>();
    private Set<LivingEntity> minionsSpawned = new HashSet<>();
    private Set<LivingEntity> bossesSpawned = new HashSet<>();
    private List<String> description = new ArrayList<>();

    private ItemStack itemStackInMainHand;
    private ItemStack itemStackInOffHand;
    private boolean canDropEquipment;
    private EntityType entityType;
    private String identifier;
    private double health;
    private String name;

    public CustomEntityModel(EntityType entityType, double health, String identifier) {
        this(entityType, null, null, null, null, null, identifier, health, null);
    }

    public CustomEntityModel(EntityType entityType, Map<Integer, ItemStack> equipment, List<String> description, List<PotionEffect> defaultPotionEffects, ItemStack itemStackInMainHand, ItemStack itemStackInOffHand, String identifier, double health, String name) {
        this.entityType = entityType;
        this.identifier = identifier;

        setItemStackInMainHand(itemStackInMainHand);
        setItemStackInOffHand(itemStackInOffHand);
        setMaxHealth(health);
        setName(name);

        if(equipment != null) equipment.forEach((integer, itemStack) -> setEquipment(integer, itemStack));
        if(description != null) setDescription(description);
        if(this.defaultPotionEffects != null) defaultPotionEffects.forEach(potionEffect -> addDefaultPotionEffects(potionEffect));
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<PotionEffect> getDefaultPotionEffects() {
        return new ArrayList<>(defaultPotionEffects);
    }

    public void addDefaultPotionEffects(PotionEffect potionEffect) {
        defaultPotionEffects.add(potionEffect);
    }

    public Set<LivingEntity> getBossesSpawned() {
        return new HashSet<>(bossesSpawned);
    }

    public Set<LivingEntity> getMinionsSpawned() {
        return new HashSet<>(minionsSpawned);
    }

    public Map<UUID, Location> getSpawnedLocations() {
        return new HashMap<>(spawnedLocations);
    }

    public Map<Integer, ItemStack> getEquipment() {
        return new HashMap<>(equipment);
    }

    public void setEquipment(int slot, ItemStack itemStack) {
        if(slot > 3) return;

        equipment.put(slot, itemStack);
    }

    public List<String> getDescription() {
        return new ArrayList<>(description);
    }

    public void setDescription(List<String> description) {
        this.description = description;
    }

    public void addDescription(String line) {
        description.add(line);
    }

    public ItemStack getItemStackInMainHand() {
        return itemStackInMainHand;
    }

    public void setItemStackInMainHand(ItemStack itemStack) {
        this.itemStackInMainHand = itemStack;
    }

    public ItemStack getItemStackInOffHand() {
        return itemStackInOffHand;
    }

    public void setItemStackInOffHand(ItemStack itemStack) {
        this.itemStackInOffHand = itemStack;
    }

    public double getMaxHealth() {
        return health;
    }

    public void setMaxHealth(double health) {
        this.health = health;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EntityType getEntityType() {
        return entityType;
    }

    public boolean canDropEquipment() {
        return canDropEquipment;
    }

    public void setCanDropEquipment(boolean bool) {
        this.canDropEquipment = bool;
    }

    public LivingEntity createNewBossEntity(Location location) {
        LivingEntity livingEntity = createNewEntity(location);

        bossesSpawned.add(livingEntity);
        spawnedLocations.put(livingEntity.getUniqueId(), location);

        return livingEntity;
    }

    public LivingEntity createNewMinionEntity(Location location) {
        LivingEntity livingEntity = createNewEntity(location);

        minionsSpawned.add(livingEntity);

        return livingEntity;
    }

    private LivingEntity createNewEntity(Location location) {
        LivingEntity livingEntity = (LivingEntity) location.getWorld().spawn(location, entityType.getEntityClass());
        EntityEquipment entityEquipment = livingEntity.getEquipment();

        livingEntity.setMaxHealth(getMaxHealth());
        livingEntity.setHealth(getMaxHealth());

        if(getName() != null) {
            livingEntity.setCustomName(getName());
            livingEntity.setCustomNameVisible(true);
        }

        if(getItemStackInMainHand() != null) {
            entityEquipment.setItemInMainHand(getItemStackInMainHand());

            if(!canDropEquipment()) entityEquipment.setItemInMainHandDropChance(0);
        }

        if(getItemStackInOffHand() != null) {
            entityEquipment.setItemInOffHand(getItemStackInOffHand());

            if(!canDropEquipment()) entityEquipment.setItemInOffHandDropChance(0);
        }

        if(!getEquipment().isEmpty()) {
            if(equipment.containsKey(0)) {
                entityEquipment.setHelmet(equipment.get(0));

                if(!canDropEquipment()) entityEquipment.setHelmetDropChance(0);
            }

            if(equipment.containsKey(1)) {
                entityEquipment.setChestplate(equipment.get(1));

                if(!canDropEquipment()) entityEquipment.setChestplateDropChance(0);
            }

            if(equipment.containsKey(2)) {
                entityEquipment.setLeggings(equipment.get(2));

                if(!canDropEquipment()) entityEquipment.setLeggingsDropChance(0);
            }

            if(equipment.containsKey(3)) {
                entityEquipment.setBoots(equipment.get(3));

                if(!canDropEquipment()) entityEquipment.setBootsDropChance(0);
            }
        }

        if(!getDefaultPotionEffects().isEmpty()) {
            livingEntity.addPotionEffects(defaultPotionEffects);
        }

        return livingEntity;
    }

}
