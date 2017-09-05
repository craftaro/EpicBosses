package net.aminecraftdev.custombosses.managers;

import net.aminecraftdev.custombosses.handlers.BossEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 06-Sep-17
 */
public class BossManager {

    private static final Map<String, BossEntity> MAP_OF_BOSSES = new HashMap<>();

    public static boolean isBossEntitySet(String identifier) {
        return MAP_OF_BOSSES.containsKey(identifier);
    }

    public static void addBossEntity(BossEntity bossEntity) {
        MAP_OF_BOSSES.put(bossEntity.getIdentifier(), bossEntity);
    }

    public static BossEntity getBossEntity(String identifier) {
        return MAP_OF_BOSSES.getOrDefault(identifier, null);
    }

}
