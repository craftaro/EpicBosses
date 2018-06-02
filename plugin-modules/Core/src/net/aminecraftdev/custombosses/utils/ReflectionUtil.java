package net.aminecraftdev.custombosses.utils;

import org.bukkit.Bukkit;

/**
 * @author Charles Cullen
 * @version 1.0.0
 * @since 01-Jun-18
 */
public class ReflectionUtil {

    private static ReflectionUtil instance = new ReflectionUtil();

    private String nmsVersion;

    private ReflectionUtil() {
        this.nmsVersion = Bukkit.getServer().getClass().getPackage().getName();
        this.nmsVersion = this.nmsVersion.substring(this.nmsVersion.lastIndexOf(".") + 1);
    }

    public String getVersion() {
        return this.nmsVersion;
    }

    public Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + this.nmsVersion + "." + name);
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Class<?> getOBCClass(String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + this.nmsVersion + "." + name);
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

     public static ReflectionUtil get() {
        return instance;
     }

}
