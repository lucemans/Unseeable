package nl.lucemans.unseeable.gui;

import nl.lucemans.NovaItems.NItem;
import nl.lucemans.ninventory.NInventory;
import nl.lucemans.unseeable.Unseeable;
import nl.lucemans.unseeable.utils.LScoreboard;
import nl.lucemans.unseeable.utils.MapUtil;
import nl.lucemans.unseeable.utils.PlayerHead;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.UUID;

public class SpectatorGui {

    public static void openGui(Player p) {
        NInventory ninv = new NInventory("Spectator Menu (" + Unseeable.currentGame.players.size() + ")", (int) Math.ceil(Unseeable.currentGame.players.size() / 9.0) * 9, Unseeable.instance);

        int i = 0;
        // Sort the list
        java.util.Map<String, Integer> r = MapUtil.sortByComparator((HashMap<String, Integer>) Unseeable.currentGame.kills.clone(), false);


                for (String str : r.keySet()) {
                    OfflinePlayer ofp = Bukkit.getOfflinePlayer(UUID.fromString(str));
                    NItem item = NItem.create(Material.SKULL_ITEM);
                    ItemStack prep = item.make();
                    SkullMeta meta = (SkullMeta) prep.getItemMeta();
                    meta.setOwningPlayer(ofp);
                    item.setMeta(meta);
                    item.setDurability((short) 3);
                    ninv.setItem(item.make(), i);
                    //lscore.content.add(Unseeable.parse(/*" &c" + place + "&6. &r" + ofp.getName() + " &5: &6" + r.get(str)*/ " &r" + r.get(str) + " " + (place == 1 ? "&6" : place == 2 ? "&a" : "&b") + ofp.getName()));
                    //place++;
                    i++;
                }

        p.openInventory(ninv.getInv());
    }
}
