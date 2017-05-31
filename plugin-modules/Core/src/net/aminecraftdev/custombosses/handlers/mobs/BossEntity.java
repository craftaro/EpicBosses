package net.aminecraftdev.custombosses.handlers.mobs;

import net.aminecraftdev.custombosses.handlers.mobs.interfaces.IDamageHandler;
import net.aminecraftdev.custombosses.handlers.mobs.interfaces.IKillHandler;
import net.aminecraftdev.custombosses.handlers.mobs.interfaces.IMobHandler;
import net.aminecraftdev.custombosses.handlers.mobs.interfaces.ISpawnHandler;
import net.aminecraftdev.custombosses.innerapi.message.MessageUtils;
import net.aminecraftdev.custombosses.managers.BossManager;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;

import java.util.UUID;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 31-May-17
 */
public class BossEntity implements IMobHandler, ISpawnHandler, IDamageHandler, IKillHandler {

    private boolean hasSkills, hasTaunts, hasCommands, hasMessages, isAutoBoss;
    private ConfigurationSection configurationSection;
    private LivingEntity livingEntity;
    private BossManager bossManager;
    private boolean isBossSpawned;
    private double maxHealth;
    private UUID uuid;

    //private Set<Skill> skills = new HashSet<>();
    //private List<String> taunts = new ArrayList<>();

    public BossEntity(ConfigurationSection configurationSection, BossManager bossManager, boolean isAutoBoss) {
        this.hasSkills = this.configurationSection.contains("Skills");
        this.hasTaunts = this.configurationSection.contains("Taunts");
        this.hasCommands = this.configurationSection.contains("Commands");
        this.hasMessages = this.configurationSection.contains("Messages");
        this.configurationSection = configurationSection;
        this.bossManager = bossManager;
        this.isBossSpawned = false;
        this.isAutoBoss = isAutoBoss;
    }

    @Override
    public ConfigurationSection getConfigurationSection() {
        return configurationSection;
    }

    @Override
    public double getMaxHealth() {
        return maxHealth;
    }

    @Override
    public double getCurrentHealth() {
        return livingEntity.getHealth();
    }

    @Override
    public UUID getUniqueId() {
        return uuid;
    }

    @Override
    public boolean isAutoBoss() {
        return isAutoBoss;
    }

    @Override
    public void spawn(Location location) {
        spawnBoss(location);
        spawnMessage(location);
    }

    @Override
    public void spawnBoss(Location location) {
        if(this.isBossSpawned) {
            /* ADD DEBUG MESSAGE HERE */
            return;
        }

        ConfigurationSection bossConfigurationSection = configurationSection.getConfigurationSection("Boss");
        String type = bossConfigurationSection.getString("type");
        String targetType = bossConfigurationSection.getString("targetType");
        String name = bossConfigurationSection.contains("name")? bossConfigurationSection.getString("name") : "";
        double health = bossConfigurationSection.getDouble("health");

        this.livingEntity = this.bossManager.getEntityHandler().getBaseEntity(type, location);

        this.livingEntity.setCustomName(MessageUtils.translateString(name));
        this.livingEntity.setCustomNameVisible(true);
        this.livingEntity.setMaxHealth(health);
        this.livingEntity.setHealth(health);
        this.livingEntity.setRemoveWhenFarAway(false);
        this.livingEntity.setCanPickupItems(false);

        if(bossConfigurationSection.contains("Armor")) this.bossManager.getEquipmentHandler().applyEquipment(this.livingEntity, bossConfigurationSection.getConfigurationSection("Armor"));
        if(bossConfigurationSection.contains("Weapon")) this.bossManager.getEquipmentHandler().applyEquipment(this.livingEntity, bossConfigurationSection.getConfigurationSection("Weapon"));
        if(bossConfigurationSection.contains("Head")) this.bossManager.getEquipmentHandler().applySkull(this.livingEntity, bossConfigurationSection.getConfigurationSection("Head"));
        if(bossConfigurationSection.contains("Potions")) this.bossManager.applyPotionEffects(this.livingEntity, bossConfigurationSection.getConfigurationSection("Potions"));

        this.maxHealth = health;
        this.livingEntity.getEquipment().setHelmetDropChance(0.0F);
        this.livingEntity.getEquipment().setChestplateDropChance(0.0F);
        this.livingEntity.getEquipment().setLeggingsDropChance(0.0F);
        this.livingEntity.getEquipment().setBootsDropChance(0.0F);

        /* HANDLE TARGET SYSTEM HERE */

        this.uuid = this.livingEntity.getUniqueId();
    }

    @Override
    public void spawnMessage(Location location) {
        if(!hasMessages) return;
        if(!configurationSection.contains("Messages.onSpawn")) return;

    }

    @Override
    public void onDamage(Player damager, int damage) {

    }

    @Override
    public void kill(Location location) {

    }

    @Override
    public void killBoss(Location location) {

    }

    @Override
    public void killMessage(Location location) {

    }
}
