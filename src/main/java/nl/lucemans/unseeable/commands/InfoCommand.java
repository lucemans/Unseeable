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

        sendMsg(p, "------MapStatus------");
        sendMsg(p, "Name: " + m.name);
        sendMsg(p, "Kills required: " + m.killsRequired);
        sendMsg(p, "Min Players: " + m.minPlayers);
        sendMsg(p, "Max Players: " + m.maxPlayers);

        sendMsg(p, "Speed: " + m.speedBoost);
        sendMsg(p, "Health: " + m.totalHearts);

        sendMsg(p, "LoserTP: " + (m.loserSpawn != null ? ("x: " + m.loserSpawn.x + " y: " + m.loserSpawn.y + " z: " + m.loserSpawn.z + " world: " + m.loserSpawn.world) : ChatColor.RED + "Not Set"));
        sendMsg(p, "WinnerTP: " + (m.winnerSpawn != null ? ("x: " + m.winnerSpawn.x + " y: " + m.winnerSpawn.y + " z: " + m.winnerSpawn.z + " world: " + m.winnerSpawn.world) : ChatColor.RED + "Not Set"));
        sendMsg(p, "Spawnpoints: " + (m.spawnPoints.size() < 2 ? ChatColor.RED : ChatColor.GREEN) + m.spawnPoints.size());
        sendMsg(p, "Firework: " + (m.fireworks.size() > 0 ? m.fireworks.size() : "None"));
        sendMsg(p, "Powerups: " + (m.powerups.size() > 0 ? m.powerups.size() : "None"));
        if (m.powerups.size() > 0) {
            sendMsg(p, "MinPowerups: " + m.minPowerups);
            sendMsg(p, "MaxPowerups: " + (m.maxPowerups == -1 ? "Auto" : m.maxPowerups));
        }
        if (!m.isSetup())
            sendMsg(p, "This map is &c&lNOT &rproperly setup.");
    }

    public void sendMsg(Player p, String str) {
        p.sendMessage(Unseeable.parse(str));

    }
}
