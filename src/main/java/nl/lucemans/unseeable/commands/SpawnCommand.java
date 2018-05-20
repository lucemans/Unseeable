package nl.lucemans.unseeable.commands;

import nl.lucemans.unseeable.Unseeable;
import nl.lucemans.unseeable.system.Map;
import nl.lucemans.unseeable.utils.SerializableLocation;
import org.bukkit.entity.Player;

/*
 * Created by Lucemans at 20/05/2018
 * See https://lucemans.nl
 */
public class SpawnCommand implements BaseCommand {

    // /usa spawn <name> add
    // /usa spawn <name> clear
    public void execute(Player p, String[] args) {
        if (args.length < 3) {
            p.sendMessage("Please use /usa spawn <name> <add/clear>");
            return;
        }

        String mapName = args[1];
        String mod = args[2];

        Map m = Unseeable.instance.findMap(mapName);
        if (m == null) {
            p.sendMessage("Map '" + mapName + "' not found.");
            return;
        }

        if (mod.equalsIgnoreCase("add")) {
            m.spawnPoints.add(new SerializableLocation(p.getLocation()));
            p.sendMessage("Point added to map '" + m.name + "' (currently " + m.spawnPoints.size() + ")");
        } else if (mod.equalsIgnoreCase("clear")) {
            m.spawnPoints.clone();
            p.sendMessage("All points have been cleared for map '" + m.name + "'.");
        } else {
            p.sendMessage("Please use 'add' or 'clear'.");
        }
    }
}
