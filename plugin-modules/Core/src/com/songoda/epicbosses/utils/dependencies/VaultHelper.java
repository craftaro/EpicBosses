package com.songoda.epicbosses.utils.dependencies;

import com.songoda.epicbosses.utils.IHelper;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 25-May-17
 */
public class VaultHelper implements IHelper {

    private Economy economy;

    @Override
    public boolean isConnected() {
        return setupEconomy();
    }

    public Economy getEconomy() {
        if(this.economy == null) {
            RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);

            if (rsp == null) {
                return null;
            }

            this.economy = rsp.getProvider();
        }

        return this.economy;
    }

    private boolean setupEconomy() {
        return Bukkit.getServer().getPluginManager().getPlugin("Vault") != null;
    }

}
