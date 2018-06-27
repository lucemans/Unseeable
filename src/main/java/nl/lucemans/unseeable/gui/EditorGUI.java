package nl.lucemans.unseeable.gui;

import nl.lucemans.NovaItems.NItem;
import nl.lucemans.ninventory.NInventory;
import nl.lucemans.unseeable.Unseeable;
import nl.lucemans.unseeable.utils.LanguageManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class EditorGUI {


    /*  Maps    |   Powerups    |   Defaults    |   Language Editor |   Permitted Commands  | */
    public static void openEditor(final Player p) {
        NInventory ninv = new NInventory(ChatColor.translateAlternateColorCodes('&', Unseeable.NAME), 9, Unseeable.instance);

        Integer i = 0;
        ninv.setItem(NItem.create(Material.WOOL).setName("&rMaps").setDescription("&r", "&7Shows a list of all maps", "&7and allows you to edit them.").make(), i);
        ninv.setLClick(i, new Runnable() {
            @Override
            public void run() {
                MapEditor.openMaps(p);
            }
        });
        i++;
        ninv.setItem(NItem.create(Material.BEACON).setName("&bPowerups").setDescription("&r", "&7Shows a global list of all", "&7powerups.").make(), i);
        ninv.setLClick(i, new Runnable() {
            @Override
            public void run() {
                PowerupEditor.editPowerups(p);
            }
        });
        i++;
        ninv.setItem(NItem.create(Material.REDSTONE).setName("&rDefaults Editor").setDescription("&r", "&7Edit the default map settings").make(), i);
        ninv.setLClick(i, new Runnable() {
            @Override
            public void run() {
                openDefaults(p);
            }
        });
        i++;
        ninv.setItem(NItem.create(Material.INK_SACK).setName("&rLanguage Editor").setDescription("&r", "&7Allows you to edit the language", "&7files ingame").make(), i);
        ninv.setLClick(i, new Runnable() {
            @Override
            public void run() {
                LanguageEditor.openLanguage(p);
            }
        });
        i++;
        ninv.setItem(NItem.create(Material.COMMAND).setName("&rIngame Commands").setDescription("&r", "&7Edit the list of permitted", "&7Ingame Commands.").make(), i);
        ninv.setLClick(i, new Runnable() {
            @Override
            public void run() {
                CommandEditor.openCommands(p);
            }
        });


        p.openInventory(ninv.getInv());
    }

    public static void openDefaults(Player p) {

    }
}
