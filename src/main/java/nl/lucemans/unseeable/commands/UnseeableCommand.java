package nl.lucemans.unseeable.commands;

import nl.lucemans.unseeable.Unseeable;
import nl.lucemans.unseeable.utils.LanguageManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/*
 * Created by Lucemans at 10/05/2018
 * See https://lucemans.nl
 */
public class UnseeableCommand implements CommandExecutor {

    private ListCommand listCommand = new ListCommand();
    private JoinCommand joinCommand = new JoinCommand();
    private StatusCommand statusCommand = new StatusCommand();
    private LeaveCommand leaveCommand = new LeaveCommand();
    private SpectateCommand spectateCommand = new SpectateCommand();

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(Unseeable.parse("&7&m------------"));
            sender.sendMessage(Unseeable.parse(Unseeable.NAME + " &rv1.0"));
            sender.sendMessage(Unseeable.parse("&rCreated by &d&lLuc&r&lemans&r."));
            sender.sendMessage(Unseeable.parse("For a list of commands &5/us help"));
            return true;
        }
        if (!(sender instanceof Player)) {
            sender.sendMessage(LanguageManager.get("lang.nonconsole", new String[]{}));
            return true;
        }
        Player p = (Player) sender;

        if (args[0].equalsIgnoreCase("list")) {
            listCommand.execute(p, args);
            return true;
        }

        if (args[0].equalsIgnoreCase("join")) {
            joinCommand.execute(p, args);
            return true;
        }

        if (args[0].equalsIgnoreCase("spectate")) {
            spectateCommand.execute(p, args);
            return true;
        }

        if (args[0].equalsIgnoreCase("leave")) {
            leaveCommand.execute(p, args);
            return true;
        }

        if (args[0].equalsIgnoreCase("info") || args[0].equalsIgnoreCase("status")) {
            statusCommand.execute(p, args);
            return true;
        }

        if (args[0].equalsIgnoreCase("help")) {
            sendHelp(p);
            return true;
        }

        p.sendMessage(LanguageManager.get("lang.suggest", new String[]{"&5/us help"}));
        return true;
    }

    private void sendHelp(Player p) {
        p.sendMessage(Unseeable.parse("&7&m---"+Unseeable.NAME+"&7&m---"));
        p.sendMessage(Unseeable.parse("&6/us list &r|| &7Lists all available maps."));
        p.sendMessage(Unseeable.parse("&6/us join &r|| &7Joins a game or a map selector."));
        p.sendMessage(Unseeable.parse("&6/us join <map> &r|| &7Joins a specific map."));
        p.sendMessage(Unseeable.parse("&6/us spectate &r|| &7Spectates the current game."));
        p.sendMessage(Unseeable.parse("&6/us leave &r|| &7Leave the game."));
        p.sendMessage(Unseeable.parse("&6/us info &r|| &7Gives Game Info."));
        p.sendMessage(Unseeable.parse("&6/us help &r|| &7Shows the help menu"));
    }

}
