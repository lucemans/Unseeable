package nl.lucemans.unseeable.tabcompleter;

import nl.lucemans.unseeable.Unseeable;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/*
 * Created by Lucemans at 28/05/2018
 * See https://lucemans.nl
 */
public class AdminCompleter extends LucTabCompleter {

    public void complete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            suggest(args[0],"setup", "spawn", "info", "property", "stop", "start", "remove", "help");
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("spawn") || args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("property") || args[0].equalsIgnoreCase("remove")) {
                res.addAll(Unseeable.instance.mapsStartingWith(args[1]));
            }
        }
        if (args.length == 3) {
            if (args[0].equalsIgnoreCase("spawn")) {
                suggest(args[2],  "add", "set", "clear");
            }
            if (args[0].equalsIgnoreCase("property")) {
                suggest(args[2], "kills", "minPlayers", "maxPlayers", "minPowerups", "maxPowerups");
            }
        }
        if (args.length == 4) {
            if (args[0].equalsIgnoreCase("spawn")) {
                if (args[2].equalsIgnoreCase("add") || args[2].equalsIgnoreCase("clear")) {
                    suggest(args[3], "spawn", "powerup", "firework");
                }
                if (args[2].equalsIgnoreCase("set")) {
                    suggest(args[3], "lose", "win");
                }
            }
        }
    }
}