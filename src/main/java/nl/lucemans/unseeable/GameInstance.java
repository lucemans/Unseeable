package nl.lucemans.unseeable;

import nl.lucemans.NovaItems.NItem;
import nl.lucemans.unseeable.system.Map;
import nl.lucemans.unseeable.utils.LanguageManager;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

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
    public HashMap<String, Double> fullHearts;
    public HashMap<String, Double> startHearts;
    public HashMap<String, Integer> startHunger;
    public HashMap<String, Float> lastSpeed;
    public Enum<GameState> state;
    public int StartTime = 0;

    public GameInstance(Map m) {
        this.m = m;
        this.players = new ArrayList<Player>();
        this.lastLocation = new HashMap<String, Location>();
        this.lastItems = new HashMap<String, HashMap<Integer, ItemStack>>();
        this.lastGamemode = new HashMap<String, GameMode>();
        this.startHearts = new HashMap<String, Double>();
        this.startHunger = new HashMap<String, Integer>();
        this.fullHearts = new HashMap<String, Double>();
        this.lastSpeed = new HashMap<String, Float>();
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
                start();
            }
        }
        if (state == GameState.DISPLAY || state == GameState.INGAME) {
            for (Player p : Bukkit.getOnlinePlayers()) {
                p.setSaturation(20f);
                p.setFoodLevel(20);
            }
        }
    }

    public void spawnPlayer(Player p) {
        p.teleport(m.spawnPoints.get(new Random().nextInt(m.spawnPoints.size())).getLocation());
        p.setHealthScale(m.totalHearts);
        p.setHealth(m.totalHearts);
        p.setWalkSpeed(m.speedBoost);
        p.setSprinting(true);
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

    public void leavePlayer(Player p) {
        if (lastLocation.containsKey(p.getUniqueId().toString()))
            p.teleport(lastLocation.get(p.getUniqueId().toString()));
        if (lastGamemode.containsKey(p.getUniqueId().toString()))
            p.setGameMode(lastGamemode.get(p.getUniqueId().toString()));
        if (lastItems.containsKey(p.getUniqueId().toString())) {
            p.getInventory().clear();
            HashMap<Integer, ItemStack> items = lastItems.get(p.getUniqueId().toString());
            for (Integer i : items.keySet()) {
                p.getInventory().setItem(i, items.get(i));
            }
        }
        if (startHunger.containsKey(p.getUniqueId().toString())) {
            p.setFoodLevel(startHunger.get(p.getUniqueId().toString()));
        }
        if (startHearts.containsKey(p.getUniqueId().toString())) {
            p.setHealth(startHearts.get(p.getUniqueId().toString()));
        }
        if (fullHearts.containsKey(p.getUniqueId().toString())) {
            p.setHealthScale(fullHearts.get(p.getUniqueId().toString()));
        }
        if (lastSpeed.containsKey(p.getUniqueId().toString())) {
            p.setWalkSpeed(lastSpeed.get(p.getUniqueId().toString()));
        }

        if (state == GameState.STARTING && players.size()-1 < m.minPlayers) {
            massSend("Game Start was canncelled due to not enough players.");
            state = GameState.COLLECTING;
            StartTime = 0;
        }
        if (state == GameState.INGAME && players.size()-1 < m.minPlayers) {
            massSend("Game was canncelled due to not enough players.");
            state = GameState.COLLECTING;
            StartTime = 0;
        }
        if (players.size() == 0)
            state = GameState.STOPPED;
    }

    public void start() {
        StartTime = 0;
        state = GameState.INGAME;
        massSend(LanguageManager.get("lang.gamestart", new String[]{}));
        for (Player p : players) {
            lastLocation.put(p.getUniqueId().toString(), p.getLocation().clone());
            lastGamemode.put(p.getUniqueId().toString(), p.getGameMode());
            startHearts.put(p.getUniqueId().toString(), p.getHealth());
            startHunger.put(p.getUniqueId().toString(), p.getFoodLevel());
            fullHearts.put(p.getUniqueId().toString(), p.getHealthScale());
            lastSpeed.put(p.getUniqueId().toString(), p.getWalkSpeed());


            HashMap<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();
            Integer slot = 0;
            for (ItemStack item : p.getInventory().getContents()) {
                items.put(slot, item);
                slot++;
            }
            lastItems.put(p.getUniqueId().toString(), items);

            p.setGameMode(GameMode.ADVENTURE);
            p.getInventory().clear();
            p.getInventory().setHeldItemSlot(0);
            p.getInventory().setItem(0, NItem.create(Material.DIAMOND_SWORD).setName("&7Sword of &rVisibility").setEnchantment(Enchantment.DURABILITY, 2).make());
            spawnPlayer(p);
        }
    }

    public void stop() {
        state = GameState.STOPPED;
        massSend(LanguageManager.get("lang.stop", new String[]{}));
        for (Player p : players) {
            Bukkit.getLogger().info("Leaving player " + p.getName());
            leavePlayer(p);
        }
        players.clear();
    }

    public void massSend(String msg) {
        for (Player p : players) {
            if (p != null)
                if (p.isOnline())
                    p.sendMessage(msg);
        }
    }

    /* Events */
    public void moveInput(PlayerMoveEvent event) {
        if (state == GameState.INGAME)
        {
            event.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
            if (event.getPlayer().getItemInHand().getType().equals(Material.DIAMOND_SWORD)) {
                event.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
                return;
            }
            if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getY() != event.getTo().getY() || event.getTo().getZ() != event.getFrom().getZ())
            {
                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 40, 10, false, false));
            }
        }
    }

    public void interactInput(PlayerInteractEvent event) {
        event.setCancelled(true);
    }
}
