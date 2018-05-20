package nl.lucemans.unseeable.commands;

import nl.lucemans.unseeable.GameInstance;
import nl.lucemans.unseeable.Unseeable;
import org.bukkit.entity.Player;

/*
 * Created by Lucemans at 21/05/2018
 * See https://lucemans.nl
 */
public class StopCommand implements BaseCommand {

    public void execute(Player p, String[] args) {
        if (Unseeable.instance.currentGame == null || Unseeable.instance.currentGame.state == GameInstance.GameState.STOPPED) {
            p.sendMessage("Game already stopped.");
            return;
        }
        Unseeable.instance.currentGame.stop();
        p.sendMessage("Game Stopped Successfully.");
        return;
    }
}
