package nl.lucemans.unseeable.system;

import nl.lucemans.unseeable.Unseeable;
import nl.lucemans.unseeable.utils.SerializableLocation;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerToggleSneakEvent;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;

public class MassLocation implements Listener {

    private Player p;
    private Map m;
    private Plugin plug;
    private ArrayList<Location> locs = new ArrayList<Location>();
    private Location origin;
    private Integer points = 0;
    private Integer powerupPoints = 0;


    public MassLocation(Player p, Map m) {
        this(p,m,Unseeable.instance);
    }

    public MassLocation(Player p, Map m, Plugin plug) {
        this.p = p;
        this.m = m;
        this.plug = plug;
        this.origin = p.getLocation().clone();
        Bukkit.getPluginManager().registerEvents(this, plug);
        for (int x = 0; x <= (m.posMark.x - m.negMark.x); x++) {
            for (int y = 0; y <= (m.posMark.y - m.negMark.y); y++) {
                for (int z = 0; z <= (m.posMark.z - m.negMark.z); z++) {
                    Block b = m.posMark.getLocation().getWorld().getBlockAt(m.negMark.getLocation().add(x, y, z));
                    if (b == null)
                        continue;
                    if (b.getType() == Material.AIR)
                        continue;
                    if (b.getType() == Material.DIAMOND_BLOCK) {
                        if (!contain(b.getLocation()))
                            locs.add(b.getLocation());
                    }
                    if (b.getType() == Material.REDSTONE_BLOCK) {
                        m.powerups.add(new SerializableLocation(b.getLocation().add(0.5, 1, 0.5)));
                        powerupPoints++;
                    }
                }
            }
        }
        teleport();
    }

    public boolean contain(Location loc) {
        for (Location _loc : locs) {
            if  (_loc.distance(loc) <= 0.2)
                return true;
        }
        return false;
    }

    @EventHandler
    public void onSneak(PlayerToggleSneakEvent event) {
        if (event.getPlayer().getUniqueId().toString().equalsIgnoreCase(p.getUniqueId().toString())) {
            if (locs.size() == 0)
                return;
            if (!event.isSneaking())
                return;
            event.setCancelled(true);
            m.spawnPoints.add(new SerializableLocation(event.getPlayer().getLocation()));
            locs.remove(0);
            points++;
            teleport();
        }
    }

    private void teleport() {
        if (locs.size() == 0) {
            p.teleport(origin);
            p.sendMessage("Finished Setting up Locations!");
            p.sendMessage("Added " + points + " SpawnPoints.");
            p.sendMessage("Added " + powerupPoints + " PowerupPoints.");
            return;
        }
        p.teleport(locs.get(0).add(0.5, 1, 0.5));
        p.sendMessage("( " + (points+1) + " / " + (locs.size() + points) + " ) Press shift to confirm.");
    }
}
