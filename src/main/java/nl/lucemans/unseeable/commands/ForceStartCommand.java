package nl.lucemans.unseeable.commands;

import nl.lucemans.unseeable.GameInstance;
import nl.lucemans.unseeable.Unseeable;
import nl.lucemans.unseeable.utils.LanguageManager;
import org.bukkit.entity.Player;

/*
 * Created by Lucemans at 21/05/2018
 * See https://lucemans.nl
 */
public class ForceStartCommand implements BaseCommand {

    public void execute(Player p, String[] args) {
        if (Unseeable.instance.currentGame != null) {
            if (Unseeable.instance.currentGame.state == GameInstance.GameState.COLLECTING || Unseeable.instance.currentGame.state == GameInstance.GameState.STARTING) {
                    Unseeable.instance.currentGame.start();
                    Unseeable.instance.currentGame.massSend(LanguageManager.get("lang.forcestart", new String[]{p.getName()}));
                    p.sendMessage(LanguageManager.get("lang.gamestartsuccess", new String[]{}));
                return;
            }
        }
        p.sendMessage(LanguageManager.get("lang.nogamestarted", new String[]{}));
    }
}
