package nl.lucemans.unseeable.commands;

import nl.lucemans.unseeable.Unseeable;
import nl.lucemans.unseeable.system.Map;
import nl.lucemans.unseeable.utils.LanguageManager;
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
            p.sendMessage(LanguageManager.get("lang.suggest", new String[]{"/usa spawn <name> <add/clear>"}));
            return;
        }

        String mapName = args[1];
        String mod = args[2];

        Map m = Unseeable.instance.findMap(mapName);
        if (m == null) {
            p.sendMessage(LanguageManager.get("lang.mapnotfound", new String[]{mapName}));
            return;
        }

        if (mod.equalsIgnoreCase("add")) {
            m.spawnPoints.add(new SerializableLocation(p.getLocation()));
            String size = "" + m.spawnPoints.size();
            p.sendMessage(LanguageManager.get("lang.spawnadd", new String[]{m.name, size }));
        } else if (mod.equalsIgnoreCase("clear")) {
            m.spawnPoints.clone();
            p.sendMessage(LanguageManager.get("lang.spawnclear", new String[]{mapName}));
        } else {
            p.sendMessage(LanguageManager.get("lang.addorclear", new String[]{}));
        }
    }
}
