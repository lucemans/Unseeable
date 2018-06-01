package nl.lucemans.unseeable.commands;

import nl.lucemans.unseeable.GameInstance;
import nl.lucemans.unseeable.Unseeable;
import nl.lucemans.unseeable.utils.LanguageManager;
import org.bukkit.entity.Player;

/*
 * Created by Lucemans at 30/05/2018
 * See https://lucemans.nl
 */
public class SpectateCommand implements BaseCommand {

    public void execute(Player p, String[] args) {
        if (Unseeable.instance.currentGame != null && (Unseeable.instance.currentGame.state == GameInstance.GameState.INGAME || Unseeable.instance.currentGame.state == GameInstance.GameState.DISPLAY))
        {
            Unseeable.instance.currentGame.joinSpectator(p);
            return;
        }
        p.sendMessage(LanguageManager.get("lang.nogamestarted", new String[]{}));
    }
}
