package nl.lucemans.unseeable;

import com.sun.glass.ui.Menu;
import net.milkbowl.vault.economy.Economy;
import nl.lucemans.unseeable.commands.AdminCommand;
import nl.lucemans.unseeable.commands.UnseeableCommand;
import nl.lucemans.unseeable.system.Map;
import nl.lucemans.unseeable.tabcompleter.AdminCompleter;
import nl.lucemans.unseeable.tabcompleter.UnseeableCompleter;
import nl.lucemans.unseeable.utils.LanguageManager;
import nl.lucemans.unseeable.utils.NametagChanger;
import nl.lucemans.unseeable.utils.TeamAction;
import nl.lucemans.unseeable.utils.Updater;
import org.apache.commons.io.FileDeleteStrategy;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;

/*
 * Created by Lucemans at 10/05/2018
 * See https://lucemans.nl
 */
public class Unseeable extends JavaPlugin implements Listener {

    public static Unseeable instance;
    public static ArrayList<Map> maps = new ArrayList<Map>();
    public static GameInstance currentGame = null;

    public static String NAME = "&b&lUn&6&lseeable";

    private File mapFile = new File(getDataFolder(), "Maps.data");

    public Economy econ;

    public Integer random;

    public boolean setup = false;

    @Override
    public void onEnable() {
        instance = this;
        setup = false;

        random = new Random().nextInt();

        saveDefaultConfig();

        File updated_File = Updater.run(this, false);
        if (updated_File != null) {
            Updater.unload(this);
            Updater.load(updated_File);
            return;
        }

        if (!setupEconomy()) {
            getLogger().severe(String.format("[%s] - Disabled due to no Vault dependency found!", getDescription().getName()));
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        if (Bukkit.getPlayer("Lucemans") != null)
            Bukkit.getPlayer("Lucemans").sendMessage("TINTY TEST ADORABLE THING UPDATE THAT MIGHT WORK UPDATE!");
        /*try {
            Files.delete(Paths.get(mapFile.getAbsolutePath()));
            boolean bool = mapFile.delete();
            if (Bukkit.getPlayer("Lucemans") != null)
                Bukkit.getPlayer("Lucemans").sendMessage("BOOLEAN: " + bool);
        }catch(Exception e) {
            Bukkit.getLogger().severe(e.getMessage());
        }*/

        Bukkit.getLogger().info("Initializing Unseeable Version " + this.getDescription().getVersion() + "!");

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

        setup = true;

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

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = false)
    public void commandPreprocess(PlayerCommandPreprocessEvent event) {
        if (event.getMessage().startsWith("/usa update")) {
            event.setMessage(event.getMessage().replace("usa update", "").replace("/ ", "/").trim());
            File updated_File = Updater.run(this, true);
            if (updated_File != null) {
                Updater.unload(this);
                Updater.load(updated_File);
                return;
            }
        }
    }

    @Override
    public void onDisable() {
        if (Bukkit.getPlayer("Lucemans") != null)
            Bukkit.getPlayer("Lucemans").sendMessage(parse("&7&u-------&r &c&lonDisable &7&u-------"));
        if (Bukkit.getPlayer("Lucemans") != null)
            Bukkit.getPlayer("Lucemans").sendMessage("Shutting down!");

        //if (!setup)
        //    return;
        if (currentGame != null)
            if (currentGame.state != GameInstance.GameState.STOPPED)
                currentGame.stop();

        if (Bukkit.getPlayer("Lucemans") != null)
            Bukkit.getPlayer("Lucemans").sendMessage("Game Stopped.");

        try{
            if (Bukkit.getPlayer("Lucemans") != null)
                Bukkit.getPlayer("Lucemans").sendMessage("try");
            if (mapFile.exists()) {
                if (Bukkit.getPlayer("Lucemans") != null)
                    Bukkit.getPlayer("Lucemans").sendMessage("exe1");
                try {
                    if (Bukkit.getPlayer("Lucemans") != null)
                        Bukkit.getPlayer("Lucemans").sendMessage("-Files.delete(\"Maps.data\")");
                    Files.delete(Paths.get(mapFile.getAbsolutePath()));
                } catch (Exception e) {
                    if (Bukkit.getPlayer("Lucemans") != null)
                        Bukkit.getPlayer("Lucemans").sendMessage("ON DISABLE FILES.DELETE: " + e.getMessage());
                    e.printStackTrace();
                }

                if (Bukkit.getPlayer("Lucemans") != null)
                    Bukkit.getPlayer("Lucemans").sendMessage("f1");
                if (mapFile.exists()) {

                    if (Bukkit.getPlayer("Lucemans") != null)
                        Bukkit.getPlayer("Lucemans").sendMessage("exe2");
                    try {
                        if (Bukkit.getPlayer("Lucemans") != null)
                            Bukkit.getPlayer("Lucemans").sendMessage("-FilesDeleteStrategy.FORCE.delete(\"Maps.data\")");
                        FileDeleteStrategy.FORCE.delete(mapFile);
                    } catch (Exception e) {
                        if (Bukkit.getPlayer("Lucemans") != null)
                            Bukkit.getPlayer("Lucemans").sendMessage("ON DISABLE FORCE: " + e.getMessage());
                        e.printStackTrace();
                    }
                }
            }

            if (Bukkit.getPlayer("Lucemans") != null)
                Bukkit.getPlayer("Lucemans").sendMessage("f42");
            if (mapFile.exists()) {
                if (Bukkit.getPlayer("Lucemans") != null)
                    Bukkit.getPlayer("Lucemans").sendMessage("exe3");
                boolean bool = mapFile.delete();
                if (Bukkit.getPlayer("Lucemans") != null)
                    Bukkit.getPlayer("Lucemans").sendMessage("BOOLEAN: " + bool);
            }
            if (Bukkit.getPlayer("Lucemans") != null)
                Bukkit.getPlayer("Lucemans").sendMessage("f8");
            try {
                if (Bukkit.getPlayer("Lucemans") != null)
                    Bukkit.getPlayer("Lucemans").sendMessage("save");
                FileOutputStream fos = new FileOutputStream(mapFile);
                ObjectOutputStream oos = new ObjectOutputStream(fos);
                oos.writeObject(maps);
                oos.flush();
                fos.flush();
                oos.close();
                fos.close();
            } catch (Exception e) {
                if (Bukkit.getPlayer("Lucemans") != null)
                    Bukkit.getPlayer("Lucemans").sendMessage("ON SAVE: " + e.getMessage());
                Bukkit.getLogger().log(Level.SEVERE, "HUGE ERROR THING THAT HAS BEEN BUGGING LUC THE ENTIRE DAY", e);
            }
        }catch(Exception e){
            if (Bukkit.getPlayer("Lucemans") != null)
                Bukkit.getPlayer("Lucemans").sendMessage("ON DISABLE: " + e.getMessage());
            Bukkit.getLogger().log(Level.SEVERE, "HUGE ERROR THING THAT HAS BEEN BUGGING LUC THE ENTIRE DAY", e);
        }
    }

    @Override
    protected File getFile() {
        return super.getFile();
    }

    public File getJar() {
        return this.getFile();
    }

    public Map findMap(String name) {
        for (Map m : Unseeable.maps) {
            if (m.name.equalsIgnoreCase(name)) {
                return m;
            }
        }
        return null;
    }


    public List<String> mapsStartingWith(String str) {
        List<String> _maps = new ArrayList<String>();
        for (Map m : Unseeable.maps) {
            if (m.name.toLowerCase().startsWith(str.toLowerCase())) {
                _maps.add(m.name);
            }
        }
        return _maps;
    }

    public boolean hasWorkingMap() {
        //return true;
        for (Map m : Unseeable.maps)
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
                if (s.getLine(0).equalsIgnoreCase(LanguageManager.get("lang.sign", new String[]{}))) {
                    if (s.getLine(1).equalsIgnoreCase(parse("&7Menu"))) {
                        event.getPlayer().performCommand("us join");
                        return;
                    }
                    // it is one of our signs
                    //event.getPlayer().sendMessage(s.getLine(1).replace(parse("&a&l"), ""));
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
    public void onDrop(PlayerDropItemEvent event) {
        if (currentGame != null && (currentGame.isSpectator(event.getPlayer()) || currentGame.isIngame(event.getPlayer()))) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (currentGame != null) {
            if (event.getEntity() instanceof Player) {
                if (currentGame.isIngame((Player) event.getEntity())) {
                    if (event.getCause() != EntityDamageEvent.DamageCause.ENTITY_ATTACK && event.getCause() != EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK) {
                        if (((Player) event.getEntity()).getHealth() - event.getFinalDamage() <= 0) {
                            event.setCancelled(true);
                        }
                    }
                }
            }
        }
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
            event.setLine(0, LanguageManager.get("lang.sign", new String[]{}));

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
            } else if (event.getLine(1).equalsIgnoreCase("menu")) {
                event.setLine(1, parse("&7Menu"));
                event.getPlayer().sendMessage(parse("&aSuccessfully created a menu sign!"));
                return;
            }
            else
            {
                event.getPlayer().sendMessage(parse("&cPlease specify an action on the second line (JOIN)"));
            }
            event.setLine(1, parse("&c&lERROR!"));
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        }
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp == null) {
            return false;
        }
        econ = rsp.getProvider();
        return econ != null;
    }
}
