package com.songoda.epicbosses.managers;

import com.songoda.epicbosses.CustomBosses;
import com.songoda.epicbosses.holder.ActiveAutoSpawnHolder;
import com.songoda.epicbosses.holder.autospawn.ActiveIntervalAutoSpawnHolder;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;

public class PlaceholderManager extends PlaceholderExpansion {

    private AutoSpawnManager autoSpawnManager;

    public PlaceholderManager(CustomBosses plugin) {
        this.autoSpawnManager = plugin.getAutoSpawnManager();
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier) {
        for (String autoSpawnName : this.autoSpawnManager.getIntervalAutoSpawns()) {
            ActiveAutoSpawnHolder autoSpawnHolder = this.autoSpawnManager.getActiveAutoSpawnHolder(autoSpawnName);
            if (!(autoSpawnHolder instanceof ActiveIntervalAutoSpawnHolder))
                continue;

            ActiveIntervalAutoSpawnHolder intervalAutoSpawnHolder = (ActiveIntervalAutoSpawnHolder) autoSpawnHolder;
            long duration = (long) (intervalAutoSpawnHolder.getRemainingMs() / 1000D);

            String formattedIdentifier = intervalAutoSpawnHolder.getIntervalSpawnElement().getPlaceholder() + "_formatted";
            if (identifier.equalsIgnoreCase(formattedIdentifier)) {
                return this.getTimeFormatted(duration);
            } else if (identifier.equals(intervalAutoSpawnHolder.getIntervalSpawnElement().getPlaceholder())) {
                return String.valueOf((int) (intervalAutoSpawnHolder.getRemainingMs() / 1000D));
            }
        }

        return null;
    }

    private String getTimeFormatted(long timeInSeconds) {
        long hours = (long) Math.floor(timeInSeconds / 60D / 60D);
        long minutes = (long) Math.floor(timeInSeconds / 60D) % 60;
        long seconds = timeInSeconds % 60;
        String formatted = "";

        if (hours > 0)
            formatted += hours + "h ";

        if (minutes > 0 || hours > 0)
            formatted += minutes + "m ";

        return formatted + seconds + "s";
    }

    @Override
    public String getIdentifier() {
        return "epicbosses";
    }

    @Override
    public String getAuthor() {
        return "Songoda";
    }

    @Override
    public String getVersion() {
        return CustomBosses.get().getDescription().getVersion();
    }

    @Override
    public boolean persist() {
        return true;
    }

}
