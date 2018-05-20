package nl.lucemans.unseeable.commands;

import nl.lucemans.NovaItems.NItem;
import nl.lucemans.ninventory.NInventory;
import nl.lucemans.unseeable.GameInstance;
import nl.lucemans.unseeable.Unseeable;
import nl.lucemans.unseeable.system.Map;
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

        if (Unseeable.instance.currentGame == null || Unseeable.instance.currentGame.state == GameInstance.GameState.STOPPED) {
            // if no game is active
            if (!Unseeable.instance.hasWorkingMap()) {
                p.sendMessage("No maps are ready to be played.");
                return;
            }

            if (args.length >= 2) {
                Map m = Unseeable.instance.findMap(args[1]);
                if (m == null) {
                    p.sendMessage("We could not find that map.");
                    return;
                }

                if (!m.isSetup()) {
                    p.sendMessage("Map not setup properly.");
                    return;
                }

                Unseeable.instance.currentGame = new GameInstance(m);
            } else {
                NInventory linv = new NInventory("Choose a map", (int) Math.ceil(((double) Unseeable.instance.maps.size()) / 9.0) * 9, Unseeable.instance);
                Integer i = 0;
                for (final Map m : Unseeable.instance.maps) {
                    linv.setItem(NItem.create(Material.WOOL).setName(m.name).setAmount(m.maxPlayers).make(), i);
                    linv.setLClick(i, new Runnable() {
                        public void run() {
                            p.performCommand("us join " + m.name);
                        }
                    });
                    i++;
                }
                p.openInventory(linv.getInv());
                return;
            }
        }

        Unseeable.instance.currentGame.joinPlayer(p);
    }
}
