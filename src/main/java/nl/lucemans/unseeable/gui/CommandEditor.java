package nl.lucemans.unseeable.gui;

import nl.lucemans.NovaItems.NItem;
import nl.lucemans.ninventory.NInventory;
import nl.lucemans.unseeable.ConfigSettings;
import nl.lucemans.unseeable.Unseeable;
import nl.lucemans.unseeable.system.ChatCallback;
import nl.lucemans.unseeable.system.ChatResponse;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class CommandEditor {

    public static void openCommands(final Player p) {
        NInventory ninv = new NInventory("Allowed Commands", (int) Math.ceil((float) (Unseeable.maps.size() + 1) / 9.0) * 9, Unseeable.instance);
        Integer i = 0;
        for (final String str : ConfigSettings.allowedCommands) {
            ninv.setItem(NItem.create(Material.PAPER).setName("&r" + "/" + str).setDescription("&r", "&rShift Right Click to &c&lRemove.").make(), i);
            ninv.setShiftRClick(i, new Runnable() {
                @Override
                public void run() {
                    ConfigSettings.allowedCommands.remove(str);
                    openCommands(p);
                }
            });
            i++;
        }
        ninv.setItem(NItem.create(Material.INK_SACK).setName("&a&lCreate &rnew").setDescription("&r", "&7Click this to add", "&7another command.").make(), i);
        ninv.setLClick(i, new Runnable() {
            @Override
            public void run() {
                p.closeInventory();
                new ChatResponse(p, "Please enter a command without the \"/\"", new ChatCallback() {
                    @Override
                    public boolean callback(String str) {
                        ConfigSettings.allowedCommands.remove(str);
                        ConfigSettings.allowedCommands.add(str);
                        openCommands(p);
                        return true;
                    }
                });
            }
        });
        p.openInventory(ninv.getInv());
    }
}
