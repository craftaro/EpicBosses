package net.aminecraftdev.custombosses.innerapi.reflection;

import org.bukkit.Location;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created by charl on 28-Apr-17.
 */
public class ChunkReflection extends ReflectionUtils {

    private static boolean _useOldMethods = false;

    static {
        if(getAPIVersion().startsWith("v1_8_R") || getAPIVersion().startsWith("v1_7_R")) {
            _useOldMethods = true;
        }
    }

    public static final Object getChunkProviderServer(Location location) {
        try {
            Class<?> c1 = getOBCClass("CraftWorld");
            Object craftWorld = c1.cast(location.getWorld()); //(CraftWorld) location.getWorld();
            Method m1 = c1.getDeclaredMethod("getHandle"); // CraftWorld.getHandle();
            Object h1 = m1.invoke(craftWorld); // WorldServer h1 = ((CraftWorld) location.getWorld()).getHandle();
            Object h2;

            if(_useOldMethods) {
                Field f1 = h1.getClass().getDeclaredField("chunkProviderServer"); // WorldServer.chunkProviderServer;
                h2 = f1.get(h1);
            } else {
                Method m3 = h1.getClass().getDeclaredMethod("getChunkProviderServer"); // WorldServer.getChunkProviderServer();
                h2 = m3.invoke(h1); // ChunkProviderServer h2 = h1.getChunkProviderServer();
            }

            return h2; // ChunkProviderServer.class
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static final boolean isChunkLoaded(Location location) {
        if(location == null) return false;
        if(location.getWorld() == null) return false;

        int x = location.getBlockX() / 16;
        int z = location.getBlockZ() / 16;

        try {
            Class<?> c1 = getChunkProviderServer(location).getClass();
            Method m1;
            Object h1 = getChunkProviderServer(location);

            if(_useOldMethods) {
                m1 = c1.getDeclaredMethod("isChunkLoaded", int.class, int.class); // ChunkProviderServer.isChunkLoaded(int.class, int.class);
            } else {
                m1 = c1.getDeclaredMethod("isLoaded", int.class, int.class); // ChunkProviderServer.isLoaded(int.class, int.class);
            }

            return (Boolean) m1.invoke(h1, x, z);
        } catch (Exception e) {
            e.printStackTrace();
            return true;
        }
    }

    public static final void loadChunk(Location location) {
        if(isChunkLoaded(location)) return;

        int x = location.getBlockX() / 16;
        int z = location.getBlockZ() / 16;

        try {
            Class<?> c1 = getChunkProviderServer(location).getClass();
            Object h1 = getChunkProviderServer(location);
            Method m1 = c1.getDeclaredMethod("loadChunk", int.class, int.class);
            m1.invoke(h1, x, z);

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}