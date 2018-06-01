package nl.lucemans.unseeable.tabcompleter;

import nl.lucemans.unseeable.Unseeable;
import nl.lucemans.unseeable.system.Map;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Created by Lucemans at 27/05/2018
 * See https://lucemans.nl
 */
public class UnseeableCompleter extends LucTabCompleter {

    public void complete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            suggest(args[0], "list", "join", "leave", "info", "help", "spectate");
        }
        if (args.length == 2) {
            if (args[0].equalsIgnoreCase("join")) {
                ArrayList<String> str = new ArrayList<String>();
                for (Map m : Unseeable.instance.maps) {
                    if (m.isSetup()) {
                        str.add(m.name);
                    }
                }
                suggest(args[1], str.toArray(new String[]{}));
            }
        }
    }
}
