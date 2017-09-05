package net.aminecraftdev.custombosses.api;

import net.aminecraftdev.custombosses.handlers.BossEntity;
import net.aminecraftdev.custombosses.managers.BossManager;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 06-Sep-17
 */
public class BossAPI {

    public boolean createBoss(String entityType, double health, String identifier) {
        try {
            EntityType.valueOf(entityType);
        } catch (IllegalArgumentException ex) {
            return false;
        }

        if(BossManager.isBossEntitySet(identifier)) return false;

        EntityType eType = EntityType.valueOf(entityType);
        BossEntity bossEntity = new BossEntity(eType, health, identifier);

        BossManager.addBossEntity(bossEntity);
        return true;
    }

    public LivingEntity spawnBoss(String identifier, Location location) {
        if(!BossManager.isBossEntitySet(identifier)) return null;

        BossEntity bossEntity = BossManager.getBossEntity(identifier);

        return bossEntity.createNewBossEntity(location);
    }

    public LivingEntity spawnMinion(String identifier, Location location) {
        if(!BossManager.isBossEntitySet(identifier)) return null;

        BossEntity bossEntity = BossManager.getBossEntity(identifier);

        return bossEntity.createNewMinionEntity(location);
    }

    public BossEntity getBoss(String identifier) {
        return BossManager.getBossEntity(identifier);
    }

}
