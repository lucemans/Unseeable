package nl.lucemans.unseeable.system;

import nl.lucemans.unseeable.Unseeable;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerMoveEvent;

public class ChatResponse implements Listener {

    private ChatCallback cb;
    private String suggest;
    private Player p;
    private boolean active = true;

    public ChatResponse(Player p, String suggest, ChatCallback cb) {
        this.p = p;
        this.suggest = suggest;
        this.cb = cb;
        p.closeInventory();
        p.sendMessage(suggest);
        Bukkit.getPluginManager().registerEvents(this, Unseeable.instance);
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onChat(AsyncPlayerChatEvent event) {
        if (!active)
            return;
        if (event.getPlayer().equals(p)) {
            event.setCancelled(true);
            boolean res = cb.callback(event.getMessage());
            if (res) {
                active = false;
                return;
            }
            p.sendMessage(suggest);
        }
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onMove(PlayerMoveEvent event) {
        if (!active)
            return;
        if (event.getPlayer().equals(p))
        {
            event.setCancelled(true);
            p.sendMessage(suggest);
        }
    }
}
