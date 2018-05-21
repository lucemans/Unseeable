package nl.lucemans.unseeable;

import nl.lucemans.unseeable.system.Map;
import nl.lucemans.unseeable.utils.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/*
 * Created by Lucemans at 10/05/2018
 * See https://lucemans.nl
 */
public class GameInstance {

    public enum GameState {
        STOPPED, STARTING, INGAME, DISPLAY, COLLECTING
    }

    public Map m;
    public ArrayList<Player> players;
    public HashMap<String, Location> lastLocation;
    public HashMap<String, HashMap<Integer, ItemStack>> lastItems;
    public HashMap<String, GameMode> lastGamemode;
    public Enum<GameState> state;
    public int StartTime = 0;

    public GameInstance(Map m) {
        this.m = m;
        this.players = new ArrayList<Player>();
        this.lastLocation = new HashMap<String, Location>();
        this.lastItems = new HashMap<String, HashMap<Integer, ItemStack>>();
        this.lastGamemode = new HashMap<String, GameMode>();
        this.state = GameState.COLLECTING;
    }

    public void tick() {
        if (state == GameState.COLLECTING) {
            if (players.size() >= m.minPlayers) {
                state = GameState.STARTING;
                massSend(LanguageManager.get("lang.startin", new String[]{"30"}));
                StartTime = 30*20;
            }
        }
        if (state == GameState.STARTING) {
            if (StartTime > 0) {
                StartTime -= 1;
                if (StartTime % 20 == 0) {
                    Integer seconds = StartTime / 20;
                    if (seconds == 20 || seconds == 10 || seconds <= 5) {
                        if (seconds > 5)
                            massSend(LanguageManager.get("lang.startin", new String[]{"" + seconds}));
                        else
                            if (seconds == 0)
                                massSend(LanguageManager.get("lang.startinfast", new String[]{"NOW"}));
                            else
                                massSend(LanguageManager.get("lang.startinfast", new String[]{seconds + ""}));
                    }
                }
            } else {
                state = GameState.INGAME;
                massSend(LanguageManager.get("lang.gamestart", new String[]{}));
                for (Player p : players) {
                    lastLocation.put(p.getUniqueId().toString(), p.getLocation().clone());
                    lastGamemode.put(p.getUniqueId().toString(), p.getGameMode());

                    HashMap<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();
                    Integer slot = 0;
                    for (ItemStack item : p.getInventory().getContents()) {
                        items.put(slot, item);
                        slot++;
                    }
                    lastItems.put(p.getUniqueId().toString(), items);

                    p.setGameMode(GameMode.ADVENTURE);
                    p.getInventory().clear();
                    spawnPlayer(p);
                }
            }
        }
    }

    public void spawnPlayer(Player p) {
        p.teleport(m.spawnPoints.get(new Random().nextInt(m.spawnPoints.size())).getLocation());
        p.sendMessage(LanguageManager.get("lang.spawn", new String[]{}));
    }

    public void joinPlayer(Player p) {
        if (players.size() >= m.maxPlayers) {
            p.sendMessage(LanguageManager.get("lang.fullroom", new String[]{}));
            return;
        }
        if (state == GameState.COLLECTING || state == GameState.STARTING) {
            if (players.contains(p)) {
                p.sendMessage(LanguageManager.get("lang.alreadyingame", new String[]{}));
                return;
            }
            players.add(p);
            p.sendMessage(LanguageManager.get("lang.queue", new String[]{m.name}));
            return;
        }
        p.sendMessage(LanguageManager.get("lang.gamestillprogress", new String[]{}));
    }

    public void stop() {
        state = GameState.STOPPED;
        massSend(LanguageManager.get("lang.stop", new String[]{}));
        for (Player p : players) {
            if (lastLocation.containsKey(p.getUniqueId().toString()))
                p.teleport(lastLocation.get(p.getUniqueId().toString()));
            if (lastGamemode.containsKey(p.getUniqueId().toString()))
                p.setGameMode(lastGamemode.get(p.getUniqueId().toString()));
            if (lastItems.containsKey(p.getUniqueId().toString())) {
                HashMap<Integer, ItemStack> items = lastItems.get(p.getUniqueId().toString());
                for (Integer i : items.keySet()) {
                    p.getInventory().setItem(i, items.get(i));
                }
            }
        }
    }

    public void massSend(String msg) {
        for (Player p : players) {
            if (p != null)
                if (p.isOnline())
                    p.sendMessage(msg);
        }
    }

}
