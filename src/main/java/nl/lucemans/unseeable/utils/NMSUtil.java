package nl.lucemans.unseeable.utils;

import nl.lucemans.unseeable.Unseeable;
import org.bukkit.Bukkit;

public class NMSUtil {

    public static Class<?> getNMS(String path) {
        try {
            //System.out.println("NMS");
            //System.out.println(Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + path);
            return Class.forName("net.minecraft.server." + Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3] + "." + path);
        }catch(Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
