package nl.lucemans.unseeable.powerups;

import nl.lucemans.NovaItems.NItem;
import nl.lucemans.unseeable.Unseeable;
import nl.lucemans.unseeable.utils.PlayerHead;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.Random;
import java.util.UUID;

/*
 * Created by Lucemans at 26/05/2018
 * See https://lucemans.nl
 */
public class PowerupBase {

    private ArmorStand entity;
    public Location location;
    private Double range = 1.5;

    public PowerType type;

    public PowerupBase(Location location) {
        entity = (ArmorStand) location.getWorld().spawnEntity(location.clone().subtract(0,0.25, 0), EntityType.ARMOR_STAND);
        this.location = location;

        type = PowerType.values()[new Random().nextInt(PowerType.values().length)];

        entity.setHelmet(PlayerHead.getItemStack(type.getBase(), 1));

        entity.setGravity(false);
        entity.setVisible(false);
    }

    public void tick() {
        Location n = entity.getLocation().clone();
        n.setPitch(n.getPitch() + 2f);
        n.setYaw(n.getYaw() + 2f);
        entity.teleport(n, PlayerTeleportEvent.TeleportCause.UNKNOWN);
    }

    public void trigger(Player p) {
        p.sendMessage("POWER UP RECIEVED");
        if (type == PowerType.VISIBILITY_I) {
            for (Player pl : Unseeable.instance.currentGame.players) {
                if (p == pl)
                    continue;
                pl.removePotionEffect(PotionEffectType.GLOWING);
                pl.addPotionEffect(new PotionEffect(PotionEffectType.GLOWING, 20 * 5, 1));
            }
        }
        if (type == PowerType.HEALTH_I) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 20*10, 10));
        }
        if (type == PowerType.HEALTH_II) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.ABSORPTION, 20*10, 10));
        }
        if (type == PowerType.DAMAGE_I) {
            p.addPotionEffect(new PotionEffect(PotionEffectType.INCREASE_DAMAGE, 20*10, 10));
            p.addPotionEffect(new PotionEffect(PotionEffectType.DAMAGE_RESISTANCE, 20*10, 10));
        }
    }

    public void userMove(PlayerMoveEvent event) {
        if (event.getTo().getWorld().getName().equalsIgnoreCase(entity.getLocation().getWorld().getName())) {
            if (event.getTo().distance(entity.getLocation()) <= range) {
                trigger(event.getPlayer());
                destroy();
            }
        }
    }

    public void destroy() {
        entity.remove();
        Unseeable.instance.currentGame.powerups.remove(this);
    }
}
