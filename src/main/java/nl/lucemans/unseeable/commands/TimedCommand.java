package nl.lucemans.unseeable.commands;

import nl.lucemans.unseeable.Unseeable;
import org.bukkit.entity.Player;

public class TimedCommand implements BaseCommand {

    @Override
    public void execute(Player p, String[] args) {
        Unseeable.instance.toggleTimedMode(p);
        p.sendMessage("Toggled Timed Mode. Currently: " + (Unseeable.instance.isTimed() ? "Enabled" : "Disabled"));
    }
}
