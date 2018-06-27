package nl.lucemans.unseeable.utils;

import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;

public class ParticleUtil {

    public static void sendParticle(Player p, String particle, float x, float y, float z, float xoff, float yoff, float zoff, int speed, int count) {
        try {
            p.spawnParticle(Particle.valueOf(particle), x, y + 0.5f, z, 1);
/*            //        PacketPlayOutWorldParticles
            //EnumParticle var1, boolean var2, float var3, float var4, float var5, float var6, float var7, float var8, float var9, int var10, int... var11
            Class class_enumParticle = NMSUtil.getNMS("EnumParticle");
            Constructor<?> constructor = NMSUtil.getNMS("PacketPlayOutWorldParticles").getConstructor(class_enumParticle, boolean.class, float.class, float.class, float.class, float.class, float.class, float.class, float.class, int.class, int.class);
/*            Object o = constructor.newInstance(class_enumParticle.getEnumConstants()[0], x, y, z, xoff, yoff, zoff, speed, count);

            Object entityPlayer = p.getClass().getMethod("getHandle").invoke(p);
            Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);

            playerConnection.getClass().getMethod("sendPacket", NMSUtil.getNMS("Packet")).invoke(playerConnection, o);
  */      } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendParticle(Player p, String particle, float x, float y, float z) {
        sendParticle(p, particle, x, y, z, 0f, 0f, 0f, 0, 1);
    }

    public static void sendParticle(Player p, String particle, Location loc) {
        sendParticle(p, particle, (float) loc.getX(), (float) loc.getY(), (float) loc.getZ());
    }

    public static void sendCloud(Player p, Location loc, boolean color) {
        if (color)
            p.spawnParticle(Particle.CLOUD, loc, 25, 0.5, 1, 0.5, 0);
        else
            p.spawnParticle(Particle.SMOKE_LARGE, loc, 25, 0.5, 1, 0.5, 0);
    }
}
