package net.aminecraftdev.custombosses.innerapi.dependencies;

import net.milkbowl.vault.chat.Chat;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.permission.Permission;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;

/**
 * @author AMinecraftDev
 * @version 1.0.0
 * @since 25-May-17
 */
public class VaultHelper {

    private static Economy economy;
    private static Chat chat;
    private static Permission permission;

    public static final Economy getEconomy() {
        return economy;
    }

    public static final Chat getChat() {
        return chat;
    }

    public static final Permission getPermission() {
        return permission;
    }
    
    public static boolean setupVault() {
        if(!setupChat()) return false;
        if(!setupPermission()) return false;
        if(!setupEconomy()) return false;
        return true;
    }
    
    private static boolean setupChat() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Chat> rsp = Bukkit.getServer().getServicesManager().getRegistration(Chat.class);
        if (rsp == null) {
            return false;
        }
        chat = rsp.getProvider();
        return chat != null;
    }
    
    private static boolean setupEconomy() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = Bukkit.getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        economy = rsp.getProvider();
        return economy != null;
    }
    
    private static boolean setupPermission() {
        if (Bukkit.getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Permission> rsp = Bukkit.getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp == null) {
            return false;
        }
        permission = rsp.getProvider();
        return permission != null;
    }


}
