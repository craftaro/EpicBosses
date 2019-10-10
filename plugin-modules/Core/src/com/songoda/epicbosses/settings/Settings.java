package com.songoda.epicbosses.settings;

import com.songoda.core.configuration.Config;
import com.songoda.core.configuration.ConfigSetting;
import com.songoda.core.hooks.EconomyManager;
import com.songoda.epicbosses.EpicBosses;

import java.util.Arrays;
import java.util.stream.Collectors;

public class Settings {

    static final Config config = EpicBosses.getInstance().getCoreConfig();

    public static final ConfigSetting DEBUG_MODE = new ConfigSetting(config, "Settings.debug", true);

    public static final ConfigSetting BOSS_TARGET_RANGE = new ConfigSetting(config, "Settings.bossTargetRange", 50);

    public static final ConfigSetting DEFAULT_NEARBY_RADIUS = new ConfigSetting(config, "Settings.defaultNearbyRadius", 250);

    public static final ConfigSetting NEARBY_FORMAT = new ConfigSetting(config, "Settings.nearbyFormat", "{name} ({distance}m)");

    public static final ConfigSetting BLOCKED_WORLDS_ENABLED = new ConfigSetting(config, "Settings.BlockedWorlds", false);

    public static final ConfigSetting BLOCKED_WORLDS = new ConfigSetting(config, "Settings.BlockedWorlds.worlds", Arrays.asList("world_the_end", "world_nether"));

    public static final ConfigSetting ECONOMY_PLUGIN = new ConfigSetting(config, "Settings.Economy", EconomyManager.getEconomy() == null ? "Vault" : EconomyManager.getEconomy().getName(),
            "Which economy plugin should be used?",
            "Supported plugins you have installed: \"" + EconomyManager.getManager().getRegisteredPlugins().stream().collect(Collectors.joining("\", \"")) + "\".");

    public static final ConfigSetting BOSS_SHOP = new ConfigSetting(config, "Toggles.bossShop", true);

    public static final ConfigSetting ENDERMAN_TELEPORTING = new ConfigSetting(config, "Toggles.endermanTeleporting", true);

    public static final ConfigSetting POTIONS_AFFECTING_BOSSES = new ConfigSetting(config, "Toggles.potionsAffectingBoss", true);

    public static final ConfigSetting MAX_NEARBY_RADIUS = new ConfigSetting(config, "Limits.maxNearbyRadius", 500);

    public static final ConfigSetting ASKYBLOCK_ENABLED = new ConfigSetting(config, "Hooks.ASkyBlock.enabled", false);

    public static final ConfigSetting ASKYBLOCK_ON_OWN_ISLAND = new ConfigSetting(config, "Hooks.ASkyBlock.onOwnIsland", false);

    public static final ConfigSetting FACTIONS_ENABLED = new ConfigSetting(config, "Hooks.Factions.enabled", false);

    public static final ConfigSetting FACTIONS_USE_WARZONE_SPAWN_REGION = new ConfigSetting(config, "Hooks.Factions.useWarzoneSpawnRegion", false);

    public static void setupConfig() {
        config.load();
        config.setAutoremove(true).setAutosave(true);

        config.saveChanges();
    }
}
