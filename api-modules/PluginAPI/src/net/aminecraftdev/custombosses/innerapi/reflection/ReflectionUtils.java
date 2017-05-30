package net.aminecraftdev.custombosses.innerapi.reflection;

import org.bukkit.Bukkit;

/**
 * Created by charl on 28-Apr-17.
 */
public class ReflectionUtils {

    private static String _nmsVersion = null;

    static {
        if(_nmsVersion == null) {
            _nmsVersion = Bukkit.getServer().getClass().getPackage().getName();
            _nmsVersion = _nmsVersion.substring(_nmsVersion.lastIndexOf(".") + 1);
        }
    }

    public static final String getAPIVersion() {
        return _nmsVersion;
    }

    public static final Class<?> getNMSClass(String name) {
        try {
            return Class.forName("net.minecraft.server." + _nmsVersion + "." + name);
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final Class<?> getOBCClass(String name) {
        try {
            return Class.forName("org.bukkit.craftbukkit." + _nmsVersion + "." + name);
        } catch(ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
