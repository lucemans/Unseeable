package nl.lucemans.unseeable.commands;

import nl.lucemans.NovaItems.NItem;
import nl.lucemans.ninventory.NInventory;
import nl.lucemans.unseeable.GameInstance;
import nl.lucemans.unseeable.Unseeable;
import nl.lucemans.unseeable.system.Map;
import nl.lucemans.unseeable.utils.LanguageManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

/*
 * Created by Lucemans at 20/05/2018
 * See https://lucemans.nl
 */
public class JoinCommand implements BaseCommand {

    // /us join <name>
    public void execute(final Player p, String[] args) {

        if (Unseeable.currentGame == null || Unseeable.currentGame.state == GameInstance.GameState.STOPPED) {
            // if no game is active
            if (!Unseeable.instance.hasWorkingMap()) {
                p.sendMessage(LanguageManager.get("lang.nomapsready", new String[]{}));
                return;
            }

            if (args.length >= 2) {
                Map m = Unseeable.instance.findMap(args[1]);
                if (m == null) {
                    p.sendMessage(LanguageManager.get("lang.mapnotfound", new String[]{args[1]}));
                    return;
                }

                if (!m.isSetup()) {
                    p.sendMessage(LanguageManager.get("lang.mapnotsetup", new String[]{m.name}));
                    return;
                }

                Unseeable.currentGame = new GameInstance(m);
            } else {
                NInventory linv = new NInventory(LanguageManager.get("lang.chooseamap", new String[]{"" + Unseeable.instance.maps.size()}), (int) Math.ceil(((double) Unseeable.instance.maps.size()) / 9.0) * 9, Unseeable.instance);
                Integer i = 0;
                for (final Map m : Unseeable.maps) {
                    linv.setItem(NItem.create(Material.valueOf(LanguageManager.get("lang.mapicon", new String[]{}))).setName(LanguageManager.get("lang.maptitle", new String[]{m.name})).setAmount(m.maxPlayers).make(), i);
                    linv.setLClick(i, new Runnable() {
                        public void run() {
                            p.performCommand("us join " + m.name);
                            if (Unseeable.currentGame != null)
                                if (Unseeable.currentGame.players.contains(p))
                                    p.closeInventory();
                        }
                    });
                    i++;
                }
                p.openInventory(linv.getInv());
                return;
            }
        }

        Unseeable.currentGame.joinPlayer(p);
    }
}
