package nl.lucemans.unseeable.commands;

import com.sk89q.worldedit.bukkit.WorldEditPlugin;
import com.sk89q.worldedit.bukkit.selections.CuboidSelection;
import com.sk89q.worldedit.bukkit.selections.Selection;
import nl.lucemans.unseeable.Unseeable;
import nl.lucemans.unseeable.system.Map;
import nl.lucemans.unseeable.utils.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

/*
 * Created by Lucemans at 10/05/2018
 * See https://lucemans.nl
 */
public class AdminCommand implements CommandExecutor {

    private SpawnCommand spawnCommand = new SpawnCommand();
    private InfoCommand infoCommand = new InfoCommand();
    private StopCommand stopCommand = new StopCommand();
    private RemoveCommand removeCommand = new RemoveCommand();
    private ForceStartCommand forceStartCommand = new ForceStartCommand();

    // -/usa setup <name> <minPlayers> <maxPlayers>
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (!(sender instanceof Player)) {
            sender.sendMessage(LanguageManager.get("lang.nonconsole", new String[]{}));
            return true;
        }

        Player p = (Player) sender;

        if (args.length == 0) {
            sendHelp(p);
            return true;
        }
        if (args[0].equalsIgnoreCase("setup")) {
            if (args.length < 4) {
                p.sendMessage(LanguageManager.get("lang.suggest", new String[]{"/usa setup <name> <minPlayers> <maxPlayers>"}));
                return true;
            }

            String name = args[1];
            Integer minPlayers = -1;
            Integer maxPlayers = -1;

            if (Unseeable.instance.findMap(name) != null) {
                p.sendMessage(LanguageManager.get("lang.mapinuse", new String[]{name}));
                return true;
            }

            try {
                minPlayers = Integer.parseInt(args[2]);
            } catch (Exception e) {
                p.sendMessage(LanguageManager.get("lang.validnumber", new String[] {"MinPlayers"}));
                return true;
            }
            if (minPlayers < 2) {
                p.sendMessage(LanguageManager.get("lang.lesstwo", new String[] {"MinPlayers"}));
                return true;
            }
            try {
                maxPlayers = Integer.parseInt(args[3]);
            } catch (Exception e) {
                p.sendMessage(LanguageManager.get("lang.validnumber", new String[] {"MaxPlayers"}));
                return true;
            }
            if (maxPlayers < 2) {
                p.sendMessage(LanguageManager.get("lang.lesstwo", new String[] {"MaxPlayers"}));
                return true;
            }

            // fetch both points, write them.
            WorldEditPlugin plug = (WorldEditPlugin) Bukkit.getPluginManager().getPlugin("WorldEdit");
            Selection sel = plug.getSelection(p);
            if (sel == null) {
                p.sendMessage(LanguageManager.get("lang.selectwe", new String[]{}));
                return true;
            }

            if (!(sel instanceof CuboidSelection)) {
                p.sendMessage(LanguageManager.get("lang.cuboidwe", new String[]{}));
                return true;
            }

            Map m = new Map(name, sel.getMinimumPoint(), sel.getMaximumPoint(), minPlayers, maxPlayers);
            Unseeable.instance.maps.add(m);

            p.sendMessage(LanguageManager.get("lang.mapcreate", new String[]{name}));
            return true;
        }
        if (args[0].equalsIgnoreCase("spawn")) {
            spawnCommand.execute(p, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("info")) {
            infoCommand.execute(p, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("stop")) {
            stopCommand.execute(p, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("start")) {
            forceStartCommand.execute(p, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("remove")) {
            removeCommand.execute(p, args);
            return true;
        }
        if (args[0].equalsIgnoreCase("help")) {
            sendHelp(p);
            return true;
        }
        sendHelp(p);
        return true;
    }

    private void sendHelp(Player p) {
        p.sendMessage(Unseeable.parse("&7&m---"+Unseeable.NAME+"&4&lAdmin&7&m---"));
        p.sendMessage(Unseeable.parse("&6/usa setup <name> <minPlayers> <maxPlayers> &r|| &7Creates a map with parameters."));
        p.sendMessage(Unseeable.parse("&6/usa spawn <name> add <spawn/powerup> &r|| &7Adds spawnpoints or powerups."));
        p.sendMessage(Unseeable.parse("&6/usa spawn <name> set <lose/win> &r|| &7Modifies the maps endpositions."));
        p.sendMessage(Unseeable.parse("&6/usa spawn <name> clear <spawn/powerup> &r|| &7Clears spawnpoints or powerups."));
        p.sendMessage(Unseeable.parse("&6/usa info <map> &r|| &7Gives Map Information."));
        p.sendMessage(Unseeable.parse("&6/usa stop &r|| &7Stops the current Game."));
        p.sendMessage(Unseeable.parse("&6/usa remove <map> &r|| &7Removes a map."));
        p.sendMessage(Unseeable.parse("&6/usa help &r|| &7Shows this help menu."));
    }
}
