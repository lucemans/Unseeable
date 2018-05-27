package nl.lucemans.unseeable.tabcompleter;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/*
 * Created by Lucemans at 28/05/2018
 * See https://lucemans.nl
 */
public abstract class LucTabCompleter implements TabCompleter {

    public ArrayList<String> res = new ArrayList<String>();

    public abstract void complete(CommandSender sender, Command cmd, String label, String[] args);

    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        res.clear();
        complete(commandSender, command, s, strings);
        return res;
    }

    public void suggest(String word, String... args) {
        ArrayList<String> str = new ArrayList<String>(Arrays.asList(args));
        for (String _str : str) {
            if (_str.startsWith(word)) {
                res.add(_str);
            }
        }
    }
}
