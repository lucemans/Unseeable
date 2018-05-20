package nl.lucemans.unseeable.commands;

import nl.lucemans.unseeable.GameInstance;
import nl.lucemans.unseeable.Unseeable;
import org.bukkit.entity.Player;

/*
 * Created by Lucemans at 20/05/2018
 * See https://lucemans.nl
 */
public class StatusCommand implements BaseCommand {

    public void execute(Player p, String[] args) {
        p.sendMessage("------GameStatus------");

        if (Unseeable.instance.currentGame == null) {
            p.sendMessage("No game has started.");
            return;
        }

        GameInstance inst = Unseeable.instance.currentGame;

        p.sendMessage("State: " + inst.state.toString());
        p.sendMessage("Map: " + inst.m.name);
        p.sendMessage("Limit: " + inst.m.minPlayers + "-" + inst.m.maxPlayers);
        p.sendMessage("Players: ");
        for (Player p2 : inst.players) {
            p.sendMessage(" " + p2.getName());
        }
    }
}
