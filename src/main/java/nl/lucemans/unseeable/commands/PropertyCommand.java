package nl.lucemans.unseeable.commands;

import nl.lucemans.unseeable.Unseeable;
import nl.lucemans.unseeable.system.Map;
import nl.lucemans.unseeable.utils.LanguageManager;
import org.bukkit.entity.Player;

/*
 * Created by Lucemans at 26/05/2018
 * See https://lucemans.nl
 */
public class PropertyCommand implements BaseCommand {

    public void execute(Player p, String[] args) {
        if (args.length < 4) {
            p.sendMessage(LanguageManager.get("lang.suggest", new String[]{"/usa property <name> <property> <value>"}));
            return;
        }

        String mapName = args[1]; // City or Stone
        String property = args[2]; // add set or clear
        String value = args[3]; // spawn powerup lose or win

        Map m = Unseeable.instance.findMap(mapName);
        if (m == null) {
            p.sendMessage(LanguageManager.get("lang.mapnotfound", new String[]{mapName}));
            return;
        }

        if (property.equalsIgnoreCase("kills")) {
            try {
                m.killsRequired = Integer.parseInt(value);
            }catch (Exception e) {
                p.sendMessage(LanguageManager.get("lang.notint", new String[]{}));
                return;
            }
            p.sendMessage(LanguageManager.get("lang.propertyupdate", new String[]{property, m.killsRequired + "", m.name}));
        }
        if (property.equalsIgnoreCase("speed")) {
            try {
                m.speedBoost = Integer.parseInt(value);
            }catch (Exception e) {
                p.sendMessage(LanguageManager.get("lang.notint", new String[]{}));
                return;
            }
            p.sendMessage(LanguageManager.get("lang.propertyupdate", new String[]{property, m.minPowerups + "", m.name}));
        }
        if (property.equalsIgnoreCase("minPlayers")) {
            try {
                m.minPlayers = Integer.parseInt(value);
            }catch (Exception e) {
                p.sendMessage(LanguageManager.get("lang.notint", new String[]{}));
                return;
            }
            p.sendMessage(LanguageManager.get("lang.propertyupdate", new String[]{property, m.minPlayers + "", m.name}));
        }
        if (property.equalsIgnoreCase("maxPlayers")) {
            try {
                m.maxPlayers = Integer.parseInt(value);
            }catch (Exception e) {
                p.sendMessage(LanguageManager.get("lang.notint", new String[]{}));
                return;
            }
            p.sendMessage(LanguageManager.get("lang.propertyupdate", new String[]{property, m.maxPlayers + "", m.name}));
        }
        if (property.equalsIgnoreCase("minPowerups")) {
            try {
                m.minPowerups = Integer.parseInt(value);
            }catch (Exception e) {
                p.sendMessage(LanguageManager.get("lang.notint", new String[]{}));
                return;
            }
            p.sendMessage(LanguageManager.get("lang.propertyupdate", new String[]{property, m.minPowerups + "", m.name}));
        }
        if (property.equalsIgnoreCase("maxPowerups")) {
            try {
                m.maxPowerups = Integer.parseInt(value);
            }catch (Exception e) {
                p.sendMessage(LanguageManager.get("lang.notint", new String[]{}));
                return;
            }
            p.sendMessage(LanguageManager.get("lang.propertyupdate", new String[]{property, m.maxPowerups + "", m.name}));
        }
        if (property.equalsIgnoreCase("name")) {
            String nameBef = m.name;
            m.name = value;
            p.sendMessage(LanguageManager.get("lang.propertyupdate", new String[]{property, m.name + "", nameBef}));
        }
    }
}
