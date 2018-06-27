package nl.lucemans.unseeable.utils;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import nl.lucemans.unseeable.Unseeable;
import org.apache.commons.net.util.Base64;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.SkullType;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.UUID;
import java.util.logging.Level;

/*
 * Created by Lucemans at 26/05/2018
 * See https://lucemans.nl
 */
public class PlayerHead {

    public static ItemStack getItemStack(String base64, int quantity) {
        String username = "Endereye";
        String uuid = UUID.randomUUID().toString();

        try {
            Package obcPackage = Bukkit.getServer().getClass().getPackage();
            String obcPackageName = obcPackage.getName();
            String obcVersion = obcPackageName.substring(obcPackageName.lastIndexOf(".") + 1);
            String nmsPackageName = "net.minecraft.server." + obcVersion;


            Class<?> nmsNbtBaseClass = Class.forName(nmsPackageName + ".NBTBase");

            Class<?> nmsNbtTagCompoundClass = Class.forName(nmsPackageName + ".NBTTagCompound");
            Method nbtSetString = nmsNbtTagCompoundClass.getDeclaredMethod("setString", new Class[]{String.class, String.class});
            Method nbtSet = nmsNbtTagCompoundClass.getDeclaredMethod("set", new Class[]{String.class, nmsNbtBaseClass});

            Class<?> nmsNbtTagListClass = Class.forName(nmsPackageName + ".NBTTagList");
            Method add = nmsNbtTagListClass.getDeclaredMethod("add", new Class[]{nmsNbtBaseClass});

            Class<?> nmsItemStackClass = Class.forName(nmsPackageName + ".ItemStack");
            Method itemStackSetTag = nmsItemStackClass.getDeclaredMethod("setTag", new Class[]{nmsNbtTagCompoundClass});
            Method itemStackGetTag = nmsItemStackClass.getDeclaredMethod("getTag", new Class[0]);

            Class<?> nmsItemClass = Class.forName(nmsPackageName + ".Item");

            Class<?> nmsItemsClass = Class.forName(nmsPackageName + ".Items");
            Field skullField = nmsItemsClass.getDeclaredField("SKULL");

            Class<?> obcCraftItemStackClass = Class.forName(obcPackageName + ".inventory.CraftItemStack");
            Method craftItemStackasBukkitCopy = obcCraftItemStackClass.getDeclaredMethod("asBukkitCopy", new Class[]{nmsItemStackClass});


            Object displayTag = nmsNbtTagCompoundClass.newInstance();
            nbtSetString.invoke(displayTag, new Object[]{"Name", username});

            Object entryTag = nmsNbtTagCompoundClass.newInstance();
            nbtSetString.invoke(entryTag, new Object[]{"Value", base64});

            Object texturesList = nmsNbtTagListClass.newInstance();
            add.invoke(texturesList, new Object[]{entryTag});

            Object propertiesTag = nmsNbtTagCompoundClass.newInstance();
            nbtSet.invoke(propertiesTag, new Object[]{"textures", texturesList});

            Object skullOwnerTag = nmsNbtTagCompoundClass.newInstance();
            nbtSetString.invoke(skullOwnerTag, new Object[]{"Id", uuid});
            nbtSet.invoke(skullOwnerTag, new Object[]{"Properties", propertiesTag});

            Object skullItem = skullField.get(null);

            Constructor nmsItemStackConstructor = nmsItemStackClass.getDeclaredConstructor(new Class[]{nmsItemClass, Integer.TYPE, Integer.TYPE});
            Object nmsStack = nmsItemStackConstructor.newInstance(new Object[]{skullItem, Integer.valueOf(quantity), Byte.valueOf((byte) SkullType.PLAYER.ordinal())});

            itemStackSetTag.invoke(nmsStack, new Object[]{nmsNbtTagCompoundClass.newInstance()});
            Object nmsStackTag = itemStackGetTag.invoke(nmsStack, new Object[0]);
            nbtSet.invoke(nmsStackTag, new Object[]{"display", displayTag});
            nbtSet.invoke(nmsStackTag, new Object[]{"SkullOwner", skullOwnerTag});

            return (ItemStack) craftItemStackasBukkitCopy.invoke(null, new Object[]{nmsStack});
        } catch (Exception ex) {
            Bukkit.getLogger().log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static ItemStack getSkull(String skinURL) {
        ItemStack head = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
        if (skinURL.isEmpty()) return head;
        ItemMeta headMeta = head.getItemMeta();
        GameProfile profile = new GameProfile(UUID.randomUUID(), null);
        byte[] encodedData = Base64.encodeBase64(String.format("{textures:[{Value:\"%s\"}]}", skinURL).getBytes());
        Bukkit.broadcastMessage(String.format("{textures:[{Value:\"%s\"}]}", skinURL));
        profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
        Field profileField = null;
        try {
            profileField = headMeta.getClass().getDeclaredField("profile");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        profileField.setAccessible(true);
        try {
            profileField.set(headMeta, profile);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        head.setItemMeta(headMeta);
        return head;
    }

    public static String getSkull(ItemStack item) {
        try {
            Unseeable.instance.getLogger().info("GetSkull");
            Field profileField = item.getItemMeta().getClass().getDeclaredField("profile");
            profileField.setAccessible(true);
            GameProfile prof = (GameProfile) profileField.get(item.getItemMeta());
            Unseeable.instance.getLogger().info(prof.toString());
            Unseeable.instance.getLogger().info(prof.getProperties().toString());
            for (Property textures : prof.getProperties().get("textures")) {
                if (textures.getName().equalsIgnoreCase("textures")) {
                    return textures.getValue();
                }
            }
        } catch (Exception e) {
            Unseeable.instance.getLogger().severe(e.toString());
            e.printStackTrace();
        }
        return "";
    }
}
