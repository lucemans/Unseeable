package nl.lucemans.unseeable.commands;

import nl.lucemans.unseeable.Unseeable;
import nl.lucemans.unseeable.system.Map;
import nl.lucemans.unseeable.utils.LanguageManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/*
 * Created by Lucemans at 20/05/2018
 * See https://lucemans.nl
 */
public class InfoCommand implements BaseCommand {

    // /usa info <mapName>
    public void execute(Player p, String[] args) {
        if (args.length < 2) {
            p.sendMessage(LanguageManager.get("lang.suggest", new String[]{"/usa info <mapName>"}));
            return;
        }

        String mapName = args[1];
        Map m = Unseeable.instance.findMap(mapName);
        if (m == null) {
            p.sendMessage(LanguageManager.get("lang.mapnotfound", new String[]{mapName}));
            return;
        }

        p.sendMessage("------MapStatus------");
        p.sendMessage("Name: " + m.name);
        p.sendMessage("Min Players: " + m.minPlayers);
        p.sendMessage("Max Players: " + m.maxPlayers);
        p.sendMessage("Speed: " + m.speedBoost);
        p.sendMessage("Health: " + m.totalHearts);
        p.sendMessage("LoserTP: " + (m.loserSpawn != null ? ("x: " + m.loserSpawn.x + " y: " + m.loserSpawn.y + " z: " + m.loserSpawn.z + " world: " + m.loserSpawn.world) : ChatColor.RED + "Not Set"));
        p.sendMessage("WinnerTP: " + (m.winnerSpawn != null ? ("x: " + m.winnerSpawn.x + " y: " + m.winnerSpawn.y + " z: " + m.winnerSpawn.z + " world: " + m.winnerSpawn.world) : ChatColor.RED + "Not Set"));
        p.sendMessage("Spawnpoints: " + (m.spawnPoints.size() < 2 ? ChatColor.RED : ChatColor.GREEN) + m.spawnPoints.size());
        p.sendMessage("IsSetup: " + (m.isSetup() ? "Yes" : "No"));
    }
}
