package nl.lucemans.unseeable.commands;

import nl.lucemans.unseeable.Unseeable;
import nl.lucemans.unseeable.system.Map;
import nl.lucemans.unseeable.utils.LanguageManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/*
 * Created by Lucemans at 10/05/2018
 * See https://lucemans.nl
 */
public class ListCommand implements BaseCommand {
    public void execute(Player p, String[] args) {
        p.sendMessage(Unseeable.parse("&7&m---" + Unseeable.NAME + "&7&m---"));
        if (Unseeable.instance.maps.size() == 0)
            p.sendMessage(LanguageManager.get("lang.nomapsready", new String[]{}));
        for (Map m : Unseeable.instance.maps) {
            p.sendMessage(" " + (m.isSetup() ? ChatColor.GREEN : ChatColor.RED) + ChatColor.BOLD + m.name);
        }
    }
}
