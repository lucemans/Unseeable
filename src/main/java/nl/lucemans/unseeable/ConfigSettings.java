package nl.lucemans.unseeable;

import nl.lucemans.NovaItems.NItem;
import nl.lucemans.unseeable.powerups.PowerupTemplate;
import nl.lucemans.unseeable.system.Map;
import nl.lucemans.unseeable.system.Map2;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

public class ConfigSettings {

    public static ArrayList<String> allowedCommands;
    public static ArrayList<PowerupTemplate> powerupTemplates;

    protected static void loadConfig(Unseeable inst) {
        allowedCommands = new ArrayList<String>((List<String>) inst.getConfig().getList("allowed_commands"));
    }

    public static boolean allowedCommand(String command) {
        for (String allow : allowedCommands)
            if (command.startsWith("/" + allow))
                return true;
        return false;
    }

    public static PowerupTemplate getTemplate(Map m) {
        ArrayList<PowerupTemplate> opts = getAllowedTemplates(m);
        if (opts.size() == 0)
            return null;
        return opts.get(new Random().nextInt(opts.size()));
    }

    public static ArrayList<PowerupTemplate> getAllowedTemplates(Map m) {
        if (!(m instanceof Map2))
            return new ArrayList<>();

        ArrayList<PowerupTemplate> res = new ArrayList<>();

        for (final PowerupTemplate temp : ConfigSettings.powerupTemplates) {
            if (((Map2) m).unlockedPowerups.contains(temp.name))
                res.add(temp);
        }

        return res;
    }
}
