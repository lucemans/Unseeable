package nl.lucemans.unseeable;

import nl.lucemans.NovaItems.NItem;
import nl.lucemans.unseeable.powerups.PowerupBase;
import nl.lucemans.unseeable.system.Map;
import nl.lucemans.unseeable.utils.*;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.ScoreboardManager;

import java.util.*;

/*
 * Created by Lucemans at 10/05/2018
 * See https://lucemans.nl
 */
public class GameInstance {

    public enum GameState {
        STOPPED, STARTING, INGAME, DISPLAY, COLLECTING
    }

    public Map m;
    // Ingame stuff
    public ArrayList<Player> players;
    public HashMap<String, Integer> kills;
    public HashMap<String, LScoreboard> scoreboards;
    public ArrayList<PowerupBase> powerups;

    // Pregame stuff
    public HashMap<String, Location> lastLocation;
    public HashMap<String, HashMap<Integer, ItemStack>> lastItems;
    public HashMap<String, GameMode> lastGamemode;
    public HashMap<String, Double> fullHearts;
    public HashMap<String, Double> startHearts;
    public HashMap<String, Integer> startHunger;
    public HashMap<String, Float> lastSpeed;
    public HashMap<String, Float> lastXP;
    public HashMap<String, Integer> lastLVL;
    public HashMap<String, String> lastDisplayName;
    public HashMap<String, Scoreboard> lastScoreboard;

    // GameState stuff
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
        this.lastXP = new HashMap<String, Float>();
        this.lastLVL = new HashMap<String, Integer>();
        this.lastDisplayName = new HashMap<String, String>();
        this.kills = new HashMap<String, Integer>();
        this.scoreboards = new HashMap<String, LScoreboard>();
        this.lastScoreboard = new HashMap<String, Scoreboard>();
        this.powerups = new ArrayList<PowerupBase>();
        this.state = GameState.COLLECTING;
    }

    public void tick() {
        if (state == GameState.COLLECTING) {
            if (players.size() >= m.minPlayers) {
                state = GameState.STARTING;
                massSend(LanguageManager.get("lang.startin", new String[]{"30"}));
                StartTime = 30 * 20;
                for (Player p : Bukkit.getOnlinePlayers()) {
                    if (!players.contains(p))
                        p.sendMessage(LanguageManager.get("lang.gamestarting", new String[]{m.name, players.size() + "", m.maxPlayers - players.size() + ""}));
                }
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
                        else if (seconds == 0)
                            massSend(LanguageManager.get("lang.startinfast", new String[]{"NOW"}));
                        else
                            massSend(LanguageManager.get("lang.startinfast", new String[]{seconds + ""}));
                    }
                }
            } else {
                start();
            }
        }
        if (state == GameState.INGAME) {
            for (Player p : players) {
                if (kills.containsKey(p.getUniqueId().toString())) {
                    if (kills.get(p.getUniqueId().toString()) >= m.killsRequired) {
                        // finish game & mark as winner
                        state = GameState.DISPLAY;
                        massSend(p.getName() + " has won!");
                        p.teleport(m.winnerSpawn.getLocation());
                        for (Player f : players) {
                            if (!p.getUniqueId().toString().equalsIgnoreCase(f.getUniqueId().toString()))
                                f.teleport(m.loserSpawn.getLocation());
                        }
                        StartTime = 10 * 20; // 10 seconds
                        break;
                    }
                }

                if (!isInField(p.getLocation())) {
                    spawnPlayer(p);
                }
            }
            java.util.Map<String, Integer> r = MapUtil.sortByComparator((HashMap<String, Integer>) kills.clone(), false);

            for (Player p : players) {
                if (scoreboards.containsKey(p.getUniqueId().toString())) {
                    LScoreboard lscore = scoreboards.get(p.getUniqueId().toString());
                    lscore.name = Unseeable.parse("&7&m---" + Unseeable.NAME + "&7&m---");
                    lscore.content = new ArrayList<String>(Arrays.asList(Unseeable.parse("First to &6" + m.killsRequired + "&r wins!")));
                    lscore.linesSkipped = 0;
                    lscore.spacer();
                    Integer place = 1;
                    for (String str : r.keySet()) {
                        if (place >= 4)
                            break;
                        OfflinePlayer ofp = Bukkit.getOfflinePlayer(UUID.fromString(str));
                        lscore.content.add(Unseeable.parse(" &c" + place + "&6. &r" + ofp.getName() + " &5: &6" + r.get(str)));
                        place++;
                    }
                    lscore.spacer();
                    Integer userplace = 0;
                    for (String str : r.keySet()) {
                        userplace++;
                        if (str.equalsIgnoreCase(p.getUniqueId().toString()))
                            break;
                    }
                    lscore.content.add(Unseeable.parse(" &c" + userplace + "&6. &rYou &5: &6" + r.get(p.getUniqueId().toString())));
                    lscore.update();
                }
            }

            for (PowerupBase powerupBase : (ArrayList<PowerupBase>) powerups.clone()) {
                powerupBase.tick();
            }
        }

        if (state == GameState.DISPLAY) {
            if (StartTime > 0) {
                StartTime -= 1;
                if (StartTime % 20 == 0) {
                    Integer seconds = StartTime / 20;
                    if (seconds == 20 || seconds == 10 || seconds <= 5) {
                        if (seconds > 5)
                            massSend(LanguageManager.get("lang.endin", new String[]{"" + seconds}));
                        else if (seconds == 0)
                            massSend(LanguageManager.get("lang.endin", new String[]{"NOW"}));
                        else
                            massSend(LanguageManager.get("lang.endin", new String[]{seconds + ""}));
                    }
                }
            } else {
                finish();
            }
        }

        if (state == GameState.DISPLAY || state == GameState.INGAME) {
            for (Player p : players) {
                p.setSaturation(20f);
                p.setFoodLevel(20);
                if (kills.containsKey(p.getUniqueId().toString())) {
                    p.setDisplayName(kills.get(p.getUniqueId().toString()) + " " + p.getName());
                    p.setLevel(kills.get(p.getUniqueId().toString()));
                    p.setExp((float) kills.get(p.getUniqueId().toString()) / m.killsRequired);
                }
            }
        }
    }

    public void spawnPlayer(Player p) {
        p.teleport(m.spawnPoints.get(new Random().nextInt(m.spawnPoints.size())).getLocation());
        p.setHealthScale(m.totalHearts);
        p.setHealth(m.totalHearts);
        p.setWalkSpeed(m.speedBoost * 0.2f);
        p.setSprinting(true);
        if (!hasSword(p)) {
            p.getInventory().setItem(0, NItem.create(Material.DIAMOND_SWORD).setName("&7Sword of &rVisibility").setEnchantment(Enchantment.DURABILITY, 2).make());
        }
        p.sendMessage(LanguageManager.get("lang.spawn", new String[]{}));
    }

    public boolean hasSword(Player p) {
        for (ItemStack item : p.getInventory().getContents()) {
            if (item.getType().equals(Material.DIAMOND_SWORD)) {
                if (item.hasItemMeta())
                    if (item.getItemMeta().getDisplayName().equalsIgnoreCase(Unseeable.parse("&7Sword of &rVisibility")))
                        return true;
            }
        }
        return false;
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
            massSend(LanguageManager.get("lang.joinqueueother", new String[]{p.getName()}));
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
        if (startHunger.containsKey(p.getUniqueId().toString()))
            p.setFoodLevel(startHunger.get(p.getUniqueId().toString()));
        if (startHearts.containsKey(p.getUniqueId().toString()))
            p.setHealth(startHearts.get(p.getUniqueId().toString()));
        if (fullHearts.containsKey(p.getUniqueId().toString()))
            p.setHealthScale(fullHearts.get(p.getUniqueId().toString()));
        if (lastSpeed.containsKey(p.getUniqueId().toString()))
            p.setWalkSpeed(lastSpeed.get(p.getUniqueId().toString()));
        if (lastXP.containsKey(p.getUniqueId().toString()))
            p.setExp(lastXP.get(p.getUniqueId().toString()));
        if (lastLVL.containsKey(p.getUniqueId().toString()))
            p.setLevel(lastLVL.get(p.getUniqueId().toString()));
        if (lastDisplayName.containsKey(p.getUniqueId().toString()))
            p.setDisplayName(lastDisplayName.get(p.getUniqueId().toString()));
        if (lastScoreboard.containsKey(p.getUniqueId().toString()))
            p.setScoreboard(lastScoreboard.get(p.getUniqueId().toString()));

        p.setSprinting(false);

        NametagChanger.changePlayerName(p, "0/" + m.killsRequired + " ", "", TeamAction.DESTROY);

        if (state == GameState.STARTING && players.size() - 1 < m.minPlayers) {
            massSend("Game Start was canncelled due to not enough players.");
            state = GameState.COLLECTING;
            StartTime = 0;
        }
        if (state == GameState.INGAME && players.size() - 1 < m.minPlayers) {
            massSend("Game was canncelled due to not enough players.");
            state = GameState.COLLECTING;
            StartTime = 0;
        }
        if (players.size() == 0)
            state = GameState.STOPPED;
    }

    public void start() {
        StartTime = 0;
        ScoreboardManager scoreman = Bukkit.getScoreboardManager();
        for (Player p : players) {
            lastLocation.put(p.getUniqueId().toString(), p.getLocation().clone());
            lastGamemode.put(p.getUniqueId().toString(), p.getGameMode());
            startHearts.put(p.getUniqueId().toString(), p.getHealth());
            startHunger.put(p.getUniqueId().toString(), p.getFoodLevel());
            fullHearts.put(p.getUniqueId().toString(), p.getHealthScale());
            lastSpeed.put(p.getUniqueId().toString(), p.getWalkSpeed());
            lastXP.put(p.getUniqueId().toString(), p.getExp());
            lastLVL.put(p.getUniqueId().toString(), p.getLevel());
            lastDisplayName.put(p.getUniqueId().toString(), p.getDisplayName());
            lastScoreboard.put(p.getUniqueId().toString(), p.getScoreboard());

            kills.put(p.getUniqueId().toString(), 0);

            HashMap<Integer, ItemStack> items = new HashMap<Integer, ItemStack>();
            Integer slot = 0;
            for (ItemStack item : p.getInventory().getContents()) {
                items.put(slot, item);
                slot++;
            }
            lastItems.put(p.getUniqueId().toString(), items);

            // setup scoreboard
            LScoreboard scoreboard = new LScoreboard();
            scoreboards.put(p.getUniqueId().toString(), scoreboard);
            p.setScoreboard(scoreboard.scoreboard);

            NametagChanger.changePlayerName(p, "0/" + m.killsRequired + " ", "", TeamAction.CREATE);
            p.setGameMode(GameMode.ADVENTURE);
            p.getInventory().clear();
            p.getInventory().setHeldItemSlot(0);
            p.getInventory().setItem(0, NItem.create(Material.DIAMOND_SWORD).setName("&7Sword of &rVisibility").setEnchantment(Enchantment.DURABILITY, 2).make());
            spawnPlayer(p);
        }
        state = GameState.INGAME;
        massSend(LanguageManager.get("lang.gamestart", new String[]{}));
    }

    public void stop() {
        state = GameState.STOPPED;
        massSend(LanguageManager.get("lang.stop", new String[]{}));
        for (Player p : players) {
            Bukkit.getLogger().info("Leaving player " + p.getName());
            leavePlayer(p);
        }
        for (PowerupBase powerupBase : (ArrayList<PowerupBase>) powerups.clone()) {
            powerupBase.destroy();
        }
        players.clear();
    }

    public void finish() {
        state = GameState.STOPPED;
        for (Player p : players) {
            leavePlayer(p);
        }
        for (PowerupBase powerupBase : (ArrayList<PowerupBase>) powerups.clone()) {
            powerupBase.destroy();
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
        if (state == GameState.INGAME) {
            event.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
            if (event.getPlayer().getItemInHand().getType().equals(Material.DIAMOND_SWORD)) {
                event.getPlayer().removePotionEffect(PotionEffectType.INVISIBILITY);
                return;
            }
            if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getY() != event.getTo().getY() || event.getTo().getZ() != event.getFrom().getZ()) {
                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20, 10, false, false));
            }
        }
    }

    public void interactInput(PlayerInteractEvent event) {
        event.setCancelled(true);
    }

    public boolean isInField(Location loc) {
        return loc.getX() >= m.negMark.x
                && loc.getX() <= m.posMark.x
                && loc.getY() >= m.negMark.y
                && loc.getY() <= m.posMark.y
                && loc.getZ() >= m.negMark.z
                && loc.getZ() <= m.posMark.z;
    }
}
