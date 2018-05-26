package nl.lucemans.unseeable.powerups;

import nl.lucemans.NovaItems.NItem;
import nl.lucemans.unseeable.Unseeable;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerMoveEvent;

/*
 * Created by Lucemans at 26/05/2018
 * See https://lucemans.nl
 */
public class PowerupBase {

    private ArmorStand entity;
    private Location location;
    private Double range = 1.5;

    public PowerupBase(Location location) {
        entity = (ArmorStand) location.getWorld().spawnEntity(location, EntityType.ARMOR_STAND);
        this.location = location;
        entity.setHelmet(NItem.create(Material.STONE).make());
    }

    public void tick() {
        entity.getLocation().setPitch(entity.getLocation().getPitch() + 0.1f);
    }

    public void trigger(Player p) {
        p.sendMessage("POWER UP RECIEVED");
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
