package nl.lucemans.unseeable;

import nl.lucemans.unseeable.system.Map;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

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
    public Enum<GameState> state;
    public int StartTime = 0;

    public GameInstance(Map m) {
        this.m = m;
        this.players = new ArrayList<Player>();
        this.lastLocation = new HashMap<String, Location>();
        this.state = GameState.COLLECTING;
    }

    public void tick() {
        if (state == GameState.COLLECTING) {
            if (players.size() >= m.minPlayers) {
                state = GameState.STARTING;
                massSend("Game Starting in 30s.");
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
                            massSend("Game Starting in " + seconds + " seconds!");
                        else
                            if (seconds == 0)
                                massSend("Game Starting in NOW!");
                            else
                                massSend("Game Starting in " + seconds + "!");
                    }
                }
            } else {
                state = GameState.INGAME;
                massSend("Game Started");
                for (Player p : players) {
                    lastLocation.put(p.getUniqueId().toString(), p.getLocation().clone());
                    spawnPlayer(p);
                }
            }
        }
    }

    public void spawnPlayer(Player p) {
        p.teleport(m.spawnPoints.get(new Random().nextInt(m.spawnPoints.size())).getLocation());
        p.sendMessage("You have been spawned.");
    }

    public void joinPlayer(Player p) {
        if (players.size() >= m.maxPlayers) {
            p.sendMessage("Sorry room was full.");
            return;
        }
        if (state == GameState.COLLECTING || state == GameState.STARTING) {
            if (players.contains(p)) {
                p.sendMessage("You are already ingame.");
                return;
            }
            players.add(p);
            p.sendMessage(Unseeable.parse("Successfully joined the queue for '&a&l"+m.name+"&r'."));
            return;
        }
        p.sendMessage("Game still in progress.");
    }

    public void stop() {
        state = GameState.STOPPED;
        massSend("Game Abruptly Stopped. We are sorry for the inconveniance");
        for (Player p : players) {
            if (lastLocation.containsKey(p.getUniqueId().toString()))
                p.teleport(lastLocation.get(p.getUniqueId().toString()));
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
