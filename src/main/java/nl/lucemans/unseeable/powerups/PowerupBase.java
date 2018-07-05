package nl.lucemans.unseeable.powerups;

import nl.lucemans.unseeable.Unseeable;
import nl.lucemans.unseeable.utils.PlayerHead;
import nl.lucemans.unseeable.utils.TitleManager;
import org.bukkit.Location;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityInteractEvent;
import org.bukkit.event.player.PlayerInteractAtEntityEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

/*
 * Created by Lucemans at 26/05/2018
 * See https://lucemans.nl
 */
public class PowerupBase {

    protected ArmorStand entity;
    public Location location;
    protected Double range = 1.5;

    public PowerupTemplate template;

    public PowerupBase(PowerupTemplate template, Location location) {
        entity = (ArmorStand) location.getWorld().spawnEntity(location.clone().subtract(0,0.50, 0), EntityType.ARMOR_STAND);
        this.location = location;

        this.template = template;

        entity.setHelmet(PlayerHead.getItemStack(template.base, 1));

        entity.setGravity(false);
        entity.setVisible(false);
    }

    public void tick() {
        Location n = entity.getLocation().clone();
        n.setPitch(n.getPitch() + 2f);
        n.setYaw(n.getYaw() + 2f);
        entity.teleport(n, PlayerTeleportEvent.TeleportCause.UNKNOWN);
    }

    public void userMove(PlayerMoveEvent event) {
        if (event.getTo().getWorld().getName().equalsIgnoreCase(entity.getLocation().getWorld().getName())) {
            if (event.getTo().distance(entity.getLocation()) <= range) {
                TitleManager.sendTitle(event.getPlayer(), template.name, template.description, 5, 15, 0);
                template.trigger(event.getPlayer());
                destroy();
            }
        }
    }

    public void onInteract(PlayerInteractAtEntityEvent event) {
        if (event.getRightClicked().getEntityId() == entity.getEntityId())
            event.setCancelled(true);
    }

    public void onInteract(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getEntityId() == entity.getEntityId())
            event.setCancelled(true);
    }

    public void destroy() {
        entity.remove();
        Unseeable.instance.currentGame.powerups.remove(this);
    }
}
