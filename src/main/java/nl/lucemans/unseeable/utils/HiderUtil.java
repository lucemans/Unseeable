package nl.lucemans.unseeable.utils;

import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;
import org.bukkit.Particle;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;

public class HiderUtil {

    public static HashMap<Player, ArrayList<Player>> canSee = new HashMap<>();

    public static void hidePlayer(Player user, Player toHide) {
        try {
            user.hidePlayer(toHide);
            if (canSee(user, toHide)) {
                canSee.get(user).remove(toHide);
                //ParticleUtil.sendParticle(user, Particle.SMOKE_LARGE.name(), toHide.getLocation());
                ParticleUtil.sendCloud(user, toHide.getLocation().add(0, 0.5, 0), false);
            }/* Lets mess around with packets n reflection to see what that does... Failed attempt...

            Constructor<?> constructor = NMSUtil.getNMS("PacketWorldPartiles").getConstructor();
            Object o = constructor.newInstance();
            Field f = o.getClass().getDeclaredField("a");
            f.setAccessible(true);
            f.setInt(o, toHide.getEntityId());
            Field f2 = o.getClass().getDeclaredField("b");
            f2.setAccessible(true);
            f2.setByte(o, (byte) 5);

            Object entityPlayer = user.getClass().getMethod("getHandle").invoke(user);
            Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);

            playerConnection.getClass().getMethod("sendPacket", NMSUtil.getNMS("Packet")).invoke(playerConnection, o);
        */} catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void showPlayer(Player user, Player toShow) {
        try {
            if (canSee(user, toShow))
                return;
            user.showPlayer(toShow);
            canSee.get(user).add(toShow);
            ParticleUtil.sendCloud(user, toShow.getLocation().add(0, 0.5, 0), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean canSee(Player user, Player target) {
        if (!canSee.containsKey(user))
            canSee.put(user, new ArrayList<Player>());
        return canSee.get(user).contains(target);
    }
}