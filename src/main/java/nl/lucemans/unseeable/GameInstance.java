package nl.lucemans.unseeable;

import nl.lucemans.NovaItems.NItem;
import nl.lucemans.ninventory.NInventory;
import nl.lucemans.unseeable.powerups.PowerupBase;
import nl.lucemans.unseeable.system.Map;
import nl.lucemans.unseeable.utils.*;
import org.bukkit.*;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
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
    public ArrayList<Player> spectators;
    public HashMap<String, Integer> kills;
    private HashMap<String, LScoreboard> scoreboards;
    public ArrayList<PowerupBase> powerups;

    // Pregame stuff
    private HashMap<String, Location> lastLocation;
    private HashMap<String, HashMap<Integer, ItemStack>> lastItems;
    private HashMap<String, GameMode> lastGamemode;
    private HashMap<String, Double> fullHearts;
    private HashMap<String, Double> startHearts;
    private HashMap<String, Integer> startHunger;
    private HashMap<String, Float> lastSpeed;
    private HashMap<String, Float> lastXP;
    private HashMap<String, Integer> lastLVL;
    private HashMap<String, String> lastDisplayName;
    private HashMap<String, Scoreboard> lastScoreboard;
    private HashMap<String, Boolean> lastAllowFlight;
    private HashMap<String, Boolean> lastFlight;

    // GameState stuff
    public Enum<GameState> state;
    private int StartTime = 0;

    public GameInstance(Map m) {
        this.m = m;
        this.players = new ArrayList<Player>();
        this.spectators = new ArrayList<Player>();
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
        this.lastFlight = new HashMap<String, Boolean>();
        this.lastAllowFlight = new HashMap<String, Boolean>();
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
            // Check for winner
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

                // respawn player
                if (!isInField(p.getLocation())) {
                    spawnPlayer(p);
                }
            }
            // Apply Effects to Spectators
            for (Player p : spectators) {
                p.removePotionEffect(PotionEffectType.INVISIBILITY);
                p.addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20*4, 1));
            }

            /** SCOREBOARDS */
            // Sort the list
            java.util.Map<String, Integer> r = MapUtil.sortByComparator((HashMap<String, Integer>) kills.clone(), false);

            // Send players scoreboards
            for (Player p : players) {
                if (scoreboards.containsKey(p.getUniqueId().toString())) {
                    LScoreboard lscore = scoreboards.get(p.getUniqueId().toString());
                    lscore.name = Unseeable.parse("&7&m---" + Unseeable.NAME + "&7&m---");
                    lscore.content = new ArrayList<String>(Arrays.asList(Unseeable.parse("First to &6" + m.killsRequired + "&r wins!")));
                    lscore.linesSkipped = 0;
                    lscore.spacer();
                    Integer place = 1;
                    for (String str : r.keySet()) {
                        if (place > 3)
                            break;
                        OfflinePlayer ofp = Bukkit.getOfflinePlayer(UUID.fromString(str));
                        lscore.content.add(Unseeable.parse(/*" &c" + place + "&6. &r" + ofp.getName() + " &5: &6" + r.get(str)*/ " &r" + r.get(str) + " " + (place == 1 ? "&6" : place == 2 ? "&a" : "&b") + ofp.getName()));
                        place++;
                    }
                    Integer userplace = 0;
                    for (String str : r.keySet()) {
                        userplace++;
                        if (str.equalsIgnoreCase(p.getUniqueId().toString()))
                            break;
                    }
                    if (userplace > 3) {
                        lscore.spacer();
                        lscore.content.add(Unseeable.parse(" &c" + userplace + "&6. &rYou &5: &6" + r.get(p.getUniqueId().toString())));
                    }
                    lscore.update();
                }
            }
            for (Player p : spectators) {
                if (scoreboards.containsKey(p.getUniqueId().toString())) {
                    LScoreboard lscore = scoreboards.get(p.getUniqueId().toString());
                    lscore.name = Unseeable.parse("&7&m---" + Unseeable.NAME + "&7&m---");
                    lscore.content = new ArrayList<String>(Arrays.asList(Unseeable.parse("First to &6" + m.killsRequired + "&r wins!")));
                    lscore.linesSkipped = 0;
                    lscore.spacer();
                    Integer place = 1;
                    for (String str : r.keySet()) {
                        if (place >= 6)
                            break;
                        OfflinePlayer ofp = Bukkit.getOfflinePlayer(UUID.fromString(str));
                        lscore.content.add(Unseeable.parse(" &c" + place + "&6. &r" + ofp.getName() + " &5: &6" + r.get(str)));
                        place++;
                    }
                    lscore.update();
                }
            }

            // Regulate Powerups
            if (m.powerups.size() > 0) {
                // do we need powerups?
                while (powerups.size() < m.minPowerups) {
                    spawnPowerup();
                }
                // do we want powerups?
                if (powerups.size() < m.maxPowerups || (m.maxPowerups == -1 && powerups.size() < m.powerups.size())) {
                    Random random = new Random();
                    if (random.nextInt(1000) == 1) {
                        spawnPowerup();
                    }
                }

                for (PowerupBase powerupBase : (ArrayList<PowerupBase>) powerups.clone()) {
                    powerupBase.tick();
                }
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
                    if ((StartTime / 20) % 5 == 0) {
                        // TODO: FIREWORKS MODE
                        // every 5 second
                        for (SerializableLocation floc : m.fireworks) {
                            Location loc = floc.getLocation();
                            InstantFirework fw = new InstantFirework(FireworkEffect.builder().withFade(Color.PURPLE).withFade(Color.BLUE).withTrail().withColor(Color.YELLOW).withColor(Color.LIME).build(), loc);
                        }
                    }
                }
            } else {
                finish();
            }

            // Organize the list again.
            java.util.Map<String, Integer> r = MapUtil.sortByComparator((HashMap<String, Integer>) kills.clone(), false);

            // Winner
            Player winner = null;
            for (String str : r.keySet()) {
                winner = Bukkit.getPlayer(UUID.fromString(str));
                break;
            }

            // Send players scoreboards
            for (Player p : players) {
                if (scoreboards.containsKey(p.getUniqueId().toString())) {
                    LScoreboard lscore = scoreboards.get(p.getUniqueId().toString());
                    lscore.name = Unseeable.parse("&7&m---" + Unseeable.NAME + "&7&m---");
                    lscore.content = new ArrayList<String>(Arrays.asList(Unseeable.parse(winner.getName() + " Has Won!")));
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
            for (Player p : spectators) {
                if (scoreboards.containsKey(p.getUniqueId().toString())) {
                    LScoreboard lscore = scoreboards.get(p.getUniqueId().toString());
                    lscore.name = Unseeable.parse("&7&m---" + Unseeable.NAME + "&7&m---");
                    lscore.content = new ArrayList<String>(Arrays.asList(Unseeable.parse(winner.getName() + " Has Won!")));
                    lscore.linesSkipped = 0;
                    lscore.spacer();
                    Integer place = 1;
                    for (String str : r.keySet()) {
                        if (place >= 6)
                            break;
                        OfflinePlayer ofp = Bukkit.getOfflinePlayer(UUID.fromString(str));
                        lscore.content.add(Unseeable.parse(" &c" + place + "&6. &r" + ofp.getName() + " &5: &6" + r.get(str)));
                        place++;
                    }
                    lscore.update();
                }
            }
        }

        /** FOOD **/
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
            for (Player p : spectators) {
                p.setSaturation(20f);
                p.setFoodLevel(20);
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

    public void joinSpectator(Player p) {
        joinGeneric(p);

        //NametagChanger.changePlayerName(p, "0/" + m.killsRequired + " ", "", TeamAction.CREATE);
        p.setGameMode(GameMode.ADVENTURE);
        p.getInventory().clear();
        p.getInventory().setHeldItemSlot(0);
        p.getInventory().setItem(0, NItem.create(Material.COMPASS).setName("&7Player Selector").setEnchantment(Enchantment.DURABILITY, 2).make());

        p.setAllowFlight(true);
        p.setFlying(true);

        p.teleport(m.spectatorSpawn.getLocation());

        spectators.add(p);
    }

    public void joinGeneric(Player p) {
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
        lastAllowFlight.put(p.getUniqueId().toString(), p.getAllowFlight());
        lastFlight.put(p.getUniqueId().toString(), p.isFlying());

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
    }

    public void leaveGeneric(Player p) {
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
        if (lastFlight.containsKey(p.getUniqueId().toString()))
            p.setFlying(lastFlight.get(p.getUniqueId().toString()));
        if (lastAllowFlight.containsKey(p.getUniqueId().toString()))
            p.setAllowFlight(lastAllowFlight.get(p.getUniqueId().toString()));
    }

    public void leavePlayer(Player p) {
        leaveGeneric(p);

        p.setSprinting(false);

        NametagChanger.changePlayerName(p, "0/" + m.killsRequired + " ", "", TeamAction.DESTROY);

        if (state == GameState.STARTING && players.size() - 1 < m.minPlayers) {
            massSend("Game Start was canncelled due to not enough players.");
            state = GameState.COLLECTING;
            StartTime = 0;
        }
        if (state == GameState.INGAME && players.size() - 1 < m.minPlayers) {
            massSend("Game was canncelled due to not enough players.");
            stop();
        }
        if (players.size() == 0)
            stop();
    }

    public void leaveSpectator(Player p) {
        leaveGeneric(p);

        p.setSprinting(false);

        if (state == GameState.STARTING && players.size() - 1 < m.minPlayers) {
            massSend("Game Start was canncelled due to not enough players.");
            state = GameState.COLLECTING;
            StartTime = 0;
        }
        if (state == GameState.INGAME && players.size() - 1 < m.minPlayers) {
            massSend("Game was canncelled due to not enough players.");
            stop();
        }
        if (players.size() == 0)
            stop();
    }

    public void start() {
        StartTime = 0;
        for (Player p : players) {
            joinGeneric(p);

            kills.put(p.getUniqueId().toString(), 0);

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
            leavePlayer(p);
        }
        for (Player p : spectators) {
            leaveSpectator(p);
        }
        for (PowerupBase powerupBase : (ArrayList<PowerupBase>) powerups.clone()) {
            powerupBase.destroy();
        }
        players.clear();
        spectators.clear();
    }

    public void finish() {
        state = GameState.STOPPED;
        for (Player p : players) {
            leavePlayer(p);
        }
        for (Player p : spectators) {
            leaveSpectator(p);
        }
        for (PowerupBase powerupBase : (ArrayList<PowerupBase>) powerups.clone()) {
            powerupBase.destroy();
        }
        players.clear();
        spectators.clear();
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
            } else if (event.getFrom().getX() != event.getTo().getX() || event.getFrom().getY() != event.getTo().getY() || event.getTo().getZ() != event.getFrom().getZ()) {
                event.getPlayer().addPotionEffect(new PotionEffect(PotionEffectType.INVISIBILITY, 20, 10, false, false));
            }
            for (PowerupBase powerupBase : (ArrayList<PowerupBase>) powerups.clone()) {
                powerupBase.userMove(event);
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

    public boolean hasPowerupAt(Location loc) {
        for (PowerupBase powerupBase : powerups) {
            if (powerupBase.location.distance(loc) == 0)
                return true;
        }
        return false;
    }

    public Location nextPowerup() {
        Location empty = null;
        ArrayList<SerializableLocation> tempPowerups = (ArrayList<SerializableLocation>) m.powerups.clone();
        Collections.shuffle(tempPowerups);
        for (SerializableLocation loc : tempPowerups) {
            Location sr = loc.getLocation();
            if (!hasPowerupAt(sr))
            {
                empty = sr;
                break;
            }
        }
        if (empty == null) {
            empty = m.powerups.get(new Random().nextInt(m.powerups.size())).getLocation();
        }
        return empty;
    }

    public PowerupBase spawnPowerup() {
        Location loc = nextPowerup();
        PowerupBase powerupBase = new PowerupBase(loc);
        powerups.add(powerupBase);
        return powerupBase;
    }
}
