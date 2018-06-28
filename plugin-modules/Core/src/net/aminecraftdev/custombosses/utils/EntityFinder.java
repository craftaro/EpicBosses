package net.aminecraftdev.custombosses.utils;

import lombok.Getter;
import net.aminecraftdev.custombosses.utils.entity.ICustomEntityHandler;
import net.aminecraftdev.custombosses.utils.entity.handlers.*;
import org.bukkit.entity.EntityType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 28-Jun-18
 */
public enum EntityFinder {

    ELDER_GUARDIAN("ElderGuardian", new ElderGuardianHandler(), "elderguardian", "elder_guardian", "elder guardian"),
    WITHER_SKELETON("WitherSkeleton", new WitherSkeletonHandler(), "witherskeleton", "wither_skeleton", "wither skeleton"),
    STRAY("Stray", new StraySkeletonHandler(), "stray"),
    HUSK("Husk", new HuskZombieHandler(), "husk"),
    ZOMBIE_VILLAGER("ZombieVillager", new ZombieVillagerHandler(), "zombievillager", "zombie_villager", "zombie villager", "villagerzombie", "villager_zombie", "villager zombie"),
    SKELETON_HORSE("SkeletonHorse", new SkeletonHorseHandler(), "skeletonhorse", "skeleton_horse", "skeleton horse"),
    ZOMBIE_HORSE("ZombieHorse", new ZombieHorseHandler(), "zombiehorse", "zombie_horse", "zombie horse");

    @Getter private ICustomEntityHandler customEntityHandler;
    @Getter private List<String> names = new ArrayList<>();
    @Getter private EntityType entityType;
    @Getter private String fancyName;

    EntityFinder(String fancyName, ICustomEntityHandler customEntityHandler, String... names) {
        this.fancyName = fancyName;
        this.customEntityHandler = customEntityHandler;

        this.names.addAll(Arrays.asList(names));
        this.names.add(fancyName);

        this.entityType = null;
    }

    EntityFinder(String fancyName, EntityType entityType, String... names) {
        this.fancyName = fancyName;
        this.entityType = entityType;

        this.names.addAll(Arrays.asList(names));
        this.names.add(fancyName);

        this.customEntityHandler = null;
    }

}
