package nl.lucemans.unseeable.tabcompleter;

import nl.lucemans.unseeable.Unseeable;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

/*
 * Created by Lucemans at 28/05/2018
 * See https://lucemans.nl
 */
public class AdminCompleter implements TabCompleter {

    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 1) {
            
        }

        return null;
    }


    private void sendHelp(Player p) {
        p.sendMessage(Unseeable.parse("&7&m---"+Unseeable.NAME+"&4&lAdmin&7&m---"));
        p.sendMessage(Unseeable.parse("&6/usa setup <name> <minPlayers> <maxPlayers> &r|| &7Creates a map with parameters."));
        p.sendMessage(Unseeable.parse("&6/usa spawn <name> add <spawn/powerup/firework> &r|| &7Adds spawnpoints, firework or powerups."));
        p.sendMessage(Unseeable.parse("&6/usa spawn <name> set <lose/win> &r|| &7Modifies the maps endpositions."));
        p.sendMessage(Unseeable.parse("&6/usa spawn <name> clear <spawn/powerup/firework> &r|| &7Clears spawnpoints, firework or powerups."));
        p.sendMessage(Unseeable.parse("&6/usa info <map> &r|| &7Gives Map Information."));
        p.sendMessage(Unseeable.parse("&6/usa property <map> <property> <value> &r|| &7Set a property."));
        p.sendMessage(Unseeable.parse("&6/usa stop &r|| &7Stops the current Game."));
        p.sendMessage(Unseeable.parse("&6/usa remove <map> &r|| &7Removes a map."));
        p.sendMessage(Unseeable.parse("&6/usa help &r|| &7Shows this help menu."));
    }
}