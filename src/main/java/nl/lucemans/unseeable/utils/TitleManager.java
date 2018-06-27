package nl.lucemans.unseeable.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;

public class TitleManager {

    public static void sendActionBar(Player p, String bar) {
        try {
            bar = ChatColor.translateAlternateColorCodes('&', bar);
            Constructor<?> constructor = NMSUtil.getNMS("PacketPlayOutChat").getConstructor(NMSUtil.getNMS("IChatBaseComponent"), NMSUtil.getNMS("ChatMessageType"));

            Object icbc = NMSUtil.getNMS("IChatBaseComponent").getDeclaredClasses()[0].getMethod("a", String.class).invoke(null, "{\"text\":\""+bar+"\"}");
            Object[] chatMessageType = NMSUtil.getNMS("ChatMessageType").getEnumConstants();
            Object packet = constructor.newInstance(icbc, chatMessageType[2]);
            Object entityPlayer = p.getClass().getMethod("getHandle").invoke(p);
            Object playerConnection = entityPlayer.getClass().getField("playerConnection").get(entityPlayer);

            playerConnection.getClass().getMethod("sendPacket", NMSUtil.getNMS("Packet")).invoke(playerConnection, packet);
        } catch (Exception e) {

        }
    }

    public static void sendTitle(Player p, String title, String subtitle, Integer fadeIn, Integer stay, Integer fadeOut) {
        p.sendTitle(ChatColor.translateAlternateColorCodes('&', title), ChatColor.translateAlternateColorCodes('&', subtitle), fadeIn, stay, fadeOut);
    }
}
