package com.songoda.epicbosses.utils;

import com.songoda.epicbosses.utils.entity.ICustomEntityHandler;
import com.songoda.epicbosses.utils.entity.handlers.*;
import org.bukkit.Location;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;

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
    ZOMBIE_HORSE("ZombieHorse", new ZombieHorseHandler(), "zombiehorse", "zombie_horse", "zombie horse"),
    DONKEY("Donkey", new DonkeyHorseHandler(), "donkey"),
    MULE("Mule", new MuleHorseHandler(), "mule"),
    EVOKER("Evoker", new EvokerHandler(), "evoker"),
    VEX("Vex", new VexHandler(), "vex"),
    VINDICATOR("Vindicator", new VindicatorHandler(), "vindicator"),
    ILLUSIONER("Illusioner", new IllusionerHandler(), "illusioner"),
    CREEPER("Creeper", EntityType.CREEPER, "creeper"),
    SKELETON("Skeleton", EntityType.SKELETON, "skeleton"),
    SPIDER("Spider", EntityType.SPIDER, "spider"),
    GIANT("Giant", EntityType.GIANT, "giant", "giant_zombie", "giant zombie", "giantzombie"),
    ZOMBIE("Zombie", new ZombieHandler(), "zombie"),
    BABY_ZOMBIE("BabyZombie", new ZombieBabyHandler(), "babyzombie", "baby_zombie", "baby zombie"),
    SLIME("Slime", new SlimeHandler(), "slime"),
    GHAST("Ghast", EntityType.GHAST, "ghast"),
    PIG_ZOMBIE("PigZombie", new PigZombieHandler(), "pigzombie", "pig zombie", "pig_zombie", "zombiepigman", "zombie_pigman", "zombie pigman"),
    BABY_PIG_ZOMBIE("BabyPigZombie", new PigZombieBabyHandler(), "babypigzombie", "baby pig zombie", "baby_pig_zombie", "babyzombiepigman", "baby_zombie_pigman", "baby zombie pigman"),
    ENDERMAN("Enderman", EntityType.ENDERMAN, "enderman"),
    CAVE_SPIDER("CaveSpider", EntityType.CAVE_SPIDER, "cavespider", "cave_spider", "cave spider"),
    SILVERFISH("Silverfish", EntityType.SILVERFISH, "silverfish"),
    BLAZE("Blaze", EntityType.BLAZE, "blaze"),
    MAGMA_CUBE("MagmaCube", new MagmaCubeHandler(), "magmacube", "magma_cube", "magma cube"),
    ENDER_DRAGON("EnderDragon", EntityType.ENDER_DRAGON, "enderdragon", "ender_dragon", "ender dragon"),
    WITHER("Wither", EntityType.WITHER, "wither"),
    BAT("Bat", EntityType.BAT, "bat"),
    WITCH("Witch", EntityType.WITCH, "witch"),
    ENDERMITE("Endermite", new EndermiteHandler(), "endermite"),
    GUARDIAN("Guardian", new GuardianHandler(), "guardian"),
    SHULKER("Shulker", new ShulkerHandler(), "shulker"),
    PIG("Pig", EntityType.PIG, "pig"),
    SHEEP("Sheep", EntityType.SHEEP, "sheep"),
    COW("Cow", EntityType.COW, "cow"),
    CHICKEN("Chicken", EntityType.CHICKEN, "chicken"),
    SQUID("Squid", EntityType.SQUID, "squid"),
    WOLF("Wolf", EntityType.WOLF, "wolf"),
    MUSHROOM_COW("MushroomCow", EntityType.MUSHROOM_COW, "mushroomcow", "mushroom_cow", "mushroom cow", "mooshroom"),
    SNOWMAN("Snowman", EntityType.SNOWMAN, "snowman"),
    OCELOT("Ocelot", EntityType.OCELOT, "ocelot"),
    IRON_GOLEM("IronGolem", EntityType.IRON_GOLEM, "irongolem", "iron_golem", "iron golem", "ig"),
    HORSE("Horse", EntityType.HORSE, "horse"),
    RABBIT("Rabbit", new RabbitHandler(), "rabbit"),
    POLAR_BEAR("PolarBear", new PolarBearHandler(), "polarbear", "polar_bear", "polar bear", "snowbear", "snow_bear", "snow bear"),
    LLAMA("Llama", new LlamaHandler(), "llama"),
    PARROT("Parrot", new ParrotHandler(), "parrot"),
    VILLAGER("Villager", new VillagerHandler(), "villager"),
    DOLPHIN("Dolphin", new DolphinHandler(), "dolphin"),
    DROWNED("Drowned", new DrownedHandler(), "drowned"),
    FISH("Fish", new FishHandler(), "fish", "tropicalfish", "tropical fish", "tropical_fish", "clownfish", "cod", "salmon", "pufferfish"),
    TURTLE("Turtle", new TurtleHandler(), "turtle"),
    PHANTOM("Phantom", new PhantomHandler(), "phantom"),
    CAT("Cat", new CatHandler(), "cat"),
    FOX("Fox", new FoxHandler(), "fox"),
    PANDA("Panda", new PandaHandler(), "panda"),
    PILLAGER("Pillager", new PillagerHandler(), "pillager"),
    RAVAGER("Ravager", new RavagerHandler(), "ravager"),
    TRADER_LLAMA("TraderLlama", new TraderLlamaHandler(), "traderllama", "trader_llama", "trader llama", "llamatrader", "llama_trader", "llama trader"),
    WANDERING_TRADER("WanderingTrader", new WanderingTraderHandler(), "wanderingtrader", "wandering_trader", "wandering trader", "tradervillager", "trader_villager", "trader villager");

    private ICustomEntityHandler customEntityHandler;
    private List<String> names = new ArrayList<>();
    private EntityType entityType;
    private String fancyName;

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

    public static EntityFinder get(String name) {
        for (EntityFinder entityFinder : values()) {
            for (String s : entityFinder.getNames()) {
                if (name.equalsIgnoreCase(s)) return entityFinder;
            }
        }

        return null;
    }

    @Override
    public String toString() {
        return this.fancyName;
    }

    public LivingEntity spawnNewLivingEntity(String input, Location location) {
        if (this.customEntityHandler != null) {
            LivingEntity livingEntity;

            try {
                livingEntity = this.customEntityHandler.getBaseEntity(input, location);
            } catch (NullPointerException ex) {
                Debug.FAILED_ATTEMPT_TO_SPAWN_BOSS.debug(ex.getMessage());
                return null;
            }

            return livingEntity;
        } else {
            return (LivingEntity) location.getWorld().spawnEntity(location, getEntityType());
        }
    }

    public ICustomEntityHandler getCustomEntityHandler() {
        return this.customEntityHandler;
    }

    public List<String> getNames() {
        return this.names;
    }

    public EntityType getEntityType() {
        return this.entityType;
    }

    public String getFancyName() {
        return this.fancyName;
    }
}
