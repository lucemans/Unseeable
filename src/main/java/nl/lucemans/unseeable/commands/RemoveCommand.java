package nl.lucemans.unseeable.commands;

import nl.lucemans.unseeable.Unseeable;
import nl.lucemans.unseeable.system.Map;
import nl.lucemans.unseeable.utils.LanguageManager;
import org.bukkit.entity.Player;

/*
 * Created by Lucemans at 21/05/2018
 * See https://lucemans.nl
 */
public class RemoveCommand implements BaseCommand {

    // /usa remove <map>
    public void execute(Player p, String[] args) {
        if (args.length < 2) {
            p.sendMessage(LanguageManager.get("lang.suggest", new String[]{"/usa remove <map>"}));
            return;
        }

        Map m = Unseeable.instance.findMap(args[1]);
        if (m == null) {
            p.sendMessage(LanguageManager.get("lang.mapnotfound", new String[]{args[1]}));
            return;
        }

        Unseeable.instance.maps.remove(m);
        p.sendMessage(LanguageManager.get("lang.mapremove", new String[]{m.name}));
    }
}
