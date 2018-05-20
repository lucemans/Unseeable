package nl.lucemans.unseeable.commands;

import org.bukkit.entity.Player;

/*
 * Created by Lucemans at 10/05/2018
 * See https://lucemans.nl
 */
public interface BaseCommand {
    public void execute(Player p, String[] args);
}
