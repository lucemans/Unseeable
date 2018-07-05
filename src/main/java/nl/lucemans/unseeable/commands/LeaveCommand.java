package nl.lucemans.unseeable.commands;

import nl.lucemans.unseeable.GameInstance;
import nl.lucemans.unseeable.Unseeable;
import nl.lucemans.unseeable.utils.LanguageManager;
import org.bukkit.entity.Player;

/*
 * Created by Lucemans at 21/05/2018
 * See https://lucemans.nl
 */
public class LeaveCommand implements BaseCommand {

    public void execute(Player p, String[] args) {
        if (Unseeable.instance.currentGame != null) {
            if (Unseeable.instance.currentGame.state != GameInstance.GameState.STOPPED) {
                if (Unseeable.instance.currentGame.players.contains(p)) {
                    Unseeable.instance.currentGame.players.remove(p);
                    Unseeable.instance.currentGame.leavePlayer(p);
                    p.sendMessage(LanguageManager.get("lang.leave", new String[]{}));
                }
                if (Unseeable.instance.currentGame.spectators.contains(p)) {
                    Unseeable.instance.currentGame.spectators.remove(p);
                    Unseeable.instance.currentGame.leaveSpectator(p);
                    p.sendMessage(LanguageManager.get("lang.leave", new String[]{}));
                }
            }
        }
    }
}
