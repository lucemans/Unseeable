//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//
package nl.lucemans.unseeable.utils;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.I18n;
import com.earth2me.essentials.Trade;
import com.earth2me.essentials.User;
import com.earth2me.essentials.commands.EssentialsCommand;
import com.earth2me.essentials.commands.NoChargeException;
import com.earth2me.essentials.commands.NotEnoughArgumentsException;
import com.earth2me.essentials.utils.StringUtil;
import java.util.List;
import java.util.Locale;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent.TeleportCause;

public class HomeCommand extends EssentialsCommand {
    public HomeCommand() {
        super("lhome");
    }

    public void run(Essentials ess, Server server, Player p, User user, String commandLabel, String[] args) throws Exception {
        Trade charge = new Trade(this.getName(), this.ess);
        User player = user;
        String homeName = "";
        if (args.length > 0) {
            String[] nameParts = args[0].split(":");
            if (nameParts.length > 1) {
                player = ess.getUser(Bukkit.getOfflinePlayer(nameParts[0]).getUniqueId());
                if (nameParts.length > 1) {
                    homeName = nameParts[1];
                }
            }
            if (player == null)
            {
                p.sendMessage("No user found");
                return;
            }
            if (player.getHome(homeName.toLowerCase()) == null)
            {
                String homes = "";
                for (String str : player.getHomes()) {
                    homes += ", " + str;
                }
                homes = homes.replaceFirst(",", "").trim();
                p.sendMessage("Home \'"+homeName+"\' not found. Try " + homes);
                return;
            }
            p.teleport(player.getHome(homeName));
        }
    }

    private String getHomeLimit(User player) {
        if (!player.getBase().isOnline()) {
            return "?";
        } else {
            return "*";
        }
    }
}