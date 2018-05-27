package nl.lucemans.unseeable;

import nl.lucemans.unseeable.commands.AdminCommand;
import nl.lucemans.unseeable.commands.UnseeableCommand;
import nl.lucemans.unseeable.system.Map;
import nl.lucemans.unseeable.tabcompleter.AdminCompleter;
import nl.lucemans.unseeable.tabcompleter.UnseeableCompleter;
import nl.lucemans.unseeable.utils.LanguageManager;
import nl.lucemans.unseeable.utils.NametagChanger;
import nl.lucemans.unseeable.utils.TeamAction;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/*
 * Created by Lucemans at 10/05/2018
 * See https://lucemans.nl
 */
public class Unseeable extends JavaPlugin implements Listener {

    public static Unseeable instance;
    public ArrayList<Map> maps = new ArrayList<Map>();
    public GameInstance currentGame = null;

    public static String NAME = "&b&lUn&6&lseeable";

    private File mapFile = new File(getDataFolder(), "Maps.data");

    @Override
    public void onEnable() {
        super.onEnable();
        instance = this;

        saveDefaultConfig();

        // Load maps
        if (!getDataFolder().exists())
            getDataFolder().mkdir();

        if (mapFile.exists())
        {
            try {
                FileInputStream fis = new FileInputStream(mapFile);
                ObjectInputStream ois = new ObjectInputStream(fis);
                maps = (ArrayList<Map>)ois.readObject();
                ois.close();
                fis.close();
            } catch (Exception e) {
                getLogger().severe(parse("The Following error in " + NAME + " occurred whilst loading the maps."));
                e.printStackTrace();
            }
        }

        getCommand("unseeable").setExecutor(new UnseeableCommand());
        getCommand("unseeableadmin").setExecutor(new AdminCommand());
        getCommand("unseeable").setTabCompleter(new UnseeableCompleter());
        getCommand("unseeableadmin").setTabCompleter(new AdminCompleter());

        Bukkit.getPluginManager().registerEvents(this, this);

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            public void run() {
                if (currentGame != null)
                    currentGame.tick();
            }
        }, 1L, 1L);
    }

    @Override
    public void onDisable() {

        if (currentGame != null && currentGame.state != GameInstance.GameState.STOPPED)
            currentGame.stop();

        try{
            if(mapFile.exists())
                mapFile.delete();
            FileOutputStream fos = new FileOutputStream(mapFile);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(maps);
            oos.flush();
            fos.flush();
            oos.close();
            fos.close();
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public Map findMap(String name) {
        for (Map m : maps) {
            if (m.name.equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }


    public List<String> mapsStartingWith(String str) {
        List<String> _maps = new ArrayList<String>();
        for (Map m : maps) {
            if (m.name.toLowerCase().startsWith(str.toLowerCase())) {
                _maps.add(m.name);
            }
        }
        return _maps;
    }

    public boolean hasWorkingMap() {
        for (Map m : maps)
            if (m.isSetup())
                return true;
        return false;
    }

    public static String parse(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        // check if user is ingame if so push it through.

        if (event.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (event.getClickedBlock().getType() == Material.WALL_SIGN || event.getClickedBlock().getType() == Material.SIGN_POST) {
                Sign s = (Sign) event.getClickedBlock().getState();
                if (s.getLine(0).equalsIgnoreCase(parse("&c["+NAME+"&c]"))) {
                    // it is one of our signs
                    event.getPlayer().sendMessage(s.getLine(1).replace(parse("&a&l"), ""));
                    Map m = findMap (s.getLine(1).replace(parse("&a&l"), ""));
                    if (m == null) {
                        event.getPlayer().sendMessage(parse("Could not find map."));
                        return;
                    }
                    else
                    {
                        if (m.isSetup()) {
                            if (Unseeable.instance.currentGame == null || Unseeable.instance.currentGame.state == GameInstance.GameState.STOPPED || Unseeable.instance.currentGame.m == m) {
                                // can start new game
                                event.getPlayer().performCommand("us join " + m.name);
                                return;
                            }
                            event.getPlayer().sendMessage(parse("A Game is already running."));
                        }
                        else {
                            event.getPlayer().sendMessage(parse("Map is not setup properly."));
                        }
                        return;
                    }
                    //event.getPlayer().sendMessage(parse("&cThis sign is setup incorrectly."));
                }
            }
        }

        if (currentGame != null && currentGame.state != GameInstance.GameState.STOPPED)
            if (currentGame.players.contains(event.getPlayer()))
                currentGame.interactInput(event);
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent event) {

    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent event) {
        if (event.getDamager() instanceof Player) {
            if (event.getEntity() instanceof Player) {
                if (currentGame == null || currentGame.state == GameInstance.GameState.STOPPED)
                    return;
                if (currentGame.players.contains(event.getEntity())) {
                    if (currentGame.players.contains(event.getDamager())) {
                        // Ingame punches ingame
                        if (currentGame.state == GameInstance.GameState.INGAME) {

                            ItemStack item = ((Player) event.getDamager()).getInventory().getItem(((Player) event.getDamager()).getInventory().getHeldItemSlot());
                            if (item == null || item.getType() == Material.AIR) {
                                event.setCancelled(true);
                                event.getDamager().sendMessage("You need to attack with your sword.");
                                return;
                            }

                            // successfull hit ingame
                            if (((Player) event.getEntity()).getHealth() - event.getFinalDamage() <= 0.0) {
                                currentGame.spawnPlayer((Player) event.getEntity());
                                event.setCancelled(true);
                                event.setDamage(0.0);
                                event.getDamager().sendMessage("Kill");
                                currentGame.kills.put(event.getDamager().getUniqueId().toString(), currentGame.kills.get(event.getDamager().getUniqueId().toString()) + 1);

                                NametagChanger.changePlayerName(((Player) event.getDamager()), currentGame.kills.get(event.getDamager().getUniqueId().toString()) + "/"+currentGame.m.killsRequired+" ", "", TeamAction.CREATE);
                            }
                        }
                        else
                        {
                            event.setCancelled(true);
                            event.getDamager().sendMessage("Its not the time for that right now.");
                            return;
                        }
                    }
                    else
                    {
                        event.setCancelled(true);
                        event.getDamager().sendMessage("Please dont damage in-game players"); //TODO: LANG
                        return;
                    }
                }
            }
        }
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (currentGame != null && currentGame.state != GameInstance.GameState.STOPPED)
            if (currentGame.players.contains(event.getPlayer()))
                currentGame.moveInput(event);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (currentGame != null && currentGame.state != GameInstance.GameState.STOPPED) {
            if (currentGame.players.contains(event.getPlayer())) {
                if (event.getMessage().startsWith("/usa") || event.getMessage().startsWith("/us")) {
                    return;
                }
                event.getPlayer().sendMessage(LanguageManager.get("lang.nocmdingame", new String[]{event.getMessage()}));
                event.setCancelled(true);
                return;
            }
        }
    }

    @EventHandler
    public void onLeave(PlayerQuitEvent event) {
        if (currentGame != null && currentGame.state != GameInstance.GameState.STOPPED) {
            if (currentGame.players.contains(event.getPlayer())) {
                currentGame.leavePlayer(event.getPlayer());
            }
        }
    }

    @EventHandler
    public void onSign(SignChangeEvent event) {
        if (event.getLine(0).equalsIgnoreCase("[usa]")) {
            event.setLine(0, parse("&c["+NAME+"&c]"));

            if (event.getLine(1).equalsIgnoreCase("join")) {
                Map m = findMap(event.getLine(2));
                if (m != null) {
                    if (m.isSetup()) {
                        event.setLine(1, parse("&a&l" + m.name));
                        event.setLine(2, parse(m.maxPlayers+" Players"));
                        event.getPlayer().sendMessage(parse("&aSuccessfully created a join sign!"));
                        return;
                    }
                    else
                    {
                        event.getPlayer().sendMessage(parse("&cThis map is not setup properly."));
                    }
                }
                else
                {
                    event.getPlayer().sendMessage(parse("&cPlease specify a map on the third line."));
                }
            }
            else
            {
                event.getPlayer().sendMessage(parse("&cPlease specify an action on the second line (JOIN)"));
            }
            event.setLine(1, parse("&c&lERROR!"));
        }
    }
}
